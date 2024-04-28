package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/4/28
 */
@Data
public class SystemInfoCreateDTO implements Serializable {

    private static final long serialVersionUID = -3151151991085263493L;

    /**
     * 系统名称
     */
    private String name;

    /**
     * 系统logo
     */
    private String logoUrl;

    /**
     * 来源id（平台或者企业）
     */
    @NotNull(message = "来源id不能为空")
    private Long sourceId;

    /**
     * 系统信息类型（0：平台 1：企业）
     */
    @NotNull(message = "来源类型不能为空")
    private Byte sourceType;

}
