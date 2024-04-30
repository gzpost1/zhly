package cn.cuiot.dmp.system.application.service.impl;

import static cn.cuiot.dmp.common.constant.CacheConst.DEPT_CODE_KEY_PREFIX;
import static cn.cuiot.dmp.common.constant.CacheConst.DEPT_NAME_KEY_PREFIX;

import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.NumberConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.RandomCodeWorker;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.system.application.constant.CurrencyConst;
import cn.cuiot.dmp.system.application.enums.DepartmentGroupEnum;
import cn.cuiot.dmp.base.application.enums.OrgTypeEnum;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DepartmentPropertyDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyByNameReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyByNameResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertDepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertSonDepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdateDepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.DepartmentTreeVO;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrganizationDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.SpaceDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDataDao;
import cn.cuiot.dmp.system.infrastructure.utils.DepartmentUtil;
import cn.cuiot.dmp.system.infrastructure.utils.OrgRedisUtil;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.system.domain.entity.Organization;
import cn.cuiot.dmp.system.domain.repository.OrganizationRepository;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * @author wensq
 * @version 1.0
 * @date 2021/12/21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private OrgRedisUtil orgRedisUtil;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private UserDataDao userDataDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SpaceDao spaceDao;

    @Autowired
    private DepartmentUtil departmentUtil;

    public static final String NULL_WORD = "null";

    public static final String INIT = "init";

    /**
     * dgroup排序列表
     */
    private static final List<Integer> SPACE_GROUP_LIST = Lists.newArrayList(7, 4, 6, 3, 2, 1);

    @Override
    public Long insertDepartment(InsertDepartmentDto dto) {
        // 重名校验
        final String siteName = dto.getDepartmentName();
        int count = departmentDao.countByDepartmentName(siteName, dto.getPkOrgId());
        if (count > 0) {
            throw new BusinessException(ResultCode.DEPARTMENT_NAME_IS_EXIST);
        }
        DepartmentEntity entity = new DepartmentEntity();
        entity.setCode(RandomCodeWorker.generateShortUuid());
        entity.setDepartmentName(siteName);
        entity.setPath(entity.getCode());
        entity.setPkOrgId(dto.getPkOrgId());
        entity.setCreatedOn(LocalDateTime.now());
        entity.setCreatedBy(dto.getCreateBy());

        return entity.getId();
    }

    @Override
    public Long insertSonDepartment(InsertSonDepartmentDto dto) {
        Long newDeptId = null;
        RLock disLock = redissonClient.getLock("LOCK_"
                + CacheConst.ORGANIZATION_INSERT_REDIS_KEY + "_" + dto.getPkOrgId());
        boolean isLock;
        try {
            //尝试获取分布式锁
            isLock = disLock.tryLock(1000, 1000, TimeUnit.MILLISECONDS);
            if (isLock) {
                newDeptId = insertSonDepartmentSafe(dto);
            }
        } catch (BusinessException e) {
            log.error("insertSonDepartment BusinessException error", e);
            throw e;
        } catch (Exception e) {
            log.error("insertSonDepartment Exception error", e);
            throw new BusinessException(ResultCode.DUPLICATE_REQUEST);
        } finally {
            // 最后解锁
            disLock.forceUnlock();
        }
        return newDeptId;
    }

    private Long insertSonDepartmentSafe(InsertSonDepartmentDto dto) {
        checkAdminUser(dto.getPkOrgId().toString(), dto.getUserId());
        final String departmentName = dto.getDepartmentName();
        final Long parentId = dto.getParentId();
        DepartmentEntity userDept = departmentDao.selectByPrimary(
                Long.valueOf(userDao.getDeptId(dto.getUserId(), dto.getPkOrgId().toString())));
        if (!Arrays
                .asList(DepartmentGroupEnum.TENANT.getCode(), DepartmentGroupEnum.SYSTEM.getCode())
                .contains(userDept.getDGroup())) {
            throw new BusinessException(ResultCode.ONLY_DEPT_USER_CAN_INSERT_DEPT);
        }
        DepartmentEntity parentDept = departmentDao.selectByPrimary(parentId);
        // 租户组织树层级限制
        Organization organization = organizationRepository
                .find(new OrganizationId(dto.getPkOrgId()));
        int maxDeptHigh = organization != null ? organization.getMaxDeptHigh() : 0;
        //获取租户账户类型
        Organization tempOrganization = organizationRepository
                .find(new OrganizationId(dto.getPkOrgId()));
        Integer orgTypeId =
                tempOrganization != null ? tempOrganization.getOrgTypeId().getValue().intValue()
                        : null;

        if (orgTypeId != null && orgTypeId.equals(OrgTypeEnum.COMMUNITY.getCode())) {
            if (maxDeptHigh < NumberConst.SEVEN) {
                organizationRepository.save(Organization.builder()
                        .id(new OrganizationId(dto.getPkOrgId()))
                        .maxDeptHigh(NumberConst.SEVEN)
                        .build());
                organization = organizationRepository.find(new OrganizationId(dto.getPkOrgId()));
                if (Objects.nonNull(organization)) {
                    maxDeptHigh = organization.getMaxDeptHigh();
                }
                parentDept = departmentDao.selectByPrimary(parentId);
            }
        }
        Integer parentDeptLevel = parentDept.getLevel();
        if (parentDeptLevel >= maxDeptHigh) {
            throw new BusinessException(ResultCode.DEPARTMENT_LEVEL_OVERRUN);
        }
        //校验本组织下组织名是否重复
        String department = departmentDao
                .selectDepartmentName(dto.getPkOrgId(), dto.getDepartmentName());
        if (department != null) {
            throw new BusinessException(ResultCode.DEPARTMENT_NAME_EXIST);
        }

        DepartmentEntity entity = new DepartmentEntity();
        String path = parentDept.getPath();
        entity.setDepartmentName(departmentName);
        entity.setSort(Optional.ofNullable(departmentDao.getMaxSortByParentId(parentId)).orElse(0));
        entity.setPkOrgId(dto.getPkOrgId());
        entity.setParentId(parentId);
        entity.setCreatedOn(LocalDateTime.now());
        entity.setCreatedBy(dto.getCreateBy());
        entity.setCode(RandomCodeWorker.generateShortUuid());
        entity.setPath(path + "-" + entity.getCode());
        entity.setDGroup(parentDept.getDGroup());
        entity.setLevel(++parentDeptLevel);
        entity.setId(SnowflakeIdWorkerUtil.nextId());
        orgRedisUtil.doubleDeleteForDbOperation(() -> departmentDao.insertDepartment(entity),
                String.valueOf(dto.getPkOrgId()));

        //获取账户日志操作对象
        String[] operationTarget = new String[1];
        operationTarget[0] = dto.getDepartmentName();
        dto.setOperationTarget(operationTarget);

        return entity.getId();
    }

    @Override
    public int insertDepartmentProperty(DepartmentPropertyDto dto) {
        return departmentDao.insertDepartmentProperty(dto.getId(),dto.getDeptId(), dto.getKey(), dto.getVal());
    }

    @Override
    public int updateDepartmentProperty(DepartmentPropertyDto dto) {
        int ret = departmentDao
                .updateDepartmentProperty(dto.getDeptId(), dto.getKey(), dto.getVal());
        redisUtil.del(DEPT_CODE_KEY_PREFIX + dto.getDeptId());
        return ret;
    }

    @Override
    public String getDepartmentProperty(Long deptId, String key) {
        return departmentDao.getDepartmentProperty(deptId, key);
    }

    @Override
    public int updateDepartment(UpdateDepartmentDto dto) {
        checkAdminUser(dto.getOrgId(), dto.getUserId());
        // 重名校验
        final Long id = dto.getId();
        final String siteName = dto.getDepartmentName();
        DepartmentEntity byPrimary = departmentDao.selectByPrimary(id);
        final Long parentId = byPrimary.getParentId();
        DepartmentEntity entity = new DepartmentEntity();
        // 根节点不存在同级别重名，不校验。不同企业间可以重名。
        if (parentId != null) {
            int count = departmentDao
                    .countByDepartmentNameForUpdate(siteName, dto.getPkOrgId(), dto.getId());
            if (count > 0) {
                throw new BusinessException(ResultCode.DEPARTMENT_NAME_EXIST);
            }
            // 切换父组织 , 空间管理需要用到s
            if (!parentId.equals(dto.getParentId())) {
                entity.setParentId(dto.getParentId());
                DepartmentEntity parentDept = departmentDao.selectByPrimary(dto.getParentId());
                entity.setPath(parentDept.getPath() + "-" + byPrimary.getCode());
            }
        }
        entity.setId(id);
        entity.setDepartmentName(siteName);

        List<Integer> result = new ArrayList<Integer>();
        orgRedisUtil.doubleDeleteForDbOperation(
                () -> result.add(departmentDao.updateDepartment(entity)),
                String.valueOf(dto.getPkOrgId()));
        redisUtil.del(DEPT_NAME_KEY_PREFIX + entity.getId());
        //获取账户日志操作对象
        String[] operationTarget = new String[1];
        operationTarget[0] = dto.getDepartmentName();
        dto.setOperationTarget(operationTarget);

        return result.get(0);
    }

    @Override
    public List<DepartmentEntity> getDeptByOrgId(String orgId) {
        return departmentDao.selectRootByOrgId(orgId);
    }

    @Override
    public DepartmentEntity getDeptById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return departmentDao.selectByPrimary(Long.parseLong(id));
    }

    @Override
    public DepartmentDto getPathByUser(Long pkUserId) {
        return departmentDao.getPathByUser(String.valueOf(pkUserId));
    }

    @Override
    public List<DepartmentTreeVO> getAllSpaceTree(String orgId, String userId) {
        // 用户组织
        DepartmentDto userPathDto = departmentDao.getPathByUser(userId);
        // 查询组织下全部组织
        List<DepartmentEntity> entityList = departmentDao
                .getAllSpaceTree(userPathDto.getPath(), orgId);
        List<DepartmentTreeVO> departmentTreeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            DepartmentTreeVO vo;
            List<DepartmentTreeVO> departmentTreeListTemp = new ArrayList<>();
            for (DepartmentEntity entity : entityList) {
                vo = new DepartmentTreeVO();
                BeanUtils.copyProperties(entity, vo);
                departmentTreeListTemp.add(vo);
            }
            // 递归组成树
            for (DepartmentTreeVO departmentTreeVO : departmentTreeListTemp) {
                if (userPathDto.getId().equals(departmentTreeVO.getId())) {
                    List<DepartmentTreeVO> childList = buildTree(departmentTreeListTemp,
                            departmentTreeVO.getId());
                    departmentTreeVO.setChildren(childList);
                    departmentTreeList.add(departmentTreeVO);
                    break;
                }
            }
        }
        return departmentTreeList;

    }

    @Override
    public List<GetDepartmentTreeLazyResDto> manageGetDepartmentTreeLazy(
            GetDepartmentTreeLazyReqDto dto) {
        final String orgId = dto.getLoginOrgId();
        final String userId = dto.getLoginUserId();
        List<GetDepartmentTreeLazyResDto> result;
        // 只能看到当前租户 所属dept为根,先获取根节点,以用户deptId为主键
        if (INIT.equals(dto.getType())) {
            Long deptId = Optional.ofNullable(userDao.getDeptId(userId, orgId)).map(Long::valueOf)
                    .orElse(null);
            result = spaceDao.getRootDepartmentLazy(orgId, deptId.toString());
            if (!Arrays.asList(DepartmentGroupEnum.SYSTEM.getCode().toString(),
                    DepartmentGroupEnum.TENANT.getCode().toString())
                    .contains(result.get(0).getDGroup())) {
                DepartmentEntity pathBySpacePath = departmentDao
                        .getPathBySpacePath(result.get(0).getPath());
                GetDepartmentTreeLazyResDto getDepartmentTreeLazyResDto = new GetDepartmentTreeLazyResDto(
                        String.valueOf(pathBySpacePath.getId()),
                        String.valueOf(pathBySpacePath.getDGroup()),
                        pathBySpacePath.getDepartmentName(),
                        String.valueOf(pathBySpacePath.getParentId()),
                        pathBySpacePath.getPath(),
                        String.valueOf(pathBySpacePath.getLevel()),
                        Const.STR_0);
                return Arrays.asList(getDepartmentTreeLazyResDto);
            }
        } else {
            result = spaceDao.getUserDepartmentLazy(dto.getParentId(),
                    Arrays.asList(DepartmentGroupEnum.SYSTEM.getCode(),
                            DepartmentGroupEnum.TENANT.getCode()));
        }
        return result;
    }

    /**
     * @param orgId -- 用户所在组织结构的Id ==对应数据表organization的id字段 ==对应site表的pk_org_id
     * 查询site表中该orgId下所有记录，形成List，存放到redis的CacheConst.ROBOT_ORGANIZATION_REDIS_KEY_orgId中。
     * list中元素的格式如下： { "createdBy":"admin", "createdOn":"2021-08-19T15:32:43",
     * "id":877938137400082432, "parentId":877934713065439232, "pkOrgId":1, "siteName":"熊猫电子",
     * "sort":0 }
     */
    @Override
    public List<DepartmentTreeVO> getDepartmentTree(String orgId, String userId, String type) {
        // 这里需要判断 .只能看到当前租户 所属dept为根
        Long deptId = Optional.ofNullable(userDao.getDeptId(userId, orgId)).map(Long::valueOf)
                .orElse(null);
        DepartmentEntity department = departmentDao.selectByPrimary(deptId);
        // 初始化departmentTreeList
        List<DepartmentTreeVO> departmentTreeList = new ArrayList<>();
        // 若为组织懒加载接口且用户所在组织为组织级以下（小区，区域等），则只返回用户所在组织
        if (!StringUtils.isEmpty(type) && type.equals(CurrencyConst.SPACE)) {
            List<Integer> dgroups = Arrays.asList(DepartmentGroupEnum.COMMUNITY.getCode(),
                    DepartmentGroupEnum.BUILDING.getCode(),
                    DepartmentGroupEnum.HOUSE.getCode(), DepartmentGroupEnum.REGION.getCode(),
                    DepartmentGroupEnum.FLOOR.getCode());
            if (dgroups.contains(department.getDGroup())) {
                DepartmentTreeVO vo = new DepartmentTreeVO();
                DepartmentEntity entity = departmentDao.getPathBySpacePath(department.getPath());
                BeanUtils.copyProperties(entity, vo);
                vo.setType(CurrencyConst.SPACE);
                departmentTreeList.add(vo);
                return departmentTreeList;
            }
        } else if (department.getDGroup().equals(DepartmentGroupEnum.BUILDING.getCode())
                || department.getDGroup().equals(DepartmentGroupEnum.FLOOR.getCode())) {
            List<DepartmentTreeVO> children = new ArrayList();
            DepartmentTreeVO vo = new DepartmentTreeVO();
            BeanUtils.copyProperties(department, vo);
            vo.setChildren(children);
            departmentTreeList.add(vo);
            return departmentTreeList;
        }
        if (department.getDGroup().equals(DepartmentGroupEnum.COMMUNITY.getCode())) {
            deptId = departmentDao.selectByPrimary(department.getParentId()).getId();
        }
        List<String> robotOrganization = redisUtil
                .lGet(CacheConst.ROBOT_ORGANIZATION_REDIS_KEY + "_" + orgId, 0, -1);
        if (CollectionUtils.isEmpty(robotOrganization)) {
            syncRedis(orgId);
            robotOrganization = redisUtil
                    .lGet(CacheConst.ROBOT_ORGANIZATION_REDIS_KEY + "_" + orgId, 0, -1);
        }
        if (!CollectionUtils.isEmpty(robotOrganization)) {
            DepartmentTreeVO vo;
            List<DepartmentTreeVO> departmentTreeListTemp = new ArrayList<>();
            for (String s : robotOrganization) {
                vo = JSONObject.parseObject(s, DepartmentTreeVO.class);
                departmentTreeListTemp.add(vo);
            }
            if (deptId != null) {
                for (DepartmentTreeVO departmentTreeVO : departmentTreeListTemp) {
                    if (deptId.equals(departmentTreeVO.getId())) {
                        List<DepartmentTreeVO> childList = buildTree(departmentTreeListTemp,
                                departmentTreeVO.getId());
                        departmentTreeVO.setChildren(childList);
                        departmentTreeList.add(departmentTreeVO);
                        break;
                    }
                }
            } else {
                for (DepartmentTreeVO departmentTreeVO : departmentTreeListTemp) {
                    if (departmentTreeVO.getParentId() == null
                            || departmentTreeVO.getParentId() == 0) {
                        List<DepartmentTreeVO> childList = buildTree(departmentTreeListTemp,
                                departmentTreeVO.getId());
                        departmentTreeVO.setChildren(childList);
                        departmentTreeList.add(departmentTreeVO);
                        break;
                    }
                }
            }
        }
        return departmentTreeList;
    }

    /**
     * 递归出组织树
     */
    private List<DepartmentTreeVO> buildTree(List<DepartmentTreeVO> robotOrganization,
            Long parentId) {
        List<DepartmentTreeVO> trees = new ArrayList<>();
        for (int i = 0; i < robotOrganization.size(); i++) {
            DepartmentTreeVO departmentTreeVO = robotOrganization.get(i);
            Long pid = null;
            Long id = departmentTreeVO.getId();
            if (departmentTreeVO.getParentId() != null) {
                pid = departmentTreeVO.getParentId();
            }
            if (parentId.equals(pid)) {
                robotOrganization.remove(i);
                i--;
                List<DepartmentTreeVO> voList = buildTree(robotOrganization, id);
                departmentTreeVO.setChildren(voList);
                trees.add(departmentTreeVO);
            }
        }
        return trees;
    }

    /**
     * 重建redis中的组织数据
     */
    private void syncRedis(String orgId) {
        List<DepartmentEntity> siteList = departmentDao.selectByOrgId(orgId);
        if (CollectionUtils.isEmpty(siteList)) {
            return;
        }
        RLock disLock = redissonClient
                .getLock("LOCK_" + CacheConst.ROBOT_ORGANIZATION_REDIS_KEY + "_" + orgId);
        boolean isLock;
        try {
            // 尝试获取分布式锁
            isLock = disLock.tryLock(1000, 1000, TimeUnit.MILLISECONDS);
            if (isLock) {
                log.debug("syncRedis Lock get success");
                redisUtil.del(CacheConst.ROBOT_ORGANIZATION_REDIS_KEY + "_" + orgId);
                for (DepartmentEntity departmentEntity : siteList) {
                    redisUtil.lSet(CacheConst.ROBOT_ORGANIZATION_REDIS_KEY + "_" + orgId,
                            JSONObject.toJSONString(departmentEntity));
                    log.debug("syncRedis item: {}" + departmentEntity.getDepartmentName());
                }
            }
        } catch (Exception e) {
        } finally {
            // 最后解锁
            disLock.forceUnlock();
        }
    }

    @LogRecord(operationCode = "deleteDepartment", operationName = "删除子组织", serviceType = ServiceTypeConst.SUPER_ORGANIZATION_MANAGEMENT)
    @Override
    public void deleteDepartment(UpdateDepartmentDto updateDepartmentDto) {
        checkAdminUser(updateDepartmentDto.getOrgId(), updateDepartmentDto.getUserId());
        Long id = updateDepartmentDto.getId();
        DepartmentEntity departmentEntity = departmentDao.selectByPrimary(id);
        String path = departmentEntity.getPath();
        String orgId = updateDepartmentDto.getOrgId();
        String loginDeptId = userDao
                .getDeptId(updateDepartmentDto.getUserId(), updateDepartmentDto.getOrgId());
        if (!departmentUtil.checkPrivilege(loginDeptId, String.valueOf(id))) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }

        // 组织有下级，不可删
        Integer count = departmentDao.countByParentId(id);
        if (count > 0) {
            throw new BusinessException(ResultCode.DEPARTMENT_HAS_CHILDREN);
        }
        // 根组织，不可删
        DepartmentEntity department = getDeptById(String.valueOf(id));

        //获取账户日志操作对象
        String[] operationTarget = new String[1];
        operationTarget[0] = department.getDepartmentName();
        updateDepartmentDto.setOperationTarget(operationTarget);

        Optional.ofNullable(department).ifPresent(e -> {
            if (e.getParentId() == null) {
                throw new BusinessException(ResultCode.DEPARTMENT_ROOT_NO_DELETE);
            }
            if (Const.NUMBER_1 == e.getIsInit()) {
                throw new BusinessException(ResultCode.DEPARTMENT_IS_INIT);
            }
        });
        // 组织有用户，不可删
        count = userDataDao.countUserOrgByDeptId(id);
        if (count > 0) {
            throw new BusinessException(ResultCode.DEPARTMENT_HAS_USER);
        }
        // 组织有关联账户，不可删
        count = organizationDao.countOrgByDeptId(id);
        if (count > 0) {
            throw new BusinessException(ResultCode.DEPARTMENT_HAS_ORG);
        }

        // 获取是否有小区
        Integer communityCount = departmentDao.getCommunityByPath(path);
        if (communityCount > NumberConst.ZERO) {
            throw new BusinessException(ResultCode.ACCOUNT_HAS_COMMUNITY);
        }

        orgRedisUtil.doubleDeleteForDbOperation(() -> departmentDao.deleteByPrimaryKey(id), orgId);
    }

    /**
     * 查询当前组织所有子组织列表（包括当前组织）
     */
    @Override
    public List<Long> getChildrenDepartmentIds(String orgId, Long deptId) {
        List<Long> childrenList = Lists.newArrayList();
        List<String> robotOrganization = redisUtil
                .lGet(CacheConst.ROBOT_ORGANIZATION_REDIS_KEY + "_" + orgId, 0, -1);
        if (CollectionUtils.isEmpty(robotOrganization)) {
            syncRedis(orgId);
            robotOrganization = redisUtil
                    .lGet(CacheConst.ROBOT_ORGANIZATION_REDIS_KEY + "_" + orgId, 0, -1);
        }
        if (!CollectionUtils.isEmpty(robotOrganization)) {
            if (deptId == null || deptId.equals(0L)) {
                return robotOrganization.stream()
                        .map(x -> JSONObject.parseObject(x, DepartmentTreeVO.class).getId())
                        .collect(Collectors.toList());
            }
            childrenList.add(deptId);
            List<Long> myChildList = getChildList(robotOrganization, deptId);
            if (!CollectionUtils.isEmpty(myChildList)) {
                childrenList.addAll(myChildList);
            }
        }
        return childrenList;
    }

    @Override
    public void batchDeleteDepartment(List<Long> ids, String orgId) {
        departmentDao.batchDelete(ids, orgId);
        departmentDao.batchDeleteProperty(ids);
    }

    private List<Long> getChildList(List<String> robotOrganization, Long deptId) {
        List<Long> childrenList = Lists.newArrayList();
        DepartmentTreeVO vo;
        for (String org : robotOrganization) {
            vo = JSONObject.parseObject(org, DepartmentTreeVO.class);
            if (vo.getParentId() != null && vo.getParentId().equals(deptId)) {
                childrenList.add(vo.getId());
                List<Long> myChildList = getChildList(robotOrganization, vo.getId());
                if (!CollectionUtils.isEmpty(myChildList)) {
                    childrenList.addAll(myChildList);
                }
            }
        }
        return childrenList;
    }

    @Override
    public GetDepartmentTreeLazyByNameResDto getUserDepartmentTreeLazyByName(
            GetDepartmentTreeLazyByNameReqDto dto) {
        String userId = dto.getUserId();
        String orgId = dto.getOrgId();
        Long deptId = Optional.ofNullable(userDao.getDeptId(userId, orgId)).map(Long::valueOf)
                .orElse(null);
        List<GetDepartmentTreeLazyResDto> rootDepartmentLazyList = spaceDao
                .getRootDepartmentLazy(orgId, deptId.toString());
        GetDepartmentTreeLazyResDto getDepartmentTreeLazyResDto = rootDepartmentLazyList.get(0);
        if (SPACE_GROUP_LIST.indexOf(Integer.valueOf(getDepartmentTreeLazyResDto.getDGroup()))
                > SPACE_GROUP_LIST.indexOf(DepartmentGroupEnum.COMMUNITY.getCode())) {
            return getDepartmentTreeByNameForFloor(dto, getDepartmentTreeLazyResDto);
        } else {
            //如果是空间级用户登陆，只能查询到其所在等级的组织
            return getDepartmentTreeByName(dto, getDepartmentTreeLazyResDto);
        }
    }

    @Override
    public String getDeptPathById(String deptId) {

        return departmentDao.getDeptPathById(deptId);
    }

    private GetDepartmentTreeLazyByNameResDto getDepartmentTreeByName(
            GetDepartmentTreeLazyByNameReqDto dto,
            GetDepartmentTreeLazyResDto getDepartmentTreeLazyResDto) {
        String dGroup = getDepartmentTreeLazyResDto.getDGroup();
        String rootId = getDepartmentTreeLazyResDto.getId();
        List<GetDepartmentTreeLazyByNameResDto> departmentList = spaceDao
                .getDepartmentByNameAndPathAndDepartmentGroup(getDepartmentTreeLazyResDto.getPath(),
                        dto.getDepartmentName(), Arrays.asList(dGroup));
        if (CollectionUtils.isEmpty(departmentList)) {
            throw new BusinessException(ResultCode.DEPT_NOT_EXISTS);
        }
        //map的key为id，值为组织节点
        Map<Long, GetDepartmentTreeLazyByNameResDto> departmentHashMap = new HashMap<>(16);
        //搜索结果可能包含跟节点，去除根节点
        Optional<GetDepartmentTreeLazyByNameResDto> rootNode = departmentList.stream()
                .filter(o -> o.getId().toString().equals(getDepartmentTreeLazyResDto.getId()))
                .findFirst();
        rootNode.ifPresent(o -> departmentList.remove(o));
        if (CollectionUtils.isEmpty(departmentList)) {
            return null;
        }
        GetDepartmentTreeLazyByNameResDto removeDto = departmentList.remove(0);
        GetDepartmentTreeLazyByNameResDto result = new GetDepartmentTreeLazyByNameResDto();
        TreeSet<GetDepartmentTreeLazyByNameResDto> childList;
        //根据parentID获得childList
        childList = spaceDao.getChildDepartmentListByParentId(removeDto.getParentId().toString());
        departmentHashMap.putAll(childList.stream().collect(Collectors
                .toMap(GetDepartmentTreeLazyByNameResDto::getId, lambdaDto -> lambdaDto)));
        //循环拼主树
        while (true) {
            if (childList.first().getParentId().toString().equals(rootId)) {
                BeanUtils.copyProperties(getDepartmentTreeLazyResDto, result);
                result.setId(Long.valueOf(getDepartmentTreeLazyResDto.getId()));
                departmentHashMap.put(result.getId(), result);
                result.setChildList(childList);
                break;
            }
            //根据parentId获得parent那一层级的结果集
            Map<Long, GetDepartmentTreeLazyByNameResDto> parentDepartmentMap = spaceDao
                    .getParentDepartmentByParentId(childList.first().getParentId().toString());
            departmentHashMap.putAll(parentDepartmentMap);
            //将childList塞入parent
            GetDepartmentTreeLazyByNameResDto parentDepartmentDto = parentDepartmentMap
                    .get(childList.first().getParentId());
            parentDepartmentDto.setChildList(childList);
            childList = parentDepartmentMap.values().stream()
                    .collect(Collectors.toCollection(TreeSet::new));
        }
        //拼副树，再拼接到主树
        for (GetDepartmentTreeLazyByNameResDto departmentTreeLazyByNameResDto : departmentList) {
            //如果map有该节点，则代表该节点已在树上
            if (departmentHashMap.containsKey(departmentTreeLazyByNameResDto.getId())) {
                continue;
            }
            removeDto = departmentTreeLazyByNameResDto;
            childList = spaceDao
                    .getChildDepartmentListByParentId(removeDto.getParentId().toString());
            while (true) {
                if (departmentHashMap.containsKey(childList.first().getParentId())) {
                    departmentHashMap.get(childList.first().getParentId()).setChildList(childList);
                    break;
                } else {
                    Map<Long, GetDepartmentTreeLazyByNameResDto> parentDepartmentMap = spaceDao
                            .getParentDepartmentByParentId(
                                    childList.first().getParentId().toString());
                    departmentHashMap.putAll(parentDepartmentMap);
                    //将childList塞入parent
                    GetDepartmentTreeLazyByNameResDto parentDepartmentDto = parentDepartmentMap
                            .get(childList.first().getParentId());
                    parentDepartmentDto.setChildList(childList);
                    childList = parentDepartmentMap.values().stream()
                            .collect(Collectors.toCollection(TreeSet::new));
                }
            }
        }
        return result;
    }

    /**
     * @param dto 搜索条件
     * @param getDepartmentTreeLazyResDto 用户自身的组织信息
     */
    private GetDepartmentTreeLazyByNameResDto getDepartmentTreeByNameForFloor(
            GetDepartmentTreeLazyByNameReqDto dto,
            GetDepartmentTreeLazyResDto getDepartmentTreeLazyResDto) {
        String dgroup = getDepartmentTreeLazyResDto.getDGroup();
        String rootId = getDepartmentTreeLazyResDto.getId();
        //如果是dgroup为1，查询1  如果dgroup为2 查询2,3 ，如果小于3，查询自身
        List<String> targetDgroupList;
        switch (dgroup) {
            case "1":
                targetDgroupList = Arrays.asList("1");
                break;
            case "2":
                targetDgroupList = Arrays.asList("2", "3");
                break;
            default:
                targetDgroupList = Arrays.asList(dgroup);
                break;
        }
        List<GetDepartmentTreeLazyByNameResDto> departmentList = spaceDao
                .getDepartmentByNameAndPathAndDepartmentGroup(getDepartmentTreeLazyResDto.getPath(),
                        dto.getDepartmentName(), targetDgroupList);
        if (CollectionUtils.isEmpty(departmentList)) {
            throw new BusinessException(ResultCode.DEPT_NOT_EXISTS);
        }
        //map的key为id，值为组织节点
        Map<Long, GetDepartmentTreeLazyByNameResDto> departmentHashMap = new HashMap<>(16);
        //搜索结果可能包含跟节点，去除根节点
        Optional<GetDepartmentTreeLazyByNameResDto> rootNode = departmentList.stream()
                .filter(o -> o.getId().toString().equals(getDepartmentTreeLazyResDto.getId()))
                .findFirst();
        rootNode.ifPresent(o -> departmentList.remove(o));
        if (CollectionUtils.isEmpty(departmentList)) {
            return null;
        }
        GetDepartmentTreeLazyByNameResDto removeDto = departmentList.remove(0);
        GetDepartmentTreeLazyByNameResDto result = new GetDepartmentTreeLazyByNameResDto();
        TreeSet<GetDepartmentTreeLazyByNameResDto> childList;
        //根据parentID获得childList
        childList = spaceDao.getChildDepartmentListByParentId(removeDto.getParentId().toString());
        departmentHashMap.putAll(childList.stream().collect(Collectors
                .toMap(GetDepartmentTreeLazyByNameResDto::getId, lambdaDto -> lambdaDto)));
        //循环拼主树
        while (true) {
            if (childList.first().getParentId().toString().equals(rootId)) {
                BeanUtils.copyProperties(getDepartmentTreeLazyResDto, result);
                result.setId(Long.valueOf(getDepartmentTreeLazyResDto.getId()));
                departmentHashMap.put(result.getId(), result);
                result.setChildList(childList);
                break;
            }
            //根据parentId获得parent那一层级的结果集
            Map<Long, GetDepartmentTreeLazyByNameResDto> parentDepartmentMap = spaceDao
                    .getParentDepartmentByParentId(childList.first().getParentId().toString());
            departmentHashMap.putAll(parentDepartmentMap);
            //将childList塞入parent
            GetDepartmentTreeLazyByNameResDto parentDepartmentDto = parentDepartmentMap
                    .get(childList.first().getParentId());
            parentDepartmentDto.setChildList(childList);
            childList = parentDepartmentMap.values().stream()
                    .collect(Collectors.toCollection(TreeSet::new));
        }
        //拼副树，再拼接到主树
        for (GetDepartmentTreeLazyByNameResDto departmentTreeLazyByNameResDto : departmentList) {
            //如果map有该节点，则代表该节点已在树上
            if (departmentHashMap.containsKey(departmentTreeLazyByNameResDto.getId())) {
                continue;
            }
            removeDto = departmentTreeLazyByNameResDto;
            childList = spaceDao
                    .getChildDepartmentListByParentId(removeDto.getParentId().toString());
            while (true) {
                if (departmentHashMap.containsKey(childList.first().getParentId())) {
                    departmentHashMap.get(childList.first().getParentId()).setChildList(childList);
                    break;
                } else {
                    Map<Long, GetDepartmentTreeLazyByNameResDto> parentDepartmentMap = spaceDao
                            .getParentDepartmentByParentId(
                                    childList.first().getParentId().toString());
                    departmentHashMap.putAll(parentDepartmentMap);
                    //将childList塞入parent
                    GetDepartmentTreeLazyByNameResDto parentDepartmentDto = parentDepartmentMap
                            .get(childList.first().getParentId());
                    parentDepartmentDto.setChildList(childList);
                    childList = parentDepartmentMap.values().stream()
                            .collect(Collectors.toCollection(TreeSet::new));
                }
            }
        }
        return result;
    }


    /**
     * 检查当前orgId，userId是否为运营侧非admin用户，是就报错
     */
    private void checkAdminUser(String orgId, String userId) {
        DepartmentEntity departmentEntity = getDeptById(userDao.getDeptId(userId, orgId));
        if (Objects.equals(departmentEntity.getDGroup(), DepartmentGroupEnum.SYSTEM.getCode())
                && !Objects.equals(userId, NumberConst.STR_TWO)) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }

    }

}
