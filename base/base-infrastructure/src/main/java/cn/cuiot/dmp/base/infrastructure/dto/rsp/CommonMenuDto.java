package cn.cuiot.dmp.base.infrastructure.dto.rsp;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 权限菜单
 *
 * @author: wuyongchong
 * @date: 2024/6/28 16:21
 */
@Data
public class CommonMenuDto implements Serializable {

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
     * 子菜单
     */
    private List<CommonMenuDto> children;
}
