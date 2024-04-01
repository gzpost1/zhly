package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.system.application.enums.OrgLabelEnum;
import cn.cuiot.dmp.system.application.service.FactoryParkFloorService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.BuildingFloorDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorAddDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorBatchDelDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorDelDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorQryDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkFloorUpdateDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 厂园区楼层Controller
 *
 * @author hk
 * @date 2023-01-12 14:07:42
 */
@RestController
@RequestMapping("/factoryPark")
public class FactoryParkFloorController extends BaseController {
    @Autowired
    private FactoryParkFloorService service;

    /**
     * 列表分页条件查询
     * @param dto
     * @return
     */
    @RequiresPermissions("factoryPark:space:Storey")
    @PostMapping(value = "/floor/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<FactoryParkFloorDto> floorList(@RequestBody @Valid FactoryParkFloorQryDto dto) {
        dto.setOrgId(Long.parseLong(getOrgId()));
        dto.setUserId(Long.parseLong(getUserId()));
        dto.setLabelId(OrgLabelEnum.FACTORY_PARK.getId());
        return service.floorList(dto);
    }

    /**
     * 楼层添加
     * @param dto
     * @return
     */
    @RequiresPermissions("factoryPark:Storey:add")
    @PostMapping(value = "/floor/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public int floorAdd(@RequestBody @Valid FactoryParkFloorAddDto dto) {
        dto.setOrgId(Long.parseLong(getOrgId()));
        dto.setUserId(Long.parseLong(getUserId()));
        return service.floorAdd(dto);
    }

    /**
     * 楼层详情
     * @param dto
     * @return
     */
    @RequiresPermissions("factoryPark:space:Storey")
    @PostMapping(value = "/floor/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public FactoryParkFloorDto floorDetail(@RequestBody @Valid FactoryParkFloorDelDto dto) {
        dto.setOrgId(Long.parseLong(getOrgId()));
        dto.setUserId(Long.parseLong(getUserId()));
        return service.floorDetail(dto);
    }

    /**
     * 根据楼座获取楼层
     * @param dto
     * @return
     */
    @RequiresPermissions("factoryPark:space:Storey")
    @PostMapping(value = "/floor/getFloorByBuilding", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FactoryParkFloorDto> getFloorByBuilding(@RequestBody @Valid BuildingFloorDto dto) {
        dto.setOrgId(Long.parseLong(getOrgId()));
        dto.setUserId(Long.parseLong(getUserId()));
        return service.getFloorByBuilding(dto);
    }

    /**
     * 楼层修改
     * @param dto
     * @return
     */
    @RequiresPermissions("factoryPark:Storey:edit")
    @PostMapping(value = "/floor/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public int floorUpdate(@RequestBody @Valid FactoryParkFloorUpdateDto dto) {
        dto.setOrgId(Long.parseLong(getOrgId()));
        dto.setUserId(Long.parseLong(getUserId()));
        return service.floorUpdate(dto);
    }

    /**
     * 楼层删除
     * @param dto
     * @return
     */
    @RequiresPermissions("factoryPark:Storey:delete")
    @PostMapping(value = "/floor/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public int floorDelete(@RequestBody @Valid FactoryParkFloorDelDto dto) {
        dto.setOrgId(Long.parseLong(getOrgId()));
        dto.setUserId(Long.parseLong(getUserId()));
        return service.floorDelete(dto);
    }

    /**
     * 楼层删除
     * @param dto
     * @return
     */
    @RequiresPermissions("factoryPark:Storey:batchDelete")
    @PostMapping(value = "/floor/batchDelete", produces = MediaType.APPLICATION_JSON_VALUE)
    public int floorBatchDelete(@RequestBody @Valid FactoryParkFloorBatchDelDto dto) {
        dto.setOrgId(Long.parseLong(getOrgId()));
        dto.setUserId(Long.parseLong(getUserId()));
        return service.floorBatchDelete(dto);
    }

}
