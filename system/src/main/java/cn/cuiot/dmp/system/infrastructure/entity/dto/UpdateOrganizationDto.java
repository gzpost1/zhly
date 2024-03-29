package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.log.intf.AbstractResourceParam;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @Author wen
 * @Description 修改账户 入参
 * @Date 2021/12/27 9:31
 * @param
 * @return
 **/
@Data
public class UpdateOrganizationDto extends AbstractResourceParam {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空")
    private Long id;

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
     * 组织id
     */
    @NotBlank(message = "组织id不能为空")
    private String deptId;

    /**
     * 账户状态(0：禁用、 1：启用）
     */
    @NotNull(message = "账户状态不能为空")
    private Integer status;

    /**
     * 账户key
     */
    private String orgKey;

    /**
     * 当前登录账户Id
     */
    private String orgId;

    /**
     * 当前登录人Id
     */
    private String userId;
    /**
     * 账户密码
     */
    private String passWord;

    /**
     * 菜单根节点list
     */
    private List<String> menuRootList;

    /**
     * 下次登陆是否重置账户密码（0：不重置密码 1：重置密码）
     */
    @NotNull(message = "重置账户密码参数不可为空")
    @Range(min = 0, max = 1, message = "是否重置账户密码参数不合法")
    private Integer resetPassword;

    /**
     *  label    账户标签（8:商务楼宇（写字楼等）,9:厂园区（工业、科技、物流等园区厂区）,
     *                   11:专业市场（建材、汽配、农贸等）,12:九小场所,13:联通管理方,14:其它商企（网吧、便利店、中小独栋企业等),16:商业综合体（购物中心、百货市场等））
     **/
    @NotNull(message = "账户标签不可为空")
    @Range(min = 8, max = 16, message = "账户标签参数不合法")
    private Integer  label;

    /**
     * 其他标签名称
     */
    private String  otherLabelName;

    /**
     * 账户(租户)类型
     */
    @JsonIgnore
    private Integer orgTypeId;
}
