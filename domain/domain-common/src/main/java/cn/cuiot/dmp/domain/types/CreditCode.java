package cn.cuiot.dmp.domain.types;

import cn.cuiot.dmp.exception.InvalidCreditCodeFormat;
import cn.hutool.core.util.CreditCodeUtil;
import cn.hutool.core.util.DesensitizedUtil;
import lombok.Data;
import lombok.NonNull;

/**
 * @Description 社会信用代码、纳税识别号
 * @Author 犬豪
 * @Date 2024/1/11 11:17
 * @Version V1.0
 */
@Data
public class CreditCode {
    public CreditCode(@NonNull String value) {
        if (!CreditCodeUtil.isCreditCodeSimple(value)) {
            throw new InvalidCreditCodeFormat(value);
        }
        this.value = value;
    }

    private String value;

    public String getDesensitizedValue() {
        //社会信用代码、纳税识别号 保留后3
        return DesensitizedUtil.idCardNum(value, 0, 3);
    }

}
