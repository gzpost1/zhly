package cn.cuiot.dmp.upload.application.service;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.common.utils.ImgScaleUtil;
import cn.cuiot.dmp.upload.application.dto.ChunkFileParam;
import cn.cuiot.dmp.upload.application.dto.DownLoadParam;
import cn.cuiot.dmp.upload.application.dto.MultipartFileParam;
import cn.cuiot.dmp.upload.application.dto.ObjectParam;
import cn.cuiot.dmp.upload.application.dto.ObjectResponse;
import cn.cuiot.dmp.upload.application.dto.UploadParam;
import cn.cuiot.dmp.upload.application.dto.UploadResponse;
import cn.cuiot.dmp.upload.domain.entity.ChunkUploadRequest;
import cn.cuiot.dmp.upload.domain.entity.ChunkUploadResponse;
import cn.cuiot.dmp.upload.domain.service.OssTemplate;
import cn.cuiot.dmp.upload.domain.types.MimeTypeEnum;
import cn.cuiot.dmp.upload.domain.types.UploadStatusConstants;
import cn.cuiot.dmp.upload.domain.types.UrlType;
import cn.cuiot.dmp.upload.infrastructure.config.OssProperties;
import cn.cuiot.dmp.upload.infrastructure.util.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件操作服务类
 * @author: wuyongchong
 * @date: 2024/4/1 20:13
 */
@Slf4j
@Service
public class OssUploadService {

    /**
     * 第一个分片
     */
    private final static Integer FIRST_CHUNK=0;

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private OssProperties ossProperties;
    
