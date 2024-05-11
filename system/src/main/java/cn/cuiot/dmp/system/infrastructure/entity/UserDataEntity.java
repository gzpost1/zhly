package cn.cuiot.dmp.system.infrastructure.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * @Description
 * @Author cds
 * @Date 2020/8/13
 */
@Data
public class UserDataEntity {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String name;

    /**
     * 用户类型（1：实体用户）
     */
    private Integer userType;

    /**
     * 密码（加密）
     */
    private String password;

    /**
     * 邮箱（加密）
     */
    private String email;

    /**
     * 手机号（加密）
     */
    private String phoneNumber;

    /**
     * 创建时间
     */
    private LocalDateTime createdOn;

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedOn;

    /**
     * 更新者
     */
    private String updatedBy;

    /**
     * 最后上线时间
     */
    private LocalDateTime lastOnlineOn;


    /**
     * 最后上线ip
     */
    private String lastOnlineIp;


    /**
     * 最后上线地址（市级）
     */
    private String lastOnlineAddress;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建者类型
     */
    private Integer createdByType;

    /**
     * 更新者类型
     */
    private Integer updatedByType;

    /**
     * 是否删除（0：未删除 1：已删除）
     */
    private Integer deletedFlag;

    /**
     * 下次登陆是否重置密码（0：不重置；1：重置）
     */
    private Integer resetPassword;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系人地址
     */
    private String contactAddress;

    /**
     * 是否同意隐私协议（0：未同意、1：同意)
     */
    private String privacyAgreement;

    private String path;

    private String orgId;

    private Integer orgTypeId;

    private Integer identity;

    private String pkOrgId;

    /**
     * 其他标签名称
     */
    private String  otherLabelName;

    /**
     * 长时间登录
     */
    private String longTimeLogin;


    /**
     * 岗位ID
     */
    private Long postId;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 组织id
     */
    private String deptId;

    /**
     * 组织名
     */
    private String deptName;

    /**
     * 组织名路径
     */
    private String deptPathName;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色key
     */
    private String roleKey;
}
