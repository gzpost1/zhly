package cn.cuiot.dmp.domain.types.id;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * @Author 犬豪
 * @Date 2023/8/28 09:31
 * @Version V1.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserId extends LongId implements Identifier {
    public UserId(@NonNull Long id) {
        super(id);
    }

    public UserId(@NonNull String id) {
        super(Long.parseLong(id));
    }
}
