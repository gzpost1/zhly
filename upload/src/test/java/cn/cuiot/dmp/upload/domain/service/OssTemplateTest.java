package cn.cuiot.dmp.upload.domain.service;

import cn.cuiot.dmp.base.application.rocketmq.MsgChannel;
import cn.cuiot.dmp.common.log.dto.OperateLogDto;
import cn.cuiot.dmp.upload.UploadApplication;
import cn.cuiot.dmp.upload.domain.entity.BucketItem;
import cn.cuiot.dmp.upload.domain.entity.ChunkUploadRequest;
import cn.cuiot.dmp.upload.domain.entity.ChunkUploadResponse;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import javax.annotation.Resource;

/**
 * @author: wuyongchong
 * @date: 2024/4/2 9:17
 */
@Slf4j
@SpringBootTest(classes = UploadApplication.class)
public class OssTemplateTest {

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private MsgChannel msgChannel;


    /**
     * 获取桶列表
     */
    @Test
    void listBuckets() throws Exception {
        List<BucketItem> bucketItems = ossTemplate.listBuckets();
        log.info("bucketItems:{}", JSON.toJSONString(bucketItems));
    }

    /**
     * 创建桶
     */
    @Test
    void createBucket() throws Exception {
        String bucketName = "demo-bucket";
        ossTemplate.createBucket(bucketName);
    }

    /**
     * 判断Bucket是否存在
     */
    @Test
    void bucketExists() throws Exception {
        String bucketName = "zhlypt";
        boolean bucketExists = ossTemplate.bucketExists(bucketName);
        Assertions.assertTrue(bucketExists, "桶不存在");
    }

    /**
     * 获得Bucket的策略
     */
    @Test
    void getBucketPolicy() throws Exception {
        String bucketName = "zhlypt";
        String bucketPolicy = ossTemplate.getBucketPolicy(bucketName);
        log.info("bucketPolicy:{}", bucketPolicy);
    }

    /**
     * 简单上传
     */
    @Test
    void putObject() {
        String bucketName = "zhlypt";
        File file = new File("D:\\minio-cli\\tmp\\124662E02BEA429692AAABECDCBFF39ADCF697DA.jpg");
        try {
            String contentType = Files.probeContentType(Paths.get(file.getName()));
            FileInputStream inputStream = new FileInputStream(file);
            String url = ossTemplate
                    .putObject(bucketName, "img/test.jpg", inputStream, contentType, true);
            log.info("url:{}", url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件流
     */
    @Test
    void getObject() {
        String bucketName = "zhlypt";
        String objectName = "img/test.jpg";
        try (InputStream inputStream = ossTemplate.getObject(bucketName, objectName)) {
            IOUtils.copy(inputStream, new FileOutputStream("D:\\minio-cli\\tmp\\download.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件URL
     */
    @Test
    void getObjectUrl() {
        String bucketName = "zhlypt";
        String objectName = "img/test.jpg";
        String objectUrl = ossTemplate.getObjectUrl(bucketName, objectName, null, null);
        log.info("objectUrl:{}", objectUrl);
    }

    /**
     * 判断object是否存在, true: 存在, false: 不存在
     */
    @Test
    void objectExists() {
        String bucketName = "zhlypt";
        String objectName = "img/test.jpg";
        boolean objectExists = ossTemplate.objectExists(bucketName, objectName);
        Assertions.assertTrue(objectExists, "object不存在");
    }

    /**
     * 获取文件外链
     */
    @Test
    void getPresignedObjectUrl() {
        String bucketName = "zhlypt";
        String objectName = "img/test.jpg";
        String presignedObjectUrl = ossTemplate.getPresignedObjectUrl(bucketName, objectName, 60);
        log.info("presignedObjectUrl:{}", presignedObjectUrl);
    }

    /**
     * 文件分片上传
     */
    @Test
    void chunkUpload() {
        String taskId = IdWorker.getIdStr();
        String bucketName = "zhlypt";
        String dir = "video";
        String fileName = "test.mp4";
        Integer chunkTotal = 4;
        try {
            for (int i = 0; i < 4; i++) {
                ChunkUploadRequest request = new ChunkUploadRequest();
                request.setBucketName(bucketName);
                request.setDir(dir);
                request.setFileName(fileName);
                request.setInputStream(
                        new FileInputStream(new File("D:\\vedio\\chunk\\" + i + ".mp4")));
                request.setExpires(null);
                request.setTaskId(taskId);
                request.setChunk(i);
                request.setChunkTotal(chunkTotal);
                ChunkUploadResponse chunkUploadResponse = ossTemplate.chunkUpload(request);
                log.info("chunkUploadResponse:{}", JSON.toJSONString(chunkUploadResponse));
            }
        } catch (Exception ex) {
            log.error("chunkUpload error", ex);
        }
    }

    @Test
    public void msgTest(){
        OperateLogDto operateLogDto = new OperateLogDto();
        operateLogDto.setOperationById("1");


        // 记录日志
        msgChannel.operationlogOutput().send(MessageBuilder.withPayload(operateLogDto)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

}
