package cn.cuiot.dmp.system.application.param.dto;

import cn.cuiot.dmp.system.domain.aggregate.CustomConfigDetail;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/19
 */
@Data
public class CustomConfigCreateDTO implements Serializable {

    private static final long serialVersionUID = -276931267296929576L;

    /**
     * 自定义配置名称
     */
    @NotBlank(message = "自定义配置名称不能为空")
    private String name;

    /**
     * 企业ID
     */
    @NotNull(message = "企业ID不能为空")
    private Long companyId;

    /**
     * 系统选项类型
     */
    @NotNull(message = "系统选项类型不能为空")
    private Byte systemOptionType;

    /**
     * 自定义配置详情列表
     */
    private List<CustomConfigDetail> customConfigDetailList;

}
