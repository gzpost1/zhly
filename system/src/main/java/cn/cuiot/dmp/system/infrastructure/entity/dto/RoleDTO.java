package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * @author guoying
 * @className RoleDTO
 * @description 角色信息DTO
 * @date 2020-08-10 16:56:10
 */
@Data
public class RoleDTO {

    /**
     * 角色自增ID
     */
    private String id;
    /**
     * 角色ID
     */
    private String roleId;
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
     * 角色权限
     */
    private String permit;
    /**
     * 角色权限名称 角色权限[0: ADMIN, 1: VIEW_ONLY]
     */
    private String permitName;
    /**
     * 角色权限
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
     * 创建者
     */
    private String createdByName;
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
     * 修改者
     */
    private String updatedByName;

    /**
     * 是否可以删除
     * 取值：【0：可以删除；1：不可以删除】
     */
    private Integer isDeleted;

    /**
     * 创建者类型
     * 取值：【0：SYSTEM; 1: Portal；2：API】
     */
    private Integer createdByType;
    /**
     * 修改者类型
     * 取值：【0：SYSTEM; 1: Portal；2：API】
     */
    private Integer updatedByType;

    /**
     * 菜单 id集合
     */
    private List<String> menuIds;
}
