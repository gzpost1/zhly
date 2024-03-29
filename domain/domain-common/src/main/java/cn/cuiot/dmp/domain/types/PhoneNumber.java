package cn.cuiot.dmp.domain.types;

import cn.cuiot.dmp.exception.InvalidPhoneNumberFormat;
import cn.cuiot.dmp.util.Sm4;
import cn.hutool.core.util.DesensitizedUtil;
import lombok.Data;
import lombok.NonNull;

/**
 * @Author 犬豪
 * @Date 2023/8/28 11:19
 * @Version V1.0
 */
@Data
public class PhoneNumber {
    private static final String PHONE_NUMBER_REGEX = "^1[3456789]\\d{9}$";

    public PhoneNumber(@NonNull String value) {

        if (value.matches(PHONE_NUMBER_REGEX)) {
            this.encryptedValue = new EncryptedValue(Sm4.encryption(value));
        } else {
            // 手机号不符合规则
            throw new InvalidPhoneNumberFormat(value);
        }
    }

    public PhoneNumber(@NonNull EncryptedValue encryptedValue) {
        String decryptValue = encryptedValue.decrypt();
        if (decryptValue.matches(PHONE_NUMBER_REGEX)) {
            this.encryptedValue = encryptedValue;
        } else {
            // 手机号不符合规则
            throw new InvalidPhoneNumberFormat(decryptValue);
        }
    }

    /**
     *
     * @param value value
     * @param validate 是否需要校验
     */
    public PhoneNumber(@NonNull String value, boolean validate) {
        if(validate){
            if (value.matches(PHONE_NUMBER_REGEX)) {
                this.encryptedValue = new EncryptedValue(Sm4.encryption(value));
            } else {
                // 手机号不符合规则
                throw new InvalidPhoneNumberFormat(value);
            }
        }else {
            this.encryptedValue = new EncryptedValue(Sm4.encryption(value));
        }
    }

    /**
    *
    * @param encryptedValue value
    * @param validate 是否需要校验
    */
    public PhoneNumber(@NonNull EncryptedValue encryptedValue, boolean validate) {
        if(validate){
            String decryptValue = encryptedValue.decrypt();
            if (decryptValue.matches(PHONE_NUMBER_REGEX)) {
                this.encryptedValue = encryptedValue;
            } else {
                // 手机号不符合规则
                throw new InvalidPhoneNumberFormat(decryptValue);
            }
        }else {
            this.encryptedValue = encryptedValue;
        }
    }

    private EncryptedValue encryptedValue;

    /**
     * 获取加密值
     */
    public String getEncryptedValue() {
        return encryptedValue.getValue();
    }

    /**
     * 获取明文
     */
    public String decrypt() {
        return encryptedValue.decrypt();
    }

    /**
     * 获取脱敏的手机号
     */
    public String getDesensitizedValue() {
        return DesensitizedUtil.mobilePhone(decrypt());
    }

}
