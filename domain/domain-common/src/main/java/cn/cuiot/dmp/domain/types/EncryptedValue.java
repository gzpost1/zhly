package cn.cuiot.dmp.domain.types;

import cn.cuiot.dmp.util.Sm4;
import lombok.Data;
import lombok.NonNull;

/**
 * @Description 加密值
 * @Author 犬豪
 * @Date 2023/9/4 13:21
 * @Version V1.0
 */
@Data
public class EncryptedValue {
    public EncryptedValue(@NonNull String value) {
        this.value = value;
    }

    /**
     * 加密的值
     */
    private String value;

    public String decrypt() {
        return Sm4.decrypt(value);
    }
}
