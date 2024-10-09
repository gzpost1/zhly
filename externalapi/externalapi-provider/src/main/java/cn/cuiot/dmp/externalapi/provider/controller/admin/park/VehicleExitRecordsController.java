package cn.cuiot.dmp.externalapi.provider.controller.admin.park;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.service.park.VehicleExitRecordsService;
import cn.cuiot.dmp.externalapi.service.vendor.park.query.VehicleExitRecordsQuery;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.VehicleExitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.baomidou.mybatisplus.core.metadata.IPage;


import org.springframework.web.bind.annotation.RestController;
/**
 * 进出记录
 * @author pengjian
 * @since 2024-09-09
 */
@RestController
@RequestMapping("/vehicle-exit-records")
public class VehicleExitRecordsController {

    @Autowired
    private VehicleExitRecordsService vehicleExitRecordsService;


    @PostMapping("/queryForPage")
    @RequiresPermissions
    public IdmResDTO<IPage<VehicleExitVO>> queryForPage(@RequestBody VehicleExitRecordsQuery query) {
     return  vehicleExitRecordsService.queryForPage(query);
    }



}
