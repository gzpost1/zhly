package cn.cuiot.dmp.system.application.service.impl;

import static cn.cuiot.dmp.common.constant.CacheConst.DEPT_NAME_KEY_PREFIX;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.RandomCodeWorker;
import cn.cuiot.dmp.system.application.enums.AttrKeyEnum;
import cn.cuiot.dmp.system.application.enums.DepartmentGroupEnum;
import cn.cuiot.dmp.system.application.service.AreaService;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.application.service.FactoryParkParkService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DepartmentPropertyDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkParkAddReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkParkListReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkParkListResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ParkDeleteReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ParkDetailReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ParkListResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ParkUpdateReqDto;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.SpaceDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.UserPropertyDao;
import cn.cuiot.dmp.system.infrastructure.utils.RedisUtil;
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
 * @classname FactoryParkParkServiceImpl
 * @description
 * @date 2023/1/13
 */
@Service
public class FactoryParkParkServiceImpl implements FactoryParkParkService {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private UserService userService;

    @Autowired
    private SpaceDao spaceDao;

    @Autowired
    private AreaService areaService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserPropertyDao userPropertyDao;

    public static final int THREE = 3;

    public static final int FIVE = 5;

    @Override
    public void parkAdd(FactoryParkParkAddReqDto dto) {
        getDeptTreePath(dto.getOrgId(), dto.getUserId(), departmentService.getDeptPathById(dto.getDeptId().toString()));
        int count = departmentDao.countByDepartmentName(dto.getParkName(), Long.parseLong(dto.getOrgId()));
        if (count > 0) {
            throw new BusinessException(ResultCode.PARK_NAME_IS_EXIST);
        }
        if (!checkAreaCode(dto.getAreaCode())) {
            throw new BusinessException(ResultCode.REQ_PARAM_ERROR);
        }
        DepartmentEntity entity = new DepartmentEntity();
        entity.setDepartmentName(dto.getParkName());
        entity.setSort(Optional.ofNullable(departmentDao.getMaxSortByParentId(dto.getDeptId())).orElse(0));
        entity.setPkOrgId(Long.parseLong(dto.getOrgId()));
        entity.setParentId(dto.getDeptId());
        entity.setCreatedOn(LocalDateTime.now());
        entity.setCreatedBy(dto.getUserId());
        entity.setCode(RandomCodeWorker.generateShortUuid());
        entity.setDescription(dto.getDescription());
        DepartmentEntity parentDept = departmentDao.selectByPrimary(dto.getDeptId());
        entity.setPath(parentDept.getPath() + "-" + entity.getCode());
        entity.setDGroup(DepartmentGroupEnum.COMMUNITY.getCode());
        entity.setLevel(parentDept.getLevel() + 1);
        departmentDao.insertDepartment(entity);

        DepartmentPropertyDto propertyDto = new DepartmentPropertyDto();
        propertyDto.setDeptId(entity.getId());
        propertyDto.setKey(AttrKeyEnum.COMMUNITY_AREA_CODE.getKey());
        propertyDto.setVal(dto.getAreaCode());
        departmentService.insertDepartmentProperty(propertyDto);
        propertyDto.setKey(AttrKeyEnum.ADDRESS.getKey());
        propertyDto.setVal(dto.getAddress());
        departmentService.insertDepartmentProperty(propertyDto);
        propertyDto.setKey(AttrKeyEnum.PARK_TYPE.getKey());
        propertyDto.setVal(dto.getParkType());
        departmentService.insertDepartmentProperty(propertyDto);
        propertyDto.setKey(AttrKeyEnum.OTHER_PARK_TYPE_NAME.getKey());
        propertyDto.setVal(dto.getOtherParkTypeName());
        departmentService.insertDepartmentProperty(propertyDto);
    }

