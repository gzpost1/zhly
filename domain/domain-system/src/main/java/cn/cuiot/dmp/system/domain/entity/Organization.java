package cn.cuiot.dmp.system.domain.entity;

import cn.cuiot.dmp.domain.entity.AbstractAggregate;
import cn.cuiot.dmp.domain.types.Email;
import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.system.domain.types.enums.OrgSourceEnum;
import cn.cuiot.dmp.system.domain.types.enums.OrgStatusEnum;
import cn.cuiot.dmp.system.domain.types.enums.OrgTypeEnum;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @Author 犬豪
 * @Date 2023/8/28 11:43
 * @Version V1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Organization extends AbstractAggregate<OrganizationId> {

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
    private OrgTypeEnum orgTypeId;

    /**
     * 账户状态（0：禁用、 1：正常）
     */
    private OrgStatusEnum status;

    /**
     * 父级账户id
     */
    private OrganizationId parentId;

    /**
     * 账户所有者id
     */
    private UserId orgOwner;

    /**
     * 邮箱
     */
    private Email email;

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
     * 创建者id
     */
    private String createdBy;

    /**
     * 创建人类型
     */
    private OperateByTypeEnum createdByType;

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
    private OperateByTypeEnum updatedByType;

    /**
     * 是否删除（0：未删除 1：已删除）
     */
    private Integer deletedFlag;

    /**
     * 组织最大层级
     */
    private Integer maxDeptHigh;

    /**
     * 来源
     */
    private OrgSourceEnum source;


    /**
     * 是否是省份账户
     */
    public boolean isProvinceType(){
        return OrgTypeEnum.PROVINCE == orgTypeId;
    }
}
