package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.constant.RegexConst;
import cn.cuiot.dmp.common.log.intf.AbstractResourceParam;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 * @author wensq
 * @version 1.0
 * @description: 新增账户 入参
 * @date 2022/9/14 18:06
 */
@Data
public class InsertOrganizationDto extends AbstractResourceParam {

    /**
     * 账户名称
     */
    @NotBlank(message = "账户名称不能为空")
    private String orgName;

    /**
     * 登录名
     */
    @NotBlank(message = "登录名不能为空")
    private String username;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;

    /**
     * 密码
     */
    private String password;

    /**
     * 账户状态(0：禁用、 1：启用）
     */
    @NotNull(message = "账户状态不能为空")
    private Integer status;

    /**
     * 组织id
     */
    @NotBlank(message = "组织id不能为空")
    private String deptId;

    /**
     * orgTypeId
     */
    @NotNull(message = "orgTypeId")
    private Integer orgTypeId;

    /**
     * 菜单根节点list
     */
//    @NotEmpty(message = "应用权限至少选择一个")
    private List<String> menuRootList;

    /**
     * 账户key
     */
    private String orgKey;

    /**
     * 当前登录账户Id
     */
    private String LoginOrgId;

    /**
     * 当前登录人Id
     */
    private String userId;

    /**
     *  label    账户标签（8:商务楼宇（写字楼等）,9:厂园区（工业、科技、物流等园区厂区）,
     *                   11:专业市场（建材、汽配、农贸等）,12:九小场所,13:联通管理方,14:其它商企（网吧、便利店、中小独栋企业等,16:商业综合体（购物中心、百货市场等）,）
     **/
    @NotNull(message = "账户标签不可为空")
    @Range(min = 8, max = 16, message = "账户标签参数不合法")
    private Integer  label;

    /**
     * 其他标签名称
     */
    private String  otherLabelName;

    /**
     * 统一社会信用代码
     */
    @NotBlank(message = "社会信用代码不得为空")
    @Length(max = 18, min = 18, message = "社会信用代码长度为18位")
    @Pattern(regexp = RegexConst.SOCIAL_CREDIT_CODE, message = "社会信用代码仅支持大写字母与数字")
    private String socialCreditCode;

    /**
     * 公司名称
     */
    @NotBlank(message = "公司名称不得为空")
    @Length(max = 32, min = 1, message = "公司名称最多支持32个字符")
    private String companyName;

    /**
     * 创建来源,默认楼宇
     */
    @JsonIgnore
    private String from = "0";

    /**
     * 来源
     */
    private Integer source;
}