    /**
     * 普通上传
     */
    public UploadResponse upload(UploadParam param) throws Exception {
        String bucketName = param.getBucketName();
        MultipartFile multipartFile = param.getFile();
        AssertUtil.isTrue((null != multipartFile && !multipartFile.isEmpty()), "上传文件为空");
        AssertUtil.isTrue(StringUtils.isNotBlank(param.getDir()), "上传目录不能为空");
        AssertUtil.isTrue(StringUtils.isNotBlank(bucketName), "桶名称不能为空");

        String suffix = multipartFile.getOriginalFilename()
                .substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        /**
         * 文件类型安全校验
         */
        AssertUtil.isTrue(checkForbiddenSuffix(suffix),
                new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "不支持该文件类型上传"));
        AssertUtil.isTrue(checkAllowSuffix(suffix),
                new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "不支持该文件类型上传"));
        AssertUtil.isTrue(FileTypeUtil.validate(param.getFile(),suffix),
                new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "上传文件类型不支持上传"));
        AssertUtil.isTrue(checkAllowDirs(param.getDir()),
                new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "目录参数不支持"));

        if (!ossTemplate.bucketExists(bucketName)) {
            ossTemplate.createBucket(bucketName);
        }
        String url = "";
        String dateDir = DateTimeUtil.dateToString(new Date(), "yyyyMM");
        
        String sinalName = "" + IdWorker.getIdStr();
        String objectDir = param.getDir() + "/" + dateDir;
        String objectName = objectDir + "/" + sinalName + "." + suffix;

        Byte compressImg = ossProperties.getCompressImg();
        if (Objects.nonNull(param.getCompressImg())) {
            compressImg = param.getCompressImg();
        }
        if (EntityConstants.YES.equals(compressImg) && MimeTypeEnum.getImageExtensions().contains(suffix)) {
            //启用图片压缩
            String localDirPath =
                    ossProperties.getTmpDirPath() + Matcher.quoteReplacement(File.separator)
                            + dateDir;
            String localFilePath =
                    localDirPath + Matcher.quoteReplacement(File.separator) + sinalName + "."
                            + suffix;
            File localFile = new File(localFilePath);
            localFile.setReadable(true);
            localFile.setWritable(true);
            try {
                if (!localFile.getParentFile().exists()) {
                    localFile.getParentFile().mkdirs();
                }
            } catch (Exception ex) {
                log.error("create localFile error,ex:{}", ex);
            }
            if (!localFile.exists()) {
                multipartFile.transferTo(localFile);
            }
            try {
                Map<String, String> scaleResultMap = ImgScaleUtil
                        .scaleImg(localFile, localDirPath, sinalName, suffix);
                for (Map.Entry<String, String> entry : scaleResultMap.entrySet()) {
                    String scaleObjectName = objectDir + "/" + entry.getKey();
                    String scaleFilePath = entry.getValue();
                    ossTemplate.putObject(bucketName, scaleObjectName,
                            FileUtil.getInputStream(scaleFilePath),
                            MimeTypeEnum.getContentType(suffix),
                            param.getPrivateRead());
                    File scaleFile = new File(scaleFilePath);
                    if (scaleFile.exists()) {
                        scaleFile.delete();
                    }
                }
                url = ossTemplate
                        .putObject(bucketName, objectName, FileUtil.getInputStream(localFile),
                                MimeTypeEnum.getContentType(suffix),param.getPrivateRead());
                File file = new File(localFilePath);
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception ex) {
                log.error("scaleImg error", ex);
                throw new BusinessException(ResultCode.INNER_ERROR, "上传失败，请联系管理员");
            }
        } else {
            url = ossTemplate.putObject(bucketName, objectName, multipartFile.getInputStream(),
                    MimeTypeEnum.getContentType(suffix),param.getPrivateRead());
        }
        String presignedObjectUrl = ossTemplate
                .getObjectUrl(bucketName, objectName, UrlType.PRESIGNED_URL, param.getExpires());
        return UploadResponse.builder()
                .status(UploadStatusConstants.FINISH)
                .bucketName(bucketName)
                .objectName(objectName)
                .url(url)
                .presignedObjectUrl(presignedObjectUrl)
                .build();
    }

    /**
     * 原名上传
     */
    public UploadResponse originUpload(UploadParam param) throws Exception {
        String bucketName = param.getBucketName();
        MultipartFile multipartFile = param.getFile();
        AssertUtil.isTrue((null != multipartFile && !multipartFile.isEmpty()), "上传文件为空");
        AssertUtil.isTrue(StringUtils.isNotBlank(param.getDir()), "上传目录不能为空");
        AssertUtil.isTrue(StringUtils.isNotBlank(bucketName), "桶名称不能为空");
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                .toLowerCase();
        /**
         * 文件类型安全校验
         */
        AssertUtil.isTrue(checkForbiddenSuffix(suffix),
                new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "不支持该文件类型上传"));
        AssertUtil.isTrue(checkAllowSuffix(suffix),
                new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "不支持该文件类型上传"));
        AssertUtil.isTrue(FileTypeUtil.validate(param.getFile(),suffix),
                new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "上传文件类型不支持上传"));
        AssertUtil.isTrue(checkAllowDirs(param.getDir()),
                new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "目录参数不支持"));

        if (!ossTemplate.bucketExists(bucketName)) {
            ossTemplate.createBucket(bucketName);
        }
        String objectName = param.getDir() + "/" + originalFilename;
        String url = ossTemplate.putObject(bucketName, objectName, multipartFile.getInputStream(),
                MimeTypeEnum.getContentType(suffix),param.getPrivateRead());
        String presignedObjectUrl = ossTemplate
                .getObjectUrl(bucketName, objectName, UrlType.PRESIGNED_URL, param.getExpires());
        return UploadResponse.builder()
                .status(UploadStatusConstants.FINISH)
                .bucketName(bucketName)
                .objectName(objectName)
                .url(url)
                .presignedObjectUrl(presignedObjectUrl)
                .build();
    }

    /**
     * 批量文件上传
     */
    public List<UploadResponse> multiUpload(MultipartFileParam param) throws Exception {
        List<UploadResponse> resultList = Lists.newArrayList();
        AssertUtil.isFalse((null == param.getFiles() || param.getFiles().length == 0), "上传文件为空");
        for (MultipartFile file : param.getFiles()) {
            AssertUtil.isFalse((null == file || file.isEmpty()), "包含空文件");
        }
        String bucketName = param.getBucketName();
        AssertUtil.isTrue(StringUtils.isNotBlank(param.getDir()), "上传目录不能为空");
        AssertUtil.isTrue(StringUtils.isNotBlank(bucketName), "桶名称不能为空");
        AssertUtil.isTrue(checkAllowDirs(param.getDir()),
                new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "目录参数不支持"));

        if (!ossTemplate.bucketExists(bucketName)) {
            ossTemplate.createBucket(bucketName);
        }

        for (MultipartFile multipartFile : param.getFiles()) {
            UploadParam uploadParam = new UploadParam();
            uploadParam.setBucketName(bucketName);
            uploadParam.setDir(param.getDir());
            uploadParam.setFile(multipartFile);
            uploadParam.setExpires(param.getExpires());
            uploadParam.setPrivateRead(param.getPrivateRead());
            UploadResponse uploadResponse = this.upload(uploadParam);
            resultList.add(uploadResponse);
        }
        return resultList;
    }

    /**
     * 分片上传
     */
    public UploadResponse chunkUpload(ChunkFileParam param) throws Exception {
        AssertUtil.notBlank(param.getTaskId(), "任务id参数不能为空");
        AssertUtil.notNull(param.getChunk(), "当前分片索引参数不能为空");
        AssertUtil.notNull(param.getChunkTotal(), "分片总数参数不能为空");
        AssertUtil.isTrue((null != param.getFile() && !param.getFile().isEmpty()), "上传文件为空");
        AssertUtil.isTrue(StringUtils.isNotBlank(param.getDir()), "上传目录不能为空");
        AssertUtil.isTrue(StringUtils.isNotBlank(param.getBucketName()), "桶名称不能为空");
        String suffix = StringUtils.substringAfterLast(param.getFileName(), ".");
        AssertUtil.notNull(suffix,"上传文件类型不支持上传");
        suffix = suffix.toLowerCase();
        /**
         * 文件类型安全校验
         */
        AssertUtil.isTrue(checkForbiddenSuffix(suffix),
                new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "不支持该文件类型上传"));
        AssertUtil.isTrue(checkAllowSuffix(suffix),
                new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "不支持该文件类型上传"));
        AssertUtil.isTrue(checkAllowDirs(param.getDir()),
                new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "目录参数不支持"));
        if(FIRST_CHUNK.equals(param.getChunk())) {
            AssertUtil.isTrue(FileTypeUtil.validate(param.getFile(),suffix),
                    new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "上传文件类型不支持上传"));
        }

        ChunkUploadRequest request = new ChunkUploadRequest();
        request.setBucketName(param.getBucketName());
        request.setDir(param.getDir());
        request.setFileName(param.getFileName());
        request.setInputStream(param.getFile().getInputStream());
        request.setExpires(param.getExpires());
        request.setTaskId(param.getTaskId());
        request.setChunk(param.getChunk());
        request.setChunkTotal(param.getChunkTotal());
        request.setPrivateRead(param.getPrivateRead());
        ChunkUploadResponse chunkUploadResponse = ossTemplate.chunkUpload(request);
        String presignedObjectUrl = ossTemplate.getObjectUrl(chunkUploadResponse.getBucketName(),
                chunkUploadResponse.getObjectName(), UrlType.PRESIGNED_URL, param.getExpires());
        return UploadResponse.builder()
                .status(chunkUploadResponse.getStatus())
                .bucketName(chunkUploadResponse.getBucketName())
                .objectName(chunkUploadResponse.getObjectName())
                .url(chunkUploadResponse.getUrl())
                .presignedObjectUrl(presignedObjectUrl)
                .build();
    }

    /**
     * 获取对象URL
     */
    public ObjectResponse getObjectUrl(ObjectParam param) {
        String url = ossTemplate.getObjectUrl(param.getBucketName(),
                param.getObjectName(), null, null);
        String presignedObjectUrl = ossTemplate.getObjectUrl(param.getBucketName(),
                param.getObjectName(), UrlType.PRESIGNED_URL, param.getExpires());
        return ObjectResponse.builder()
                .bucketName(param.getBucketName())
                .objectName(param.getObjectName())
                .url(url)
                .presignedObjectUrl(presignedObjectUrl)
                .build();
    }

    /**
     * 获取文件流
     */
    public InputStream getObject(DownLoadParam param) {
        InputStream inputStream = ossTemplate.getObject(param.getBucketName(),
                param.getObjectName());
        return inputStream;
    }

    /**
     * 设置Bucket的策略
     */
    public void setBucketPolicy(String bucketName,String policy) {
        ossTemplate.setBucketPolicy(bucketName,policy);
    }

    /**
     * 检测允许目录
     */
    private boolean checkAllowDirs(String dir) {
        List<String> dirList = ossProperties.getAllowDirs();
        log.info("allowDirs:{}", JSON.toJSONString(dirList));
        if (dirList.stream().anyMatch(str -> dir.startsWith(str))) {
            return true;
        }
        return false;
    }

    /**
     * 检测禁止后缀
     */
    private boolean checkForbiddenSuffix(String suffix) {
        if (StringUtils.isBlank(suffix)) {
            return false;
        }
        List<String> suffixList = ossProperties.getForbiddenSuffix();
        log.info("forbiddenSuffix:{}", JSON.toJSONString(suffixList));
        if (suffixList.stream().anyMatch(str -> str.startsWith(suffix))) {
            return false;
        }
        return true;
    }

    /**
     * 允许上传的文件后缀
     * @param suffix
     * @return
     */
    private boolean checkAllowSuffix(String suffix) {
        List<String> suffixList = ossProperties.getAllowSuffix();
        log.info("suffixList:{}", JSON.toJSONString(suffixList));
       if(CollectionUtils.isEmpty(suffixList)){
           return true;
       }
        if(suffixList.contains(suffix)){
            return true;
        }
        return false;
    }
    
}
