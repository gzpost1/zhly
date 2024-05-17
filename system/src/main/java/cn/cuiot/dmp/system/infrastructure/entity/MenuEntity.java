package cn.cuiot.dmp.system.infrastructure.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
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
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedOn;

    /**
     * 修改者
     */
    private String updatedBy;

    /**
     * 子菜单
     */
    private List<MenuEntity> children;
}
