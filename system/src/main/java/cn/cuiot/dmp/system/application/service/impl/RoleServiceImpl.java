package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.system.application.enums.RolePermitEnum;
import cn.cuiot.dmp.system.application.enums.RoleTypeEnum;
import cn.cuiot.dmp.system.application.enums.UserSourceTypeEnum;
import cn.cuiot.dmp.system.application.service.RoleService;
import cn.cuiot.dmp.system.infrastructure.entity.RoleEntity;
import cn.cuiot.dmp.system.infrastructure.entity.bo.RoleBo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.AddMenuDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CreateRoleDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RoleCreatedDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RoleDTO;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.RoleDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDao;
import cn.cuiot.dmp.system.domain.entity.Organization;
import cn.cuiot.dmp.system.domain.types.enums.OrgTypeEnum;
import cn.cuiot.dmp.system.domain.repository.OrganizationRepository;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Resource;
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
    private OrganizationRepository organizationRepository;

    /**
     * 默认管理员角色key
     */
    private static final String DEFAULT_ROLE_KEY = "default";

    /**
     * 默认只读角色key
     */
    private static final String READONLY_ROLE_KEY = "readonly";

    /**
     * 省份管理员角色id
     */
    private static final String PROVINCE_ROLE_ID = "3";

    /**
     * 省份管理员角色key
     */
    private static final String PROVINCE_ROLE_KEY = "PROVICE_ADMIN";

    private static final String ROLE_ID = "roleId";

    private static final String USER_ID = "userId";

    private static final String ORG_ID = "orgId";

    private static final List<Long> DEFAULT_ROLE_ID = Arrays.asList(1L, 2L, 4L, 5L, 2778L, 2779L, 3000L);

    @Override
    public PageResult<RoleDTO> getRoleListByPage(Map<String, Object> paramsMap) {
        String roleId = userDao.getRoleId(String.valueOf(paramsMap.get(USER_ID)), String.valueOf(paramsMap.get(ORG_ID)));
        // 省份管理员只能看到自己的角色、企业普通用户也只能看到自己
        if (PROVINCE_ROLE_ID.equals(roleId)) {
            paramsMap.put("roleKey", PROVINCE_ROLE_KEY);
        }

        Page<RoleDTO> page = PageHelper.startPage((Integer) paramsMap.get("currentPage"), (Integer) paramsMap.get("pageSize"));
        // 超管要看到企业管理员的角色，在sql里面判断
        List<RoleDTO> roleDtoList = this.roleDao.selectRoleListByPage(paramsMap);
        if (!CollectionUtils.isEmpty(roleDtoList)) {
            List<Long> roleIdList = new ArrayList<>(roleDtoList.size());
            roleDtoList.forEach(roleDTO -> roleIdList.add(Long.valueOf(roleDTO.getId())));

            // 查询角色关联的用户
            List<Map<String, Long>> userIdList = this.roleDao.selectUserIdsByRoleIds(String.valueOf(paramsMap.get(ORG_ID)), roleIdList);
            roleDtoList.forEach(roleDTO ->
                    userIdList.forEach(map -> {
                        if (null != map.get(USER_ID) && map.get(ROLE_ID).equals(Long.valueOf(roleDTO.getId()))) {
                            // 不可以删除
                            roleDTO.setIsDeleted(1);
                            roleIdList.remove(map.get(ROLE_ID));
                        }
                    })
            );
        }
        PageInfo<RoleDTO> roleDtoPageInfo = page.toPageInfo();
        return new PageResult<>(roleDtoPageInfo);
    }

    @Override
    public List<RoleDTO> getRoleListNotDefault(Map<String, Object> paramsMap) {
        return this.roleDao.selectRoleList(paramsMap);
    }

    @LogRecord(operationCode = "deleteRoles", operationName = "删除角色", serviceType = ServiceTypeConst.SECURITY_SETTING)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoles(RoleBo roleBo) {
        // 预置角色不能删除
        DEFAULT_ROLE_ID.stream().map(String::valueOf).filter(s -> s.equals(roleBo.getRoleId())).findAny().ifPresent(s -> {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        });
        // 获取操作对象
        List<RoleEntity> roleEntityList = this.roleDao.selectRoleByRoleIds(roleBo.getDeleteIdList());
        String[] operationTargetArray = new String[roleEntityList.size()];
        for (int i = 0; i < roleEntityList.size(); i++) {
            operationTargetArray[i] = roleEntityList.get(i).getRoleName();
        }
        roleBo.setOperationTarget(operationTargetArray);

        // 查询角色关联的用户
        List<Map<String, Long>> userIdList = this.roleDao.selectUserIdsByRoleIds(roleBo.getOrgId(), roleBo.getDeleteIdList());
        if (!CollectionUtils.isEmpty(userIdList) && userIdList.parallelStream().anyMatch(map -> {
            return null != map.get(USER_ID);
        })) {
            throw new BusinessException(ResultCode.ROLE_USER_APP_ALREADY_BIND);
        }

        // 删除成功的角色数量
        int count = this.roleDao.deleteRolesByIds(roleBo.getOrgId(), roleBo.getDeleteIdList(), LocalDateTime.now());

        // 批量删除账户角色关联关系
        this.roleDao.deleteOrgRole(roleBo.getOrgId(), roleBo.getDeleteIdList());

        // 批量删除角色和菜单的关联关系
        this.roleDao.deleteBatchMenuRole(roleBo.getOrgId(), roleBo.getDeleteIdList());

        return count;
    }

    @Override
    public List<RoleDTO> getRoleListByOrgId(String orgId, String userId) {
        return this.roleDao.selectRoleListByOrgId(orgId);
    }

    @LogRecord(operationCode = "createRole", operationName = "新增角色", serviceType = ServiceTypeConst.SECURITY_SETTING)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(CreateRoleDto dto) {
        RoleDTO roleDTO = this.roleDao.selectRoleByUserId(Long.parseLong(dto.getLoginOrgId()), Long.parseLong(dto.getLoginUserId()));
        if (null == roleDTO) {
            throw new BusinessException(ResultCode.QUERY_ROLE_DETAILS_ERROR);
        }
        //查询角色对应菜单的权限id集合
        List<AddMenuDto> addMenuDtos = roleDao.getRoleMenu(Integer.parseInt(roleDTO.getId()));
        List<String> menuIds = new ArrayList<>();
        for (AddMenuDto addMenuDto : addMenuDtos) {
            menuIds.add(String.valueOf(addMenuDto.getId()));
        }
        for (String menuId : dto.getMenuIds()) {
            if (!menuIds.contains(menuId)) {
                throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
            }
        }

        Map<String, Object> paramsMap = new HashMap<>(3);
        // roleEntity 赋值
        RoleEntity entity = new RoleEntity();
        entity.setOrgId(dto.getLoginOrgId());
        entity.setCreatedByType(UserSourceTypeEnum.PORTAL.getCode());
        entity.setCreatedBy(dto.getLoginUserId());
        entity.setRoleId(String.valueOf(SnowflakeIdWorkerUtil.nextId()));
        entity.setRoleKey(String.valueOf(SnowflakeIdWorkerUtil.nextId()));
        //添加角色类型为自定义
        entity.setRoleType(RoleTypeEnum.CUSTOMIZE.getCode());
        entity.setPermit(RolePermitEnum.CUSTOMIZE.getCode());
        entity.setRoleName(dto.getRoleName());
        entity.setDescription(dto.getDescription());
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
        //插入账户和角色的关系
        this.roleDao.insertOrgRole(paramsMap);

        // 获取操作对象
        String[] operationTargetArray = new String[1];
        operationTargetArray[0] = dto.getRoleName();
        dto.setOperationTarget(operationTargetArray);

        return rolePk;

    }

    @Override
    public RoleDTO getRoleInfo(Long orgId, Long id) {
        RoleDTO roleDTO = this.roleDao.selectRoleByUserId(orgId, id);
        if (null == roleDTO) {
            throw new BusinessException(ResultCode.QUERY_ROLE_DETAILS_ERROR);
        }
        // 设置角色权限名称
        roleDTO.setPermitName(RolePermitEnum.getRolePermitNameByPermitCode(Integer.valueOf(roleDTO.getPermit())));
        return roleDTO;
    }

    @Override
    public RoleCreatedDTO getRoleAll(String roleId, String orgId, String userId) {
        RoleCreatedDTO roleCreatedDTO = roleDao.selectOneRole(Long.parseLong(orgId), Long.parseLong(roleId), Long.parseLong(userId));
        if (roleCreatedDTO == null || roleCreatedDTO.getId() == null) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }

        //查询角色对应菜单的权限id集合
        List<AddMenuDto> addMenuDtos = roleDao.getRoleMenu(Integer.parseInt(roleId));
        List<String> menuIds = new ArrayList<>();
        for (AddMenuDto addMenuDto : addMenuDtos) {
            menuIds.add(String.valueOf(addMenuDto.getId()));
        }

        roleCreatedDTO.setMenuIds(menuIds);

        return roleCreatedDTO;
    }

    @LogRecord(operationCode = "updateRole", operationName = "修改角色", serviceType = ServiceTypeConst.SECURITY_SETTING)
    @Override
    public Long updateRole(RoleBo roleBo) {
        // 本身角色及预置角色不让编辑
        RoleDTO roleDTO = roleDao.selectRoleByUserId(Long.parseLong(roleBo.getOrgId()), Long.parseLong(roleBo.getUserId()));
        if (Objects.equals(roleDTO.getId(), roleBo.getRoleId())) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }

        DEFAULT_ROLE_ID.stream().map(String::valueOf).filter(s -> s.equals(roleBo.getRoleId())).findAny().ifPresent(s -> {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        });

        RoleDTO oldRoleDTO = this.roleDao.selectRoleById(Long.valueOf(roleBo.getOrgId()), Long.valueOf(roleBo.getId()));
        if (null == oldRoleDTO || !roleBo.getRoleKey().equals(oldRoleDTO.getRoleKey())) {
            throw new BusinessException(ResultCode.ROLE_NOT_EXIST);
        }
        // 系统默认角色不能修改
        if (DEFAULT_ROLE_KEY.equals(roleBo.getRoleKey()) || READONLY_ROLE_KEY.equals(roleBo.getRoleKey())) {
            throw new BusinessException(ResultCode.DEFAULT_ROLE_NOT_OPERATE);
        }
        // 修改者类型
        roleBo.setUpdatedByType(UserSourceTypeEnum.PORTAL.getCode());
        // 修改者
        roleBo.setUpdatedBy(roleBo.getUserId());
        //更改时间
        roleBo.setUpdatedOn(LocalDateTime.now());

        List<Long> idList = new ArrayList<>();
        idList.add(roleBo.getId());
        roleBo.setDeleteIdList(idList);
        // 获取操作对象
        List<RoleEntity> roleEntityList = this.roleDao.selectRoleByRoleIds(roleBo.getDeleteIdList());
        String[] operationTargetArray = new String[roleEntityList.size()];
        for (int i = 0; i < roleEntityList.size(); i++) {
            operationTargetArray[i] = roleEntityList.get(i).getRoleName();
        }
        roleBo.setOperationTarget(operationTargetArray);

        Organization organization = organizationRepository.find(new OrganizationId(roleBo.getOrgId()));
        //如果为默认管理员角色
        if (Integer.parseInt(roleBo.getPermit()) == RolePermitEnum.ADMIN.getCode()) {
            if (organization.getOrgTypeId().getValue().equals(OrgTypeEnum.SUPER.getValue())) {
                roleBo.setRoleId("3");
                roleBo.setPermit(String.valueOf(RolePermitEnum.SUPER_ADMIN.getCode()));

                admiAndReadonly(roleBo);
            }
            if (organization.getOrgTypeId().getValue().equals(OrgTypeEnum.PROVINCE.getValue())) {
                roleBo.setRoleId("5");
                roleBo.setPermit(String.valueOf(RolePermitEnum.PROVINCE_ADMIN.getCode()));
                admiAndReadonly(roleBo);
            }
            if (organization.getOrgTypeId().getValue().equals(OrgTypeEnum.PRIVATE.getValue())
                    || organization.getOrgTypeId().getValue().equals(OrgTypeEnum.ENTERPRISE.getValue())
                    || organization.getOrgTypeId().getValue().equals(OrgTypeEnum.CHILD.getValue())) {
                roleBo.setRoleId("1");


                admiAndReadonly(roleBo);
            }
        }
        //如果为默认只读角色
        if (Integer.parseInt(roleBo.getPermit()) == RolePermitEnum.VIEW_ONLY.getCode()) {

            if (organization.getOrgTypeId().getValue().equals(OrgTypeEnum.SUPER.getValue())) {
                roleBo.setRoleId("4");
                roleBo.setPermit(String.valueOf(RolePermitEnum.SUPER_OPERATOR.getCode()));
                admiAndReadonly(roleBo);
            }
            if (organization.getOrgTypeId().getValue().equals(OrgTypeEnum.PRIVATE.getValue())
                    || organization.getOrgTypeId().getValue().equals(OrgTypeEnum.ENTERPRISE.getValue())
                    || organization.getOrgTypeId().getValue().equals(OrgTypeEnum.CHILD.getValue())) {
                roleBo.setRoleId("2");


                admiAndReadonly(roleBo);
            }
        }

        //如果为自定义角色角色
        if (Integer.parseInt(roleBo.getPermit()) == RolePermitEnum.CUSTOMIZE.getCode()) {
            //更改角色信息
            roleBo.setRoleType(Optional.ofNullable(roleBo.getRoleType()).orElse(RoleTypeEnum.CUSTOMIZE.getCode()));
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
            roleDao.deleteMenuRole(roleBo.getOrgId(), roleBo.getId());
            //添加与菜单的关联关系
            roleDao.insertMenusRole(roleBo);
        }

        return Long.valueOf(oldRoleDTO.getId());
    }

    /**
     * 角色与默认角色的关联方法
     *
     * @param roleBo
     */
    private void admiAndReadonly(RoleBo roleBo) {
        //更改角色信息
        roleBo.setRoleType(1);
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
        roleDao.deleteMenuRole(roleBo.getOrgId(), roleBo.getId());
        //查询系统默认只读角色菜单
        List<String> menuPksByRolePks = roleDao.findMenuPksByRolePk(Long.parseLong(roleBo.getRoleId()));
        //默认只读角色与管理员角色关联
        roleDao.insertMenuRole(roleBo.getId(), menuPksByRolePks, roleBo.getUserId(), 2);
    }


}

