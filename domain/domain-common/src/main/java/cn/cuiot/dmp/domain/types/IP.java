package cn.cuiot.dmp.domain.types;

import lombok.Data;
import lombok.NonNull;

/**
 * @Author 犬豪
 * @Date 2023/8/28 11:23
 * @Version V1.0
 */
@Data
public class IP {
    public IP(@NonNull String value) {
        this.value = value;
    }

    private String value;
}
