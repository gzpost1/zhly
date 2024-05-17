package cn.cuiot.dmp.system.infrastructure.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * @Description
 * @Author cds
 * @Date 2020/8/12
 */
@Data
public class OrganizationEntity {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 账户key
     */
    private String orgKey;

    /**
     * 账户名称
     */
    private String orgName;

    /**
     * 账户(租户)类型
     */
    private Integer orgTypeId;

    /**
     * 账户状态（0：禁用、 1：正常）
     */
    private Integer status;

    /**
     * 父级账户id
     */
    private Long parentId;

    /**
     * 账户所有者id(admin_user_id)
     */
    private Long orgOwner;

    /**
     * 备注
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createdOn;

    /**
     * 创建者id
     */
    private String createdBy;

    /**
     * 创建人类型
     */
    private Integer createdByType;

    /**
     * 更新时间
     */
    private LocalDateTime updatedOn;

    /**
     * 更新者id
     */
    private String updatedBy;

    /**
     * 更新人类型
     */
    private Integer updatedByType;

    /**
     * 是否删除（0：未删除 1：已删除）
     */
    private Integer deletedFlag;

    /**
     * 组织最大层级
     */
    private Integer maxDeptHigh;

    /**
     * 密码（加密）
     */
    private String password;

    /**
     * 下次登陆是否重置密码（0：不重置；1：重置）
     */
    private Integer resetPassword;

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
     * 统一社会信用代码
     */
    private String socialCreditCode;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 来源
     */
    private String source;
}
