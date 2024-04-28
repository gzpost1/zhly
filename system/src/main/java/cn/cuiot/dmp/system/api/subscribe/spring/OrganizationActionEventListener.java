package cn.cuiot.dmp.system.api.subscribe.spring;

import cn.cuiot.dmp.system.application.param.event.OrganizationCreateActionEvent;
import cn.cuiot.dmp.system.application.param.event.OrganizationDisableActionEvent;
import cn.cuiot.dmp.system.application.param.event.OrganizationEnableActionEvent;
import cn.cuiot.dmp.system.application.param.event.OrganizationUpdateActionEvent;
import cn.cuiot.dmp.system.application.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 企业账号操作事件监听器
 *
 * @author: wuyongchong
 * @date: 2024/4/28 16:40
 */
@Slf4j
@Component
public class OrganizationActionEventListener {

    @Autowired
    private OrganizationService organizationService;

    @EventListener
    public void organizationUpdateActionEvent(OrganizationUpdateActionEvent event) {
        log.info("organizationUpdateActionEvent id:{},data:{}",event.getId(),event);
        //记录变更日志
        organizationService.recordOrganizationChange(event);
    }

    @EventListener
    public void organizationCreateActionEvent(OrganizationCreateActionEvent event) {
        log.info("organizationCreateActionEvent id:{},data:{}",event.getId(),event);
        //记录变更日志
        organizationService.recordOrganizationChange(event);
    }

    @EventListener
    public void organizationEnableActionEvent(OrganizationEnableActionEvent event) {
        log.info("organizationEnableActionEvent id:{},data:{}",event.getId(),event);
        //记录变更日志
        organizationService.recordOrganizationChange(event);
    }

    @EventListener
    public void organizationDisableActionEvent(OrganizationDisableActionEvent event) {
        log.info("organizationDisableActionEvent id:{},data:{}",event.getId(),event);
        //记录变更日志
        organizationService.recordOrganizationChange(event);
    }

}
