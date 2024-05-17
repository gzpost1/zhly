package cn.cuiot.dmp.system.domain.types.verify;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * 用户验证码
 * 
 * @Author 犬豪
 * @Date 2023/8/30 14:27
 * @Version V1.0
 */
@ToString
@Getter
public abstract class VerificationCode {

    protected VerificationCode(@NonNull String code) {
        this.code = code;
    }

    /**
     * 验证码
     */
    private String code;

}
