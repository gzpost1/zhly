package cn.cuiot.dmp.system.infrastructure.entity.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author wen
 * @Description 行政区域entity
 * @Date 2021/12/27 9:31
 * @return
 **/
@Data
public class SysParamDto {

    /**
     * 系统名称
     */
    @NotBlank(message = "系统名称不能为空")
    @Length(max = 30, message = "系统名称不可超过30字")
    private String title;

    /**
     * 系统logo
     */
    @NotBlank(message = "系统logo不能为空")
    private String logoPath;

    /**
     * 当前登录用户-账户id(前端不用管)
     */
    private Long sessionOrgId;

    /**
     * 当前登录用户-用户id(前端不用管)
     */
    private Long sessionUserId;

}
