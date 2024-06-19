package cn.cuiot.dmp.base.infrastructure.dto.rsp;

import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/6/13
 */
@Data
public class AuditConfigRspDTO implements Serializable {

    private static final long serialVersionUID = -3258141381289123425L;

    /**
     * 主键ID
     */
    private Long id;

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
