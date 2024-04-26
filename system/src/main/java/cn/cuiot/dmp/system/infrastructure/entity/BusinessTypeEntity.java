package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务类型表
 *
 * @author caorui
 * @date 2024/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "business_type", autoResultMap = true)
public class BusinessTypeEntity extends BaseEntity {

    private static final long serialVersionUID = -5276683068069773276L;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 层级类型(0:根节点，默认企业名称；最多可添加4级；)
     */
    private Byte levelType;

    /**
     * 企业ID
     */
    private Long companyId;

}
