package cn.cuiot.dmp.domain.entity;

import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import cn.cuiot.dmp.domain.types.id.Identifier;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author 犬豪
 * @Date 2023/8/28 11:09
 * @Version V1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractDomainEntity<ID extends Identifier> implements DomainEntity<ID> {

    private ID id;

    /**
     * 创建时间
     */
    private LocalDateTime createdOn;

    /**
     * 创建者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的。
     */
    private String createdBy;

    /**
     * 创建者类型（1：system、2：用户、3：open api）
     */
    private OperateByTypeEnum createdByType;

    /**
     * 更新时间
     */
    private LocalDateTime updatedOn;

    /**
     * 更新者。取值：{userKey}：Portal用户更新的；{appKey}：API更新的。
     */
    private String updatedBy;

    /**
     * 更新者类型（1：system、2：用户、3：open api）
     */
    private OperateByTypeEnum updatedByType;

    /**
     * 是否删除，0未删除，1删除
     */
    private Integer deletedFlag;

    /**
     * 删除人id
     */
    private String deletedBy;

    /**
     * 删除时间
     */
    private LocalDateTime deletedOn;

    /**
     * 删除者类型（1：system、2：用户、3：open api）
     */
    private OperateByTypeEnum deleteByType;
}
