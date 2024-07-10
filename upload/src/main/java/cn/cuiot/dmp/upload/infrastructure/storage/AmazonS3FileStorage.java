package cn.cuiot.dmp.upload.infrastructure.storage;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.upload.domain.entity.BucketItem;
import cn.cuiot.dmp.upload.domain.entity.ChunkContext;
import cn.cuiot.dmp.upload.domain.entity.ChunkPartETag;
import cn.cuiot.dmp.upload.domain.entity.ChunkUploadRequest;
import cn.cuiot.dmp.upload.domain.entity.ChunkUploadResponse;
import cn.cuiot.dmp.upload.domain.entity.ObjectItem;
import cn.cuiot.dmp.upload.domain.storage.FileStorage;
import cn.cuiot.dmp.upload.domain.types.MimeTypeEnum;
import cn.cuiot.dmp.upload.domain.types.UploadStatusConstants;
import cn.cuiot.dmp.upload.domain.types.UrlType;
import cn.cuiot.dmp.upload.infrastructure.cache.OssRedisCache;
import cn.cuiot.dmp.upload.infrastructure.config.OssProperties;
import com.alibaba.fastjson.JSON;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.BucketPolicy;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.SetObjectAclRequest;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: wuyongchong
 * @date: 2023/10/12 16:35
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "oss", name = "platform", havingValue = "amazonS3")
public class AmazonS3FileStorage extends FileStorage {

    @Autowired
    private OssRedisCache ossRedisCache;

    private final OssProperties ossProperties;

    private AmazonS3 ossClient;

