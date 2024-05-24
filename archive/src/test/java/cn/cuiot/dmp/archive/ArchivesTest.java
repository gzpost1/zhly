package cn.cuiot.dmp.archive;

import cn.cuiot.dmp.archive.api.controller.HousesArchivesController;
import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.service.HousesArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author liujianyu
 * @description 档案测试类
 * @since 2024-05-15 14:34
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArchiveApplication.class)
public class ArchivesTest {

    @Autowired
    private HousesArchivesService housesArchivesService;

    @Autowired
    private HousesArchivesController housesArchivesController;

    @Test
    public void testCURD(){
        HousesArchivesEntity entity = new HousesArchivesEntity();
        entity.setName("测试第二个房屋修改");
        entity.setLoupanId(1L);
        entity.setCode("liujianyu3");
        entity.setRoomNum("10010");
        List<String> image = new ArrayList<>();
        image.add("123.jpg");
        image.add("234.jpg");
        entity.setListingImages(image);
        List<Long> basic = new ArrayList<>();
        basic.add(1L);
        basic.add(2L);
        entity.setBasicServices(basic);
        housesArchivesController.create(entity);
    }

    /**
     * 测试档案参数校验
     */
    @Test
    public void testHousesCheckParams(){
        HousesArchivesEntity entity = new HousesArchivesEntity();
        housesArchivesService.checkParams(entity);
    }
}
