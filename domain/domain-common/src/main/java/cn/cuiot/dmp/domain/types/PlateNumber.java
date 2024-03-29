package cn.cuiot.dmp.domain.types;

import cn.cuiot.dmp.exception.InvalidPlateNumberFormat;
import cn.cuiot.dmp.util.Sm4;
import cn.hutool.core.util.DesensitizedUtil;
import lombok.Data;
import lombok.NonNull;

/**
* 号牌号码
* @author zhangjg
* @date 2024/1/11 15:11
*/
@Data
public class PlateNumber {
    private static final String PLATE_NUMBER_REGEX = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";

    private EncryptedValue encryptedValue;

    public PlateNumber(@NonNull String value) {

        if (value.matches(PLATE_NUMBER_REGEX)) {
            this.encryptedValue = new EncryptedValue(Sm4.encryption(value));
        } else {
            // 车牌号码不符合规则
            throw new InvalidPlateNumberFormat(value);
        }
    }

    public PlateNumber(@NonNull EncryptedValue encryptedValue) {
        String decryptValue = encryptedValue.decrypt();
        if (decryptValue.matches(PLATE_NUMBER_REGEX)) {
            this.encryptedValue = encryptedValue;
        } else {
            // 车牌号码不符合规则
            throw new InvalidPlateNumberFormat(decryptValue);
        }
    }

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
     * 获取脱敏的车牌号码
     */
    public String getDesensitizedValue() {
        return DesensitizedUtil.carLicense(decrypt());
    }
}
