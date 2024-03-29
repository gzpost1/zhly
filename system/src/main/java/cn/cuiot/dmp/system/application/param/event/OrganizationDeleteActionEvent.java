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
public class OrganizationDeleteActionEvent extends OrganizationActionEvent {
    public OrganizationDeleteActionEvent() {
        setAction(EventActionEnum.DELETE.getValue());
    }
}
