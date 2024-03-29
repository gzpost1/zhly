package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.infrastructure.entity.dto.BuildingFloorDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorAddDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorBatchDelDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorDelDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorQryDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorUpdateDto;
import java.util.List;

/**
 * @author hekai
 * @version 1.0
 * @date 2022/1/12 10:42
 */
public interface FactoryParkFloorService {
    /**
     * 获取楼层信息
     *
     * @param dto
     * @return
     */
    PageResult<FactoryParkFloorDto> floorList(FactoryParkFloorQryDto dto);

    /**
     * 楼层新增
     *
     * @param dto
     * @return
     */
    int floorAdd(FactoryParkFloorAddDto dto);

    /**
     * 楼层新增
     *
     * @param dto
     * @return
     */
    FactoryParkFloorDto floorDetail(FactoryParkFloorDelDto dto);

    /**
     * 楼层新增
     *
     * @param dto
     * @return
     */
    List<FactoryParkFloorDto> getFloorByBuilding(BuildingFloorDto dto);

    /**
     * 楼层修改
     *
     * @param dto
     * @return
     */
    int floorUpdate(FactoryParkFloorUpdateDto dto);

    /**
     * 楼层修改
     *
     * @param dto
     * @return
     */
    int floorDelete(FactoryParkFloorDelDto dto);

    /**
     * 楼层修改
     *
     * @param dto
     * @return
     */
    int floorBatchDelete(FactoryParkFloorBatchDelDto dto);
}
