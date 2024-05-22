package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangze
 * @classname LoginResDTO
 * @description 用户登录响应DTO
 * @date 2020-06-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResDTO {

    /**
     * 用户id
     */
    private String userId;

    /**
     * token
     */
    private String token;

    /**
     * refreshCode
     */
    private String refreshCode;

    /**
     * 账户ID
     */
    private String orgId;

    /**
     * 账户名称
     */
    private String orgName;

    /**
     * 账户类型
     */
    private Integer orgTypeId;

    /**
     * 用户的手机号
     */
    private String phoneNumber;

    /**
     * 是否异地(true:异地，false：非异地)
     */
    private Boolean isAllopatry;

    /**
     * 下次登陆是否重置密码（0：不重置；1：重置）
     */
    private Integer resetPassword;

    /**
     * 是否同意隐私协议（0：未同意、1：同意)
     */
    private String privacyAgreement;

    /**
     * 岗位ID
     */
    private Long postId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 组织部门ID
     */
    private Long deptId;
}
