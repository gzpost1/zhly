package cn.cuiot.dmp.base.infrastructure.dto.req;

import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/6/13
 */
@Data
@Accessors(chain = true)
public class AuditConfigTypeReqDTO implements Serializable {

    private static final long serialVersionUID = 7488192730375382775L;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 审核配置类型
     * @see AuditConfigTypeEnum
     */
    private Byte auditConfigType;

    /**
     * 审核配置名称
     */
    private String name;
}
