package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.constant.RegexConst;
import cn.cuiot.dmp.common.log.intf.AbstractResourceParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 企业新增入参
 * @author wuyc
 * @date 2022/9/14 18:06
 */
@Data
public class InsertOrganizationDto extends AbstractResourceParam {

    /**
     * 企业名称
     */
    @NotBlank(message = "请输入企业名称")
    @Length(max = 50, message = "企业名称不可超过50字")
    private String companyName;

    /**
     * 企业编码
     */
    @NotBlank(message = "请输入企业编码")
    @Length(max = 50, message = "企业编码不可超过50字")
    private String orgKey;


    /**
     * 所属组织id
     */
    @NotNull(message = "请选择所属组织")
    private Long deptId;


    /**
     * 管理员姓名
     */
    @NotBlank(message = "请输入管理员姓名")
    @Length(max = 20, message = "管理员姓名不可超过20字")
    private String adminName;


    /**
     * 管理员手机
     */
    @NotBlank(message = "请输入手机号")
    @Length(min = 11,max = 11, message = "请输入正确的11位手机号")
    private String phoneNumber;

    /**
     * 企业有效期-开始时间
     */
    @NotNull(message = "请选择企业有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expStartDate;

    /**
     * 企业有效期-结束时间
     */
    @NotNull(message = "请选择企业有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
     * 当前登录用户-账户id(前端不用管)
     */
    private Long sessionOrgId;

    /**
     * 当前登录用户-用户id(前端不用管)
     */
    private Long sessionUserId;
}
