package cn.cuiot.dmp.domain.types.id;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * @Author 犬豪
 * @Date 2023/8/28 10:48
 * @Version V1.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OrganizationId extends LongId implements Identifier {
    public OrganizationId(@NonNull Long id) {
        super(id);
    }

    public OrganizationId(@NonNull String id) {
        super(Long.parseLong(id));
    }
}
