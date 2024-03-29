package cn.cuiot.dmp.system.user_manage.messaging;

import cn.cuiot.dmp.system.user_manage.domain.event.ResetPasswordSuccessEvent;

/**
 * @Description 用户消息发送
 * @Author 犬豪
 * @Date 2023/8/30 16:55
 * @Version V1.0
 */
public interface UserDomainEventSender {
    boolean publishResetPasswordSuccessEvent(ResetPasswordSuccessEvent event);
    
}
