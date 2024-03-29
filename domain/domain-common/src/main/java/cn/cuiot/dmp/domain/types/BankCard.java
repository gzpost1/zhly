package cn.cuiot.dmp.domain.types;

import cn.cuiot.dmp.exception.InvalidBankCardFormat;
import cn.cuiot.dmp.util.Sm4;
import cn.hutool.core.util.DesensitizedUtil;
import lombok.Data;
import lombok.NonNull;

/**
 * 银行卡号
 *
 * @Author 犬豪
 * @Version V1.0
 */
@Data
public class BankCard {

    private EncryptedValue encryptedValue;

    public BankCard(@NonNull String value) {
        if (!value.chars().allMatch(Character::isDigit)) {
            throw new InvalidBankCardFormat(value);
        }
        this.encryptedValue = new EncryptedValue(Sm4.encryption(value));
    }

    public BankCard (@NonNull EncryptedValue encryptedValue) {
        String value = encryptedValue.decrypt();
        if (!value.chars().allMatch(Character::isDigit)) {
            throw new InvalidBankCardFormat(value);
        }
        this.encryptedValue = encryptedValue;
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

    public String getDesensitizedValue() {
        //银行卡号保留前5后4
        return DesensitizedUtil.idCardNum(encryptedValue.decrypt(), 5, 4);
    }
}
