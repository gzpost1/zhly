package cn.cuiot.dmp.archive;

import cn.cuiot.dmp.archive.api.controller.DeviceArchivesController;
import cn.cuiot.dmp.archive.api.controller.HousesArchivesController;
import cn.cuiot.dmp.archive.api.controller.ParkingArchivesController;
import cn.cuiot.dmp.archive.api.controller.RoomArchivesController;
import cn.cuiot.dmp.archive.application.param.dto.ArchiveBatchUpdateDTO;
import cn.cuiot.dmp.archive.application.param.query.HousesArchivesQuery;
import cn.cuiot.dmp.archive.application.service.HousesArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.DeviceArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.ParkingArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.RoomArchivesEntity;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.*;


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

    @Autowired
    private RoomArchivesController roomArchivesController;

    @Autowired
    private ParkingArchivesController parkingArchivesController;

    @Autowired
    private DeviceArchivesController deviceArchivesController;

    @Test
    public void testCURD(){
        HousesArchivesEntity entity = new HousesArchivesEntity();
        entity.setName("测试第二");
        entity.setLoupanId(1L);
        entity.setRoomNum("dsafkd");
        entity.setCode("liujianyu5");
        List<String> image = new ArrayList<>();
        image.add("123.jpg");
        image.add("234.jpg");
        entity.setListingImages(image);
        List<Long> list = new ArrayList<>();
        list.add(2L);
        list.add(3L);
        entity.setBasicServices(list);
        housesArchivesController.create(entity);

        HousesArchivesQuery query = new HousesArchivesQuery();
        query.setLoupanId(1L);
        IdmResDTO<IPage<HousesArchivesEntity>> res = housesArchivesController.queryForPage(query);
        Long r = res.getData().getRecords().get(1).getBasicServices().get(0)+res.getData().getRecords().get(1).getBasicServices().get(1);
        System.out.println(r);
        System.out.println(res);
    }

    /**
     * 测试档案参数校验
     */
    @Test
    public void testHousesCheckParams(){
        Set<Long> cur = new HashSet<>();
        cur.add(1L);
        cur.add(2L);
        List<Long> list = new ArrayList<>();
        list.add(3L);
        list.add(4L);
        cur.addAll(list);
        System.out.println(cur);
    }
}
