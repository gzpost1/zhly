package cn.cuiot.dmp.base.infrastructure.dto.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/9
 */
@Data
public class FormConfigReqDTO implements Serializable {

    private static final long serialVersionUID = -3946161268604323087L;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

    /**
     * 表单配置ID列表
     */
    @NotEmpty(message = "表单配置ID列表")
    private List<Long> idList;

}
