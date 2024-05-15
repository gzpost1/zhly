package cn.cuiot.dmp.archive;

import cn.cuiot.dmp.archive.application.service.HousesArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


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

    /**
     * 测试档案参数校验
     */
    @Test
    public void testHousesCheckParams(){
        HousesArchivesEntity entity = new HousesArchivesEntity();
        housesArchivesService.checkParams(entity);
    }
}
