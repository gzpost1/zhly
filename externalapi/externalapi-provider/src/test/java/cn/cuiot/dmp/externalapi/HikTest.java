package cn.cuiot.dmp.externalapi;

import cn.cuiot.dmp.common.bean.external.HIKEntranceGuardBO;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.provider.ExternalapiApplication;
import cn.cuiot.dmp.externalapi.service.vendor.hik.HikApiFeignService;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.HikAcsListReq;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikAcsListResp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest(classes = ExternalapiApplication.class)
@RunWith(SpringRunner.class)
public class HikTest {

    @Autowired
    private HikApiFeignService hikApiFeignService;

    @Test
    public void acsDeviceSearch() {
        HikAcsListReq req = new HikAcsListReq();
        req.setPageNo(1L);
        req.setPageSize(100L);

        HIKEntranceGuardBO bo = new HIKEntranceGuardBO();
        HikAcsListResp resp = hikApiFeignService.queryAcsDeviceSearch(req, bo);
        log.info("resp:{}", JsonUtil.writeValueAsString(resp));
    }
}