    public AmazonS3FileStorage(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    @PostConstruct
    public void initClient() {
        AmazonS3ClientBuilder clientBuilder = AmazonS3ClientBuilder.standard();
        ClientConfiguration config = new ClientConfiguration();
        config.setProtocol(com.amazonaws.Protocol.HTTPS);
        AWSCredentials acre = new BasicAWSCredentials(ossProperties.getAccessKey(),
                ossProperties.getSecretKey());
        AWSCredentialsProvider acrep = new AWSStaticCredentialsProvider(acre);
        AwsClientBuilder.EndpointConfiguration econfig = new AwsClientBuilder.EndpointConfiguration(
                ossProperties.getEndpoint(), null);
        clientBuilder.setClientConfiguration(config);
        clientBuilder.setCredentials(acrep);
        clientBuilder.setEndpointConfiguration(econfig);
        clientBuilder.enablePathStyleAccess();
        this.ossClient = clientBuilder.build();
    }

    @Override
    public List<BucketItem> listBuckets() throws Exception {
        List<Bucket> buckets = ossClient.listBuckets();
        return Optional.ofNullable(buckets).orElse(Lists.newArrayList()).stream().map(item->{
            BucketItem bucketItem = new BucketItem();
            bucketItem.setBucketName(item.getName());
            bucketItem.setCreationDate(item.getCreationDate());
            return bucketItem;
        }).collect(Collectors.toList());
    }

    @Override
    public void createBucket(String bucketName) throws Exception {
        if (!bucketExists(bucketName)) {
            ossClient.createBucket((bucketName));
        }
    }

    @Override
    public boolean bucketExists(String bucketName) throws Exception {
        return ossClient.doesBucketExistV2(bucketName);
    }

    @Override
    public void removeBucket(String bucketName) throws Exception {
        ossClient.deleteBucket(bucketName);
    }

    @Override
    public String getBucketPolicy(String bucketName) throws Exception {
        BucketPolicy bucketPolicy = ossClient.getBucketPolicy(bucketName);
        if (Objects.nonNull(bucketPolicy)) {
            return bucketPolicy.getPolicyText();
        }
        return null;
    }

    @Override
    public String putObject(String bucketName, String objectName, InputStream stream,
            String contextType,Boolean privateRead) throws Exception {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contextType);
        metadata.setContentLength(stream.available());
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, stream,
                metadata);
        /**
         * 设置文件ACL
         */
        if(Boolean.TRUE.equals(privateRead)){
            putObjectRequest.withCannedAcl(CannedAccessControlList.AuthenticatedRead);
        }else{
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        }
        putObjectRequest.getRequestClientOptions().setReadLimit(stream.available() + 1);
        PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
        log.info("putObject putObjectResult:{}", JSON.toJSONString(putObjectResult));
        String url = ossClient.getUrl(bucketName, objectName).toString();
        log.info("putObject url:{}", url);
        //url = ossProperties.getDomainUrl()+"/"+bucketName+"/"+objectName;
        //log.info("putObject replaceUrl:{}", url);
        return url;
    }

    @Override
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        S3Object s3Object = ossClient.getObject(bucketName, objectName);
        return s3Object.getObjectContent();
    }

    @Override
    public String getObjectUrl(String bucketName, String objectName, String urlType,
            Integer expires) throws Exception {
        String url;
        if (UrlType.PRESIGNED_URL.equals(urlType)) {
            url = getPresignedObjectUrl(bucketName, objectName, expires);
        } else {
            url = ossClient.getUrl(bucketName, objectName).toString();
            //url = ossProperties.getDomainUrl()+"/"+bucketName+"/"+objectName;
        }
        log.info("getObjectUrl url:{}", url);
        return url;
    }

    @Override
    public boolean objectExists(String bucketName, String objectName) throws Exception {
        return ossClient.doesObjectExist(bucketName, objectName);
    }

    @Override
    public void removeObject(String bucketName, String objectName) throws Exception {
        ossClient.deleteObject(bucketName, objectName);
    }

    @Override
    public void removeObjects(String bucketName, List<String> objectNames) throws Exception {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
        List<DeleteObjectsRequest.KeyVersion> keys = objectNames.stream()
                .map(ite -> new DeleteObjectsRequest.KeyVersion(ite))
                .collect(Collectors.toList());
        deleteObjectsRequest.withKeys(keys);
        ossClient.deleteObjects(deleteObjectsRequest);
    }

    @Override
    public String getPresignedObjectUrl(String bucketName, String objectName, Integer expires)
            throws Exception {
        Date expiration = DateTimeUtil
                .localDateTimeToDate(LocalDateTime.now().plusSeconds(expires));
        URL urlResult = ossClient.generatePresignedUrl(
                new GeneratePresignedUrlRequest(bucketName, objectName).withMethod(HttpMethod.GET)
                        .withExpiration(expiration));
        String url = urlResult.toString();
        log.info("getPresignedObjectUrl url:{}", url);
        //url = url.replace(ossProperties.getEndpoint(), ossProperties.getDomainUrl());
        //log.info("getPresignedObjectUrl replaced url:{}", url);
        //url = ossProperties.getDomainUrl()+"/"+bucketName+urlResult.getFile();
        //log.info("getPresignedObjectUrl replaced url:{}", url);
        return url;
    }

    @Override
    public void copyObject(String bucketName, String objectName, String targetBucketName,
            String targetObjectName) throws Exception {
        ossClient.copyObject(bucketName, objectName, targetBucketName, targetObjectName);
    }

    @Override
    public List<ObjectItem> getObjectsByPrefix(String bucketName, String prefix, boolean recursive)
            throws Exception {
        List<ObjectItem> resultList = Lists.newArrayList();
        ObjectListing objectListing = ossClient.listObjects(bucketName, prefix);
        if (Objects.nonNull(objectListing)) {
            List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
            if (CollectionUtils.isNotEmpty(objectSummaries)) {
                for (S3ObjectSummary s3ObjectSummary : objectSummaries) {
                    ObjectItem objectItem = new ObjectItem();
                    objectItem.setBucketName(bucketName);
                    objectItem.setObjectName(s3ObjectSummary.getKey());
                    objectItem.setSize(s3ObjectSummary.getSize());
                    objectItem.setUserMetadata(null);
                    resultList.add(objectItem);
                }
            }
        }
        return resultList;
    }

    @Override
    public ChunkUploadResponse chunkUpload(ChunkUploadRequest param) throws Exception {
        String bucketName = param.getBucketName();
        Integer chunkIndex = param.getChunk();
        Integer chunkTotal = param.getChunkTotal();
        String suffix = param.getFileName()
                .substring(param.getFileName().lastIndexOf(".") + 1);
        String contentType = MimeTypeEnum.getContentType(suffix);
        String dateDir = DateTimeUtil.dateToString(new Date(), "yyyyMM");
        String objectDir =param.getDir() + "/" + dateDir;
        String objectName =objectDir+ "/" + param.getTaskId() + "." + suffix;
        if(Boolean.TRUE.equals(param.getOriginName())){
            objectName = objectDir+ "/" + param.getTaskId() + "/" + param.getFileName();
        }
        if(chunkIndex.equals(0)){
            InitiateMultipartUploadResult initiateResult = initiateMultipartUpload(
                    bucketName, objectName, contentType);
            ChunkContext chunkContext = new ChunkContext();
            chunkContext.setUploadId(initiateResult.getUploadId());
            chunkContext.setPartETags(Lists.newArrayList());
            ossRedisCache.putChunkCache(param.getTaskId(),chunkContext);
        }
        ChunkContext chunkContext = ossRedisCache.getChunkCache(param.getTaskId());
        if (Objects.isNull(chunkContext)) {
            log.error("chunkUpload error get chunkContext null");
            throw new BusinessException(ResultCode.INNER_ERROR, "系统异常，文件上传失败");
        }
        try {
            InputStream inputStream = param.getInputStream();
            UploadPartRequest partRequest = new UploadPartRequest();
            partRequest.setBucketName(bucketName);
            partRequest.setKey(objectName);
            partRequest.setUploadId(chunkContext.getUploadId());
            partRequest.setInputStream(inputStream);
            partRequest.setPartSize(inputStream.available());
            partRequest.setPartNumber(chunkIndex + 1);
            partRequest.setLastPart((chunkIndex + 1)>=chunkTotal);
            partRequest.setRequesterPays(Boolean.TRUE);
            UploadPartResult uploadResult = ossClient.uploadPart(partRequest);
            chunkContext.getPartETags().add(new ChunkPartETag(uploadResult.getPartETag().getPartNumber(),uploadResult.getPartETag().getETag()));
            ossRedisCache.putChunkCache(param.getTaskId(),chunkContext);
            if((chunkIndex + 1)>=chunkTotal) {
                List<PartETag> partETags = chunkContext.getPartETags().stream().map(ite->new PartETag(ite.getPartNumber(),ite.geteTag())).sorted(
                        Comparator.comparingInt(PartETag::getPartNumber)).collect(
                        Collectors.toList());
                CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, objectName, chunkContext.getUploadId(), partETags);
                CompleteMultipartUploadResult completeMultipartUploadResult = ossClient
                        .completeMultipartUpload(completeMultipartUploadRequest);
                /**
                 * 设置文件ACL
                 */
                if(Boolean.TRUE.equals(param.getPrivateRead())){
                    ossClient.setObjectAcl(new SetObjectAclRequest(bucketName, objectName, CannedAccessControlList.AuthenticatedRead));
                }else{
                    ossClient.setObjectAcl(new SetObjectAclRequest(bucketName, objectName, CannedAccessControlList.PublicRead));
                }
                ossRedisCache.invalidateChunkCache(param.getTaskId());
                String url = getObjectUrl(bucketName, objectName, null, null);
                return ChunkUploadResponse.builder()
                        .status(UploadStatusConstants.FINISH)
                        .url(url)
                        .bucketName(bucketName)
                        .objectName(objectName)
                        .build();
            }
            return ChunkUploadResponse.builder()
                    .status(UploadStatusConstants.UN_FINISH)
                    .url(objectName)
                    .bucketName(bucketName)
                    .objectName(objectName)
                    .build();
        }catch (Exception ex){
            log.error("chunkUpload===uploadPart error",ex);
            AbortMultipartUploadRequest abortMultipartUploadRequest = new AbortMultipartUploadRequest(bucketName,
                    objectName, chunkContext.getUploadId());
            ossClient.abortMultipartUpload(abortMultipartUploadRequest);
            throw new BusinessException(ResultCode.INNER_ERROR, "系统异常，文件上传失败");
        }
    }

    private InitiateMultipartUploadResult initiateMultipartUpload(String bucketName,
            String objectName, String contentType)throws Exception {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        if (!bucketExists(bucketName)) {
            createBucket(bucketName);
        }
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName,objectName, metadata);
        request.setRequesterPays(Boolean.TRUE);
        InitiateMultipartUploadResult res = ossClient.initiateMultipartUpload(request);
        if (StringUtils.isBlank(res.getUploadId())) {
            log.error("initiateMultipartUpload error get uploadId is blank");
            throw new BusinessException(ResultCode.INNER_ERROR, "系统异常，文件上传失败");
        }
        return res;
    }

}
