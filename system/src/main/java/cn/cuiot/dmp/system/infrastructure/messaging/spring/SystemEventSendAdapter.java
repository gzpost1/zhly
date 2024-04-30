package cn.cuiot.dmp.system.infrastructure.messaging.spring;

import cn.cuiot.dmp.system.application.param.event.OrganizationActionEvent;
import cn.cuiot.dmp.system.application.param.event.OrganizationCreateActionEvent;
import cn.cuiot.dmp.system.application.param.event.OrganizationDeleteActionEvent;
import cn.cuiot.dmp.system.application.param.event.OrganizationDisableActionEvent;
import cn.cuiot.dmp.system.application.param.event.OrganizationEnableActionEvent;
import cn.cuiot.dmp.system.application.param.event.OrganizationUpdateActionEvent;
import cn.cuiot.dmp.system.application.param.event.UserActionEvent;
import cn.cuiot.dmp.system.application.param.event.UserCreateActionEvent;
import cn.cuiot.dmp.system.application.param.event.UserDeleteActionEvent;
import cn.cuiot.dmp.system.application.param.event.UserUpdateActionEvent;
import cn.cuiot.dmp.system.infrastructure.entity.OrganizationEntity;
import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description system系统内部事件发送适配器，主要为了包装一层转换
 * @Author yth
 * @Date 2023/8/14 14:55
 * @Version V1.0
 */
