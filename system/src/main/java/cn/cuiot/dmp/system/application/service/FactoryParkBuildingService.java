package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.infrastructure.entity.dto.BuildingDeleteReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.BuildingDetailReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.BuildingUpdateReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkBuildingAddReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkBuildingListReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkBuildingListResDto;
import java.util.List;

/**
 * @author wqd
 * @classname FactoryParkBuildingService
 * @description
 * @date 2023/1/13
 */
public interface FactoryParkBuildingService {

    /**
     * 楼栋添加
     * @param dto
     */
    void buildingAdd(FactoryParkBuildingAddReqDto dto);

    /**
     * 楼栋列表
     * @param dto
     * @return
     */
    PageResult<FactoryParkBuildingListResDto> buildingList(FactoryParkBuildingListReqDto dto);

    /**
     * 根据区域ID获取楼栋列表
     * @param dto
     * @return
     */
    List<FactoryParkBuildingListResDto> buildingListByRegionId(FactoryParkBuildingListReqDto dto);

    /**
     * 楼栋详情
     * @param dto
     * @return
     */
    FactoryParkBuildingListResDto buildingDetail(BuildingDetailReqDto dto);

    /**
     * 楼栋修改
     * @param dto
     */
    void buildingUpdate(BuildingUpdateReqDto dto);

    /**
     * 楼栋删除
     * @param dto
     */
    void buildingDelete(BuildingDeleteReqDto dto);

}
