package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caorui
 * @date 2024/6/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserHouseAuditStatusDTO extends UserHouseAuditCreateDTO {

    private static final long serialVersionUID = -4214062520515412021L;

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

    /**
     * 审核状态(0:待审核,1:审核通过,2:审核驳回 3:认证失效)
     */
    @NotNull(message = "审核状态不能为空")
    private Byte auditStatus;

    /**
     * 驳回理由
     */
    private String rejectReason;

    /**
     * 绑定客户ID
     */
    private Long bindCustomerId;

    /**
     * 绑定客户类型(1:客户本人 2:客户家庭成员)
     */
    private Byte bindCustomerType;

    /**
     * 绑定客户成员ID
     */
    private Long bindCustomerMemberId;

}
