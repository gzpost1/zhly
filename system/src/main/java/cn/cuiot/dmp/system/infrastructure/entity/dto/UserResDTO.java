package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.houbb.sensitive.annotation.Sensitive;
import com.github.houbb.sensitive.core.api.strategory.StrategyEmail;
import com.github.houbb.sensitive.core.api.strategory.StrategyPhone;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangze
 * @classname UserResDTO
 * @description 用户响应DTO
 * @date 2020-06-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResDTO {

    /**
     * 主键id
     */
    private String id;

    /**
     * 账户id
     */
    private String orgId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码（加密）
     */
    private String password;

    /**
     * 邮箱
     * 脱敏字段
     */
    @Sensitive(strategy = StrategyEmail.class)
    private String email;

    /**
     * 手机号
     * 脱敏字段
     */
    @Sensitive(strategy = StrategyPhone.class)
    private String phoneNumber;

    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户权限
     */
    private List<MenuEntity> menu;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 机器人：账户类型（1：联通总部-L1、2：联通省分-L2、3：服务提供方-L3、4：服务使用方-L3）
     */
    /**
     * CPE：账户类型（1：个人账户、2：企业账户、3：子账户、4：超级账户、5：省份账户）
     */
    private Integer orgTypeId;

    /**
     * 账户标签id（9：厂园区）
     */
    private Integer orgLabelTypeId;
    /**
     * orgKey
     */
    private String orgKey;

    /**
     * 是否商用（1：测试、2：商用）
     */
    private Integer orgLevel;

    /**
     * 组织最大层级
     */
    private Integer maxDeptHigh;

    /**
     * 平台主题颜色
     * 新增2021/01/20
     */
    private String themeColor;

    /**
     * 最后上线时间
     */
    private LocalDateTime lastOnlineOn;

    /**
     * 用户是否阅读过导航（0：没有、1：有）
     */
    private Integer introduction;

    /**
     * 用户偏好设置角色（1：设备运营、2：企业管理员、3：IT运维）
     */
    private String type;

    /**
     * 是否需要完善注册信息（0：无需、1：需要）
     */
    private String completeOrgInfo;

    /**
     * logo地址
     */
    private String logoAddress;

    /**
     * 标题
     */
    private String title;

    /**
     * 第二标题
     */
    private String secondTitle;

    /**
     * 是否同意隐私协议（0：未同意、1：同意)
     */
    private String privacyAgreement;

    /**
     * 版本公告（0：未通知、1：已通知）
     */
    private String releaseNote;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色Key
     */
    private String roleKey;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 用户的组织类型
     */
    private String dGroup;

}
