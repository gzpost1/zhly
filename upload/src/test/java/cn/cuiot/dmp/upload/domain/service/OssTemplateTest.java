package cn.cuiot.dmp.upload.domain.service;

import cn.cuiot.dmp.upload.UploadApplication;
import cn.cuiot.dmp.upload.domain.entity.BucketItem;
import com.alibaba.fastjson.JSON;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: wuyongchong
 * @date: 2024/4/2 9:17
 */
@Slf4j
@SpringBootTest(classes = UploadApplication.class)
public class OssTemplateTest {

    @Autowired
    private OssTemplate ossTemplate;

    /**
     * 获取桶列表
     */
    @Test
    void listBuckets() throws Exception {
        List<BucketItem> bucketItems = ossTemplate.listBuckets();
        log.info("bucketItems:{}", JSON.toJSONString(bucketItems));
    }

}
