package cn.cuiot.dmp.system.infrastructure.entity.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhh
 * @description
 * @author: 新增角色-自定义角色实体类
 * @create: 2020-09-22 10:40
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class RoleCreatedDTO {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 账户id
     */
    private String orgId;

    /**
     * 角色主键id
     */
    private Long id;

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
     * 备注
     */
    private String description;

    /**
     * 角色权限（1：admin；2：readonly；3：自定义）
     */
    private String permit;

    /**
     * 菜单 id集合
     */
    private List<String> menuIds;

    /**
     * api id集合
     */
    private List<String> apiIds;

    /**
     * 角色类型(1:默认角色,2:自定义角色)
     */
    private Integer roleType;

    /**
     * 资源权限（1：产品和设备全部权限；2：产品权限；3：设备权限）
     */
    private Integer resourcesPermit;

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

}
