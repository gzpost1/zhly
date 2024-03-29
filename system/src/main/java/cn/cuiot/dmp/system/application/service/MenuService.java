package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetMenuRootByOrgTypeIdResDto;
import java.util.List;

/**
 * @author wangyh
 * @className MenuService
 * @description 菜单管理业务层接口
 * @date 2020-08-31 19:04:14
 */
public interface MenuService {

    /**
     * 获取所有菜单
     * @param description
     * @param orgId
     * @param userId
     * @return
     */
    List<MenuEntity> getAllMenu(String description, String orgId, String userId);

    /**
     * 根据orgTypeId查询菜单根节点
     * @param orgTypeId
     * @return
     */
    List<GetMenuRootByOrgTypeIdResDto> getMenuRootByOrgTypeId(String orgTypeId);
}
