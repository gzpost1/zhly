package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.FileObjectParam;
import cn.cuiot.dmp.base.infrastructure.dto.FileObjectResponse;
import cn.cuiot.dmp.base.infrastructure.dto.FileUploadParam;
import cn.cuiot.dmp.base.infrastructure.dto.FileUploadResponse;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.system.SystemApplication;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: wuyongchong
 * @date: 2024/4/2 9:17
 */
@Slf4j
@SpringBootTest(classes = SystemApplication.class)
public class OssUploadClientTest {

    @Autowired
    private OssUploadClient ossUploadClient;

    /**
     * 获取桶列表
     */
    @Test
    void uploadFile() throws Exception {
        FileUploadParam param = new FileUploadParam();
        param.setFile(new File("D:\\minio-cli\\tmp\\test.jpg"));
        param.setDir("excel");
        param.setExpires(86400);
        param.setOriginName(false);
        param.setCompressImg(EntityConstants.NO);
        FileUploadResponse uploadResponse = ossUploadClient.uploadFile(param);
        Assert.assertNotNull(uploadResponse);
        log.info("uploadFile response:{}", uploadResponse);
    }

    /**
     * 获取对象访问URL
     */
    @Test
    void getObjectUrl() throws Exception {
        FileObjectParam param = new FileObjectParam();
        param.setObjectName("excel/202404/1775037889042952194.jpg");
        param.setExpires(86400);
        FileObjectResponse response = ossUploadClient.getObjectUrl(param);
        Assert.assertNotNull(response);
        log.info("getObjectUrl response:{}", response);
    }

}
