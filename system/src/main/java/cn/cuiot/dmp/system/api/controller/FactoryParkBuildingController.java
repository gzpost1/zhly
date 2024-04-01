package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.system.application.enums.OrgLabelEnum;
import cn.cuiot.dmp.system.application.service.FactoryParkBuildingService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.BuildingDeleteReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.BuildingDetailReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.BuildingUpdateReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkBuildingAddReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkBuildingListReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkBuildingListResDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wqd
 * @classname FactoryParkBuildingController
 * @description 厂园区楼栋
 * @date 2023/1/13
 */
@RestController
@RequestMapping("/factoryPark/building")
public class FactoryParkBuildingController extends BaseController {

    @Autowired
    private FactoryParkBuildingService factoryParkBuildingService;

    @RequiresPermissions("factoryPark:building:add")
    @PostMapping("add")
    public void buildingAdd(@RequestBody @Valid FactoryParkBuildingAddReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        factoryParkBuildingService.buildingAdd(dto);
    }

    @RequiresPermissions("factoryPark:space:building")
    @PostMapping("list")
    public PageResult<FactoryParkBuildingListResDto> buildingList(@RequestBody @Valid FactoryParkBuildingListReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        dto.setLabelId(OrgLabelEnum.FACTORY_PARK.getId());
        return factoryParkBuildingService.buildingList(dto);
    }

    @RequiresPermissions("factoryPark:space:building")
    @PostMapping("listByRegionId")
    public List<FactoryParkBuildingListResDto> buildingListByRegionId(@RequestBody FactoryParkBuildingListReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        dto.setLabelId(OrgLabelEnum.FACTORY_PARK.getId());
        return factoryParkBuildingService.buildingListByRegionId(dto);
    }

    @RequiresPermissions("factoryPark:space:building")
    @PostMapping("detail")
    public FactoryParkBuildingListResDto buildingDetail(@RequestBody @Valid BuildingDetailReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        return factoryParkBuildingService.buildingDetail(dto);
    }

    @RequiresPermissions("factoryPark:building:edit")
    @PostMapping("update")
    public void buildingUpdate(@RequestBody @Valid BuildingUpdateReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        factoryParkBuildingService.buildingUpdate(dto);
    }

    @RequiresPermissions("factoryPark:building:delete")
    @PostMapping("delete")
    public void buildingDelete(@RequestBody @Valid BuildingDeleteReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        factoryParkBuildingService.buildingDelete(dto);
    }

}
