package cn.cuiot.dmp.upload.api.controller;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.upload.application.dto.ChunkFileParam;
import cn.cuiot.dmp.upload.application.dto.DownLoadParam;
import cn.cuiot.dmp.upload.application.dto.MultipartFileParam;
import cn.cuiot.dmp.upload.application.dto.ObjectListParam;
import cn.cuiot.dmp.upload.application.dto.ObjectParam;
import cn.cuiot.dmp.upload.application.dto.ObjectResponse;
import cn.cuiot.dmp.upload.application.dto.UploadParam;
import cn.cuiot.dmp.upload.application.dto.UploadResponse;
import cn.cuiot.dmp.upload.application.service.OssUploadService;
import cn.cuiot.dmp.upload.infrastructure.config.OssProperties;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OSS文件上传
 *
 * @author: wuyongchong
 * @date: 2023/10/20 13:54
 */
@Slf4j
@RestController
@RequestMapping("/oss")
public class OssUploadController {

    @Autowired
    private OssUploadService ossUploadService;

    @Autowired
    private OssProperties ossProperties;

    /**
     * 普通上传
     */
    @PostMapping("/upload")
    public IdmResDTO<UploadResponse> upload(@Valid UploadParam param) throws Exception {
        if (StringUtils.isBlank(param.getBucketName())) {
            param.setBucketName(ossProperties.getBucketName());
        }
        UploadResponse uploadResponse = ossUploadService.upload(param);
        return IdmResDTO.success(uploadResponse);
    }

    /**
     * 原名上传
     */
    @PostMapping("/originUpload")
    public IdmResDTO<UploadResponse> originUpload(@Valid UploadParam param) throws Exception {
        if (StringUtils.isBlank(param.getBucketName())) {
            param.setBucketName(ossProperties.getBucketName());
        }
        UploadResponse uploadResponse = ossUploadService.originUpload(param);
        return IdmResDTO.success(uploadResponse);
    }

    /**
     * 批量文件上传
     */
    @PostMapping("/multiUpload")
    public IdmResDTO<List<UploadResponse>> multiUpload(@Valid MultipartFileParam param)
            throws Exception {
        if (StringUtils.isBlank(param.getBucketName())) {
            param.setBucketName(ossProperties.getBucketName());
        }
        List<UploadResponse> list = ossUploadService.multiUpload(param);
        return IdmResDTO.success(list);
    }

    /**
     * 大文件分片上传
     */
    @PostMapping("chunkUpload")
    public IdmResDTO<UploadResponse> chunkUpload(@Valid ChunkFileParam param) throws Exception {
        if (StringUtils.isBlank(param.getBucketName())) {
            param.setBucketName(ossProperties.getBucketName());
        }
        UploadResponse uploadResponse = ossUploadService.chunkUpload(param);
        return IdmResDTO.success(uploadResponse);
    }

    /**
     * 获取对象URL
     */
    @PostMapping("getObjectUrl")
    public IdmResDTO<ObjectResponse> getObjectUrl(@RequestBody @Valid ObjectParam param)
            throws Exception {
        if (StringUtils.isBlank(param.getBucketName())) {
            param.setBucketName(ossProperties.getBucketName());
        }
        ObjectResponse response = ossUploadService.getObjectUrl(param);
        return IdmResDTO.success(response);
    }

    /**
     * 批量获取对象URL
     */
    @PostMapping("getObjectUrlList")
    public IdmResDTO<List<ObjectResponse>> getObjectUrlList(
            @RequestBody @Valid ObjectListParam query)
            throws Exception {
        if (StringUtils.isBlank(query.getBucketName())) {
            query.setBucketName(ossProperties.getBucketName());
        }
        List<ObjectResponse> resultList = Lists.newArrayList();
        List<String> objectNames = query.getObjectNames();
        for (String objectName : objectNames) {
            ObjectParam param = new ObjectParam();
            param.setBucketName(query.getBucketName());
            param.setObjectName(objectName);
            param.setExpires(query.getExpires());
            ObjectResponse response = ossUploadService.getObjectUrl(param);
            resultList.add(response);
        }
        return IdmResDTO.success(resultList);
    }

    /**
     * 下载对象
     */
    @PostMapping("downLoadObject")
    public void downLoadObject(@RequestBody @Valid DownLoadParam param,
            HttpServletResponse response) {
        if (StringUtils.isBlank(param.getBucketName())) {
            param.setBucketName(ossProperties.getBucketName());
        }
        InputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            String fileName = param.getObjectName()
                    .substring(param.getObjectName().lastIndexOf("/") + 1);
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder
                    .encode(fileName, "UTF-8"));
            outputStream = response.getOutputStream();
            inputStream = ossUploadService.getObject(param);
            FileCopyUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            log.error("下载失败", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
