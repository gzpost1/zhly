package cn.cuiot.dmp.lease.dto.contractTemplate;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caorui
 * @date 2024/6/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractTemplateUpdateDTO extends ContractTemplateCreateDTO {

    private static final long serialVersionUID = -3505297532410492202L;

    /**
     * 合同模板id
     */
    @NotNull(message = "合同模板id不能为空")
    private Long id;

}
