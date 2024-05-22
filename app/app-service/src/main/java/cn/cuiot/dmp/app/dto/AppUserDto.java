package cn.cuiot.dmp.app.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author: wuyongchong
 * @date: 2024/5/22 15:15
 */
@Data
public class AppUserDto implements Serializable {

    /**
     * 用户表主键id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 姓名/昵称
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态 1-启用 0-停用
     */
    private Byte status;

    /**
     * 用户类型（1：员工 2：业主）
     */
    private Integer userType;

    /**
     * 小程序openid
     */
    private String openid;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 岗位ID
     */
    private Long postId;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 组织id
     */
    private String deptId;

    /**
     * 组织名
     */
    private String deptName;

    /**
     * 租户id
     */
    private String orgId;

    /**
     * 租户名
     */
    private String orgName;

    /**
     * 企业类型
     */
    private Integer orgTypeId;

    /**
     * 组织名路径
     */
    private String deptPathName;

    /**
     * 组织路径
     */
    private String deptPath;


    /**
     * token
     */
    private String token;

    /**
     * refreshCode
     */
    private String refreshCode;

}
