package cn.cuiot.dmp.externalapi;

import cn.cuiot.dmp.externalapi.provider.ExternalapiApplication;
import cn.cuiot.dmp.externalapi.service.query.hik.HaikangAcsDoorControlDto;
import cn.cuiot.dmp.externalapi.service.service.hik.HaikangAcsDoorService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: wuyongchong
 * @date: 2024/10/11 16:47
 */
@Slf4j
@SpringBootTest(classes = ExternalapiApplication.class)
@RunWith(SpringRunner.class)
public class HaikangAcsDoorServiceTest {

    @Autowired
    private HaikangAcsDoorService haikangAcsDoorService;

    @Test
    public void doControlDoorTest(){
        HaikangAcsDoorControlDto dto = new HaikangAcsDoorControlDto();
        dto.setCompanyId(1810947563259998210L);
        dto.setControlType("2");
        dto.setIndexCodes(Lists.newArrayList("c788bb00f64249629036ea2d205292ca"));

        haikangAcsDoorService.doControlDoor(dto);

    }

    @Test
    public void queryDoorStateTest(){

    }

}
