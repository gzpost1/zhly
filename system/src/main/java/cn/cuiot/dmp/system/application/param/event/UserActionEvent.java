package cn.cuiot.dmp.system.application.param.event;

import cn.cuiot.dmp.base.application.param.event.ActionEvent;
import cn.cuiot.dmp.common.serialize.LocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @Description
 * @Author yth
 * @Date 2023/8/14 14:55
 * @Version V1.0
 */
@Data
public class UserActionEvent extends ActionEvent {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;
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
     * 头像
     */
    private String avatar;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 最后上线ip
     */
    private String lastOnlineIp;

    /**
     * 最后上线地址（市级）
     */
    private String lastOnlineAddress;
    /**
     * 最后上线时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastOnlineOn;

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
     * 长时间登录
     */
    private String longTimeLogin;

    /**
     * 创建时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdOn;

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 创建者类型
     */
    private Integer createdByType;

    /**
     * 更新时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedOn;

    /**
     * 更新者
     */
    private String updatedBy;

    /**
     * 更新者类型
     */
    private Integer updatedByType;

    /**
     * 是否删除（0：未删除 1：已删除）
     */
    private Integer deletedFlag;

    /**
     * 删除时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime deletedOn;

    /**
     * 删除人ID
     */
    private String deletedBy;

}
