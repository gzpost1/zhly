package cn.cuiot.dmp.system.infrastructure.entity.bo;


import cn.cuiot.dmp.common.log.intf.AbstractResourceParam;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RoleBo
 * @author guoying
 * @className RoleBo
 * @description 角色日志操作对象实体
 * @date 2020-10-16 15:38:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleBo extends AbstractResourceParam {

    /**
     * 账户id
     */
    private String sessionOrgId;

    /**
     * 用户id
     */
    private String sessionUserId;

    /**
     * 删除idList
     */
    private List<Long> deleteIdList;

    /**
     * 角色主键id
     */
    private Long id;

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
     * 角色类型(1:默认角色,2:自定义角色)
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
    private Integer isDeleted = 0;

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
     * 状态 1-启用 0-停用
     */
    private Byte status;
}
