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
public class OrganizationActionEvent extends ActionEvent {
    /**
     * 主键id
     */
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
     * 组织最大层级
     */
    private Integer maxDeptHigh;

    private String source;

    /**
     * 创建时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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
     * 是否删除（0：未删除 1：已删除）
     */
    private Integer deletedFlag;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime deletedOn;

    private String deletedBy;

    /**
     * 更新时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedOn;

    /**
     * 更新者id
     */
    private String updatedBy;

    /**
     * 更新人类型
     */
    private Integer updatedByType;

}
