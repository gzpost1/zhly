package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.infrastructure.dto.BaseRoleDto;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseRoleReqDto;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.system.application.enums.RolePermitEnum;
import cn.cuiot.dmp.system.application.enums.RoleTypeEnum;
import cn.cuiot.dmp.system.application.enums.UserSourceTypeEnum;
import cn.cuiot.dmp.system.application.param.assembler.RoleConverter;
import cn.cuiot.dmp.system.application.service.MenuService;
import cn.cuiot.dmp.system.application.service.RoleService;
import cn.cuiot.dmp.system.domain.entity.Organization;
import cn.cuiot.dmp.system.domain.repository.OrganizationRepository;
import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;
import cn.cuiot.dmp.system.infrastructure.entity.RoleEntity;
import cn.cuiot.dmp.system.infrastructure.entity.bo.RoleBo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.AddMenuDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CreateRoleDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RoleDTO;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.RoleDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * @author chenwl
 * @classname PropertyComplaintServiceImpl
 * @description
 * @date 2022/8/8
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Resource
    private UserDao userDao;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private RoleConverter roleConverter;

    /**
     * 默认管理员角色key
     */
    private static final String DEFAULT_ROLE_KEY = "default";

    /**
     * 默认只读角色key
     */
    private static final String READONLY_ROLE_KEY = "readonly";


    private static final String ROLE_ID = "roleId";

    private static final String USER_ID = "userId";

    private static final String ORG_ID = "orgId";

    private static final List<Long> DEFAULT_ROLE_ID = Arrays.asList(1L);

    @Override
    public PageResult<RoleDTO> getRoleListByPage(Map<String, Object> paramsMap) {
       PageHelper.startPage((Integer) paramsMap.get("pageNo"),
                (Integer) paramsMap.get("pageSize"));
        // 超管要看到企业管理员的角色，在sql里面判断
        List<RoleDTO> roleDtoList = this.roleDao.selectRoleListByPage(paramsMap);
        PageInfo<RoleDTO> page = new PageInfo<>(roleDtoList);
        if (!CollectionUtils.isEmpty(roleDtoList)) {
            List<Long> roleIdList = Lists.newArrayList();
            roleDtoList.forEach(roleDTO -> roleIdList.add(Long.valueOf(roleDTO.getId())));
            // 查询角色关联的用户
            List<Map<String, Long>> userIdList = this.roleDao
                    .selectUserIdsByRoleIds(String.valueOf(paramsMap.get(ORG_ID)), roleIdList);
            roleDtoList.forEach(roleDTO ->
                    userIdList.forEach(map -> {
                        if (null != map.get(USER_ID) && map.get(ROLE_ID)
                                .equals(Long.valueOf(roleDTO.getId()))) {
                            // 不可以删除
                            roleDTO.setIsDeleted(1);
                            roleIdList.remove(map.get(ROLE_ID));
                        }
                    })
            );
        }
        return new PageResult<>(page);
    }

    @Override
    public List<RoleDTO> getRoleListNotDefault(Map<String, Object> paramsMap) {
        return this.roleDao.selectRoleList(paramsMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoles(RoleBo roleBo) {
        String sessionOrgId = roleBo.getSessionOrgId();
        List<Long> deleteIdList = Optional.ofNullable(roleBo.getDeleteIdList())
                .orElse(Lists.newArrayList());
        if (Objects.nonNull(roleBo.getId())) {
            if (!deleteIdList.contains(roleBo.getId())) {
                deleteIdList.add(roleBo.getId());
            }
        }

        for (Long deleteId : deleteIdList) {
            // 预置角色不能删除
            DEFAULT_ROLE_ID.stream().filter(s -> s.equals(deleteId)).findAny().ifPresent(s -> {
                throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION,"该角色不能删除");
            });
        }

        // 获取操作对象
        List<RoleEntity> roleEntityList = this.roleDao
                .selectRoleByRoleIds(deleteIdList);

        String[] operationTargetArray = new String[roleEntityList.size()];
        for (int i = 0; i < roleEntityList.size(); i++) {
            operationTargetArray[i] = roleEntityList.get(i).getRoleName();
        }
        roleBo.setOperationTarget(operationTargetArray);

        // 查询角色关联的用户
        List<Map<String, Long>> userIdList = this.roleDao
                .selectUserIdsByRoleIds(sessionOrgId, deleteIdList);

        if (!CollectionUtils.isEmpty(userIdList) && userIdList.parallelStream().anyMatch(map -> {
            return null != map.get(USER_ID);
        })) {
            throw new BusinessException(ResultCode.ROLE_USER_APP_ALREADY_BIND,"该角色下存在用户，不可删除");
        }

        // 删除成功的角色数量
        int count = this.roleDao
                .deleteRolesByIds(roleBo.getSessionOrgId(), deleteIdList,
                        LocalDateTime.now());

        // 批量删除账户角色关联关系
        this.roleDao.deleteOrgRole(roleBo.getSessionOrgId(), deleteIdList);

        // 批量删除角色和菜单的关联关系
        this.roleDao.deleteBatchMenuRole(roleBo.getSessionOrgId(), deleteIdList);

        return count;
    }

    @Override
    public List<RoleDTO> getRoleListByOrgId(String orgId, String userId) {
        return this.roleDao.selectRoleListByOrgId(orgId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(CreateRoleDto dto) {
        if (CollectionUtils.isEmpty(dto.getMenuIds())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "请配置角色权限");
        }
        /**
         * 权限授权检测
         */
        checkSelectMenuIds(dto.getLoginOrgId(), dto.getLoginUserId(), dto.getMenuIds());

        Map<String, Object> paramsMap = new HashMap<>(3);
        // roleEntity 赋值
        RoleEntity entity = new RoleEntity();
        entity.setOrgId(dto.getLoginOrgId());
        entity.setCreatedByType(UserSourceTypeEnum.PORTAL.getCode());
        entity.setCreatedBy(dto.getLoginUserId());
        entity.setRoleKey(String.valueOf(SnowflakeIdWorkerUtil.nextId()));
        //添加角色类型为自定义
        entity.setRoleType(RoleTypeEnum.CUSTOMIZE.getCode());
        entity.setPermit(RolePermitEnum.CUSTOMIZE.getCode());
        entity.setRoleName(dto.getRoleName());
        entity.setDescription(dto.getDescription());
        entity.setId(SnowflakeIdWorkerUtil.nextId());
        entity.setStatus(EntityConstants.ENABLED);
        try {
            this.roleDao.insertRole(entity);
        } catch (Exception e) {
            // 角色名唯一索引冲突处理
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw new BusinessException(ResultCode.ROLE_NAME_ALREADY_EXIST, e);
            }
            throw new BusinessException(ResultCode.INNER_ERROR, e);
        }
        Long rolePk = entity.getId();
        //添加菜单和角色的关联关系
        roleDao.insertMenuRole(rolePk, dto.getMenuIds(), dto.getLoginUserId(), 2);

        paramsMap = new HashMap<>(3);
        paramsMap.put("roleId", rolePk);
        paramsMap.put(ORG_ID, dto.getLoginOrgId());
        paramsMap.put("createdBy", dto.getLoginUserId());
        paramsMap.put("id", SnowflakeIdWorkerUtil.nextId());
        //插入账户和角色的关系
        this.roleDao.insertOrgRole(paramsMap);

        // 获取操作对象
        String[] operationTargetArray = new String[1];
        operationTargetArray[0] = dto.getRoleName();
        dto.setOperationTarget(operationTargetArray);

        return rolePk;

    }

    /**
     * 权限授权检测
     */
    private void checkSelectMenuIds(String loginOrgId, String loginUserId,
            List<String> menuIdList) {
        List<MenuEntity> permissionMenus = menuService
                .getPermissionMenus(loginOrgId, loginUserId);
        if (CollectionUtils.isEmpty(permissionMenus)) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        List<String> menuIds = new ArrayList<>();
        for (MenuEntity menuEntity : permissionMenus) {
            menuIds.add(String.valueOf(menuEntity.getId()));
        }
        for (String menuId : menuIdList) {
            if (!menuIds.contains(menuId)) {
                throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
            }
        }
    }

    @Override
    public RoleDTO getRoleInfo(String roleId, String orgId, String userId) {
        RoleDTO roleDTO = roleDao
                .selectOneRole(Long.parseLong(orgId), Long.parseLong(roleId),
                        Long.parseLong(userId));
        if (roleDTO == null || roleDTO.getId() == null) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }

        //查询角色对应菜单的权限id集合
        List<AddMenuDto> addMenuDtos = roleDao.getRoleMenu(Long.valueOf(roleId));
        List<String> menuIds = new ArrayList<>();
        for (AddMenuDto addMenuDto : addMenuDtos) {
            menuIds.add(String.valueOf(addMenuDto.getId()));
        }

        roleDTO.setMenuIds(menuIds);

        // 设置角色权限名称
        roleDTO.setPermitName(
                RolePermitEnum.getRolePermitNameByPermitCode(Integer.valueOf(roleDTO.getPermit())));

        return roleDTO;
    }

    @Override
    public Long updateRole(RoleBo roleBo) {
        // 本身角色不让编辑
       /* RoleDTO roleDTO = roleDao.selectRoleByUserId(Long.parseLong(roleBo.getSessionOrgId()),
                Long.parseLong(roleBo.getSessionUserId()));
        if (Objects.equals(roleDTO.getId(), roleBo.getId().toString())) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }*/
        // 预置角色不能编辑
        DEFAULT_ROLE_ID.stream().filter(s -> s.equals(roleBo.getId())).findAny().ifPresent(s -> {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION,"该角色不能修改");
        });
        // 系统默认角色和只读角色不能修改
        if (DEFAULT_ROLE_KEY.equals(roleBo.getRoleKey()) || READONLY_ROLE_KEY
                .equals(roleBo.getRoleKey())) {
            throw new BusinessException(ResultCode.DEFAULT_ROLE_NOT_OPERATE);
        }

        RoleDTO oldRoleDTO = this.roleDao
                .selectRoleById(Long.valueOf(roleBo.getSessionOrgId()), roleBo.getId());
        if (null == oldRoleDTO) {
            throw new BusinessException(ResultCode.ROLE_NOT_EXIST);
        }

        /**
         * 权限授权检测
         */
        if (CollectionUtils.isEmpty(roleBo.getMenuIds())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL, "请配置角色权限");
        }
        checkSelectMenuIds(roleBo.getSessionOrgId(), roleBo.getSessionUserId(),
                roleBo.getMenuIds());

        // 修改者类型
        roleBo.setUpdatedByType(UserSourceTypeEnum.PORTAL.getCode());
        // 修改者
        roleBo.setUpdatedBy(roleBo.getSessionUserId());
        //更改时间
        roleBo.setUpdatedOn(LocalDateTime.now());

        Organization organization = organizationRepository
                .find(new OrganizationId(roleBo.getSessionOrgId()));

        roleBo.setRoleType(
                Optional.ofNullable(roleBo.getRoleType()).orElse(RoleTypeEnum.CUSTOMIZE.getCode()));
        try {
            roleDao.updateRole(roleBo);
        } catch (Exception e) {
            // 角色名唯一索引冲突处理
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw new BusinessException(ResultCode.ROLE_NAME_ALREADY_EXIST, e);
            }
            throw new BusinessException(ResultCode.INNER_ERROR, e);
        }
        //解除与菜单的关联关系
        roleDao.deleteMenuRole(roleBo.getId());
        //添加与菜单的关联关系
        roleDao.insertMenusRole(roleBo);

        return Long.valueOf(oldRoleDTO.getId());
    }

    /**
     * 查询角色
     */
    @Override
    public List<BaseRoleDto> lookUpRoleList(BaseRoleReqDto query) {
        List<RoleEntity> roleEntities = roleDao
                .lookUpRoleList(query.getOrgId(), query.getRoleIdList());
        List<BaseRoleDto> dtoList = Optional.ofNullable(roleEntities).orElse(Lists.newArrayList())
                .stream().map(item -> {
                    return roleConverter.entityToBaseRoleDto(item);
                }).collect(Collectors.toList());
        return dtoList;
    }

    /**
     * 启停用
     */
    @Override
    public void updateStatus(UpdateStatusParam updateStatusParam, Long sessionUserId,
            Long sessionOrgId) {
        Long roleId = updateStatusParam.getId();
        RoleDTO roleDTO = this.roleDao.selectRoleById(sessionOrgId,roleId);
        if(Objects.isNull(roleDTO)){
            throw new BusinessException(ResultCode.ROLE_NOT_EXIST);
        }
        // 预置角色不能启停用
        DEFAULT_ROLE_ID.stream().filter(s -> s.equals(roleId)).findAny().ifPresent(s -> {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        });
        if(EntityConstants.DISABLED.equals(updateStatusParam.getStatus())){
            // 查询角色关联的用户
            List<Map<String, Long>> userIdList = this.roleDao
                    .selectUserIdsByRoleIds(sessionOrgId.toString(),Lists.newArrayList(roleId));

            if (!CollectionUtils.isEmpty(userIdList) && userIdList.parallelStream().anyMatch(map -> {
                return null != map.get(USER_ID);
            })) {
                throw new BusinessException(ResultCode.ROLE_USER_APP_ALREADY_BIND,"该角色下存在用户，不可停用");
            }
        }
        RoleBo roleBo = new RoleBo();
        roleBo.setId(roleId);
        // 修改者类型
        roleBo.setUpdatedByType(UserSourceTypeEnum.PORTAL.getCode());
        // 修改者
        roleBo.setUpdatedBy(roleBo.getSessionUserId());
        //更改时间
        roleBo.setUpdatedOn(LocalDateTime.now());
        //状态
        roleBo.setStatus(updateStatusParam.getStatus());

        roleDao.updateRole(roleBo);
    }
}

