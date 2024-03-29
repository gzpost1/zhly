package cn.cuiot.dmp.system.infrastructure.entity;

import java.time.LocalDateTime;

/**
 * @author guoying
 * @className RoleEntity
 * @description 角色信息实体
 * @date 2020-08-10 16:41:33
 */
public class RoleEntity {

    /**
     * 角色自增ID
     */
    private Long id;
    /**
     * 角色ID
     */
    private String roleId;

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

    public RoleEntity() {
    }

    public RoleEntity(Long id, String roleId, String roleName, String roleKey, String description, Integer permit, Integer roleType, LocalDateTime createdOn, String createdBy, Integer createdByType, LocalDateTime updatedOn, String updatedBy, Integer updatedByType) {
        this.id = id;
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleKey = roleKey;
        this.description = description;
        this.permit = permit;
        this.roleType = roleType;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.createdByType = createdByType;
        this.updatedOn = updatedOn;
        this.updatedBy = updatedBy;
        this.updatedByType = updatedByType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPermit() {
        return permit;
    }

    public void setPermit(Integer permit) {
        this.permit = permit;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getCreatedByType() {
        return createdByType;
    }

    public void setCreatedByType(Integer createdByType) {
        this.createdByType = createdByType;
    }

    public Integer getUpdatedByType() {
        return updatedByType;
    }

    public void setUpdatedByType(Integer updatedByType) {
        this.updatedByType = updatedByType;
    }
}
