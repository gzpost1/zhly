package cn.cuiot.dmp.system.application.param.dto;

import cn.cuiot.dmp.system.infrastructure.entity.AuditConfigEntity;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/7/1
 */
@Data
public class AuditConfigTypeStatusDTO implements Serializable {

    private static final long serialVersionUID = -2763930261334841270L;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 审核配置列表
     */
    @NotEmpty(message = "审核配置列表不能为空")
    private List<AuditConfigEntity> auditConfigList;

}
