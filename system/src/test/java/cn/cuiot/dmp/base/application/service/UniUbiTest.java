package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.system.SystemApplication;
import cn.cuiot.dmp.system.api.controller.app.UniUbiController;
import cn.cuiot.dmp.system.application.service.UniUbiService;
import cn.cuiot.dmp.system.infrastructure.entity.bean.UniUbiDeviceQueryReq;
import cn.cuiot.dmp.system.infrastructure.entity.vo.UniUbiEntranceGuardQueryVO;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: wuyongchong
 * @date: 2024/4/2 9:17
 */
@Slf4j
@SpringBootTest(classes = SystemApplication.class)
public class UniUbiTest {

    @Autowired
    private UniUbiService uniUbiService;
    @Autowired
    private UniUbiController uniUbiController;


    @Test
    void queryForPage_test() {
        UniUbiEntranceGuardQueryVO query = new UniUbiEntranceGuardQueryVO();
        query.setName("大厅2");
        System.out.println(JSONObject.toJSONString(uniUbiController.queryForPage(query)));
    }

    @Test
    void queryDevicePageV2_test() {
        UniUbiDeviceQueryReq query = new UniUbiDeviceQueryReq();
        System.out.println(uniUbiService.queryDevicePageV2(query));
    }

}
