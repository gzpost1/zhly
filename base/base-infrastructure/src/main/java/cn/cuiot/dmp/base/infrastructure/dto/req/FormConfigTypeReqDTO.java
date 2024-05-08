package cn.cuiot.dmp.base.infrastructure.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormConfigTypeReqDTO implements Serializable {

    private static final long serialVersionUID = -5384952333778390489L;

    /**
     * 组织id
     */
    @NotNull(message = "组织id不能为空")
    private Long orgId;

    /**
     * 表单配置类型ID列表
     */
    @NotEmpty(message = "表单配置类型ID列表不能为空")
    private List<Long> formConfigTypeIdList;

}
