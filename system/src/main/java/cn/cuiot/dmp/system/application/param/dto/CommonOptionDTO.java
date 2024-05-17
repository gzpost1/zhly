package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/15
 */
@Data
public class CommonOptionDTO implements Serializable {

    private static final long serialVersionUID = 6089117979382996545L;

    /**
     * 常用选项名称
     */
    private String name;

    /**
     * 企业ID
     */
    @NotNull(message = "企业ID不能为空")
    private Long companyId;

}
