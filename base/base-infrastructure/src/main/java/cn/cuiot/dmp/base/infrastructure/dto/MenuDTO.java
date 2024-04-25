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
     * 菜单主键id
     */
    private String id;

    /**
     * 菜单路径
     */
    private String menuUrl;

    /**
     * api对应的路径
     */
    private String apiUrl;

    /**
     * 菜单menuId
     */
    private String menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 备注
     */
    private String description;

    /**
     * 菜单类型
     */
    private Integer menuType;

    /**
     * 菜单模块类型
     */
    private String moduleType;

    /**
     * 子菜单集合
     */
    private List<MenuDTO> children;
}
