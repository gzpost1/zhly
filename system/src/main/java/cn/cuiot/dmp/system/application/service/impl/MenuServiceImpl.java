package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.common.utils.TreeUtil;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.system.application.service.MenuService;
import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuByOrgTypeIdResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MenuTreeNode;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.MenuDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrgMenuDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDao;
import cn.cuiot.dmp.system.user_manage.domain.entity.Organization;
import cn.cuiot.dmp.system.user_manage.repository.OrganizationRepository;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
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

}
