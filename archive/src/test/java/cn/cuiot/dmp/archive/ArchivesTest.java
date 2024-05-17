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

import java.util.Collections;


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
        entity.setName("测试第一个房屋修改");
        entity.setId(1790993009622487041L);

        ArchiveBatchUpdateDTO dto = new ArchiveBatchUpdateDTO();
        dto.setLoupanId(2L);
        dto.setIds(Collections.singletonList(1790993009622487041L));
        housesArchivesController.updateByIds(dto);
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
