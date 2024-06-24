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
public class CommonOptionTypeCreateDTO implements Serializable {

    private static final long serialVersionUID = 3064064921671877897L;

    /**
     * 类型名称
     */
    @NotBlank(message = "类型名称不能为空")
    private String name;

    /**
     * 选项类别
     */
    @NotNull(message = "选项类别不能为空")
    private Byte category;

    /**
     * 上级ID
     */
    @NotNull(message = "上级ID不能为空")
    private Long parentId;

    /**
     * 层级类型(0:根节点，默认"全部"；最多可添加4级；)
     */
    @NotNull(message = "层级类型不能为空")
    private Byte levelType;

    /**
     * 企业ID
     */
    @NotNull(message = "企业ID不能为空")
    private Long companyId;

}
