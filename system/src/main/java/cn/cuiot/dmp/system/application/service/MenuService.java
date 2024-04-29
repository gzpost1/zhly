package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuByOrgTypeIdResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuTreeNode;
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
     * @param orgId
     * @param userId
     * @return
     */
    List<MenuTreeNode> getAllMenu(String orgId, String userId);

    /**
     * 根据orgTypeId查询菜单
     * @param orgTypeId
     * @return
     */
    List<MenuByOrgTypeIdResDto> getMenuByOrgTypeId(String orgTypeId);

    /**
     * 获得权限信息
     * @param userId
     * @param orgId
     * @param permisstionCode
     * @return
     */
    MenuEntity lookUpPermission(String userId,String orgId,String permisstionCode);
}
