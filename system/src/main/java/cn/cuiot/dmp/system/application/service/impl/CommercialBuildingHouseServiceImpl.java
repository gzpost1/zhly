package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.RandomCodeWorker;
import cn.cuiot.dmp.system.application.enums.DepartmentGroupEnum;
import cn.cuiot.dmp.system.application.service.CommercialBuildingHouseService;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.HousePropertyEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PropertyHouseAddReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PropertyHouseInfoResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PropertyHouseListReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PropertyHouseUpdateReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.PropertyHouseListReqVO;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.HouseDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


/**
 * @author huw51
 */
@Service
@Slf4j
public class CommercialBuildingHouseServiceImpl implements CommercialBuildingHouseService {


    @Autowired
    private HouseDao houseDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    private static final String LINK_WORD = "-";

    private static final int HOUSE_LEVEL = 5;

    @Override
    public PageResult<PropertyHouseListReqVO> selectAll(PropertyHouseListReqDto dto) {
        //根据deptId 查询所选组织或者小区的path, 用空间树的path过滤结果
        dto.setSpaceTreePath(getDeptTreePath(dto.getOrgId(), dto.getUserId(), dto.getSpaceTreePath()));
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<PropertyHouseListReqVO> voList = houseDao.selectPropertyHouseList(dto);
        PageInfo<PropertyHouseListReqVO> pageInfo = new PageInfo<>(voList);
        return new PageResult<>(pageInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(PropertyHouseAddReqDto dto) {
        //越权校验
        DepartmentEntity deptById = departmentService.getDeptById(userService.getDeptId(String.valueOf(dto.getLoginUserId()),
                String.valueOf(dto.getPkOrgId())));
        String spaceTreePace = departmentDao.selectByPrimary(dto.getFloorId()).getPath();
        if (!spaceTreePace.startsWith(deptById.getPath())) {
            throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
        }
        // 创建组织
        Long selfDeptId = createDepartment(dto, dto.getFloorId(), dto.getPkOrgId(), dto.getLoginUserId());
        HousePropertyEntity housePropertyEntity = new HousePropertyEntity();
        BeanUtils.copyProperties(dto, housePropertyEntity);
        housePropertyEntity.setCreatedBy(dto.getLoginUserId());
        housePropertyEntity.setCreatedOn(LocalDateTime.now());
        housePropertyEntity.setSelfDeptId(selfDeptId);
        if (StringUtils.isEmpty(housePropertyEntity.getUsedArea())) {
            housePropertyEntity.setUsedArea("");
        }
        if (StringUtils.isEmpty(housePropertyEntity.getPublicArea())) {
            housePropertyEntity.setPublicArea("");
        }
        //检查房号是否存在
        int nameExist = houseDao.selectPropertyHouseNameExist(housePropertyEntity);

        if (nameExist != 0) {
            throw new BusinessException(ResultCode.HOUSE_HAS_EXIST);
        }
        int insert = houseDao.insertPropertyHouse(housePropertyEntity);
        return insert;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(PropertyHouseUpdateReqDto dto) {
        //越权校验
        DepartmentEntity deptById = departmentService.getDeptById(userService.getDeptId(String.valueOf(dto.getLoginUserId()),
                String.valueOf(dto.getOrgId())));
        String spaceTreePace = departmentDao.getHousePathByHousePrimaryKey(dto.getId());
        if (!spaceTreePace.startsWith(deptById.getPath())) {
            throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
        }
        final String loginUserId = dto.getLoginUserId();
        // 修改组织（物业侧不允许修改）
        HousePropertyEntity housePropertyEntity = new HousePropertyEntity();
        BeanUtils.copyProperties(dto, housePropertyEntity);
        housePropertyEntity.setUpdatedBy(loginUserId);
        housePropertyEntity.setUpdatedOn(LocalDateTime.now());
        if (StringUtils.isEmpty(housePropertyEntity.getUsedArea())) {
            housePropertyEntity.setUsedArea("");
        }
        if (StringUtils.isEmpty(housePropertyEntity.getPublicArea())) {
            housePropertyEntity.setPublicArea("");
        }
        int nameExist = houseDao.selectPropertyHouseNameExist(housePropertyEntity);
        if (nameExist != 0) {
            throw new BusinessException(ResultCode.HOUSE_HAS_EXIST);
        }
        //
        String selfDeptId = houseDao.getDeptIdByHouseId(dto.getId());
        DepartmentEntity departmentEntity = departmentDao.getDepartmentById(selfDeptId);
        int updateHouse = houseDao.updatePropertyHouse(housePropertyEntity);
        // house表更新后，需要在department表中更新房屋名称。
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dto.getHouseName()) && !dto.getHouseName().equals(departmentEntity.getDepartmentName())) {
            departmentDao.updateDepartmentNameById(dto.getHouseName(), departmentEntity.getId());
        }
        return updateHouse;
    }

    @Override
    public PropertyHouseInfoResDto selectById(Long id, String orgId, String userId) {
        // 获取用户组织path
        String spacePath = getDeptTreePath(orgId, userId, null);
        if (StringUtils.isEmpty(spacePath)) {
            throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
        }
        PropertyHouseInfoResDto houseEntity = houseDao.selectPropertyByPrimary(id, spacePath);
        return houseEntity;
    }

    private Long createDepartment(PropertyHouseAddReqDto dto, Long floorNumId, Long pkOrgId, String loginUserId) {
        //获取最后一位path的code即为楼层
        DepartmentEntity houseEntity = new DepartmentEntity();
        houseEntity.setDepartmentName(dto.getHouseName());
        houseEntity.setSort(Optional.ofNullable(departmentDao.getMaxSortByParentId(floorNumId)).orElse(0));
        houseEntity.setPkOrgId(pkOrgId);
        houseEntity.setParentId(floorNumId);
        houseEntity.setCreatedOn(LocalDateTime.now());
        houseEntity.setCreatedBy(loginUserId);
        houseEntity.setCode(RandomCodeWorker.generateShortUuid());
        houseEntity.setDescription(dto.getDescription());
        DepartmentEntity floorEntity = departmentDao.selectByPrimary(floorNumId);
        houseEntity.setPath(floorEntity.getPath() + LINK_WORD + houseEntity.getCode());
        houseEntity.setDGroup(DepartmentGroupEnum.HOUSE.getCode());
        houseEntity.setLevel(HOUSE_LEVEL);
        departmentDao.insertDepartment(houseEntity);
        return houseEntity.getId();
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
        if (!StringUtils.isEmpty(deptTreePath) && !deptTreePath.startsWith(deptById.getPath())) {
            throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
        }
        return StringUtils.isEmpty(deptTreePath) ? deptById.getPath() : deptTreePath;
    }

}