    @Override
    public PageResult<FactoryParkParkListResDto> parkList(FactoryParkParkListReqDto dto) {
        dto.setDeptTreePath(getDeptTreePath(dto.getOrgId(), dto.getUserId(), dto.getDeptTreePath()));
        Page<FactoryParkParkListResDto> parkListResDtoPage = PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        dto.setGroup(DepartmentGroupEnum.COMMUNITY.getCode());
        List<FactoryParkParkListResDto> parkListResDtoList = spaceDao.getDeptListByPathAndGroupFactoryParkForList(dto.getDeptTreePath(),
                dto.getGroup(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getKeyword(),
                dto.getLabelId(),
                dto.getParkType());
        parkListResDtoList.forEach(resDto -> {
            resDto.setOtherParkTypeName(departmentService.getDepartmentProperty(Long.valueOf(resDto.getParkId()), AttrKeyEnum.OTHER_PARK_TYPE_NAME.getKey()));
            resDto.setAddress(departmentService.getDepartmentProperty(Long.valueOf(resDto.getParkId()), AttrKeyEnum.ADDRESS.getKey()));
            String areaCodes = departmentService.getDepartmentProperty(Long.valueOf(resDto.getParkId()), AttrKeyEnum.COMMUNITY_AREA_CODE.getKey());
            if (!StringUtils.isEmpty(areaCodes)) {
                resDto.setAreaName(areaService.getAreaNamesByCodes(areaCodes));
            }
        });
        return new PageResult<>(parkListResDtoPage.toPageInfo());
    }

    @Override
    public List<FactoryParkParkListResDto> parkListByDeptTreePath(FactoryParkParkListReqDto dto) {
        dto.setDeptTreePath(getDeptTreePath(dto.getOrgId(), dto.getUserId(), dto.getDeptTreePath()));
        dto.setGroup(DepartmentGroupEnum.COMMUNITY.getCode());
        List<FactoryParkParkListResDto> parkListResDtoList = spaceDao.getDeptListByPathAndGroupFactoryPark(dto.getDeptTreePath(),
                dto.getGroup(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getKeyword(),
                dto.getLabelId(),
                null);
        DepartmentEntity deptById = departmentService.getDeptById(userService.getDeptId(dto.getUserId(), dto.getOrgId()));
        if (!Arrays.asList(DepartmentGroupEnum.TENANT.getCode(), DepartmentGroupEnum.SYSTEM.getCode()).contains(deptById.getCode())) {
            parkListResDtoList = parkListResDtoList.stream().filter(o -> deptById.getPath().startsWith(o.getDeptTreePath()) | o.getDeptTreePath().startsWith(deptById.getPath())).collect(Collectors.toList());
        }
        return parkListResDtoList;
    }

    @Override
    public ParkListResDto parkDetail(ParkDetailReqDto dto) {
        ParkListResDto result = spaceDao.getParkDetail(dto.getParkId(), departmentDao.getPathBySpacePath(departmentService.getDeptById(userService.getDeptId(dto.getUserId(), dto.getOrgId())).getPath()).getPath());
        if (!StringUtils.isEmpty(result.getAreaCode())) {
            result.setAreaName(areaService.getAreaNamesByCodes(result.getAreaCode()));
        }
        return result;
    }

    @Override
    public void parkUpdate(ParkUpdateReqDto dto) {
        getDeptTreePath(dto.getOrgId(), dto.getUserId(), departmentService.getDeptPathById(dto.getParkId().toString()));
        if (!checkAreaCode(dto.getAreaCode())) {
            throw new BusinessException(ResultCode.REQ_PARAM_ERROR);
        }
        int count = departmentDao.countByDepartmentNameForUpdate(dto.getParkName(),Long.parseLong(dto.getOrgId()), dto.getParkId());
        if (count > 0) {
            throw new BusinessException(ResultCode.COMMUNITY_NAME_IS_EXIST);
        }
        DepartmentEntity entity = new DepartmentEntity();


        entity.setId(dto.getParkId());
        entity.setDepartmentName(dto.getParkName());
        entity.setDescription(dto.getDescription());
        departmentDao.updateDepartment(entity);
        redisUtil.del(DEPT_NAME_KEY_PREFIX + entity.getId());
        DepartmentPropertyDto propertyDto = new DepartmentPropertyDto();
        propertyDto.setDeptId(dto.getParkId());
        propertyDto.setKey(AttrKeyEnum.COMMUNITY_AREA_CODE.getKey());
        propertyDto.setVal(dto.getAreaCode());
        departmentService.updateDepartmentProperty(propertyDto);
        propertyDto.setKey(AttrKeyEnum.ADDRESS.getKey());
        propertyDto.setVal(dto.getAddress());
        departmentService.updateDepartmentProperty(propertyDto);
        propertyDto.setKey(AttrKeyEnum.PARK_TYPE.getKey());
        propertyDto.setVal(dto.getParkType());
        departmentService.updateDepartmentProperty(propertyDto);
        propertyDto.setKey(AttrKeyEnum.OTHER_PARK_TYPE_NAME.getKey());
        propertyDto.setVal(dto.getOtherParkTypeName());
        departmentService.updateDepartmentProperty(propertyDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void parkDelete(ParkDeleteReqDto dto) {
        Integer count = departmentDao.countByParentIds(dto.getParkIds());
        if (count > 0) {
            throw new BusinessException(ResultCode.PARK_HAS_CHILDREN);
        }
        dto.getParkIds().forEach(parkId -> {
            DepartmentEntity department = departmentService.getDeptById(parkId.toString());
            userService.checkDepartmentUser(department.getPkOrgId(), department.getPath());
            String checkUserInDeptId = userService.checkUserInDeptId(parkId);
            if (!StringUtils.isEmpty(checkUserInDeptId)) {
                throw new BusinessException(ResultCode.SPACE_USER_ALREADY_USE);
            }
        });
        departmentService.batchDeleteDepartment(dto.getParkIds(), dto.getOrgId());
    }

    private boolean checkAreaCode(String areaCode) {
        String[] areaCodes = areaCode.split(",");
        return areaCodes.length <= FIVE && areaCodes.length >= THREE;
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
        DepartmentEntity deptById = departmentService.getDeptById(userService.getDeptId(userId, orgId));
        if (!StringUtils.isEmpty(deptTreePath)) {
            if (!deptTreePath.startsWith(deptById.getPath()) & !deptById.getPath().startsWith(deptTreePath)){
                throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
            }
        }
        return StringUtils.isEmpty(deptTreePath) ? deptById.getPath() : deptTreePath;
    }

}
