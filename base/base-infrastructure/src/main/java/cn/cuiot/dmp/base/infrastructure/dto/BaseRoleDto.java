package cn.cuiot.dmp.base.infrastructure.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * 角色信息DTO
 */
@Data
public class BaseRoleDto implements Serializable {

    /**
     * 角色自增ID
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long orgId;

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
     * 角色权限名称 角色权限[0: ADMIN, 1: VIEW_ONLY]
     */
    private String permitName;
    /**
     * 角色权限
     */
    private Integer roleType;

}
