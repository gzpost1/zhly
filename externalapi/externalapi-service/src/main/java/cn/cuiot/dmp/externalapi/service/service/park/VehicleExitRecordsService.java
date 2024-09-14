package cn.cuiot.dmp.externalapi.service.service.park;

import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.entity.park.ParkInfoEntity;
import cn.cuiot.dmp.externalapi.service.entity.park.VehicleExitRecordsEntity;
import cn.cuiot.dmp.externalapi.service.mapper.park.VehicleExitRecordsMapper;
import cn.cuiot.dmp.externalapi.service.vendor.park.query.VehicleExitRecordsQuery;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.VehicleExitVO;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @since 2024-09-09
 */
@Service
public class VehicleExitRecordsService extends ServiceImpl<VehicleExitRecordsMapper, VehicleExitRecordsEntity>{

    @Autowired
    private ApiArchiveService apiArchiveService;

    @Autowired
    private ArchiveFeignService archiveFeignService;
    /**
     * 批量插入
     * @param carList
     */
    public void insertOrUpdateBatch( List<VehicleExitRecordsEntity> carList){
        getBaseMapper().insertOrUpdateBatch(carList);
    }


    /**
     * 分页查询
     * @param
     * @param query
     * @return
     */
    public IdmResDTO<IPage<VehicleExitVO>> queryForPage(VehicleExitRecordsQuery query) {
        if(CollectionUtil.isEmpty(query.getCommunityIds())){
            DepartmentReqDto dto = new DepartmentReqDto();
            dto.setDeptId(LoginInfoHolder.getCurrentDeptId());
            dto.setSelfReturn(true);
            List<BuildingArchive> archives = Optional.ofNullable(apiArchiveService.lookupBuildingArchiveByDepartmentList(dto))
                    .orElse(new ArrayList<>());
            List<Long> ids = archives.stream().map(BuildingArchive::getId).collect(Collectors.toList());
            query.setCommunityIds(ids);
        }
        IPage<VehicleExitVO> voiPage = getBaseMapper().queryForPage(new Page<>(query.getPageNo(), query.getPageSize()),query);
        List<VehicleExitVO> records = voiPage.getRecords();

        if(CollectionUtil.isEmpty(records)){
            return IdmResDTO.success(voiPage);
        }
        List<Long> communityIds = records.stream().filter(it -> Objects.nonNull(it.getCommunityId())).
                map(VehicleExitVO::getCommunityId).collect(Collectors.toList());

        Map<Long, String> propertyMap = Optional.ofNullable(getPropertyMap(communityIds)).orElse(new HashMap<>());

        records.stream().filter(it->Objects.nonNull(it.getCommunityId())).forEach(item->{
            item.setCommunityName(propertyMap.get(item.getCommunityId()));
        });
        return IdmResDTO.success(voiPage);
    }


    /**
     * 根据楼盘id获取楼盘信息
     * @param propertyIds
     * @return
     */
    public Map<Long,String> getPropertyMap(List<Long> propertyIds){
        BuildingArchiveReq req = new BuildingArchiveReq();
        req.setIdList(propertyIds);
        IdmResDTO<List<BuildingArchive>> listIdmResDTO = archiveFeignService.buildingArchiveQueryForList(req);
        List<BuildingArchive> archiveList = Optional.ofNullable(listIdmResDTO.getData()).orElseThrow(()->new BusinessException(ErrorCode.NOT_FOUND.getCode(),
                ErrorCode.NOT_FOUND.getMessage()));
        return archiveList.stream().collect(Collectors.toMap(BuildingArchive::getId,BuildingArchive::getName));
    }
}
