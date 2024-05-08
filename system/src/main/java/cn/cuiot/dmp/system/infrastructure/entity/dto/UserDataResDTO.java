package cn.cuiot.dmp.system.infrastructure.entity.dto;

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
     * 角色key
     */
    private String roleKey;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime lastOnlineOn;

    /**
     * 创建者
     */
    @JsonProperty("createBy")
    private String createdBy;

    /**
     * 创建时间
     */
    @JsonProperty("createTime")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * 修改者
     */
    @JsonProperty("modifiedBy")
    private String updatedBy;

    /**
     * 修改时间
     */
    @JsonProperty("modifiedOn")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
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

    /**
     * 是否开通服务,0:未开通服务,1:已经开通服务
     */
    private Integer isServe;


    private String pkOrgId;

    /**
     *  label    用户标签（1:商务楼宇（写字楼等）,2:厂园区（工业、科技、物流等园区厂区）,3:商业综合体（购物中心、百货市场等）,
     *                   4:专业市场（建材、汽配、农贸等）,5:九小场所,6:联通管理方,7:其它商企（网吧、便利店、中小独栋企业等）
     **/
    private Integer  label;

    /**
     * 其他标签名称
     */
    private String  otherLabelName;

    /**
     * 标签名称
     */
    private String  labelName;

    /**
     * 组织树
     */
    private String  deptTreePath;

    /**
     * 长期登录
     */
    private String longTimeLogin;
}
