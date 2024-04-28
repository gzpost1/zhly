package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 租户表
 * </p>
 *
 * @author yth
 * @since 2023-08-29
 */
@Getter
@Setter
@TableName("organization")
public class OrganizationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账户key
     */
    private String orgKey;

    /**
     * 账户id
     */
    private String orgId;

    /**
     * 账户名称
     */
    private String orgName;

    /**
     * 租户类型id;organization_type.id
     */
    private Long orgTypeId;

    /**
     * 账户状态（0：禁用、1：正常）
     */
    private Integer status;

    /**
     * 父级账户id
     */
    private Long parentId;

    /**
     * 账户所有者
     */
    private Long orgOwner;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 统一社会信用代码
     */
    private String socialCreditCode;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 备注
     */
    private String description;

    /**
     * 企业有效期-开始时间
     */
    private Date expStartDate;

    /**
     * 企业有效期-结束时间
     */
    private Date expEndDate;

    /**
     * 创建时间
     */
    private LocalDateTime createdOn;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建者类型（1：system、2：用户、3：open api）
     */
    private Integer createdByType;

    /**
     * 修改时间
     */
    private LocalDateTime updatedOn;

    /**
     * 修改人
     */
    private String updatedBy;

    /**
     * 修改者类型（1：system、2：用户、3：open api）
     */
    private Integer updatedByType;

    /**
     * 是否删除（0：未删除 1：已删除）
     */
    @TableLogic
    private Integer deletedFlag;

    /**
     * 删除时间
     */
    private LocalDateTime deletedOn;

    /**
     * 删除人
     */
    private String deletedBy;

    /**
     * 组织最大层级
     */
    private Integer maxDeptHigh;

    /**
     * 来源 0（楼宇）1（物联应用平台)
     */
    private Integer source;
}
