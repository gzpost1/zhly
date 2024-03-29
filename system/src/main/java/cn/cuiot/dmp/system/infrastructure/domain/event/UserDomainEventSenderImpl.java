package cn.cuiot.dmp.system.infrastructure.domain.event;

import cn.cuiot.dmp.system.user_manage.domain.event.ResetPasswordSuccessEvent;
import cn.cuiot.dmp.system.user_manage.messaging.UserDomainEventSender;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author 犬豪
 * @Date 2023/8/30 17:57
 * @Version V1.0
 */
@Component
public class UserDomainEventSenderImpl implements UserDomainEventSender, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public boolean publishResetPasswordSuccessEvent(ResetPasswordSuccessEvent event) {
        applicationContext.publishEvent(event);
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
