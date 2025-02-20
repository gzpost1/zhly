package cn.cuiot.dmp.base.infrastructure.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户信息DTO
 */
@Data
public class BaseUserDto implements Serializable {

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
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户类型（1：实体用户）
     */
    private Integer userType;

    /**
     * 状态
     */
    private Integer status;

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
     * 小程序openid
     */
    private String openid;

}
