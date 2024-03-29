package cn.cuiot.dmp.domain.types;

import cn.cuiot.dmp.exception.InvalidTelNumberFormat;
import cn.hutool.core.util.PhoneUtil;
import lombok.Data;
import lombok.NonNull;

/**
 * @Description 座机号码
 * @Author 犬豪
 * @Version V1.0
 */
@Data
public class TelNumber {
    public TelNumber(@NonNull String value) {
        if (!PhoneUtil.isTel(value)) {
            throw new InvalidTelNumberFormat(value);
        }
        this.value = value;
    }

    private String value;

}
