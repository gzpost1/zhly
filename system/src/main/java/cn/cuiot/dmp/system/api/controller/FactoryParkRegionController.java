package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.application.enums.OrgLabelEnum;
import cn.cuiot.dmp.system.application.service.FactoryPackRegionService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionAddReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionDeleteReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionDetailReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionListReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionListResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RegionUpdateReqDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wqd
 * @classname FactoryRegionRegionController
 * @description 厂园区区域
 * @date 2023/1/12
 */
@RestController
@RequestMapping("/factoryPark/region")
public class FactoryParkRegionController extends BaseController {

    @Autowired
    private FactoryPackRegionService factoryPackRegionService;

    @RequiresPermissions("factoryPark:region:add")
    @PostMapping("add")
    public void regionAdd(@RequestBody @Valid RegionAddReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        factoryPackRegionService.regionAdd(dto);
    }

    @RequiresPermissions("factoryPark:space:region")
    @PostMapping("list")
    public PageResult<RegionListResDto> regionList(@RequestBody @Valid RegionListReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        dto.setLabelId(OrgLabelEnum.FACTORY_PARK.getId());
        return factoryPackRegionService.regionList(dto);
    }

    @RequiresPermissions("factoryPark:space:region")
    @PostMapping("listByParkId")
    public List<RegionListResDto> regionListByParkId(@RequestBody RegionListReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        dto.setLabelId(OrgLabelEnum.FACTORY_PARK.getId());
        return factoryPackRegionService.regionListByParkId(dto);
    }

    @RequiresPermissions("factoryPark:space:region")
    @PostMapping("detail")
    public RegionListResDto regionDetail(@RequestBody @Valid RegionDetailReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        return factoryPackRegionService.regionDetail(dto);
    }

    @RequiresPermissions("factoryPark:region:edit")
    @PostMapping("update")
    public void regionUpdate(@RequestBody @Valid RegionUpdateReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        factoryPackRegionService.regionUpdate(dto);
    }

    @RequiresPermissions("factoryPark:region:delete")
    @PostMapping("delete")
    public void regionDelete(@RequestBody @Valid RegionDeleteReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        factoryPackRegionService.regionDelete(dto);
    }

}
