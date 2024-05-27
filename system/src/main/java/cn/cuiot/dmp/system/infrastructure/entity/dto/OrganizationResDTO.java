package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.Data;

/**
 * @Description 查询账户信息返回
 * @Author cds
 * @Date 2020/9/1
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationResDTO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 账户名称
     */
    private String orgName;

    /**
     * 账户key
     */
    private String orgKey;

    /**
     * 账户类型（1：个人账户、2：企业账户、3：子账户、4：超级账户、5：省份账户）
     */
    private Integer orgTypeId;

    /**
     * 父级账户id
     */
    private Long parentId;

    /**
     * 父账户key
     */
    private String parentOrgKey;

    /**
     * 父账户名称
     */
    private String parentOrgName;

    /**
     * 创建时间
     */
    private LocalDateTime createdOn;

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 创建者名称
     */
    private String createdName;

    /**
     * 创建人类型
     */
    private Integer createdByType;

    /**
     * 更新时间
     */
    private LocalDateTime updatedOn;

    /**
     * 更新者
     */
    private String updatedBy;

    /**
     * 更新者名称
     */
    private String updatedByName;

    /**
     * 更新人类型
     */
    private Integer updatedByType;

    /**
     * 备注
     */
    private String description;

    /**
     * 账户所有者id
     */
    private Long orgOwner;

    /**
     * 账户所有者名称
     */
    private String orgOwnerName;

    /**
     * 最后登录者名称
     */
    private String lastOnlineUser;

    /**
     *
     */
    private String owner;

    /**
     * 最后登陆时间
     */
    private LocalDateTime lastLoginOn;

    /**
     * 父账户名称
     */
    private String parentName;

    /**
     * 父账户key
     */
    private String parentKey;

    /**
     * 账户状态（0：禁用、 1：正常）
     */
    private Integer status;

    /**
     * 是否商用（1：测试、2：商用）
     */
    private Integer level;

    /**
     * 子集
     */
    private List<OrganizationResDTO> childs;

    /**
     * 测试转商用的时间
     */
    private String commercializationTime;

    /**
     * 虚拟账户号
     */
    private String virtualAccountId;

    /**
     * 是否删除（0：未删除 1：已删除）
     */
    private Integer deletedFlag;

    /**
     * 账户子集
     */
    private List<OrganizationResDTO> children;

    /**
     * 密码
     */
    private String password;


    /**
     * 企业有效期-开始时间
     */
    private Date expStartDate;

    /**
     * 企业有效期-结束时间
     */
    private Date expEndDate;


    /**
     * 计算企业状态
     */
    public Byte getOrgStatus() {
        if (Objects.nonNull(expStartDate)) {
            if (LocalDateTime.now().isBefore(DateTimeUtil.dateToLocalDateTime(expStartDate))) {
                return EntityConstants.NOT_EFFECTIVE;
            }
        }
        if (Objects.nonNull(expEndDate)) {
            if (LocalDateTime.now()
                    .isBefore(DateTimeUtil.dateToLocalDateTime(expEndDate).plusDays(1))) {
                return EntityConstants.NORMAL;
            }
            return EntityConstants.EXPIRE;
        }
        return EntityConstants.NORMAL;
    }
}
