package cn.cuiot.dmp.domain.types.id;

import cn.cuiot.dmp.util.Sm4;
import lombok.Data;

/**
 * @Description Long类型ID基础类
 * @Author 犬豪
 * @Date 2023/8/28 10:08
 * @Version V1.0
 */
@Data
public abstract class LongId implements Identifier {
    protected LongId(Long value) {
        this.value = value;
    }

    private Long value;

    public String getStrValue() {
        return String.valueOf(value);
    }


    public String encryptionSm4() {
        return Sm4.encryption(getStrValue());
    }
}
