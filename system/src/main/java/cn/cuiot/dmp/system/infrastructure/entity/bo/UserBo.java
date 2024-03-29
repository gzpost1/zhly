package cn.cuiot.dmp.system.infrastructure.entity.bo;

import cn.cuiot.dmp.common.log.intf.AbstractResourceParam;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ResetPasswordReqDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhh
 * @description 用户日志操作对象实体
 * @author: YouName
 * @create: 2020-10-28 14:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBo extends AbstractResourceParam {

    /**
     * 用户主键id
     */
    private Long id;

    /**
     * 重置密码实体
     */
    private ResetPasswordReqDTO resetPasswordReqDTO;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 更改后的密码
     */
    private String password;

    /**
     * 短信验证码
     */
    private String smsCode;

    /**
     * 邮箱验证码
     */
    private String emailCode;

    /**
     * id集合
     */
    private List<Long> ids;

    /**
     * 账户id
     */
    private String orgId;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 下次登陆是否重置密码
     */
    private Integer resetPassword;

    /**
     * 角色key
     */
    private String roleKey;

    /**
     * 创建者id
     */
    private String createdBy;

    /**
     * 组织id
     */
    private String deptId;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系人地址
     */
    private String contactAddress;

    /**
     * 登陆用户的userId
     */
    private String loginUserId;

    /**
     *  label    用户标签（1:商务楼宇（写字楼等）,2:厂园区（工业、科技、物流等园区厂区）,3:商业综合体（购物中心、百货市场等）,
     *                   4:专业市场（建材、汽配、农贸等）,5:九小场所,6:联通管理方,7:其它商企（网吧、便利店、中小独栋企业等）
     **/
    private Integer  label;

    /**
     * 其他商企标签名称
     */
    private String  otherLabelName;

    /**
     *  labelName    用户标签名称（商务楼宇（写字楼等）,厂园区（工业、科技、物流等园区厂区）,商业综合体（购物中心、百货市场等）,
     *                   专业市场（建材、汽配、农贸等）,九小场所,6:联通管理方,其它商企（网吧、便利店、中小独栋企业等）
     **/
    private String  labelName;

    private String longTimeLogin;

    /**
     * 0:个人账户，1:商企账户
     */
    private String type;

    /**
     * 注册来源，默认楼宇
     */
    private String from = "0";

}
