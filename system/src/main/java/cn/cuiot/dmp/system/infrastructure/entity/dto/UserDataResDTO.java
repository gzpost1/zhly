package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 用户管理返回
 * @Author cds
 * @Date 2020/8/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDataResDTO implements Serializable {
    /**
     * 用户表主键id
     */
    private String id;

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
     * 邮箱
     */
    private String email;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastOnlineOn;

    /**
     * 创建者
     */
    @JsonProperty("createBy")
    private String createdBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * 修改者
     */
    private String updatedBy;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedOn;

    /**
     * 用户类型
     */
    private Integer userType;

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
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系人地址
     */
    private String contactAddress;

    /**
     * 租户id
     */
    private String orgId;

    /**
     * 租户名
     */
    private String orgName;

    /**
     * 租户归属组织
     */
    private String orgDeptName;

    /**
     * 是否是账户默认的用户
     */
    private Boolean isOwner;


    private String pkOrgId;

    /**
     * 组织树
     */
    private String  deptTreePath;

    /**
     * 长期登录
     */
    private String longTimeLogin;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 小程序openid
     */
    private String openid;
}
