package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/8
 */
@Data
public class FormConfigTypeQueryDTO implements Serializable {

    private static final long serialVersionUID = -5841674660970569369L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 企业ID
     */
    @NotNull(message = "企业ID不能为空")
    private Long companyId;

    /**
     * 初始化标志位(0:非初始化数据,1:初始化数据)
     */
    @NotNull(message = "初始化标志位不能为空")
    private Byte initFlag;

}
