package cn.cuiot.dmp.system.infrastructure.entity.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @Author wen
 * @Description 行政区域entity
 * @Date 2021/12/27 9:31
 * @param
 * @return
 **/
@Data
public class SysParamDto {

    /**
     * 账户id
     */
    private Long orgId;

    /**
     * 登陆账户id
     */
    private Long userId;

    /**
     * 用户组织
     */
    private String updaterPath;

    /**
     * title
     */
    @NotBlank(message = "title不能为空")
    private String title;

    /**
     * 等级
     */
    @NotNull(message = "logoId不能为空")
    @Range(min = 1, max = 2, message = "logoId枚举值不合法")
    private Integer logoId;

    /**
     * 组织
     */
    @NotBlank(message = "组织不能为空")
    private String deptTreePath;

    /**
     * 是否覆盖（0：覆盖； 1：不覆盖）
     */
    @Pattern(regexp = "^0|1$", message = "覆盖状态参数不合法")
    private String overwrite;

}
