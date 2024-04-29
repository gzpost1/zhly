package cn.cuiot.dmp.base.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author zhh
 * @description
 * @author: 菜单权限列表
 * @create: 2020-09-23 09:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuDTO {
    /**
     * 菜单主键ID
     */
    private Long id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 路由地址
     */
    private String menuUrl;

    /**
     * 组件路径
     */
    private String componentUri;

    /**
     * 对应api url
     */
    private String apiUrl;


    /**
     * 图标
     */
    private String icon;

    /**
     * 菜单类型:1-菜单 2-功能按钮
     */
    private Integer menuType;

    /**
     * 权限代码
     */
    private String permissionCode;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 菜单描述
     */
    private String description;

    /**
     * 外链 1 是 0否
     */
    private Integer externalLink;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否隐藏 1:隐藏，0:显示
     */
    private Integer hidden;

    /**
     * 状态 1启用 0停用
     */
    private Integer status;

    /**
     * 子菜单集合
     */
    private List<MenuDTO> children;
}
