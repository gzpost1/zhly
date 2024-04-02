package cn.cuiot.dmp.upload.application.service;

import cn.cuiot.dmp.upload.UploadApplication;
import cn.cuiot.dmp.upload.application.dto.UploadParam;
import cn.cuiot.dmp.upload.application.dto.UploadResponse;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: wuyongchong
 * @date: 2024/4/2 8:50
 */
@Slf4j
@SpringBootTest(classes = UploadApplication.class)
public class OssUploadServiceTest {

    @Autowired
    private OssUploadService ossUploadService;

    @Test
    void upload() throws Exception {

    }

    @Test
    void originUpload() {
    }

    @Test
    void multiUpload() {
    }

    @Test
    void chunkUpload() {
    }

    @Test
    void getObjectUrl() {
    }

    @Test
    void getObject() {
    }
}