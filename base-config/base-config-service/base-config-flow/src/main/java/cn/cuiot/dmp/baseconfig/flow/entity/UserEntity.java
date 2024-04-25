package cn.cuiot.dmp.baseconfig.flow.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;

/**
 * @author pengjian
 * @since 2024-04-24
 */
@Data
@TableName("user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * pk
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 用户id
     */
    private String userId;


    /**
     * 用户名
     */
    private String username;


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
    private Byte status;


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
    private Date lastOnlineOn;


    /**
     * 创建时间
     */
    private Date createdOn;


    /**
     * 创建者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的。
     */
    private String createdBy;


    /**
     * 创建者类型（1：system、2：用户、3：open api）
     */
    private Byte createdByType;


    /**
     * 更新时间
     */
    private Date updatedOn;


    /**
     * 更新者。取值：{userKey}：Portal用户更新的；{appKey}：API更新的。
     */
    private String updatedBy;


    /**
     * 更新者类型（1：system、2：用户、3：open api）
     */
    private Byte updatedByType;


    /**
     * 是否删除（0：未删除 1：已删除）
     */
    private Byte deletedFlag;


    /**
     * 删除时间
     */
    private Date deletedOn;


    /**
     * 删除人id
     */
    private String deletedBy;


    /**
     * 用户类型（1：实体用户、2：业主用户,3：门禁人员、4：门禁访客、5：个人用户 ）
     */
    private Byte userType;


    /**
     * 联系人
     */
    private String contactPerson;


    /**
     * 联系人地址
     */
    private String contactAddress;


    /**
     * 0：开通 1：不开通
     */
    private Byte activateSmsReminderFlag;


    /**
     * 0：关闭，1：开启
     */
    private Byte longTimeLogin;


    /**
     * 删除者类型（1：system、2：用户、3：open api）
     */
    private Byte deleteByType;



}
