package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统配置-初始化配置-审核配置表
 *
 * @author caorui
 * @date 2024/6/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "audit_config", autoResultMap = true)
public class AuditConfigEntity extends BaseEntity {

    private static final long serialVersionUID = -1699038266525278831L;

    /**
     * 审核配置名称
     */
    private String name;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 审核配置类型
     * @see AuditConfigTypeEnum
     */
    private Byte auditConfigType;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

}
