package cn.cuiot.dmp.system.application.service.impl;


import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.system.application.param.assembler.HouseConverter;
import cn.cuiot.dmp.system.application.service.BaseHouseService;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.HouseDeleteDto;
import cn.cuiot.dmp.system.infrastructure.entity.po.HousePo;
import cn.cuiot.dmp.system.infrastructure.entity.vo.CommercialComplexHouseDetailResVO;
import cn.cuiot.dmp.system.infrastructure.entity.vo.CommercialComplexHouseListVO;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.ShopDao;
import com.github.pagehelper.PageInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author huwei
 */
@Slf4j
@Service
public class BaseHouseServiceImpl implements BaseHouseService {

    @Autowired
    private ShopDao shopDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private HouseConverter houseConverter;

    /**
     * 商铺详情
     *
     * @param id
     * @return
     */
    @Override
    public CommercialComplexHouseDetailResVO detail(Long id, String orgId, String userId) {
        // 获取用户组织path
        String spacePath = getDeptTreePath(orgId, userId, null);
        // 查询获取po
        HousePo baseHousePO = shopDao.detail(id, spacePath);
        // 类型转化
        CommercialComplexHouseDetailResVO commercialComplexHouseVO = houseConverter.toCommercialComplexByBaseHousePO(baseHousePO);
        return commercialComplexHouseVO;
    }

    /**
     * 批量删除商铺
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(HouseDeleteDto dto) {
        List<Long> idList = dto.getIds();
        String userId = dto.getUserId();
        // 批量越权校验
        DepartmentEntity departmentEntity = departmentService.getDeptById(userService.getDeptId(String.valueOf(dto.getUserId()),
                String.valueOf(dto.getOrgId())));
        List<String> spaceTreePaceList = departmentDao.getHousePathListByIdList(idList);
        List<Long> deptIdList = departmentDao.getDeptIdListByHouseIdList(idList);
        for (String temp : spaceTreePaceList){
            if (!temp.startsWith(departmentEntity.getPath())) {
                throw new BusinessException(ResultCode.DEPARTMENT_ULTRA_VIRES);
            }
        }
        departmentService.batchDeleteDepartment(deptIdList, dto.getOrgId());
        return shopDao.batchDelete(idList, userId);
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

    /**
     * 分页
     *
     * @param housePOPageInfo
     * @return
     */
    private PageInfo<CommercialComplexHouseListVO> dataEntityListToDataDtoList(PageInfo<HousePo> housePOPageInfo) {
        if (housePOPageInfo == null) {
            return null;
        }
        PageInfo<CommercialComplexHouseListVO> pageInfo = new PageInfo<>();
        pageInfo.setTotal(housePOPageInfo.getTotal());
        pageInfo.setList(departmentDtoListToBuildingListDtoList(housePOPageInfo.getList()));
        pageInfo.setPageNum(housePOPageInfo.getPageNum());
        pageInfo.setPageSize(housePOPageInfo.getPageSize());
        pageInfo.setSize(housePOPageInfo.getSize());
        pageInfo.setStartRow(housePOPageInfo.getStartRow());
        pageInfo.setEndRow(housePOPageInfo.getEndRow());
        pageInfo.setPages(housePOPageInfo.getPages());
        pageInfo.setPrePage(housePOPageInfo.getPrePage());
        pageInfo.setNextPage(housePOPageInfo.getNextPage());
        pageInfo.setIsFirstPage(housePOPageInfo.isIsFirstPage());
        pageInfo.setIsLastPage(housePOPageInfo.isIsLastPage());
        pageInfo.setHasPreviousPage(housePOPageInfo.isHasPreviousPage());
        pageInfo.setHasNextPage(housePOPageInfo.isHasNextPage());
        pageInfo.setNavigatePages(housePOPageInfo.getNavigatePages());
        int[] navigatepageNums = housePOPageInfo.getNavigatepageNums();
        if (navigatepageNums != null) {
            pageInfo.setNavigatepageNums(Arrays.copyOf(navigatepageNums, navigatepageNums.length));
        }
        pageInfo.setNavigateFirstPage(housePOPageInfo.getNavigateFirstPage());
        pageInfo.setNavigateLastPage(housePOPageInfo.getNavigateLastPage());

        return pageInfo;
    }

    protected List<CommercialComplexHouseListVO> departmentDtoListToBuildingListDtoList(List<HousePo> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<CommercialComplexHouseListVO> resultList = new ArrayList<>(list.size());
        for (HousePo temp : list) {
            CommercialComplexHouseListVO commercialComplexHouseListVO = houseConverter.toCommercialComplexHouseListVOByBaseHousePO(temp);
            resultList.add(commercialComplexHouseListVO);
        }
        return resultList;
    }
}
