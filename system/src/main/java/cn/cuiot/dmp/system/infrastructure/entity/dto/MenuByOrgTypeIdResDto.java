package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * MenuByOrgTypeIdResDto
 * @author wqd
 * @description
 * @date 2022/11/2
 */
@Data
public class MenuByOrgTypeIdResDto {

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 菜单名
     */
    private String menuName;

}
