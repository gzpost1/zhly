package cn.cuiot.dmp.system.application.param.event;

import cn.cuiot.dmp.common.enums.EventActionEnum;
import lombok.Data;

/**
 * @Description
 * @Author yth
 * @Date 2023/8/14 14:55
 * @Version V1.0
 */
@Data
public class UserDeleteActionEvent extends UserActionEvent {
    public UserDeleteActionEvent() {
        setAction(EventActionEnum.DELETE.getValue());
    }

}
