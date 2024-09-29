package cn.cuiot.dmp.system.application.service.impl;

import static cn.cuiot.dmp.common.constant.CacheConst.DEPT_CODE_KEY_PREFIX;
import static cn.cuiot.dmp.common.constant.CacheConst.DEPT_NAME_KEY_PREFIX;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.infrastructure.constants.MsgBindingNameConstants;
import cn.cuiot.dmp.base.infrastructure.constants.MsgTagConstants;
import cn.cuiot.dmp.base.infrastructure.constants.SendMsgRedisKeyConstants;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.DepartmentTreeRspDTO;
import cn.cuiot.dmp.base.infrastructure.stream.StreamMessageSender;
import cn.cuiot.dmp.base.infrastructure.stream.messaging.SimpleMsg;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.NumberConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.RandomCodeWorker;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.system.application.constant.CurrencyConst;
import cn.cuiot.dmp.system.application.constant.DepartmentConstant;
import cn.cuiot.dmp.system.application.enums.DepartmentGroupEnum;
import cn.cuiot.dmp.system.application.param.assembler.DepartmentConverter;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.domain.entity.Organization;
import cn.cuiot.dmp.system.domain.repository.OrganizationRepository;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DepartmentPropertyDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyByNameReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyByNameResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertSonDepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdateDepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.DepartmentTreeVO;
import cn.cuiot.dmp.system.infrastructure.entity.vo.DepartmentUserVO;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OrganizationDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.SpaceDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserDataDao;
import cn.cuiot.dmp.system.infrastructure.utils.DepartmentUtil;
import cn.cuiot.dmp.system.infrastructure.utils.OrgRedisUtil;
import cn.hutool.core.collection.CollUtil;
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
    private DepartmentConverter departmentConverter;

    @Autowired
    private DepartmentUtil departmentUtil;

    @Autowired
    private StreamMessageSender streamMessageSender;

    public static final String NULL_WORD = "null";

    public static final String INIT = "init";

    /**
     * 全部类型
     */
    public static final String ALL_TYPE = "all";

    /**
     * dgroup排序列表
     */
    private static final List<Integer> SPACE_GROUP_LIST = Lists.newArrayList(7, 4, 6, 3, 2, 1);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertSonDepartment(InsertSonDepartmentDto dto) {
        DepartmentEntity newDept = null;
        RLock disLock = redissonClient.getLock("LOCK_"
                + CacheConst.ORGANIZATION_INSERT_REDIS_KEY + "_" + dto.getPkOrgId());
        boolean isLock;
        try {
            //尝试获取分布式锁
            isLock = disLock.tryLock(1000, 1000, TimeUnit.MILLISECONDS);
            if (isLock) {
                newDept = insertSonDepartmentSafe(dto);
            }
            //发送MQ消息
            streamMessageSender.sendGenericMessage(
                    MsgBindingNameConstants.SYSTEM_PRODUCER,
                    SimpleMsg.builder()
                            .delayTimeLevel(2)
                            .operateTag(MsgTagConstants.DEPARTMENT_ADD)
                            .data(newDept)
                            .dataId(newDept.getId())
                            .info("创建组织部门")
                            .build());
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
        return newDept.getId();
    }

    private DepartmentEntity insertSonDepartmentSafe(InsertSonDepartmentDto dto) {
        //checkAdminUser(dto.getPkOrgId().toString(), dto.getUserId());
        final String departmentName = dto.getDepartmentName();
        final Long parentId = dto.getParentId();
        final Long pkOrgId = dto.getPkOrgId();
        final String pkUserId = dto.getUserId();

        //只有组织级用户才可添加组织
        DepartmentEntity userDept = departmentDao.selectByPrimary(
                Long.valueOf(userDao.getDeptId(pkUserId, pkOrgId.toString())));
        if (!Arrays
                .asList(DepartmentGroupEnum.TENANT.getCode(), DepartmentGroupEnum.SYSTEM.getCode())
                .contains(userDept.getDGroup())) {
            throw new BusinessException(ResultCode.ONLY_DEPT_USER_CAN_INSERT_DEPT);
        }

        DepartmentEntity parentDept = departmentDao.selectByPrimary(parentId);

        //获取所属企业账户
        Organization organization = organizationRepository
                .find(new OrganizationId(pkOrgId));

        //获取组织树层级限制
        int maxDeptHigh = organization != null ? organization.getMaxDeptHigh() : 0;

        //获取租户账户类型
        Integer orgTypeId =
                organization != null ? organization.getOrgTypeId().getValue().intValue() : null;

        //层级限制判断
        Integer parentDeptLevel = parentDept.getLevel();
        if ((parentDeptLevel+1) >= maxDeptHigh) {
            throw new BusinessException(ResultCode.DEPARTMENT_LEVEL_OVERRUN);
        }

        //同级组织名称不可重复
        String department = departmentDao
                .selectDepartmentName(pkOrgId, parentId, dto.getDepartmentName());
        if (department != null) {
            throw new BusinessException(ResultCode.DEPARTMENT_NAME_EXIST);
        }

        String path = parentDept.getPath();

        DepartmentEntity entity = new DepartmentEntity();
        entity.setDepartmentName(departmentName);
        entity.setSort(Optional.ofNullable(departmentDao.getMaxSortByParentId(parentId)).orElse(0));
        entity.setPkOrgId(pkOrgId);
        entity.setParentId(parentId);
        entity.setCreatedOn(LocalDateTime.now());
        entity.setCreatedBy(dto.getCreateBy());
        entity.setCode(RandomCodeWorker.generateShortUuid());
        entity.setPath(path + "-" + entity.getCode());
        entity.setPathName(parentDept.getPathName() + ">" + departmentName);
        entity.setDGroup(parentDept.getDGroup());
        entity.setLevel(++parentDeptLevel);
        entity.setId(SnowflakeIdWorkerUtil.nextId());
        orgRedisUtil.doubleDeleteForDbOperation(() -> departmentDao.insertDepartment(entity),
                String.valueOf(dto.getPkOrgId()));

        //获取账户日志操作对象
        String[] operationTarget = new String[1];
        operationTarget[0] = dto.getDepartmentName();
        dto.setOperationTarget(operationTarget);

        return entity;
    }

    @Override
    public int insertDepartmentProperty(DepartmentPropertyDto dto) {
        return departmentDao
                .insertDepartmentProperty(dto.getId(), dto.getDeptId(), dto.getKey(), dto.getVal());
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
    @Transactional(rollbackFor = Exception.class)
    public int updateDepartment(UpdateDepartmentDto dto) {
        //checkAdminUser(dto.getOrgId(), dto.getUserId());

        final Long id = dto.getId();
        final Long pkOrgId = dto.getPkOrgId();
        final String departmentName = dto.getDepartmentName();
        DepartmentEntity byPrimary = departmentDao.selectByPrimary(id);
        final Long parentId = byPrimary.getParentId();

        //同级组织名称不可重复
        if (Objects.nonNull(parentId)) {
            int count = departmentDao
                    .countByDepartmentNameForUpdate(departmentName, pkOrgId, parentId, id);
            if (count > 0) {
                throw new BusinessException(ResultCode.DEPARTMENT_NAME_EXIST);
            }
        }

        DepartmentEntity entity = new DepartmentEntity();
        entity.setId(id);
        entity.setDepartmentName(departmentName);

        List<Integer> result = new ArrayList<Integer>();

        orgRedisUtil.doubleDeleteForDbOperation(
                () -> result.add(departmentDao.updateDepartment(entity)),
                String.valueOf(dto.getPkOrgId()));

        redisUtil.del(DEPT_NAME_KEY_PREFIX + entity.getId());

        //名称变更，需要变更path name
        if (!byPrimary.getDepartmentName().equals(departmentName)) {
            String newPathName = departmentName;
            if (Objects.nonNull(parentId)) {
                DepartmentEntity parentDept = departmentDao.selectByPrimary(parentId);
                newPathName = parentDept.getPathName() + ">" + departmentName;
            }
            departmentDao.updatePathNameByPath(byPrimary.getPath(), byPrimary.getPathName(),
                    newPathName);
        }

        //获取账户日志操作对象
        String[] operationTarget = new String[1];
        operationTarget[0] = dto.getDepartmentName();
        dto.setOperationTarget(operationTarget);

        //发送MQ消息
        DepartmentEntity newDept = departmentDao.selectByPrimary(id);
        streamMessageSender.sendGenericMessage(
                MsgBindingNameConstants.SYSTEM_PRODUCER,
                SimpleMsg.builder()
                        .delayTimeLevel(2)
                        .operateTag(MsgTagConstants.DEPARTMENT_UPDATE)
                        .data(newDept)
                        .dataId(newDept.getId())
                        .info("修改组织部门")
                        .build());

        // 删除发送短信用到的组织缓存
        redisUtil.del(SendMsgRedisKeyConstants.SMS_DEPARTMENT + byPrimary.getPkOrgId());

        return result.get(0);
    }

    @Override
    public List<DepartmentEntity> getDeptRootByOrgId(String orgId) {
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
     *              查询site表中该orgId下所有记录，形成List，存放到redis的CacheConst.ROBOT_ORGANIZATION_REDIS_KEY_orgId中。
     *              list中元素的格式如下： { "createdBy":"admin", "createdOn":"2021-08-19T15:32:43",
     *              "id":877938137400082432, "parentId":877934713065439232, "pkOrgId":1, "siteName":"熊猫电子",
     *              "sort":0 }
     */
    @Override
    public List<DepartmentTreeVO> getDepartmentTree(String orgId, String userId, String type) {
        // 只能看到当前租户 所属dept为根
        Long deptId = Optional.ofNullable(userDao.getDeptId(userId, orgId)).map(Long::valueOf)
                .orElse(null);

        DepartmentEntity department = departmentDao.selectByPrimary(deptId);

        // 初始化departmentTreeList
        List<DepartmentTreeVO> departmentTreeList = new ArrayList<>();

        List<DepartmentEntity> siteList = departmentDao.selectByOrgId(orgId);

        if (!CollectionUtils.isEmpty(siteList)) {
            DepartmentTreeVO vo;
            List<DepartmentTreeVO> departmentTreeListTemp = new ArrayList<>();
            for (DepartmentEntity s : siteList) {
                vo = JSONObject.parseObject(JSONObject.toJSONString(s), DepartmentTreeVO.class);
                departmentTreeListTemp.add(vo);
            }

            if(ALL_TYPE.equals(type)){
                for (DepartmentTreeVO departmentTreeVO : departmentTreeListTemp) {
                    departmentTreeVO.setDisabled(true);
                    if (deptId.equals(departmentTreeVO.getId())) {
                        departmentTreeVO.setDisabled(false);
                    }
                    if (departmentTreeVO.getPath().startsWith(department.getPath())) {
                        departmentTreeVO.setDisabled(false);
                    }
                }
                //返回全部
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
            }else{
                for (DepartmentTreeVO departmentTreeVO : departmentTreeListTemp) {
                    departmentTreeVO.setDisabled(false);
                }
                //只返回当前所属组织以及下级
                for (DepartmentTreeVO departmentTreeVO : departmentTreeListTemp) {
                    if (deptId.equals(departmentTreeVO.getId())) {
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

    @Override
    public void deleteDepartment(UpdateDepartmentDto updateDepartmentDto) {
        //checkAdminUser(updateDepartmentDto.getOrgId(), updateDepartmentDto.getUserId());
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
            /*if (Const.NUMBER_1 == e.getIsInit()) {
                throw new BusinessException(ResultCode.DEPARTMENT_IS_INIT);
            }*/
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
       /* Integer communityCount = departmentDao.getCommunityByPath(path);
        if (communityCount > NumberConst.ZERO) {
            throw new BusinessException(ResultCode.ACCOUNT_HAS_COMMUNITY);
        }*/

        orgRedisUtil.doubleDeleteForDbOperation(() -> departmentDao.deleteByPrimaryKey(id), orgId);

        // 删除发送短信用到的组织缓存
        redisUtil.del(SendMsgRedisKeyConstants.SMS_DEPARTMENT + departmentEntity.getPkOrgId());

        //发送MQ消息
        streamMessageSender.sendGenericMessage(
                MsgBindingNameConstants.SYSTEM_PRODUCER,
                SimpleMsg.builder()
                        .delayTimeLevel(2)
                        .operateTag(MsgTagConstants.DEPARTMENT_DELETE)
                        .data(department)
                        .dataId(department.getId())
                        .info("删除组织部门")
                        .build());
    }

    /**
     * 查询当前组织所有子组织列表（包括当前组织）
     */
    @Override
    public List<Long> getChildrenDepartmentIds(String orgId, Long deptId) {
        List<Long> childrenList = Lists.newArrayList();

        List<DepartmentEntity> siteList = departmentDao.selectByOrgId(orgId);

        if (!CollectionUtils.isEmpty(siteList)) {
            if (deptId == null || deptId.equals(0L)) {
                return siteList.stream()
                        .map(x -> JSONObject
                                .parseObject(JSONObject.toJSONString(x), DepartmentTreeVO.class)
                                .getId())
                        .collect(Collectors.toList());
            }
            childrenList.add(deptId);
            List<Long> myChildList = getChildList(siteList, deptId);
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

        // 删除发送短信用到的组织缓存
        redisUtil.del(SendMsgRedisKeyConstants.SMS_DEPARTMENT + orgId);
    }

    private List<Long> getChildList(List<DepartmentEntity> siteList, Long deptId) {
        List<Long> childrenList = Lists.newArrayList();
        DepartmentTreeVO vo;
        for (DepartmentEntity entity : siteList) {
            vo = JSONObject.parseObject(JSONObject.toJSONString(entity), DepartmentTreeVO.class);
            if (vo.getParentId() != null && vo.getParentId().equals(deptId)) {
                childrenList.add(vo.getId());
                List<Long> myChildList = getChildList(siteList, vo.getId());
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
     * @param dto                         搜索条件
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

    /**
     * 查询部门
     */
    @Override
    public List<DepartmentDto> lookUpDepartmentList(DepartmentReqDto query) {
        List<DepartmentEntity> selectList = departmentDao.lookUpDepartmentList(query);
        List<DepartmentDto> dtoList = Optional.ofNullable(selectList).orElse(Lists.newArrayList())
                .stream().map(item -> {
                    return departmentConverter.entityToDTO(item);
                }).collect(Collectors.toList());

        return dtoList;
    }

    /**
     * 查询子部门
     *
     * @param query
     * @return
     */
    @Override
    public List<DepartmentDto> lookUpDepartmentChildList(DepartmentReqDto query) {
        if (Objects.isNull(query.getDeptId())) {
            throw new BusinessException(ResultCode.PARAM_NOT_NULL);
        }
        DepartmentEntity departmentEntity = departmentDao
                .getDepartmentById(query.getDeptId().toString());
        if (Objects.isNull(departmentEntity)) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST);
        }
        List<DepartmentEntity> childList = Lists.newArrayList();
        List<DepartmentEntity> selectList = null;
        if (NumberConst.ONE.equals(query.getReturnType())) {
            //只返回直接子部门
            selectList = departmentDao
                    .getDepartmentListByParentIdAndPath(departmentEntity.getPkOrgId(), departmentEntity.getId(), null);
        } else {
            //返回所有子部门
            selectList = departmentDao
                    .getDepartmentListByParentIdAndPath(departmentEntity.getPkOrgId(), null, departmentEntity.getPath());
            //先去除自己
            selectList = Optional.ofNullable(selectList).orElse(Lists.newArrayList())
                    .stream()
                    .filter(item -> !item.getId().equals(departmentEntity.getId()))
                    .collect(Collectors.toList());
        }
        if (Boolean.TRUE.equals(query.getSelfReturn())) {
            childList.add(departmentEntity);
        }
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(selectList)) {
            childList.addAll(selectList);
        }

        List<DepartmentDto> dtoList = Optional.ofNullable(childList).orElse(Lists.newArrayList())
                .stream()
                .map(item -> {
                    return departmentConverter.entityToDTO(item);
                }).collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public List<DepartmentUserVO> getDepartmentOrUserByType(Long orgId, Long deptId, String type) {
        AssertUtil.notNull(orgId, "企业id不能为空");
        AssertUtil.notBlank(type, "查询类型不能为空");
        List<DepartmentUserVO> departmentUserVOList = new ArrayList<>();
        List<DepartmentUserVO> departmentVOList = new ArrayList<>();
        List<DepartmentUserVO> userVOList = new ArrayList<>();
        List<DepartmentEntity> departmentEntityList;
        List<UserDataEntity> userDataEntityList = new ArrayList<>();
        if (Objects.isNull(deptId)) {
            departmentEntityList = departmentDao.selectRootByOrgId(orgId.toString());
            // 如果查询的类型为用户
            if (DepartmentConstant.USER_TYPE.equals(type)) {
                userDataEntityList = userDataDao.getUserByDeptId(orgId);
            }
        } else {
            departmentEntityList = departmentDao.getDepartmentListByParentIdAndPath(orgId, deptId, null);
            // 如果查询的类型为用户
            if (DepartmentConstant.USER_TYPE.equals(type)) {
                userDataEntityList = userDataDao.getUserByDeptId(deptId);
            }
        }
        if (!CollectionUtils.isEmpty(departmentEntityList)) {
            departmentVOList = departmentEntityList.stream()
                    .map(o -> {
                        DepartmentUserVO departmentUserVO = new DepartmentUserVO();
                        departmentUserVO.setId(o.getId());
                        departmentUserVO.setName(o.getDepartmentName());
                        departmentUserVO.setType(DepartmentConstant.DEPARTMENT_TYPE);
                        return departmentUserVO;
                    })
                    .collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(userDataEntityList)) {
            userVOList = userDataEntityList.stream()
                    .map(o -> {
                        DepartmentUserVO departmentUserVO = new DepartmentUserVO();
                        departmentUserVO.setId(o.getId());
                        departmentUserVO.setName(o.getName());
                        departmentUserVO.setType(DepartmentConstant.USER_TYPE);
                        return departmentUserVO;
                    })
                    .collect(Collectors.toList());
        }
        departmentUserVOList.addAll(departmentVOList);
        departmentUserVOList.addAll(userVOList);
        return departmentUserVOList;
    }

    @Override
    public DepartmentTreeRspDTO getDepartmentTreeRspDTO(DepartmentTreeVO rootTreeNode) {
        DepartmentTreeRspDTO departmentTreeRspDTO = new DepartmentTreeRspDTO();
        BeanUtils.copyProperties(rootTreeNode, departmentTreeRspDTO);
        departmentTreeRspDTO.setChildren(new ArrayList<>());
        if (!CollectionUtils.isEmpty(rootTreeNode.getChildren())) {
            for (DepartmentTreeVO child : rootTreeNode.getChildren()) {
                departmentTreeRspDTO.getChildren().add(getDepartmentTreeRspDTO(child));
            }
        }
        return departmentTreeRspDTO;
    }

    @Override
    public List<DepartmentDto> lookUpDepartmentChildList2(DepartmentReqDto query) {
        if (CollUtil.isEmpty(query.getDeptIdList())){
            return new ArrayList<>();
        }
        return departmentDao.querySubDepartment(query);
    }

}
