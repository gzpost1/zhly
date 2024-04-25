package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.common.constant.NumberConst;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.RandomCodeWorker;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.system.application.enums.AttrKeyEnum;
import cn.cuiot.dmp.system.application.enums.DepartmentGroupEnum;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.application.service.FactoryParkFloorService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.BuildingFloorDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CommunityListQryDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DepartmentPropertyDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorAddDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorBatchDelDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorDelDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorQryDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorUpdateDto;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.SpaceDao;
import cn.cuiot.dmp.system.user_manage.domain.entity.Organization;
import cn.cuiot.dmp.system.user_manage.domain.types.enums.OrgTypeEnum;
import cn.cuiot.dmp.system.user_manage.repository.OrganizationRepository;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author wensq
 * @version 1.0
 * @date 2022/1/5 10:42
 */
@Slf4j
@Service
public class FactoryParkFloorServiceImpl implements FactoryParkFloorService {
    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SpaceDao spaceDao;

    @Autowired
    private OrganizationRepository organizationRepository;

    /**
     * 代表正向楼层
     */
    private String CONST_FLOOR_P = "P";

    /**
     * 代表负向楼层
     */
    private String CONST_FLOOR_M = "M";

    @Override
    public PageResult<FactoryParkFloorDto> floorList(FactoryParkFloorQryDto dto) {
        // 获取用户组织
        DepartmentDto userDept = getUserDept(String.valueOf(dto.getUserId()));
        if(!StringUtils.isEmpty(dto.getDeptTreePath())) {
            if(!dto.getDeptTreePath().startsWith(userDept.getPath())){
                throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
            }
        } else {
            dto.setDeptTreePath(userDept.getPath());
        }
        PageInfo<FactoryParkFloorDto> resultPageInfo;
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        dto.setGroup(DepartmentGroupEnum.FLOOR.getCode());
        dto.setPath(dto.getDeptTreePath());
        CommunityListQryDto communityListQryDto = new CommunityListQryDto();
        BeanUtils.copyProperties(dto, communityListQryDto);
        List<DepartmentDto> entities = spaceDao.getDeptListByPathAndGroupFloor(dto.getDeptTreePath(), dto.getFloorName(),
                dto.getLabelId(), dto.getGroup());
        // 创建分页
        PageInfo<DepartmentDto> pageInfo = new PageInfo<>(entities);
        resultPageInfo = dataEntityListToDataDtoList(pageInfo);
        for (FactoryParkFloorDto floorDto : resultPageInfo.getList()) {
            String departmentProperty = departmentDao.getDepartmentProperty(Long.parseLong(floorDto.getFloorId()), AttrKeyEnum.CURRENT_FLOOR.getKey());
            floorDto.setCurrentFloor(departmentProperty);
            DepartmentEntity building = departmentService.getDeptById(String.valueOf(floorDto.getParentId()));
            floorDto.setBuildingName(building.getDepartmentName());
            floorDto.setBuildingId(String.valueOf(building.getId()));
            String floorNum = departmentService.getDepartmentProperty(building.getId(), AttrKeyEnum.FLOOR_NUM.getKey());
            floorDto.setBuildingNum(floorNum);
            DepartmentEntity regionEntity = departmentService.getDeptById(String.valueOf(building.getParentId()));
            floorDto.setRegionId(String.valueOf(regionEntity.getId()));
            floorDto.setRegionName(regionEntity.getDepartmentName());
            DepartmentEntity parkEntity = departmentService.getDeptById(String.valueOf(regionEntity.getParentId()));
            floorDto.setParkName(parkEntity.getDepartmentName());
            floorDto.setParkId(String.valueOf(parkEntity.getId()));
        }
        return new PageResult<>(resultPageInfo);
    }

