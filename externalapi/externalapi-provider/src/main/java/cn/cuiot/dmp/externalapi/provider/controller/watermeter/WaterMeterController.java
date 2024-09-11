package cn.cuiot.dmp.externalapi.provider.controller.watermeter;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.utils.PageUtils;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.entity.water.WaterManagementEntity;
import cn.cuiot.dmp.externalapi.service.service.water.WaterManagementService;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.UpdateWaterManagementVO;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.WaterBatchMeterOperateVO;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.WaterMeterOperateVO;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.WaterMeterQueryVO;
import cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean.WaterMeterCommandControlReq;
import cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean.WaterMeterPage;
import cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean.WaterMeterReportDataQueryReq;
import cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean.WaterMeterReportDataResp;
import cn.cuiot.dmp.externalapi.service.vendor.watermeter.service.WaterMeterService;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 管理后台-物联网水表（山东科德）
 *
 * @date 2024/8/21 16:01
 * @author gxp
 */
@RestController
@RequestMapping("/waterMeter")
public class WaterMeterController {

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
    public IdmResDTO<IPage<WaterMeterReportDataResp>> queryoldForPage(@RequestBody WaterMeterQueryVO vo) {
        WaterMeterPage<WaterMeterReportDataResp> waterMeterPage = waterManagementService.queryReportData(new WaterMeterReportDataQueryReq(vo));
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
//    @RequiresPermissions
    public IdmResDTO<IPage<WaterManagementEntity>> queryForPage(@RequestBody WaterMeterQueryVO vo){
        return waterManagementService.queryForPage(vo);
    }

    /**
     * 更新名称与楼盘信息
     * @param vo
     * @return
     */
    @PostMapping(value = "/updateWaterManagement")
    @RequiresPermissions
    public IdmResDTO updateWaterManagement(@RequestBody @Valid UpdateWaterManagementVO vo){
        return waterManagementService.updateWaterManagement(vo);
    }

    /**
     * 删除
     * @param vo
     * @return
     */
    @PostMapping(value = "/deleteWaterManagement")
    @RequiresPermissions
    public IdmResDTO deleteWaterManagement(@RequestBody @Valid UpdateWaterManagementVO vo){
        return waterManagementService.deleteWaterManagement(vo);
    }

    /**
     * 同步水表
     * @return
     */
    @PostMapping(value = "/syncWaterMeter")
    public IdmResDTO syncWaterMeter(){
        return waterManagementService.syncWaterMeter();
    }

    /**
     * 查询详情
     * @return
     */
    @PostMapping(value = "/queryWaterManagement")
    @RequiresPermissions
    public IdmResDTO<WaterManagementEntity> queryWaterManagement(@RequestBody @Valid IdParam idParam){
        return waterManagementService.queryWaterManagement(idParam);
    }
    /**
     * 阀控操作
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/operate")
    @RequiresPermissions
    public IdmResDTO open(@RequestBody @Valid WaterBatchMeterOperateVO param) {
        List<String> imei = param.getImei();
        if(CollectionUtil.isNotEmpty(imei)){
            imei.stream().forEach(item->{
                WaterMeterOperateVO vo = new WaterMeterOperateVO();
                vo.setImei(item);
                vo.setValveControlType(param.getValveControlType());
                waterManagementService.deviceCommandV2(
                        new WaterMeterCommandControlReq(
                                Lists.newArrayList(
                                        new WaterMeterCommandControlReq.CommandControlInfo(vo)
                                )));
            });
        }

        return IdmResDTO.success();
    }

}
