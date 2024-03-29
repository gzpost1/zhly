package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author zhh
 * @version 1.0
 * @date 2020/9/7 20:09
 * @deprecated 新增用户
 */
@Data
public class InsertUserDTO {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String passWord;
    /**
     * 下次登陆是否重置密码
     */
    private Integer resetPassword;
    /**
     * 角色key
     */
    private String roleKey;
    /**
     * userId
     */
    private String userId;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 账户id
     */
    private String orgId;
    /**
     * 创建者id
     */
    private String createdBy;
    /**
     * 新增手机号  --2021/01/22
     */
    private String phoneNumber;

    /**
     * email
     */
    private String email;
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
     * 登陆人userid
     */
    private String loginUserId;

    /**
     *  label    用户标签（1:商务楼宇（写字楼等）,2:厂园区（工业、科技、物流等园区厂区）,3:商业综合体（购物中心、百货市场等）,
     *                   4:专业市场（建材、汽配、农贸等）,5:九小场所,6:联通管理方,7:其它商企（网吧、便利店、中小独栋企业等）
     **/
    @JsonIgnore
    private Integer  label;

    /**
     * 其他标签名称
     */
    private String  otherLabelName;

    /**
     * 长时间登录
     */
    private String longTimeLogin;

    /**
     * 用户注册来源,默认楼宇
     */
    @JsonIgnore
    private String from = "0";
}
