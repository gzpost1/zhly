package cn.cuiot.dmp.system.application.param.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * @Description 应用层数据封装
 * @Author 犬豪
 * @Date 2023/9/5 13:30
 * @Version V1.0
 */
@Data
public class UserDTO {
    /**
     * pk
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
     * hash密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 用户最近登录ip
     */
    private String lastOnlineIp;

    /**
     * 用户最近登录地址
     */
    private String lastOnlineAddress;

    /**
     * 用户最后上线时间
     */
    private LocalDateTime lastOnlineOn;

    /**
     * 创建时间
     */
    private LocalDateTime createdOn;

    /**
     * 创建者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的。
     */
    private String createdBy;

    /**
     * 创建者类型（1：system、2：用户、3：open api）
     */
    private Integer createdByType;

    /**
     * 更新时间
     */
    private LocalDateTime updatedOn;

    /**
     * 更新者。取值：{userKey}：Portal用户更新的；{appKey}：API更新的。
     */
    private String updatedBy;

    /**
     * 更新者类型（1：system、2：用户、3：open api）
     */
    private Integer updatedByType;

    /**
     * 是否删除（0：未删除 1：已删除）
     */
    private Integer deletedFlag;

    /**
     * 删除时间
     */
    private LocalDateTime deletedOn;

    /**
     * 删除人id
     */
    private String deletedBy;

    /**
     * 用户类型（1：实体用户）
     */
    private Integer userType;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系人地址
     */
    private String contactAddress;

    /**
     * 0：关闭，1：开启
     */
    private Integer longTimeLogin;

    /**
     * 岗位ID
     */
    private Long postId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 小程序openid
     */
    private String openid;
}
