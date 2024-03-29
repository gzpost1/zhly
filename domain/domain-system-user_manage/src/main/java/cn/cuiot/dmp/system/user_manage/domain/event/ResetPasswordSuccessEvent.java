package cn.cuiot.dmp.system.user_manage.domain.event;

import cn.cuiot.dmp.domain.types.Password;
import cn.cuiot.dmp.system.user_manage.domain.types.verify.UserVerificationCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 重置密码成功事件
 *
 * @Author 犬豪
 * @Date 2023/8/30 16:50
 * @Version V1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class ResetPasswordSuccessEvent extends UserUpdateEvent {

    private Password oldPassword;
    private Password newPassword;
    private UserVerificationCode userVerificationCode;

}
