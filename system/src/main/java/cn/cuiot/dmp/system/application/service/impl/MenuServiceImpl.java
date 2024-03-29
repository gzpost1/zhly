package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.system.application.service.MenuService;
import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetMenuRootByOrgTypeIdResDto;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.MenuDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrgMenuDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDao;
import cn.cuiot.dmp.system.user_manage.domain.entity.Organization;
import cn.cuiot.dmp.system.user_manage.repository.OrganizationRepository;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author guoying
 * @className MenuServiceImpl
 * @description 菜单管理业务层
 * @date 2020-08-10 19:04:55
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {

	@Resource
	private OrganizationRepository organizationRepository;

	@Resource
	private UserDao userDao;

	@Resource
	private MenuDao menuDao;

	@Autowired
	private OrgMenuDao orgMenuDao;

	/**
	 * 获取菜单
	 *
	 * @return
	 */
	@Override
	public List<MenuEntity> getAllMenu(String description, String orgId, String userId) {
		//判断账户类型
		Organization organization = organizationRepository.find(new OrganizationId(Long.valueOf(orgId)));
		if (Objects.isNull(organization)) {
			return Lists.newArrayList();
		}
		Integer orgTypeId = Math.toIntExact(organization.getOrgTypeId().getValue());
		String roleId = userDao.getRoleId(userId, orgId);
		List<String> banMenuIdList = orgMenuDao.getBanMenuIdList(orgId);
		List<MenuEntity> allMenuList = menuDao.getAllRoleMenu(roleId, orgTypeId);
		Map<Long, List<MenuEntity>> map = allMenuList.stream().collect(Collectors.groupingBy(MenuEntity::getParentMenuId));
		if (StringUtils.isEmpty(description)) {
			List<MenuEntity> menuList = map.get(0L);
			menuList = menuList.stream().filter(o -> !banMenuIdList.contains(o.getMenuId())).collect(Collectors.toList());
			for (MenuEntity menu1 : menuList) {
				//遍历一级菜单
				List<MenuEntity> menuList1 = setChildrenList(menu1, banMenuIdList, map);
				//遍历二级菜单
				for (MenuEntity menu2 : menuList1) {
					List<MenuEntity> menuList2 = setChildrenList(menu2, banMenuIdList, map);
					//遍历三级菜单
					for (MenuEntity menu3 : menuList2) {
						List<MenuEntity> menuList3 = setChildrenList(menu3, banMenuIdList, map);
						// 遍历四级菜单
						for (MenuEntity menu4 : menuList3) {
							setChildrenList(menu4, banMenuIdList, map);
						}
					}
				}
			}
			return menuList;
		} else {
			return menuDao.selectMenu(description);
		}
	}

	/**
	 * 设备下级菜单
	 * @param menu
	 * @param banMenuIdList
	 * @param map
	 * @return
	 */
	private List<MenuEntity> setChildrenList(MenuEntity menu, List<String> banMenuIdList, Map<Long, List<MenuEntity>> map) {
		String menuId = menu.getMenuId();
		List<MenuEntity> menuList = map.get(Long.parseLong(menuId));
		if (!CollectionUtils.isEmpty(menuList)) {
			menuList = menuList.stream().filter(o -> !banMenuIdList.contains(o.getMenuId())).collect(Collectors.toList());
			//菜单数据为空为null
			if (!CollectionUtils.isEmpty(menuList)) {
				menu.setChildren(menuList);
			}
		}

		return Optional.ofNullable(menuList).orElse(Collections.emptyList());
	}

    @Override
    public List<GetMenuRootByOrgTypeIdResDto> getMenuRootByOrgTypeId(String orgTypeId) {
        return menuDao.getMenuRootByOrgTypeId(orgTypeId);
    }

}
