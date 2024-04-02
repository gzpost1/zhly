package cn.cuiot.dmp.base.infrastructure.feign;

import cn.cuiot.dmp.base.infrastructure.dto.FileChunkFileParam;
import cn.cuiot.dmp.base.infrastructure.dto.FileObjectListParam;
import cn.cuiot.dmp.base.infrastructure.dto.FileObjectParam;
import cn.cuiot.dmp.base.infrastructure.dto.FileObjectResponse;
import cn.cuiot.dmp.base.infrastructure.dto.FileUploadParam;
import cn.cuiot.dmp.base.infrastructure.dto.FileUploadResponse;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import java.util.List;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 文件操作Feign服务
 *
 * @author: wuyongchong
 * @date: 2024/4/1 20:46
 */
@Component
@FeignClient(value = "community-upload")
public interface OssUploadFeignService {

    /**
     * 普通上传
     */
    @PostMapping(value = "/oss/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    IdmResDTO<FileUploadResponse> upload(@Valid FileUploadParam param);

    /**
     * 原名上传
     */
    @PostMapping(value = "/oss/originUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    IdmResDTO<FileUploadResponse> originUpload(@Valid FileUploadParam param);

    /**
     * 大文件分片上传
     */
    @PostMapping(value = "/oss/chunkUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    IdmResDTO<FileUploadResponse> chunkUpload(@Valid FileChunkFileParam param);

    /**
     * 获取对象URL
     */
    @PostMapping("/oss/getObjectUrl")
    IdmResDTO<FileObjectResponse> getObjectUrl(@RequestBody @Valid FileObjectParam param);

    /**
     * 批量获取对象URL
     */
    @PostMapping("/oss/getObjectUrlList")
    IdmResDTO<List<FileObjectResponse>> getObjectUrlList(
            @RequestBody @Valid FileObjectListParam query);

}
