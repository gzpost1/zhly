package cn.cuiot.dmp.externalapi.provider.controller.app.watermeter;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.utils.PageUtils;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.entity.water.WaterManagementEntity;
import cn.cuiot.dmp.externalapi.service.service.water.WaterManagementService;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.WaterMeterOperateVO;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.WaterMeterQueryVO;
import cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean.WaterMeterCommandControlReq;
import cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean.WaterMeterPage;
import cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean.WaterMeterReportDataQueryReq;
import cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean.WaterMeterReportDataResp;
import cn.cuiot.dmp.externalapi.service.vendor.watermeter.service.WaterMeterService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 小程序-物联网水表（山东科德）
 *
 * @date 2024/8/21 16:01
 * @author gxp
 */
@RestController
@RequestMapping("/app/waterMeter")
public class WaterMeterAppController {

    @Autowired
    private WaterMeterService waterMeterService;

    @Autowired
    private WaterManagementService waterManagementService;


    /**
     * 查询分页
     *
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryOldForPage")
    @RequiresPermissions
    public IdmResDTO<IPage<WaterMeterReportDataResp>> queryOldForPage(@RequestBody WaterMeterQueryVO vo) {
        WaterMeterPage<WaterMeterReportDataResp> waterMeterPage = waterMeterService.queryReportData(new WaterMeterReportDataQueryReq(vo));
        //分页
        return IdmResDTO.success(PageUtils.page(vo.getPageNo(), vo.getPageSize(), waterMeterPage.getData()));
    }


    /**
     * 查询分页-新
     *
     * @param vo
     * @return
     */
    @PostMapping(value = "/queryForPage")
    @RequiresPermissions
    public IdmResDTO<IPage<WaterManagementEntity>> queryForPage(@RequestBody WaterMeterQueryVO vo){
        return waterManagementService.queryForPage(vo);
    }

    /**
     * 阀控操作
     *
     * @param vo
     * @return
     */
    @PostMapping(value = "/operate")
    @RequiresPermissions
    public IdmResDTO open(@RequestBody @Valid WaterMeterOperateVO vo) {
        return IdmResDTO.success(waterManagementService.deviceCommandV2(
                new WaterMeterCommandControlReq(
                        Lists.newArrayList(
                                new WaterMeterCommandControlReq.CommandControlInfo(vo)
                        )))
                .getSuccess());
    }

}
