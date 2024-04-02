package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.FileObjectParam;
import cn.cuiot.dmp.base.infrastructure.dto.FileObjectResponse;
import cn.cuiot.dmp.base.infrastructure.dto.FileUploadParam;
import cn.cuiot.dmp.base.infrastructure.dto.FileUploadResponse;

/**
 * 文件操作客户端接口
 *
 * @author: wuyongchong
 * @date: 2024/4/1 20:54
 */
public interface OssUploadClient {

    /**
     * 上传文件
     */
    FileUploadResponse uploadFile(FileUploadParam param);


    /**
     * 获取对象访问URL
     */
    FileObjectResponse getObjectUrl(FileObjectParam param);
}
