package cn.cuiot.dmp.base.infrastructure.dto.rsp;

import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/6/13
 */
@Data
public class AuditConfigTypeRspDTO implements Serializable {

    private static final long serialVersionUID = 2796559130809388876L;

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
    private List<AuditConfigRspDTO> auditConfigList;

}
