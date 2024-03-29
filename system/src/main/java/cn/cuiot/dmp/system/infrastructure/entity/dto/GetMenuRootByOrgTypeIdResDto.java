package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author wqd
 * @classname GetMenuRootByOrgTypeIdResDto
 * @description
 * @date 2022/11/2
 */
@Data
public class GetMenuRootByOrgTypeIdResDto {

    /**
     * 菜单根节点id
     */
    private String menuRootId;

    /**
     * 菜单根节点名
     */
    private String menuRootName;

}