    private PageInfo<FactoryParkFloorDto> dataEntityListToDataDtoList(PageInfo<DepartmentDto> entityPageInfo){
        if ( entityPageInfo == null ) {
            return null;
        }
        PageInfo<FactoryParkFloorDto> pageInfo = new PageInfo<FactoryParkFloorDto>();
        pageInfo.setTotal( entityPageInfo.getTotal() );
        pageInfo.setList( departmentDtoListToBuildingListDtoList( entityPageInfo.getList() ) );
        pageInfo.setPageNum( entityPageInfo.getPageNum() );
        pageInfo.setPageSize( entityPageInfo.getPageSize() );
        pageInfo.setSize( entityPageInfo.getSize() );
        pageInfo.setStartRow( entityPageInfo.getStartRow() );
        pageInfo.setEndRow( entityPageInfo.getEndRow() );
        pageInfo.setPages( entityPageInfo.getPages() );
        pageInfo.setPrePage( entityPageInfo.getPrePage() );
        pageInfo.setNextPage( entityPageInfo.getNextPage() );
        pageInfo.setIsFirstPage( entityPageInfo.isIsFirstPage() );
        pageInfo.setIsLastPage( entityPageInfo.isIsLastPage() );
        pageInfo.setHasPreviousPage( entityPageInfo.isHasPreviousPage() );
        pageInfo.setHasNextPage( entityPageInfo.isHasNextPage() );
        pageInfo.setNavigatePages( entityPageInfo.getNavigatePages() );
        int[] navigatepageNums = entityPageInfo.getNavigatepageNums();
        if ( navigatepageNums != null ) {
            pageInfo.setNavigatepageNums( Arrays.copyOf( navigatepageNums, navigatepageNums.length ) );
        }
        pageInfo.setNavigateFirstPage( entityPageInfo.getNavigateFirstPage() );
        pageInfo.setNavigateLastPage( entityPageInfo.getNavigateLastPage() );

        return pageInfo;
    }

    protected List<FactoryParkFloorDto> departmentDtoListToBuildingListDtoList(List<DepartmentDto> list) {
        if ( list == null ) {
            return null;
        }
        List<FactoryParkFloorDto> list1 = new ArrayList<FactoryParkFloorDto>( list.size() );
        for ( DepartmentDto departmentDto : list ) {
            FactoryParkFloorDto floorListDto = new FactoryParkFloorDto();
            floorListDto.setFloorId(String.valueOf(departmentDto.getId()));
            floorListDto.setFloorName( departmentDto.getName() );
            floorListDto.setCode( departmentDto.getCode() );
            floorListDto.setParentId( departmentDto.getParentId() );
            floorListDto.setOrgId( departmentDto.getOrgId() );
            floorListDto.setPath( departmentDto.getPath() );
            floorListDto.setSort( departmentDto.getSort() );
            floorListDto.setCreatedOn( departmentDto.getCreatedOn() );
            floorListDto.setDescription( departmentDto.getDescription() );
            floorListDto.setCreatedBy( departmentDto.getCreatedBy() );
            list1.add(floorListDto);
        }

        return list1;
    }

