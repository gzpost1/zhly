package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统配置-初始化配置-审核配置分类表
 *
 * @author caorui
 * @date 2024/6/11
 */
@Data
@TableName(value = "audit_config_type", autoResultMap = true)
public class AuditConfigTypeEntity implements Serializable {

    private static final long serialVersionUID = -4232928964316198058L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 审核配置分类名称
     */
    private String name;

    /**
     * 审核配置类型
     * @see AuditConfigTypeEnum
     */
    private Byte auditConfigType;

    /**
     * 逻辑删除，1已删除，0未删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private Integer deletedFlag;

}
