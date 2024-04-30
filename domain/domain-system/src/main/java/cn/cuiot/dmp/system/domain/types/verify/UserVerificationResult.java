package cn.cuiot.dmp.system.domain.types.verify;

import cn.cuiot.dmp.system.domain.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @Description 用户验证结果
 * @Author 犬豪
 * @Date 2023/8/31 09:22
 * @Version V1.0
 */
@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
public class UserVerificationResult extends VerificationResult {
    public static final UserVerificationResult FAIL = new UserVerificationResult(false);

    public UserVerificationResult(boolean pass) {
        super(pass);
    }

    public UserVerificationResult(boolean pass, User user) {
        super(pass);
        this.user = user;
    }

    private User user;

}
