package cn.cuiot.dmp.system.user_manage.domain.entity;

import cn.cuiot.dmp.domain.entity.AbstractAggregate;
import cn.cuiot.dmp.domain.types.Address;
import cn.cuiot.dmp.domain.types.Email;
import cn.cuiot.dmp.domain.types.IP;
import cn.cuiot.dmp.domain.types.Password;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.system.user_manage.domain.types.enums.UserStatusEnum;
import cn.cuiot.dmp.system.user_manage.domain.types.enums.UserTypeEnum;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @Author 犬豪
 * @Date 2023/8/28 11:06
 * @Version V1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class User extends AbstractAggregate<UserId> {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码（加密）
     */
    private Password password;

    /**
     * 邮箱（加密）
     */
    private Email email;

    /**
     * 手机号（加密）
     */
    private PhoneNumber phoneNumber;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态
     */
    private UserStatusEnum status;

    /**
     * 最后上线ip
     */
    private IP lastOnlineIp;

    /**
     * 最后上线地址（市级）
     */
    private Address lastOnlineAddress;

    /**
     * 最后上线时间
     */
    private LocalDateTime lastOnlineOn;

    /**
     * 创建时间
     */
    private LocalDateTime createdOn;

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 创建者类型
     */
    private OperateByTypeEnum createdByType;

    /**
     * 更新时间
     */
    private LocalDateTime updatedOn;

    /**
     * 更新者
     */
    private String updatedBy;

    /**
     * 更新者类型
     */
    private OperateByTypeEnum updatedByType;

    /**
     * 是否删除（0：未删除 1：已删除）
     */
    private Integer deletedFlag;

    /**
     * 用户类型（1：实体用户）
     */
    private UserTypeEnum userType;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系人地址
     */
    private Address contactAddress;

    /**
     * 长时间登录
     */
    private Integer longTimeLogin;

    /**
     * 自己更新自己
     */
    public void updatedByPortal() {
        this.updatedBy = getId().getStrValue();
        this.updatedByType = OperateByTypeEnum.USER;
        this.updatedOn = LocalDateTime.now();
    }

    public void updatedByPortal(String updatedBy) {
        this.updatedBy = updatedBy;
        this.updatedByType = OperateByTypeEnum.USER;
        this.updatedOn = LocalDateTime.now();
    }

    public void updatedByPortal(Long updatedBy) {
        this.updatedBy = String.valueOf(updatedBy);
        this.updatedByType = OperateByTypeEnum.USER;
        this.updatedOn = LocalDateTime.now();
    }

    /**
     * 获取解密的手机号
     */
    public String getDecryptedPhoneNumber() {
        if (phoneNumber == null) {
            return null;
        }
        return phoneNumber.decrypt();
    }

    /**
     * 获取脱敏的手机号
     * @return
     */
    public String getDesensitizedPhoneNumber() {
        if (phoneNumber == null) {
            return null;
        }
        return phoneNumber.getDesensitizedValue();
    }

    /**
     * 获取加密的手机号
     */
    public String getEncryptedPhoneNumber() {
        if (phoneNumber == null) {
            return null;
        }
        return phoneNumber.getEncryptedValue();
    }


    /**
     * 获取密码
     */
    public String getEncryptedPassword() {
        if (password == null) {
            return null;
        }
        return password.getHashEncryptValue();
    }

    /**
     * 获取加密的邮箱
     */
    public String getEncryptedEmail() {
        if (email == null) {
            return null;
        }
        return email.getEncryptedValue();
    }


    public String getLastOnlineIpStr() {
        if (lastOnlineIp == null) {
            return null;
        }
        return lastOnlineIp.getValue();
    }

    public String getLastOnlineAddressStr() {
        if (lastOnlineAddress == null) {
            return null;
        }
        return lastOnlineAddress.getValue();
    }

    public String getContactAddressStr() {
        if (contactAddress == null) {
            return null;
        }
        return contactAddress.getValue();
    }

}