    @Override
    public int floorAdd(FactoryParkFloorAddDto dto) {
        DepartmentEntity deptById = departmentService.getDeptById(userService.getDeptId(String.valueOf(dto.getUserId()),
                String.valueOf(dto.getOrgId())));
        String spaceTreePace = departmentDao.selectByPrimary(Long.parseLong(dto.getBuildingId())).getPath();
        if(!spaceTreePace.startsWith(deptById.getPath())){
            throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
        }
        Organization organization = organizationRepository.find(new OrganizationId(dto.getOrgId()));
        if (!OrgTypeEnum.COMMUNITY.getValue().equals(organization.getOrgTypeId().getValue())) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        try {
            Integer.valueOf(dto.getCurrentFloor());
        } catch (Exception e) {
            throw new BusinessException(ResultCode.CURRENT_FLOOR_IS_NUMBER);
        }
        if(Integer.valueOf(dto.getCurrentFloor()).equals(NumberConst.ZERO)) {
            throw new BusinessException(ResultCode.CURRENT_FLOOR_NOT_ZERO);
        }
        DepartmentEntity parentDept = departmentDao.selectByPrimary(Long.parseLong(dto.getBuildingId()));
        if(!parentDept.getDGroup().equals(DepartmentGroupEnum.BUILDING.getCode())){
            throw new BusinessException(ResultCode.BUILDING_NAME_NOT_EXIST);
        }
        Integer nameCount = departmentDao.selectByPropertyAndPatentId(dto.getCurrentFloor(), Long.parseLong(dto.getBuildingId()));
        if (nameCount != null) {
            throw new BusinessException(ResultCode.FLOOR_NAME_IS_EXIST);
        }
        String departmentProperty = departmentDao.getDepartmentProperty(parentDept.getId(), AttrKeyEnum.FLOOR_NUM.getKey());
        if(Integer.valueOf(dto.getCurrentFloor()) > Integer.valueOf(departmentProperty) ||
                Integer.valueOf(dto.getCurrentFloor()) < NumberConst.M_FOUR) {
            throw new BusinessException(ResultCode.CURRENT_FLOOR_MORE_THEN_FLOOR);
        }
        DepartmentEntity entity = new DepartmentEntity();
        entity.setDepartmentName(dto.getFloorName());
        entity.setSort(Optional.ofNullable(departmentDao.getMaxSortByParentId(dto.getDeptId())).orElse(NumberConst.ZERO));
        entity.setPkOrgId(dto.getOrgId());
        entity.setParentId(Long.parseLong(dto.getBuildingId()));
        entity.setCreatedOn(LocalDateTime.now());
        entity.setCreatedBy(String.valueOf(dto.getUserId()));
        entity.setCode(RandomCodeWorker.generateShortUuid());
        entity.setDescription(dto.getDescription());
        // M代表0以上楼层，B代表负层
        entity.setPath(parentDept.getPath() + "-" + entity.getCode());
        entity.setDGroup(DepartmentGroupEnum.FLOOR.getCode());
        entity.setLevel(parentDept.getLevel() + NumberConst.ONE);
        departmentDao.insertDepartment(entity);

        DepartmentPropertyDto propertyDto = new DepartmentPropertyDto();
        propertyDto.setDeptId(entity.getId());
        propertyDto.setKey(AttrKeyEnum.CURRENT_FLOOR.getKey());
        propertyDto.setVal(dto.getCurrentFloor());
        return departmentService.insertDepartmentProperty(propertyDto);
    }

    @Override
    public FactoryParkFloorDto floorDetail(FactoryParkFloorDelDto dto) {
        FactoryParkFloorDto resDto = new FactoryParkFloorDto();
        DepartmentEntity floorDept = departmentDao.selectByPrimary(Long.parseLong(dto.getFloorId()));
        DepartmentEntity deptById = departmentService.getDeptById(userService.getDeptId(String.valueOf(dto.getUserId()),
                String.valueOf(dto.getOrgId())));
        if(!floorDept.getPath().startsWith(deptById.getPath())){
            throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
        }
        resDto.setFloorId(String.valueOf(floorDept.getId()));
        resDto.setFloorName(floorDept.getDepartmentName());
        String currentNum = departmentService.getDepartmentProperty(floorDept.getId(), AttrKeyEnum.CURRENT_FLOOR.getKey());
        DepartmentEntity parentDept = departmentDao.selectByPrimary(floorDept.getParentId());
        resDto.setBuildingId(String.valueOf(parentDept.getId()));
        resDto.setBuildingName(parentDept.getDepartmentName());
        resDto.setCurrentFloor(currentNum);
        String departmentProperty = departmentDao.getDepartmentProperty(parentDept.getId(), AttrKeyEnum.FLOOR_NUM.getKey());
        resDto.setBuildingNum(departmentProperty);
        DepartmentEntity regionDept = departmentDao.selectByPrimary(parentDept.getParentId());
        resDto.setRegionName(regionDept.getDepartmentName());
        resDto.setRegionId(String.valueOf(regionDept.getId()));
        DepartmentEntity parkDept = departmentDao.selectByPrimary(regionDept.getParentId());
        resDto.setParkId(String.valueOf(parkDept.getId()));
        resDto.setParkName(parkDept.getDepartmentName());
        DepartmentEntity path = departmentDao.selectByPrimary(parkDept.getParentId());
        resDto.setPath(path.getPath());
        resDto.setDescription(floorDept.getDescription());
        return resDto;
    }

