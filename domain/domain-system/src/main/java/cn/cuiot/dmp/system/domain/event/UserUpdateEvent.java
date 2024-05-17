package cn.cuiot.dmp.system.domain.event;

import cn.cuiot.dmp.domain.event.DomainEvent;
import cn.cuiot.dmp.system.domain.entity.User;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @Author 犬豪
 * @Date 2023/8/31 14:15
 * @Version V1.0
 */

@Data
@SuperBuilder
public class UserUpdateEvent implements DomainEvent {

    protected User user;

}
