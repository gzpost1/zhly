package cn.cuiot.dmp.lease.dto.contractTemplate;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/6/24
 */
@Data
public class ContractTemplateCreateDTO implements Serializable {

    private static final long serialVersionUID = 1755405783803850582L;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 合同模板名称
     */
    @NotBlank(message = "合同模板名称不能为空")
    private String name;

    /**
     * 合同性质（系统配置自定义）
     */
    @NotNull(message = "合同性质不能为空")
    private Long natureId;

    /**
     * 合同类型（系统配置自定义）
     */
    @NotNull(message = "合同类型不能为空")
    private Long typeId;

    /**
     * 模板用途
     */
    @NotBlank(message = "模板用途不能为空")
    private String usage;

    /**
     * 模板备注
     */
    private String remark;

    /**
     * 表单配置详情
     */
    private String formConfigDetail;

    /**
     * 收费项目列表（系统配置自定义）
     */
    @NotEmpty(message = "收费项目列表不能为空")
    private List<String> chargeItemIds;

}