@Component
public class SystemEventSendAdapter implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    /**
     * UserActionEvent相关事件
     */
    public void sendUserCreateActionEvent(UserDataEntity userData) {
        applicationContext.publishEvent(buildUserCreateActionEvent(userData));
    }

    public void sendUserUpdateActionEvent(UserDataEntity userData) {
        applicationContext.publishEvent(buildUserUpdateActionEvent(userData));
    }

    public void sendUserDeleteActionEvent(UserDataEntity userData) {
        applicationContext.publishEvent(buildUserDeleteActionEvent(userData));
    }

    /**
     * OrganizationActionEvent
     */
    public void sendOrganizationCreateActionEvent(OrganizationEntity organizationEntity) {
        applicationContext.publishEvent(buildOrganizationCreateActionEvent(organizationEntity));
    }

    public void sendOrganizationDeleteActionEvent(OrganizationEntity organizationEntity) {
        applicationContext.publishEvent(buildOrganizationDeleteActionEvent(organizationEntity));
    }

    public void sendOrganizationUpdateActionEvent(OrganizationEntity organizationEntity) {
        applicationContext.publishEvent(buildOrganizationUpdateActionEvent(organizationEntity));
    }

    public void sendOrganizationEnableActionEvent(OrganizationEntity organizationEntity) {
        applicationContext.publishEvent(buildOrganizationEnableActionEvent(organizationEntity));
    }

    public void sendOrganizationDisableActionEvent(OrganizationEntity organizationEntity) {
        applicationContext.publishEvent(buildOrganizationDisableActionEvent(organizationEntity));
    }

    private OrganizationDeleteActionEvent buildOrganizationDeleteActionEvent(OrganizationEntity organizationEntity) {
        OrganizationDeleteActionEvent organizationDeleteActionEvent = new OrganizationDeleteActionEvent();
        setOrganizationActionEventProperties(organizationEntity, organizationDeleteActionEvent);
        return organizationDeleteActionEvent;
    }

    private OrganizationUpdateActionEvent buildOrganizationUpdateActionEvent(OrganizationEntity organizationEntity) {
        OrganizationUpdateActionEvent organizationUpdateActionEvent = new OrganizationUpdateActionEvent();
        setOrganizationActionEventProperties(organizationEntity, organizationUpdateActionEvent);
        return organizationUpdateActionEvent;
    }

    private OrganizationEnableActionEvent buildOrganizationEnableActionEvent(OrganizationEntity organizationEntity) {
        OrganizationEnableActionEvent event = new OrganizationEnableActionEvent();
        setOrganizationActionEventProperties(organizationEntity, event);
        return event;
    }

    private OrganizationDisableActionEvent buildOrganizationDisableActionEvent(OrganizationEntity organizationEntity) {
        OrganizationDisableActionEvent event = new OrganizationDisableActionEvent();
        setOrganizationActionEventProperties(organizationEntity, event);
        return event;
    }

    private OrganizationCreateActionEvent buildOrganizationCreateActionEvent(OrganizationEntity organizationEntity) {
        OrganizationCreateActionEvent organizationCreateActionEvent = new OrganizationCreateActionEvent();
        setOrganizationActionEventProperties(organizationEntity, organizationCreateActionEvent);
        return organizationCreateActionEvent;
    }

    private static void setOrganizationActionEventProperties(OrganizationEntity organizationEntity,
        OrganizationActionEvent organizationActionEvent) {
        organizationActionEvent.setId(organizationEntity.getId());
        organizationActionEvent.setOrgKey(organizationEntity.getOrgKey());
        organizationActionEvent.setOrgName(organizationEntity.getOrgName());
        organizationActionEvent.setOrgTypeId(organizationEntity.getOrgTypeId());
        organizationActionEvent.setStatus(organizationEntity.getStatus());
        organizationActionEvent.setParentId(organizationEntity.getParentId());
        organizationActionEvent.setOrgOwner(organizationEntity.getOrgOwner());
        organizationActionEvent.setSocialCreditCode(organizationEntity.getSocialCreditCode());
        organizationActionEvent.setCompanyName(organizationEntity.getCompanyName());
        organizationActionEvent.setDescription(organizationEntity.getDescription());
        organizationActionEvent.setMaxDeptHigh(organizationEntity.getMaxDeptHigh());
        organizationActionEvent.setSource(organizationEntity.getSource());
        organizationActionEvent.setCreatedOn(organizationEntity.getCreatedOn());
        organizationActionEvent.setCreatedBy(organizationEntity.getCreatedBy());
        organizationActionEvent.setCreatedByType(organizationEntity.getCreatedByType());
        organizationActionEvent.setDeletedFlag(organizationEntity.getDeletedFlag());
        organizationActionEvent.setUpdatedOn(organizationEntity.getUpdatedOn());
        organizationActionEvent.setUpdatedBy(organizationEntity.getUpdatedBy());
        organizationActionEvent.setUpdatedByType(organizationEntity.getUpdatedByType());
    }

    private UserDeleteActionEvent buildUserDeleteActionEvent(UserDataEntity userData) {
        UserDeleteActionEvent userDeleteActionEvent = new UserDeleteActionEvent();
        setUserActionEventProperties(userData, userDeleteActionEvent);
        return userDeleteActionEvent;
    }

    private UserUpdateActionEvent buildUserUpdateActionEvent(UserDataEntity userData) {
        UserUpdateActionEvent userUpdateActionEvent = new UserUpdateActionEvent();
        setUserActionEventProperties(userData, userUpdateActionEvent);
        return userUpdateActionEvent;
    }

    private static void setUserActionEventProperties(UserDataEntity userData, UserActionEvent userActionEvent) {
        userActionEvent.setId(userData.getId());
        userActionEvent.setUserId(userData.getUserId());
        userActionEvent.setUsername(userData.getUsername());
        userActionEvent.setPassword(userData.getPassword());
        userActionEvent.setEmail(userData.getEmail());
        userActionEvent.setPhoneNumber(userData.getPhoneNumber());
        userActionEvent.setAvatar(userData.getAvatar());
        userActionEvent.setStatus(userData.getStatus());
        userActionEvent.setLastOnlineIp(userData.getLastOnlineIp());
        userActionEvent.setLastOnlineAddress(userData.getLastOnlineAddress());
        userActionEvent.setLastOnlineOn(userData.getLastOnlineOn());
        userActionEvent.setUserType(userData.getUserType());
        userActionEvent.setContactPerson(userData.getContactPerson());
        userActionEvent.setContactAddress(userData.getContactAddress());
        userActionEvent.setLongTimeLogin(userData.getLongTimeLogin());
        userActionEvent.setCreatedOn(userData.getCreatedOn());
        userActionEvent.setCreatedBy(userData.getCreatedBy());
        userActionEvent.setCreatedByType(userData.getCreatedByType());
        userActionEvent.setUpdatedOn(userData.getUpdatedOn());
        userActionEvent.setUpdatedBy(userData.getUpdatedBy());
        userActionEvent.setUpdatedByType(userData.getUpdatedByType());
        userActionEvent.setDeletedFlag(userData.getDeletedFlag());
    }

    private UserCreateActionEvent buildUserCreateActionEvent(UserDataEntity userData) {
        UserCreateActionEvent userCreateActionEvent = new UserCreateActionEvent();
        setUserActionEventProperties(userData, userCreateActionEvent);
        return userCreateActionEvent;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
