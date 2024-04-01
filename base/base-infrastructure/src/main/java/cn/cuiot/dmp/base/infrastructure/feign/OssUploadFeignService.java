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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 文件操作Feign服务
 *
 * @author: wuyongchong
 * @date: 2024/4/1 20:46
 */
@FeignClient(value = "upload")
public interface OssUploadFeignService {

    /**
     * 普通上传
     */
    @PostMapping("/upload")
    IdmResDTO<FileUploadResponse> upload(@Valid FileUploadParam param);

    /**
     * 原名上传
     */
    @PostMapping("/originUpload")
    IdmResDTO<FileUploadResponse> originUpload(@Valid FileUploadParam param);

    /**
     * 大文件分片上传
     */
    @PostMapping("chunkUpload")
    IdmResDTO<FileUploadResponse> chunkUpload(@Valid FileChunkFileParam param);

    /**
     * 获取对象URL
     */
    @PostMapping("getObjectUrl")
    IdmResDTO<FileObjectResponse> getObjectUrl(@RequestBody @Valid FileObjectParam param);

    /**
     * 批量获取对象URL
     */
    @PostMapping("getObjectUrlList")
    IdmResDTO<List<FileObjectResponse>> getObjectUrlList(
            @RequestBody @Valid FileObjectListParam query);

}