    @Override
    public List<FactoryParkFloorDto> getFloorByBuilding(BuildingFloorDto dto) {
        Organization organization = organizationRepository.find(new OrganizationId(dto.getOrgId()));
        if (!OrgTypeEnum.COMMUNITY.getValue().equals(organization.getOrgTypeId().getValue())) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        String deptPath = departmentService.getDeptById(userService.getDeptId(String.valueOf(dto.getUserId()), String.valueOf(dto.getOrgId()))).getPath();
        getDeptTreePath(String.valueOf(dto.getOrgId()), String.valueOf(dto.getUserId()), dto.getDeptTreePath());
        List<FactoryParkFloorDto> list = new ArrayList<>();
        List<DepartmentEntity> departmentList = departmentDao.getDepartmentListByParentIdAndPath(Long.parseLong(dto.getBuildingId()), deptPath);
        for (DepartmentEntity departmentEntity : departmentList) {
            String departmentProperty = departmentDao.getDepartmentProperty(departmentEntity.getId(), AttrKeyEnum.CURRENT_FLOOR.getKey());
            FactoryParkFloorDto floorDto = new FactoryParkFloorDto();
            floorDto.setFloorId(String.valueOf(departmentEntity.getId()));
            floorDto.setFloorName(departmentEntity.getDepartmentName());
            floorDto.setCurrentFloor(departmentProperty);
            list.add(floorDto);
        }
        return list;
    }

