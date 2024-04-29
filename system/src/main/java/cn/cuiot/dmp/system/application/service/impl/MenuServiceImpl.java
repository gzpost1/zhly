package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.base.infrastructure.dto.MenuDTO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.common.utils.TreeUtil;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.system.application.service.MenuService;
import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuByOrgTypeIdResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuQuery;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuTreeNode;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.MenuDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrgMenuDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDao;
import cn.cuiot.dmp.system.user_manage.domain.entity.Organization;
import cn.cuiot.dmp.system.user_manage.repository.OrganizationRepository;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     */
    @Override
    public List<MenuTreeNode> getAllMenu(String orgId, String userId) {
        //判断账户类型
        Organization organization = organizationRepository
                .find(new OrganizationId(Long.valueOf(orgId)));
        if (Objects.isNull(organization)) {
            return Lists.newArrayList();
        }
        String roleId = userDao.getRoleId(userId, orgId);

        List<String> allowMenuIdList = orgMenuDao.getAllowMenuIdList(orgId);
        if (CollectionUtils.isEmpty(allowMenuIdList)) {
            return Lists.newArrayList();
        }
        List<MenuEntity> allMenuList = menuDao.getAllRoleMenu(roleId);
        if (CollectionUtils.isEmpty(allMenuList)) {
            return Lists.newArrayList();
        }
        allMenuList = allMenuList.stream()
                .filter(item -> allowMenuIdList.contains(item.getId().toString())).collect(
                        Collectors.toList());

        List<MenuTreeNode> treeList = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(allMenuList)) {
            treeList = allMenuList.stream().map(item -> {
                MenuTreeNode treeNode = new MenuTreeNode(
                        item.getId().toString(),
                        StrUtil.toStringOrNull(item.getParentId()),
                        item.getMenuName(),
                        item.getMenuUrl(),
                        item.getComponentUri(),
                        item.getApiUrl(),
                        item.getIcon(),
                        item.getMenuType(),
                        item.getPermissionCode(),
                        item.getDescription(),
                        item.getExternalLink(),
                        item.getSort(),
                        item.getHidden(),
                        item.getStatus());
                return treeNode;
            }).collect(Collectors.toList());
        }
        return TreeUtil.makeTree(treeList);
    }

    @Override
    public List<MenuByOrgTypeIdResDto> getMenuByOrgTypeId(String orgTypeId) {
        return menuDao.getMenuByOrgTypeId(orgTypeId);
    }

    /**
     * 获得权限信息
     */
    @Override
    public MenuEntity lookUpPermission(String userId, String orgId,
            String permisstionCode) {
        return menuDao.lookUpPermission(userId, orgId, permisstionCode);
    }

    /**
     * 获取菜单列表
     */
    @Override
    public List<MenuTreeNode> queryForList(MenuQuery query) {

        List<MenuEntity> selectList = menuDao.queryForList(query);

        List<MenuTreeNode> treeList = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(selectList)) {
            treeList = selectList.stream().map(item -> {
                MenuTreeNode treeNode = new MenuTreeNode(
                        item.getId().toString(),
                        StrUtil.toStringOrNull(item.getParentId()),
                        item.getMenuName(),
                        item.getMenuUrl(),
                        item.getComponentUri(),
                        item.getApiUrl(),
                        item.getIcon(),
                        item.getMenuType(),
                        item.getPermissionCode(),
                        item.getDescription(),
                        item.getExternalLink(),
                        item.getSort(),
                        item.getHidden(),
                        item.getStatus());
                return treeNode;
            }).collect(Collectors.toList());
        }
        return TreeUtil.makeTree(treeList);
    }

    /**
     * 创建菜单
     */
    @Override
    public void create(MenuDTO menuDTO) {
        MenuEntity menuEntity = BeanMapper.map(menuDTO, MenuEntity.class);
        menuDao.insertMenu(menuEntity);
    }

    /**
     * 修改菜单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MenuDTO menuDTO) {
        MenuEntity menuEntity = BeanMapper.map(menuDTO, MenuEntity.class);
        menuDao.updateMenu(menuEntity);
        if (EntityConstants.DISABLED.equals(menuDTO.getStatus())) {
            List<MenuEntity> childList = getChildList(menuEntity.getId());
            if (CollectionUtils.isNotEmpty(childList)) {
                for (MenuEntity childMenu : childList) {
                    childMenu.setUpdatedBy(menuDTO.getUpdatedBy());
                    childMenu.setUpdatedOn(menuDTO.getUpdatedOn());
                    childMenu.setStatus(menuDTO.getStatus());
                    menuDao.updateMenu(childMenu);
                }
            }
        }
    }

    /**
     * 删除菜单项
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        List<MenuEntity> childList = getChildList(id);
        if (CollectionUtils.isNotEmpty(childList)) {
            for (MenuEntity childMenu : childList) {
                menuDao.deleteMenuById(childMenu.getId());
            }
        }
        menuDao.deleteMenuById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Byte status, String sessionUserId, String sessionOrgId) {
        MenuEntity menuEntity = menuDao.getById(id);
        menuEntity.setStatus(status.intValue());
        menuEntity.setUpdatedBy(sessionUserId);
        menuEntity.setUpdatedOn(LocalDateTime.now());
        menuDao.updateMenu(menuEntity);
        if (EntityConstants.DISABLED.equals(status)) {
            List<MenuEntity> childList = getChildList(menuEntity.getId());
            if (CollectionUtils.isNotEmpty(childList)) {
                for (MenuEntity childMenu : childList) {
                    childMenu.setUpdatedBy(sessionUserId);
                    childMenu.setUpdatedOn(LocalDateTime.now());
                    childMenu.setStatus(status.intValue());
                    menuDao.updateMenu(childMenu);
                }
            }
        }
    }

    /**
     * 递归获得子菜单
     */
    @Override
    public List<MenuEntity> getChildList(Long id) {
        List<MenuEntity> childList = Lists.newArrayList();
        List<MenuEntity> selectList = menuDao.selectChildList(id);
        if (CollectionUtils.isNotEmpty(selectList)) {
            for (MenuEntity menuEntity : selectList) {
                childList.add(menuEntity);
                List<MenuEntity> childResources = getChildList(menuEntity.getId());
                if (CollectionUtils.isNotEmpty(selectList)) {
                    childList.addAll(childResources);
                }
            }
        }
        return childList;
    }


}
