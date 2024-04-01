package cn.cuiot.dmp.base.application.service.impl;

import cn.cuiot.dmp.base.application.service.OssUploadClient;
import cn.cuiot.dmp.base.infrastructure.dto.FileObjectParam;
import cn.cuiot.dmp.base.infrastructure.dto.FileObjectResponse;
import cn.cuiot.dmp.base.infrastructure.dto.FileUploadParam;
import cn.cuiot.dmp.base.infrastructure.dto.FileUploadResponse;
import cn.cuiot.dmp.base.infrastructure.feign.OssUploadFeignService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import java.io.File;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: wuyongchong
 * @date: 2024/4/1 20:55
 */
@Slf4j
@Service
public class OssUploadClientImpl implements OssUploadClient {

    @Autowired
    private OssUploadFeignService ossUploadFeignService;

    /**
     * 上传文件
     */
    @Override
    public FileUploadResponse uploadFile(FileUploadParam param) {
        log.info("OssUploadClient==upload,bucketName:{},dir:{},expires:{}", param.getBucketName(),
                param.getDir(), param.getExpires());
        File file = param.getFile();
        if (Objects.isNull(file)) {
            log.error("OssUploadClient==upload,file null");
            throw new RuntimeException("上传文件为空");
        }
        try {
            IdmResDTO<FileUploadResponse> uploadResult = null;
            if (Boolean.TRUE.equals(param.getOriginName())) {
                uploadResult = ossUploadFeignService
                        .originUpload(param);
            } else {
                uploadResult = ossUploadFeignService.upload(param);
            }
            if (Objects.nonNull(uploadResult) && ResultCode.SUCCESS.getCode()
                    .equals(uploadResult.getCode())) {
                return uploadResult.getData();
            }
            throw new RuntimeException("上传文件失败");
        } catch (Exception ex) {
            log.info("OssUploadClient==upload==fail", ex);
            throw new RuntimeException("上传文件失败");
        }
    }

    /**
     * 获取对象访问URL
     */
    @Override
    public FileObjectResponse getObjectUrl(FileObjectParam param) {
        log.info("OssUploadClient==getObjectUrl,bucketName:{},objectName:{},expires:{}",
                param.getBucketName(),
                param.getObjectName(), param.getExpires());
        try {
            IdmResDTO<FileObjectResponse> uploadResult = ossUploadFeignService.getObjectUrl(param);
            if (Objects.nonNull(uploadResult) && ResultCode.SUCCESS.getCode()
                    .equals(uploadResult.getCode())) {
                return uploadResult.getData();
            }
            throw new RuntimeException("获取对象URL失败");
        } catch (Exception ex) {
            log.info("OssUploadClient==getObjectUrl==fail", ex);
            throw new RuntimeException("获取对象URL失败");
        }
    }
}
