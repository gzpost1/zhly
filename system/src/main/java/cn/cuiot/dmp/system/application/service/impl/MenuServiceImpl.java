package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.base.infrastructure.dto.AuthorizeParam;
import cn.cuiot.dmp.base.infrastructure.dto.MenuDTO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.enums.OrgTypeEnum;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.common.utils.TreeUtil;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.system.application.service.MenuService;
import cn.cuiot.dmp.system.domain.entity.Organization;
import cn.cuiot.dmp.system.domain.repository.OrganizationRepository;
import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuByOrgTypeIdResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuQuery;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuTreeNode;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgMenuDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.OrgTypeMenuDto;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.MenuDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrgMenuDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrgTypeMenuDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.RoleDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDao;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

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

    private final static String PERMISSION_PATH_SEPERATOR = ";";

    @Resource
    private OrganizationRepository organizationRepository;

    @Resource
    private UserDao userDao;

    @Resource
    private RoleDao roleDao;

    @Resource
    private MenuDao menuDao;

    @Autowired
    private OrgMenuDao orgMenuDao;

    @Autowired
    private OrgTypeMenuDao orgTypeMenuDao;

    /**
     * 企业菜单权限类型
     */
    private final static String ENTERPRISE_TYPE="enterprise";

    /**
     * 获取菜单
     */
    @Override
    public List<MenuTreeNode> getAllMenu(String orgId, String userId,String type) {
        List<MenuEntity> permissionMenus = null;
        if(ENTERPRISE_TYPE.equals(type)){
            permissionMenus = menuDao.selectMenuByOrgTypeId(OrgTypeEnum.ENTERPRISE.getValue());
        }else{
            permissionMenus = getPermissionMenus(orgId, userId);
        }
        List<MenuTreeNode> treeList = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(permissionMenus)) {
            treeList = permissionMenus.stream().map(item -> {
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
     * 获取权限菜单
     * @param orgId
     * @param userId
     * @return
     */
    @Override
    public List<MenuEntity> getPermissionMenus(String orgId, String userId) {
        //判断账户类型
        Organization organization = organizationRepository
                .find(new OrganizationId(Long.valueOf(orgId)));
        if (Objects.isNull(organization)) {
            return Lists.newArrayList();
        }
        List<String> allowMenuIdList = orgMenuDao.getAllowMenuIdList(orgId);
        if (CollectionUtils.isEmpty(allowMenuIdList)) {
            return Lists.newArrayList();
        }
        List<MenuEntity> allMenuList = Lists.newArrayList();
        if(organization.getOrgOwner().getStrValue().equals(userId)){
            //平台管理员
            if(OrgTypeEnum.PLATFORM.getValue().equals(organization.getOrgTypeId().getValue())){
                allMenuList = menuDao.selectMenuByOrgTypeId(organization.getOrgTypeId().getValue());
            }else{
                //企业管理员
                allMenuList = menuDao.selectMenuByOrgId(Long.valueOf(orgId));

            }
        }else{
            String roleId = userDao.getRoleId(userId, orgId);
            allMenuList = menuDao.getAllRoleMenu(roleId);
        }
        if (CollectionUtils.isEmpty(allMenuList)) {
            return Lists.newArrayList();
        }
        allMenuList = allMenuList.stream()
                .filter(item -> allowMenuIdList.contains(item.getId().toString())).collect(
                        Collectors.toList());
        return allMenuList;
    }

    @Override
    public List<MenuByOrgTypeIdResDto> getMenuByOrgTypeId(String orgTypeId) {
        return menuDao.getMenuByOrgTypeId(orgTypeId);
    }

    /**
     * 获得权限信息
     */
    @Override
    public MenuEntity lookUpPermission(String userId, String orgId,String reqPermission) {
        PathMatcher pathMatcher = new AntPathMatcher();
        List<MenuEntity> permissionMenus = getPermissionMenus(orgId, userId);
        if(CollectionUtils.isNotEmpty(permissionMenus)){
            for(MenuEntity menuEntity:permissionMenus){
                if(StringUtils.isNotBlank(menuEntity.getPermissionCode())){
                    if (reqPermission.equals(menuEntity.getPermissionCode())) {
                        return menuEntity;
                    }
                }
                if(StringUtils.isNotBlank(menuEntity.getApiUrl())){
                    String[] urlArr = menuEntity.getApiUrl().split(PERMISSION_PATH_SEPERATOR);
                    for (String urlPerm : urlArr) {
                        if(StringUtils.isNotBlank(urlPerm)){
                            if (pathMatcher.match(urlPerm, reqPermission)) {
                                return menuEntity;
                            }
                        }
                    }
                }
            }
        }
        return null;
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
        menuEntity.setId(SnowflakeIdWorkerUtil.nextId());
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

    /**
     * 初始化授权
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authorize(AuthorizeParam authorizeParam) {
        orgTypeMenuDao.deleteMenu(authorizeParam.getOrgTypeId());
        if (CollectionUtils.isNotEmpty(authorizeParam.getResourceIds())) {
            List<OrgTypeMenuDto> list = authorizeParam.getResourceIds().stream().map(menuId -> {
                return new OrgTypeMenuDto(SnowflakeIdWorkerUtil.nextId(), Long.valueOf(menuId),
                        authorizeParam.getOrgTypeId());
            }).collect(Collectors.toList());
            orgTypeMenuDao.insertMenu(list);
        }
        if(OrgTypeEnum.PLATFORM.getValue().equals(authorizeParam.getOrgTypeId())){
            orgMenuDao.deleteByOrgId(1L);
            if (CollectionUtils.isNotEmpty(authorizeParam.getResourceIds())) {
                List<OrgMenuDto> menuDtoList = authorizeParam.getResourceIds().stream().map(ite -> {
                    return new OrgMenuDto(SnowflakeIdWorkerUtil.nextId(), 1L, Long.valueOf(ite),
                            LocalDateTime.now(),Long.valueOf(authorizeParam.getSessionUserId()));
                }).collect(Collectors.toList());
                orgMenuDao.insertOrgMenu(menuDtoList);
            }
        }
    }

    /**
     * 查询详情
     */
    @Override
    public MenuEntity queryForDetail(Long id) {
        return menuDao.getById(id);
    }
}