    @Override
    public int floorUpdate(FactoryParkFloorUpdateDto dto) {
        getDeptTreePath(String.valueOf(dto.getOrgId()), String.valueOf(dto.getUserId()), dto.getDeptTreePath());
        Organization organization = organizationRepository.find(new OrganizationId(dto.getOrgId()));
        if (!OrgTypeEnum.COMMUNITY.getValue().equals(organization.getOrgTypeId().getValue())) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        List<Long> ids = new ArrayList<>();
        ids.add(Long.parseLong(dto.getFloorId()));
        Integer count = departmentDao.countByParentIds(ids);
        if (count > NumberConst.ZERO) {
            String currentNum = departmentService.getDepartmentProperty(Long.parseLong(dto.getFloorId()), AttrKeyEnum.CURRENT_FLOOR.getKey());
            if(!dto.getCurrentFloor().equals(currentNum)) {
                throw new BusinessException(ResultCode.CURRENT_FLOOR_NOT_UPDATE);
            }
        }
        try {
            Integer.valueOf(dto.getFloorNum());
            Integer.valueOf(dto.getCurrentFloor());
        } catch (Exception e) {
            throw new BusinessException(ResultCode.CURRENT_FLOOR_IS_NUMBER);
        }
        if(Integer.valueOf(dto.getCurrentFloor()).equals(NumberConst.ZERO)) {
            throw new BusinessException(ResultCode.CURRENT_FLOOR_NOT_ZERO);
        }
        if(Integer.valueOf(dto.getCurrentFloor()) > Integer.valueOf(dto.getFloorNum()) ||
                Integer.valueOf(dto.getCurrentFloor()) < NumberConst.M_FOUR) {
            throw new BusinessException(ResultCode.CURRENT_FLOOR_MORE_THEN_FLOOR);
        }
        Integer nameCount = departmentDao.selectByPropertyAndPatentIdForUpdate(dto.getCurrentFloor(),
                Long.parseLong(dto.getBuildingId()), Long.parseLong(dto.getFloorId()));
        if (nameCount != null) {
            throw new BusinessException(ResultCode.FLOOR_NAME_IS_EXIST);
        }
        DepartmentEntity entity = new DepartmentEntity();
        entity.setParentId(Long.parseLong(dto.getBuildingId()));
        entity.setId(Long.parseLong(dto.getFloorId()));
        entity.setDepartmentName(dto.getFloorName());
        entity.setDescription(dto.getDescription());
        departmentDao.updateDepartment(entity);
        DepartmentPropertyDto propertyDto = new DepartmentPropertyDto();
        propertyDto.setDeptId(Long.parseLong(dto.getFloorId()));
        propertyDto.setVal(dto.getCurrentFloor());
        propertyDto.setKey(AttrKeyEnum.CURRENT_FLOOR.getKey());
        return departmentService.updateDepartmentProperty(propertyDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int floorDelete(FactoryParkFloorDelDto dto) {
        Organization organization = organizationRepository.find(new OrganizationId(dto.getOrgId()));
        if (!OrgTypeEnum.COMMUNITY.getValue().equals(organization.getOrgTypeId().getValue())) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        DepartmentEntity deptById = departmentService.getDeptById(userService.getDeptId(String.valueOf(dto.getUserId()),
                String.valueOf(dto.getOrgId())));
        DepartmentEntity floorDept = departmentDao.selectByPrimary(Long.parseLong(dto.getFloorId()));
        if (!floorDept.getPath().startsWith(deptById.getPath())) {
            throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
        }
        userService.checkDepartmentUser(floorDept.getPkOrgId(), floorDept.getPath());
        List<Long> ids = new ArrayList<>();
        ids.add(Long.parseLong(dto.getFloorId()));
        Integer count = departmentDao.countByParentIds(ids);
        if (count > NumberConst.ZERO) {
            throw new BusinessException(ResultCode.FLOOR_HAS_CHILDREN);
        }
        try {
            departmentService.batchDeleteDepartment(ids, String.valueOf(dto.getOrgId()));
            departmentDao.batchDeleteProperty(ids);
        } catch (Exception e) {
            log.error("楼层删除失败 {}", e.getMessage());
            throw new BusinessException(ResultCode.INNER_ERROR);
        }
        return NumberConst.ONE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int floorBatchDelete(FactoryParkFloorBatchDelDto dto) {
        Organization organization = organizationRepository.find(new OrganizationId(dto.getOrgId()));
        if (!OrgTypeEnum.COMMUNITY.getValue().equals(organization.getOrgTypeId().getValue())) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
        Integer count = departmentDao.countByParentIds(dto.getFloorIds());
        if (count > NumberConst.ZERO) {
            throw new BusinessException(ResultCode.FLOOR_HAS_CHILDREN);
        }

        DepartmentEntity deptById = departmentService.getDeptById(userService.getDeptId(String.valueOf(dto.getUserId()),
                String.valueOf(dto.getOrgId())));
        List<DepartmentEntity> departmentList = departmentDao.getDepartmentList(dto.getFloorIds());
        List<Long> list = new ArrayList<>();
        for (DepartmentEntity entity : departmentList) {
            if(entity.getPath().startsWith(deptById.getPath())) {
                list.add(entity.getId());
            }
        }
        if(CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
        }
        dto.setFloorIds(list);
        try {
            departmentService.batchDeleteDepartment(dto.getFloorIds(), String.valueOf(dto.getOrgId()));
            departmentDao.batchDeleteProperty(dto.getFloorIds());
        } catch (Exception e) {
            log.error("楼层删除失败 {}", e.getMessage());
            throw new BusinessException(ResultCode.INNER_ERROR);
        }
        return NumberConst.ONE;
    }

    /**
     * 根据用户id获取组织信息
     *
     * @param userId
     * @return
     */
    private DepartmentDto getUserDept(String userId) {
        try {
            DepartmentDto dept = departmentDao.getPathByUser(userId);
            if (Objects.isNull(dept) || StringUtils.isEmpty(dept.getPath())) {
                throw new BusinessException(ResultCode.QUERY_USER_DEPT_ERROR);
            }
            return dept;
        } catch (BusinessException e) {
            throw new BusinessException(ResultCode.QUERY_USER_DEPT_ERROR, e);
        }

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
        if (!StringUtils.isEmpty(deptTreePath)) {
            if (!deptTreePath.startsWith(deptById.getPath())){
                throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
            }
        }
        return StringUtils.isEmpty(deptTreePath) ? deptById.getPath() : deptTreePath;
    }
}
