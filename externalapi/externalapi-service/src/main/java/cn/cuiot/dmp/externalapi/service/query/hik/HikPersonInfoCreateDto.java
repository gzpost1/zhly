package cn.cuiot.dmp.externalapi.service.query.hik;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 海康人员信息新增DTO
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikPersonInfoCreateDto {
    /**
     * 人员名称，1~32个字符；不能包含特殊字符
     */
    @NotBlank(message = "人员名称不能为空")
    @Length(max = 32,message = "人员名称限32字")
    private String personName;

    /**
     * 性别，1：男；2：女；0：未知
     */
    @NotBlank(message = "性别不能为空")
    private String gender;

    /**
     * 所属组织标识，必须是已存在组织
     */
    @NotBlank(message = "所属组织不能为空")
    private String orgIndexCode;

    /**
     * 证件类型（111:身份证，414:护照，113:户口簿，335:驾驶证，131:工作证，133:学生证，990:其它）
     */
    @NotBlank(message = "证件类型不能为空")
    private String certificateType;

    /**
     * 证件号码，1-20位数字字母
     */
    @NotBlank(message = "证件号码不能为空")
    @Length(max = 20, message = "证件号码限20字")
    @Pattern(regexp = "[a-zA-Z0-9]{1,20}$", message = "证件号码必须是数字或字母")
    private String certificateNo;

    /**
     * 手机号，11位数字
     */
    @Length(max = 11,message = "手机号限11字")
    private String phoneNo;

    /**
     * 工号，1-32个字符
     */
    @Length(max = 32,message = "工号限32字")
    @Pattern(regexp = "[a-zA-Z0-9\\u4e00-\\u9fa5]{1,32}$", message = "工号不能输入特殊字符")
    private String jobNo;
}
