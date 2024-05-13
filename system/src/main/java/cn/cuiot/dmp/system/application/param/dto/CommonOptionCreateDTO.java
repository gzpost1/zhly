package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/11
 */
@Data
public class CommonOptionCreateDTO implements Serializable {

    private static final long serialVersionUID = -6406243108615063051L;

    /**
     * 常用选项名称
     */
    @NotBlank(message = "常用选项名称不能为空")
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

    /**
     * 常用选项详情
     */
    private String commonOptionDetail;

}
