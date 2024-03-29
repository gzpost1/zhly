package cn.cuiot.dmp.system.infrastructure.entity;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * @author Gengyu
 * @Date 2020/9/24
 */
@Data
public class MenuEntity {
    /**
     * 子菜单
     */
    private List<MenuEntity> children;
    /**
     * 菜单自增ID
     */
    private Long id;
    /**
     * 菜单id
     */
    private String menuId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单地址
     */
    private String menuUrl;

    /**
     * 对应api url
     */
    private String apiUrl;

    /**
     *
     */
    private String componentUri;
    /**
     * 菜单描述
     */
    private String description;
    /**
     * 图标
     */
    private String icon;

    /**
     * 图标激活
     */
    private String iconActive;
    /**
     * 菜单类型:1模块 2菜单项 3查看操作 4编辑操作
     */
    private Integer menuType;
    /**
     * menu_type为3时必填
     */
    private String permissionCode;
    /**
     * 父id， 顶级存储0
     */
    private Long parentMenuId;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 1:true:隐藏，0:false:显示
     */
    private Integer hidden;
    /**
     * 菜单创建时间
     */
    private LocalDateTime createdOn;
    /**
     * 创建者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的
     */
    private String createdBy;
    /**
     * 创建者类型【1：SYSTEM; 2: Portal；3：API】
     */
    private Integer createdByType;
    /**
     * 角色修改时间
     */
    private LocalDateTime updatedOn;
    /**
     * 修改者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的
     */
    private String updatedBy;
    /**
     * 修改者类型【1：SYSTEM; 2: Portal；3：API】
     */
    private Integer updatedByType;

    /**
     * 菜单分类（1：超级管理员菜单、2：普通用户菜单）
     */
    private Integer moduleType;

    /**
     * 区分不同 前端微服务
     **/
    private String catalog;
}
