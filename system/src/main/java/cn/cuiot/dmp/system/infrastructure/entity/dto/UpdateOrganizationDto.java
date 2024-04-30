package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.log.intf.AbstractResourceParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 企业修改入参
 * @Author wuyc
 * @Date 2021/12/27 9:31
 **/
@Data
public class UpdateOrganizationDto extends AbstractResourceParam {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空")
    private Long id;

    /**
     * 企业名称
     */
    @NotBlank(message = "请输入企业名称")
    @Length(max = 50, message = "企业名称不可超过50字")
    private String companyName;


    /**
     * 所属组织id
     */
    @NotBlank(message = "请选择所属组织")
    private Long deptId;

    /**
     * 企业有效期-开始时间
     */
    @NotNull(message = "请选择企业有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expStartDate;

    /**
     * 企业有效期-结束时间
     */
    @NotNull(message = "请选择企业有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expEndDate;

    /**
     * 备注
     */
    @Length(max = 200, message = "备注不可超过200字")
    private String description;

    /**
     * 企业权限配置
     */
    @NotEmpty(message = "请配置企业权限")
    private List<String> menuList;


    /**
     * 下次登陆是否重置账户密码（0：不重置密码 1：重置密码）
     */
    @Range(min = 0, max = 1, message = "是否重置账户密码参数不合法")
    private Integer resetPassword;

    /**
     * 当前登录用户-账户id(前端不用管)
     */
    private Long sessionOrgId;

    /**
     * 当前登录用户-用户id(前端不用管)
     */
    private Long sessionUserId;

    /**
     * 账户所有者-用户ID(前端不用管)
     */
    private String orgOwnerUserId;

    /**
     * 账户所有者-用户名(前端不用管)
     */
    private String orgOwnerUsername;
}
