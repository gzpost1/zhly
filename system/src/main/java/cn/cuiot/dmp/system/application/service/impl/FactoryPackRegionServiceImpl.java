package cn.cuiot.dmp.system.application.service.impl;

import static cn.cuiot.dmp.common.constant.CacheConst.DEPT_NAME_KEY_PREFIX;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.RandomCodeWorker;
import cn.cuiot.dmp.system.application.enums.AttrKeyEnum;
import cn.cuiot.dmp.system.application.enums.DepartmentGroupEnum;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.application.service.FactoryPackRegionService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DepartmentPropertyDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionAddReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionDeleteReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionDetailReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionListReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionListResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionUpdateReqDto;
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
 * @classname PropertyRegionServiceImpl
 * @description
 * @date 2023/1/12
 */
@Service
public class FactoryPackRegionServiceImpl implements FactoryPackRegionService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SpaceDao spaceDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserPropertyDao userPropertyDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void regionAdd(RegionAddReqDto dto) {
        getDeptTreePath(dto.getOrgId(), dto.getUserId(), departmentService.getDeptPathById(dto.getParkId().toString()));
        Integer nameCount = departmentDao.selectByNameAndPatentId(dto.getRegionName(), dto.getParkId());
        if (nameCount != null) {
            throw new BusinessException(ResultCode.REGION_NAME_IS_EXIST);
        }
        DepartmentEntity entity = new DepartmentEntity();
        entity.setDepartmentName(dto.getRegionName());
        entity.setSort(Optional.ofNullable(departmentDao.getMaxSortByParentId(dto.getDeptId())).orElse(0));
        entity.setPkOrgId(Long.parseLong(dto.getOrgId()));
        entity.setParentId(dto.getParkId());
        entity.setCreatedOn(LocalDateTime.now());
        entity.setCreatedBy(dto.getUserId());
        entity.setCode(RandomCodeWorker.generateShortUuid());
        entity.setDescription(dto.getDescription());
        DepartmentEntity parentDept = departmentDao.selectByPrimary(dto.getParkId());
        entity.setPath(parentDept.getPath() + "-" + entity.getCode());
        entity.setDGroup(DepartmentGroupEnum.REGION.getCode());
        entity.setLevel(parentDept.getLevel() + 1);
        departmentDao.insertDepartment(entity);

        DepartmentPropertyDto propertyDto = new DepartmentPropertyDto();
        propertyDto.setDeptId(entity.getId());
        propertyDto.setKey(AttrKeyEnum.REGION_TYPE.getKey());
        propertyDto.setVal(dto.getRegionType());
        departmentService.insertDepartmentProperty(propertyDto);
        propertyDto.setKey(AttrKeyEnum.OTHER_REGION_TYPE_NAME.getKey());
        propertyDto.setVal(dto.getOtherRegionTypeName());
        departmentService.insertDepartmentProperty(propertyDto);
    }

    @Override
    public PageResult<RegionListResDto> regionList(RegionListReqDto dto) {
        dto.setDeptTreePath(getDeptTreePath(dto.getOrgId(), dto.getUserId(), dto.getDeptTreePath()));
        Page<RegionListResDto> regionListResDtoPage = PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        dto.setGroup(DepartmentGroupEnum.REGION.getCode());
        spaceDao.getDeptListByPathAndGroupRegion(dto.getDeptTreePath(),
                null,
                dto.getGroup(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getKeyword(),
                dto.getLabelId());
        return new PageResult<>(regionListResDtoPage.toPageInfo());
    }

    @Override
    public List<RegionListResDto> regionListByParkId(RegionListReqDto dto) {
        dto.setDeptTreePath(getDeptTreePath(dto.getOrgId(), dto.getUserId(), dto.getDeptTreePath()));
        dto.setGroup(DepartmentGroupEnum.REGION.getCode());
        List<RegionListResDto> regionListResDtoList = spaceDao.getDeptListByPathAndGroupRegion(dto.getDeptTreePath(),
                dto.getParkId(),
                dto.getGroup(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getKeyword(),
                dto.getLabelId());
        DepartmentEntity deptById = departmentService.getDeptById(userService.getDeptId(dto.getUserId(), dto.getOrgId()));
        if (!Arrays.asList(DepartmentGroupEnum.TENANT.getCode(), DepartmentGroupEnum.SYSTEM.getCode()).contains(deptById.getCode())) {
            regionListResDtoList = regionListResDtoList.stream().filter(o -> deptById.getPath().startsWith(o.getDeptTreePath()) | o.getDeptTreePath().startsWith(deptById.getPath())).collect(Collectors.toList());
        }
        return regionListResDtoList;
    }

    @Override
    public RegionListResDto regionDetail(RegionDetailReqDto dto) {
        return spaceDao.getRegionDetail(dto.getRegionId(), departmentDao.getPathBySpacePath(departmentService.getDeptById(userService.getDeptId(dto.getUserId(), dto.getOrgId())).getPath()).getPath());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void regionUpdate(RegionUpdateReqDto dto) {
        getDeptTreePath(dto.getOrgId(), dto.getUserId(), departmentService.getDeptPathById(dto.getRegionId().toString()));
        Integer nameCount = departmentDao.selectByNameAndPatentIdForUpdate(dto.getRegionName(), dto.getParkId(), dto.getRegionId());
        if (nameCount != null) {
            throw new BusinessException(ResultCode.REGION_NAME_IS_EXIST);
        }
        DepartmentEntity entity = new DepartmentEntity();

        entity.setId(dto.getRegionId());
        entity.setDepartmentName(dto.getRegionName());
        entity.setDescription(dto.getDescription());
        departmentDao.updateDepartment(entity);
        redisUtil.del(DEPT_NAME_KEY_PREFIX + entity.getId());
        DepartmentPropertyDto propertyDto = new DepartmentPropertyDto();
        propertyDto.setDeptId(dto.getRegionId());
        propertyDto.setKey(AttrKeyEnum.REGION_TYPE.getKey());
        propertyDto.setVal(dto.getRegionType());
        departmentService.updateDepartmentProperty(propertyDto);
        propertyDto.setKey(AttrKeyEnum.OTHER_REGION_TYPE_NAME.getKey());
        propertyDto.setVal(dto.getOtherRegionTypeName());
        departmentService.updateDepartmentProperty(propertyDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void regionDelete(RegionDeleteReqDto dto) {
        Integer count = departmentDao.countByParentIds(dto.getRegionIds());
        if (count > 0) {
            throw new BusinessException(ResultCode.REGION_HAS_CHILDREN);
        }

        dto.getRegionIds().forEach(regionId -> {
            String checkUserInDeptId = userService.checkUserInDeptId(regionId);
            if (!StringUtils.isEmpty(checkUserInDeptId)) {
                throw new BusinessException(ResultCode.SPACE_USER_ALREADY_USE);
            }
        });
        departmentService.batchDeleteDepartment(dto.getRegionIds(), dto.getOrgId());
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
