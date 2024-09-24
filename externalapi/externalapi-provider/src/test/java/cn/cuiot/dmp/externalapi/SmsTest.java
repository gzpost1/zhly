package cn.cuiot.dmp.externalapi;

import cn.cuiot.dmp.externalapi.provider.ExternalapiApplication;
import cn.cuiot.dmp.sms.vendor.SmsApiFeignService;
import cn.cuiot.dmp.sms.vendor.resp.SmsBaseResp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExternalapiApplication.class)
public class SmsTest {

    @Autowired
    private SmsApiFeignService smsApiFeignService;

    @Test
    public void getBalance() {
        SmsBaseResp<Integer> balance = smsApiFeignService.getBalance();
        System.out.println(balance);
    }
}
