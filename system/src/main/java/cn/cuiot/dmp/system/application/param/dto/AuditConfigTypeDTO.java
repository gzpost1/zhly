package cn.cuiot.dmp.system.application.param.dto;

import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import cn.cuiot.dmp.system.infrastructure.entity.AuditConfigEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/6/11
 */
@Data
public class AuditConfigTypeDTO implements Serializable {

    private static final long serialVersionUID = 8366137326761141662L;

    /**
     * 主键ID
     */
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
     * 审核配置列表
     */
    private List<AuditConfigEntity> auditConfigList;

}
