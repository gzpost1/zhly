package cn.cuiot.dmp.system.application.service.impl;

import static cn.cuiot.dmp.common.constant.CacheConst.DEPT_NAME_KEY_PREFIX;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.RandomCodeWorker;
import cn.cuiot.dmp.system.application.enums.AttrKeyEnum;
import cn.cuiot.dmp.system.application.enums.DepartmentGroupEnum;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.application.service.FactoryParkBuildingService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.BuildingDeleteReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.BuildingDetailReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.BuildingUpdateReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DepartmentPropertyDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkBuildingAddReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkBuildingListReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkBuildingListResDto;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.SpaceDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserPropertyDao;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author wqd
 * @classname FactoryParkBuildingServiceImpl
 * @description
 * @date 2023/1/13
 */
@Service
public class FactoryParkBuildingServiceImpl implements FactoryParkBuildingService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private SpaceDao spaceDao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserPropertyDao userPropertyDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buildingAdd(FactoryParkBuildingAddReqDto dto) {
        getDeptTreePath(dto.getOrgId(), dto.getUserId(), departmentService.getDeptPathById(dto.getRegionId().toString()));
        Integer nameCount = departmentDao.selectByNameAndPatentId(dto.getBuildingName(), dto.getRegionId());
        if (nameCount != null) {
            throw new BusinessException(ResultCode.REGION_BUILDING_NAME_IS_EXIST);
        }
        DepartmentEntity entity = new DepartmentEntity();
        entity.setDepartmentName(dto.getBuildingName());
        entity.setSort(Optional.ofNullable(departmentDao.getMaxSortByParentId(dto.getDeptId())).orElse(0));
        entity.setPkOrgId(Long.parseLong(dto.getOrgId()));
        entity.setParentId(dto.getRegionId());
        entity.setCreatedOn(LocalDateTime.now());
        entity.setCreatedBy(dto.getUserId());
        entity.setCode(RandomCodeWorker.generateShortUuid());
        entity.setDescription(dto.getDescription());
        DepartmentEntity parentDept = departmentDao.selectByPrimary(dto.getRegionId());
        entity.setPath(parentDept.getPath() + "-" + entity.getCode());
        entity.setDGroup(DepartmentGroupEnum.BUILDING.getCode());
        entity.setLevel(parentDept.getLevel() + 1);
        departmentDao.insertDepartment(entity);

