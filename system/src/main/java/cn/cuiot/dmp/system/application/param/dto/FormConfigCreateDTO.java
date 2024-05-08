package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/8
 */
@Data
public class FormConfigCreateDTO implements Serializable {

    private static final long serialVersionUID = -1078309284411933501L;

    /**
     * 表单名称
     */
    @NotBlank(message = "表单名称不能为空")
    private String name;

    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空")
    private Long typeId;

    /**
     * 企业ID
     */
    @NotNull(message = "企业ID不能为空")
    private Long companyId;

}
