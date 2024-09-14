package cn.cuiot.dmp.externalapi;

import cn.cuiot.dmp.common.bean.external.GWEntranceGuardBO;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.provider.ExternalapiApplication;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.DmpDeviceCreateReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.DmpDeviceReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.BaseDmpResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDeviceResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.dmp.DmpDeviceRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest(classes = ExternalapiApplication.class)
@RunWith(SpringRunner.class)
public class DmpTest {

    @Autowired
    private DmpDeviceRemoteService dmpDeviceRemoteService;

    @Test
    public void getDevice() {
        DmpDeviceReq req = new DmpDeviceReq();
        req.setDeviceKey("ZYTB215F002L00700161");
        req.setProductKey("cukppp0rn2bk5xsc");

        GWEntranceGuardBO bo = new GWEntranceGuardBO();
        bo.setAppId("jG4KsHbMFT");
        bo.setAppSecret("CUCNBMPmjuv8gDHlSKvI8h15H2YaDJ");
        bo.setProductKey("cu1fmdm1vngrxzhf");
        BaseDmpResp<DmpDeviceResp> device = dmpDeviceRemoteService.getDevice(req, bo);
        log.info("device......{}", JsonUtil.writeValueAsString(device));
    }

    @Test
    public void createDevice() {
        DmpDeviceCreateReq req = new DmpDeviceCreateReq();
        req.setProductKey("cukppp0rn2bk5xsc");
        req.setDeviceKey("6666666688");
        req.setDeviceName("测试api创建设备");
        req.setDescription("测试数据");

        GWEntranceGuardBO bo = new GWEntranceGuardBO();
        bo.setAppId("jG4KsHbMFT");
        bo.setAppSecret("CUCNBMPmjuv8gDHlSKvI8h15H2YaDJ");
        bo.setProductKey("cukppp0rn2bk5xsc");
        dmpDeviceRemoteService.createDevice(req, bo);
    }

    @Test
    public void deleteDevice() {
        DmpDeviceReq req = new DmpDeviceReq();
        req.setProductKey("cukppp0rn2bk5xsc");
        req.setDeviceKey("1834454175973978114");

        GWEntranceGuardBO bo = new GWEntranceGuardBO();
        bo.setAppId("jG4KsHbMFT");
        bo.setAppSecret("CUCNBMPmjuv8gDHlSKvI8h15H2YaDJ");
        bo.setProductKey("cukppp0rn2bk5xsc");
        dmpDeviceRemoteService.deleteDevice(req, bo);
    }
}
