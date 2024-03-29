package cn.cuiot.dmp.domain.types;

import cn.cuiot.dmp.util.Sm4;
import cn.hutool.core.util.DesensitizedUtil;
import lombok.Data;
import lombok.NonNull;

/**
* 姓名
* @author zhangjg
* @date 2024/1/24 20:03
*/
@Data
public class Name {

    public Name(@NonNull String value) {
        this.encryptedValue = new EncryptedValue(Sm4.encryption(value));
    }

    public Name(@NonNull EncryptedValue encryptedValue) {
        this.encryptedValue = encryptedValue;
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
     * 获取脱敏的姓名
     */
    public String getDesensitizedValue() {
        return DesensitizedUtil.chineseName(decrypt());
    }

}
