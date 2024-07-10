package cn.cuiot.dmp.upload.infrastructure.storage;

import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.upload.domain.entity.BucketItem;
import cn.cuiot.dmp.upload.domain.entity.ChunkUploadRequest;
import cn.cuiot.dmp.upload.domain.entity.ChunkUploadResponse;
import cn.cuiot.dmp.upload.domain.entity.ObjectItem;
import cn.cuiot.dmp.upload.domain.storage.FileStorage;
import cn.cuiot.dmp.upload.domain.types.MimeTypeEnum;
import cn.cuiot.dmp.upload.domain.types.UploadStatusConstants;
import cn.cuiot.dmp.upload.domain.types.UrlType;
import cn.cuiot.dmp.upload.infrastructure.config.OssProperties;
import cn.hutool.core.net.URLEncodeUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.minio.BucketExistsArgs;
import io.minio.ComposeObjectArgs;
import io.minio.ComposeSource;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetBucketPolicyArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.SetBucketPolicyArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author: wuyongchong
 * @date: 2023/10/12 14:16
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "oss", name = "platform", havingValue = "minio")
public class MinioFileStorage extends FileStorage {

    private final OssProperties ossProperties;

    private MinioClient ossClient;

    public MinioFileStorage(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    @PostConstruct
    public void initClient() {
        this.ossClient = MinioClient.builder()
                .endpoint(ossProperties.getEndpoint())
                .credentials(ossProperties.getAccessKey(), ossProperties.getSecretKey()).build();
    }

    @Override
    public List<BucketItem> listBuckets() throws Exception {
        List<Bucket> buckets = ossClient.listBuckets();
        return Optional.ofNullable(buckets).orElse(Lists.newArrayList()).stream().map(item->{
            BucketItem bucketItem = new BucketItem();
            bucketItem.setBucketName(item.name());
            bucketItem.setCreationDate(Objects.isNull(item.creationDate())?null:Date.from(item.creationDate().toInstant()));
            return bucketItem;
        }).collect(Collectors.toList());
    }

    @Override
    public void createBucket(String bucketName) throws Exception {
        if (!bucketExists(bucketName)) {
            ossClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    @Override
    public boolean bucketExists(String bucketName) throws Exception {
        return ossClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    @Override
    public void removeBucket(String bucketName) throws Exception {
        ossClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    @Override
    public void setBucketPolicy(String bucketName, String policy) throws Exception {
        SetBucketPolicyArgs policyArgs = SetBucketPolicyArgs.builder()
                .bucket(bucketName)
                .config(policy)
                .build();
        ossClient.setBucketPolicy(policyArgs);
    }

    @Override
    public String getBucketPolicy(String bucketName) throws Exception {
        return ossClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket(bucketName).build());
    }

    @Override
    public String putObject(String bucketName, String objectName, InputStream stream,
            String contextType,Boolean privateRead) throws Exception {
        ObjectWriteResponse response = ossClient
                .putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName)
                        .stream(stream, stream.available(), -1)
                        .contentType(contextType)
                        .build());
        log.info("putObject response:{}", JSON.toJSONString(response));
        String url = ossProperties.getDomainUrl() + "/" + bucketName + "/" + objectName;
        String encodeUrl = URLEncodeUtil.encode(url);
        log.info("putObject url:{},encodeUrl:{}", url, encodeUrl);
        return encodeUrl;
    }

    @Override
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        return ossClient
                .getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    @Override
    public String getObjectUrl(String bucketName, String objectName, String urlType,
            Integer expires) throws Exception {
        String url;
        if (UrlType.PRESIGNED_URL.equals(urlType)) {
            url = getPresignedObjectUrl(bucketName, objectName, expires);
        } else {
            url = ossProperties.getDomainUrl() + "/" + bucketName + "/" + objectName;
            url = URLEncodeUtil.encode(url);
        }
        log.info("getObjectUrl url:{}", url);
        return url;
    }

    @Override
    public boolean objectExists(String bucketName, String objectName) throws Exception {
        try {
            StatObjectArgs args = StatObjectArgs.builder().bucket(bucketName).object(objectName)
                    .build();
            StatObjectResponse response = ossClient.statObject(args);
            log.info("objectExists response:{}", response);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void removeObject(String bucketName, String objectName) throws Exception {
        ossClient.removeObject(
                RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    @Override
    public void removeObjects(String bucketName, List<String> objectNames) throws Exception {
        List<DeleteObject> objectPaths = objectNames.stream()
                .map(filePath -> new DeleteObject(filePath))
                .collect(Collectors.toList());
        Iterable<Result<DeleteError>> results = ossClient.removeObjects(
                RemoveObjectsArgs.builder().bucket(bucketName).objects(objectPaths).build());
        for (Result<DeleteError> result : results) {
            DeleteError error = result.get();
            log.error("Error in deleting object " + error.objectName() + "; " + error.message());
        }
    }

    @Override
    public String getPresignedObjectUrl(String bucketName, String objectName, Integer expires)
            throws Exception {
        GetPresignedObjectUrlArgs.Builder argsBuilder = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .method(Method.GET);
        if (Objects.nonNull(expires)) {
            argsBuilder.expiry(expires, TimeUnit.SECONDS);
        }
        //Map<String, String> reqParams = new HashMap<String, String>();
        //reqParams.put("response-content-type", "application/json");
        //argsBuilder.extraQueryParams(reqParams);
        String url = ossClient.getPresignedObjectUrl(argsBuilder.build());
        log.info("getPresignedObjectUrl url:{}", url);
        url = url.replace(ossProperties.getEndpoint(), ossProperties.getDomainUrl());
        log.info("getPresignedObjectUrl replaced url:{}", url);
        return url;
    }

    @Override
    public void copyObject(String bucketName, String objectName, String targetBucketName,
            String targetObjectName) throws Exception {
        ossClient.copyObject(
                CopyObjectArgs.builder()
                        .source(CopySource.builder().bucket(bucketName).object(objectName).build())
                        .bucket(targetBucketName)
                        .object(targetObjectName)
                        .build());

    }

    @Override
    public List<ObjectItem> getObjectsByPrefix(String bucketName, String prefix, boolean recursive)
            throws Exception {
        List<ObjectItem> resultList = Lists.newArrayList();
        Iterable<Result<Item>> objectsIterator = ossClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).recursive(recursive)
                        .build());
        if (objectsIterator != null) {
            for (Result<Item> itemResult : objectsIterator) {
                Item item = itemResult.get();
                ObjectItem objectItem = new ObjectItem();
                objectItem.setBucketName(bucketName);
                objectItem.setObjectName(item.objectName());
                objectItem.setSize(item.size());
                objectItem.setUserMetadata(item.userMetadata());
                resultList.add(objectItem);
            }
        }
        return resultList;
    }

    @Override
    public ChunkUploadResponse chunkUpload(ChunkUploadRequest param) throws Exception {
        String bucketName = param.getBucketName();
        Integer chunkTotal = param.getChunkTotal();
        String suffix = param.getFileName()
                .substring(param.getFileName().lastIndexOf(".") + 1);
        String chunkDir = param.getDir() + "/" + param.getTaskId();
        String chunkObjectName = chunkDir + "/" + param.getChunk() + "." + suffix;
        String contentType = MimeTypeEnum.getContentType(suffix);
        //上传分片文件
        String chunkUrl = putObject(bucketName, chunkObjectName, param.getInputStream(),
                contentType,param.getPrivateRead());
        if ((param.getChunk() + 1) >= param.getChunkTotal()) {
            //合并文件
            List<ObjectItem> objectItems = getObjectsByPrefix(bucketName, chunkDir + "/",
                    false);
            objectItems = Optional.ofNullable(objectItems).orElse(Lists.newArrayList()).stream()
                    .sorted((ite1,ite2)->{
                     return Long
                             .valueOf(ite1.getObjectName().replace(chunkDir+ "/","").replace("." + suffix,""))
                             .compareTo(Long.valueOf(ite2.getObjectName().replace(chunkDir+ "/","").replace("." + suffix,"")));
                    }).collect(Collectors.toList());
            if (objectItems.size() >= chunkTotal) {
                String dateDir = DateTimeUtil.dateToString(new Date(), "yyyyMM");
                String objectName =
                        param.getDir() + "/" + dateDir + "/" + param.getTaskId() + "." + suffix;
                if(Boolean.TRUE.equals(param.getOriginName())){
                    objectName =
                            param.getDir() + "/" + dateDir + "/" + param.getTaskId() + "/" + param.getFileName();
                }

                List<ComposeSource> sourceObjectList = objectItems.stream()
                        .map(ite -> ComposeSource.builder()
                                .bucket(bucketName)
                                .object(ite.getObjectName())
                                .build())
                        .collect(Collectors.toList());

                ObjectWriteResponse objectWriteResponse = ossClient.composeObject(
                        ComposeObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .sources(sourceObjectList)
                                .build());
                //删除分片文件
                List<String> removeObjects = objectItems.stream()
                        .map(ite -> ite.getObjectName()).collect(Collectors.toList());
                removeObjects(bucketName, removeObjects);
                //返回结果
                String url = URLEncodeUtil
                        .encode(ossProperties.getDomainUrl() + "/" + bucketName + "/" + objectName);
                return ChunkUploadResponse.builder()
                        .status(UploadStatusConstants.FINISH)
                        .url(url)
                        .bucketName(bucketName)
                        .objectName(objectName)
                        .build();
            }
        }
        return ChunkUploadResponse.builder()
                .status(UploadStatusConstants.UN_FINISH)
                .url(chunkUrl)
                .bucketName(bucketName)
                .objectName(chunkObjectName)
                .build();
    }

}
