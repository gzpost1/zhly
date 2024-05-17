package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.bean.TreeNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: wuyongchong
 * @date: 2024/4/29 15:02
 */
@Getter
@Setter
@ToString
public class MenuTreeNode extends TreeNode<MenuTreeNode> {

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


    public MenuTreeNode() {
    }

    public MenuTreeNode(String id, String parentId, String title) {
        super(id, parentId, title);
    }

    public MenuTreeNode(String id, String parentId, String title, String menuUrl,
            String componentUri, String apiUrl, String icon,
            Integer menuType, String permissionCode, String description,
            Integer externalLink, Integer sort, Integer hidden, Integer status) {
        super(id, parentId, title);
        this.menuUrl = menuUrl;
        this.componentUri = componentUri;
        this.apiUrl = apiUrl;
        this.icon = icon;
        this.menuType = menuType;
        this.permissionCode = permissionCode;
        this.description = description;
        this.externalLink = externalLink;
        this.sort = sort;
        this.hidden = hidden;
        this.status = status;
    }
}
