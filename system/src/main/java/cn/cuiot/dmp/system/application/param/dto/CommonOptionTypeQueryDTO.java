package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/11
 */
@Data
public class CommonOptionTypeQueryDTO implements Serializable {

    private static final long serialVersionUID = 5395022254963239289L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 选项类别
     */
    @NotNull(message = "选项类别不能为空")
    private Byte category;

    /**
     * 企业ID
     */
    @NotNull(message = "企业ID不能为空")
    private Long companyId;

}
