package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.common.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.application.enums.OrgLabelEnum;
import cn.cuiot.dmp.system.application.service.FactoryParkParkService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkParkAddReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkParkListReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FactoryParkParkListResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ParkDeleteReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ParkDetailReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ParkListResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ParkUpdateReqDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wqd
 * @classname FactoryParkParkController
 * @description 厂园区控制层
 * @date 2023/1/12
 */
@RestController
@RequestMapping("/factoryPark/park")
public class FactoryParkParkController extends BaseController {

    @Autowired
    private FactoryParkParkService factoryParkParkService;

    @RequiresPermissions("factoryPark:park:add")
    @PostMapping("add")
    public void parkAdd(@RequestBody @Valid FactoryParkParkAddReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        factoryParkParkService.parkAdd(dto);
    }

    @RequiresPermissions("factoryPark:space:park")
    @PostMapping("list")
    public PageResult<FactoryParkParkListResDto> parkList(@RequestBody @Valid FactoryParkParkListReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        dto.setLabelId(OrgLabelEnum.FACTORY_PARK.getId());
        return factoryParkParkService.parkList(dto);
    }

    @RequiresPermissions("factoryPark:space:park")
    @PostMapping("listByDeptTreePath")
    public List<FactoryParkParkListResDto> parkListByDeptTreePath(@RequestBody FactoryParkParkListReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        dto.setLabelId(OrgLabelEnum.FACTORY_PARK.getId());
        return factoryParkParkService.parkListByDeptTreePath(dto);
    }

    @RequiresPermissions("factoryPark:park:detail")
    @PostMapping("detail")
    public ParkListResDto parkDetail(@RequestBody @Valid ParkDetailReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        return factoryParkParkService.parkDetail(dto);
    }

    @RequiresPermissions("factoryPark:park:edit")
    @PostMapping("update")
    public void parkUpdate(@RequestBody @Valid ParkUpdateReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        factoryParkParkService.parkUpdate(dto);
    }

    @RequiresPermissions("factoryPark:park:delete")
    @PostMapping("delete")
    public void parkDelete(@RequestBody @Valid ParkDeleteReqDto dto) {
        dto.setOrgId(getOrgId());
        dto.setUserId(getUserId());
        factoryParkParkService.parkDelete(dto);
    }
}
