package cn.cuiot.dmp.system.infrastructure.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author guoying
 * @className RoleEntity
 * @description 角色信息实体
 * @date 2020-08-10 16:41:33
 */
@Data
public class RoleEntity implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 租住ID
     */
    private String orgId;

    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色key
     */
    private String roleKey;
    /**
     * 备注
     */
    private String description;
    /**
     * 角色权限  [0: ADMIN, 1: VIEW_ONLY]
     */
    private Integer permit;
    /**
     * 角色类型
     */
    private Integer roleType;
    /**
     * 角色创建时间
     */
    private LocalDateTime createdOn;
    /**
     * 创建者
     * 取值：【SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的】
     */
    private String createdBy;
    /**
     * 创建者类型
     * 取值：【0：SYSTEM; 1: Portal；2：API】
     */
    private Integer createdByType;
    /**
     * 角色修改时间
     */
    private LocalDateTime updatedOn;
    /**
     * 修改者
     * 取值：【SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的】
     */
    private String updatedBy;
    /**
     * 修改者类型
     * 取值：【0：SYSTEM; 1: Portal；2：API】
     */
    private Integer updatedByType;
}
