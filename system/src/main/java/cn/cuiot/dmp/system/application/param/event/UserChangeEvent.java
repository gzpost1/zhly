package cn.cuiot.dmp.system.application.param.event;

import cn.cuiot.dmp.system.application.enums.UserChangeActionEnum;
import org.springframework.context.ApplicationEvent;

/**
 * 账户、用户改变事件
 * @author lixf
 */
public class UserChangeEvent extends ApplicationEvent {
    private UserChangeActionEnum action;

    public UserChangeEvent(Object source, UserChangeActionEnum action) {
        super(source);
        this.action = action;
    }

    public UserChangeActionEnum getAction() {
        return action;
    }

}