        DepartmentPropertyDto propertyDto = new DepartmentPropertyDto();
        propertyDto.setDeptId(entity.getId());
        propertyDto.setKey(AttrKeyEnum.FLOOR_NUM.getKey());
        propertyDto.setVal(dto.getBuildingFloor().toString());
        departmentService.insertDepartmentProperty(propertyDto);
    }

    @Override
    public PageResult<FactoryParkBuildingListResDto> buildingList(FactoryParkBuildingListReqDto dto) {
        dto.setDeptTreePath(getDeptTreePath(dto.getOrgId(), dto.getUserId(), dto.getDeptTreePath()));
        Page<FactoryParkBuildingListResDto> buildingListResDtoPage = PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        dto.setGroup(DepartmentGroupEnum.BUILDING.getCode());
        List<FactoryParkBuildingListResDto> buildingListResDtoList = spaceDao.getDeptListByPathAndGroupFactoryBuilding(dto.getDeptTreePath(),
                null,
                dto.getGroup(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getKeyword(),
                dto.getLabelId());
        buildingListResDtoList.forEach(resDto -> resDto.setBuildingFloor(departmentService.getDepartmentProperty(Long.valueOf(resDto.getBuildingId()), AttrKeyEnum.FLOOR_NUM.getKey())));
        return new PageResult<>(buildingListResDtoPage.toPageInfo());
    }

    @Override
    public List<FactoryParkBuildingListResDto> buildingListByRegionId(FactoryParkBuildingListReqDto dto) {
        dto.setDeptTreePath(getDeptTreePath(dto.getOrgId(), dto.getUserId(), dto.getDeptTreePath()));
        dto.setGroup(DepartmentGroupEnum.BUILDING.getCode());
        List<FactoryParkBuildingListResDto> buildingListResDtoList = spaceDao.getDeptListByPathAndGroupFactoryBuilding(dto.getDeptTreePath(),
                dto.getRegionId(),
                dto.getGroup(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getKeyword(),
                dto.getLabelId());
        DepartmentEntity deptById = departmentService.getDeptById(userService.getDeptId(dto.getUserId(), dto.getOrgId()));
        if (!Arrays.asList(DepartmentGroupEnum.TENANT.getCode(), DepartmentGroupEnum.SYSTEM.getCode()).contains(deptById.getCode())) {
            buildingListResDtoList = buildingListResDtoList.stream().filter(o -> deptById.getPath().startsWith(o.getDeptTreePath()) | o.getDeptTreePath().startsWith(deptById.getPath())).collect(Collectors.toList());
        }
        return buildingListResDtoList;
    }

    @Override
    public FactoryParkBuildingListResDto buildingDetail(BuildingDetailReqDto dto) {
        return spaceDao.getFactoryBuildingDetail(dto.getBuildingId(), departmentDao.getPathBySpacePath(departmentService.getDeptById(userService.getDeptId(dto.getUserId(), dto.getOrgId())).getPath()).getPath());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buildingUpdate(BuildingUpdateReqDto dto) {
        getDeptTreePath(dto.getOrgId(), dto.getUserId(), departmentService.getDeptPathById(dto.getBuildingId().toString()));
        Integer nameCount = departmentDao.selectByNameAndPatentIdForUpdate(dto.getBuildingName(), dto.getRegionId(), dto.getBuildingId());
        if (nameCount != null) {
            throw new BusinessException(ResultCode.REGION_BUILDING_NAME_IS_EXIST);
        }
        DepartmentEntity entity = new DepartmentEntity();


        entity.setId(dto.getBuildingId());
        entity.setDepartmentName(dto.getBuildingName());
        entity.setDescription(dto.getDescription());
        departmentDao.updateDepartment(entity);
        redisUtil.del(DEPT_NAME_KEY_PREFIX + entity.getId());
        DepartmentPropertyDto propertyDto = new DepartmentPropertyDto();
        propertyDto.setDeptId(dto.getBuildingId());
        propertyDto.setKey(AttrKeyEnum.FLOOR_NUM.getKey());
        propertyDto.setVal(dto.getBuildingFloor().toString());
        departmentService.updateDepartmentProperty(propertyDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buildingDelete(BuildingDeleteReqDto dto) {
        Integer count = departmentDao.countByParentIds(dto.getBuildingIds());
        if (count > 0) {
            throw new BusinessException(ResultCode.COMMERCIAL_BUILDING_HAS_CHILDREN);
        }
        dto.getBuildingIds().forEach(buildIngId -> {
            DepartmentEntity department = departmentService.getDeptById(buildIngId.toString());
            userService.checkDepartmentUser(department.getPkOrgId(), department.getPath());
            String checkUserInDeptId = userService.checkUserInDeptId(buildIngId);
            if (!StringUtils.isEmpty(checkUserInDeptId)) {
                throw new BusinessException(ResultCode.SPACE_USER_ALREADY_USE);
            }
        });
        departmentService.batchDeleteDepartment(dto.getBuildingIds(), dto.getOrgId());
    }

    /**
     * 根据参数获得组织树
     *
     * @param orgId
     * @param userId
     * @param deptTreePath
     * @return
     */
    private String getDeptTreePath(String orgId, String userId, String deptTreePath) {
        DepartmentEntity deptById = departmentDao.getPathBySpacePath(departmentService.getDeptById(userService.getDeptId(userId, orgId)).getPath());
        if (!StringUtils.isEmpty(deptTreePath) && !deptTreePath.startsWith(deptById.getPath())) {
            throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
        }
        return StringUtils.isEmpty(deptTreePath) ? deptById.getPath() : deptTreePath;
    }
}
