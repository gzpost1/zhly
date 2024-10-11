package cn.cuiot.dmp.pay.service.service.init;

import cn.cuiot.dmp.pay.service.service.entity.SysPayChannelSetting;
import cn.cuiot.dmp.pay.service.service.normal.PaySettingInit;
import cn.cuiot.dmp.pay.service.service.service.SysPayChannelSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 启动自动注册支付服务
 *
 * @author huq
 * @ClassName AutoRegisterPayConfig
 * @Date 2021/6/15 16:24
 **/
@Slf4j
@Component
@Order(1)
public class AutoRegisterPayConfig implements ApplicationListener<ApplicationInitializingEvent> {

    @Autowired
    private SysPayChannelSettingService settingService;

    @Autowired
    private List<PaySettingInit> settingInitList;

    @Override
    public void onApplicationEvent(ApplicationInitializingEvent applicationInitializingEvent) {
        List<SysPayChannelSetting> channelSettingList = settingService.list();
        channelSettingList.forEach(item -> {
            try {
                settingService.initPaySetting(item);
            } catch (Exception ex) {
                ex.printStackTrace();
                log.warn("支付渠道注册失败：{},失败渠道：{}", ex.getMessage(), item.getName());
            }
        });
        for (PaySettingInit paySettingInit : settingInitList) {
            paySettingInit.init();
        }
        log.info("支付渠道注册完成");
    }
}
