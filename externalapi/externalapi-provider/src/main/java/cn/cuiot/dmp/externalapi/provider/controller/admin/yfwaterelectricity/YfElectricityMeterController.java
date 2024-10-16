package cn.cuiot.dmp.externalapi.provider.controller.admin.yfwaterelectricity;


import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.base.application.utils.ExcelUtil;
import cn.cuiot.dmp.base.application.utils.ExcelUtils;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.common.validator.ValidGroup;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.query.yfwaterelectricity.YfElectricityMeterDTO;
import cn.cuiot.dmp.externalapi.service.query.yfwaterelectricity.YfWaterMeterDTO;
import cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.IYfElectricityMeterService;
import cn.cuiot.dmp.externalapi.service.sync.yfwaterelectricity.YFWaterElectricityDeviceDataSyncService;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.YfElectricityMeterStatisticsVO;
import cn.cuiot.dmp.externalapi.service.vo.yfwaterelectricity.YfElectricityMeterVO;
import cn.cuiot.dmp.externalapi.service.vo.yfwaterelectricity.YfWaterMeterVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 *
 * 管理端-智慧物联-宇泛电表
 *
 *
 * @author xiaotao
 * @since 2024-10-10
 */
@RestController
@RequestMapping("/yf/electricity")
public class YfElectricityMeterController extends BaseController {


    @Autowired
    private IYfElectricityMeterService yfElectricityMeterService;

    @Autowired
    private YFWaterElectricityDeviceDataSyncService yfWaterElectricityDeviceDataSyncService;


    @Autowired
    private ExcelExportService excelExportService;


    /**
     * 新增电表
     * @return Long
     */
    @PostMapping(value = "/create")
 
    public IdmResDTO<Long> create(@RequestBody @Validated(value = ValidGroup.Crud.Insert.class)YfElectricityMeterDTO electricityMeterDTO){
        Long id = yfElectricityMeterService.create(electricityMeterDTO);
        return IdmResDTO.success(id);
    }


    /**
     * 编辑电表
     * @return Long
     */
    @PostMapping(value = "/update")
 
    public IdmResDTO<Boolean> update(@RequestBody @Validated(value = ValidGroup.Crud.Update.class)YfElectricityMeterDTO electricityMeterDTO){
        Boolean res = yfElectricityMeterService.update(electricityMeterDTO);
        return IdmResDTO.success(res);
    }

    /**
     * 删除电表
     * @return Long
     */
    @PostMapping(value = "/delete")
 
    public IdmResDTO<Boolean> delete(@RequestBody IdsParam idsParam){
        Boolean res = yfElectricityMeterService.delete(idsParam);
        return IdmResDTO.success(res);
    }


    /**
     * 电表分页查询
     * @param vo YfElectricityMeterDTO
     * @return YfElectricityMeterVO
     */
    @PostMapping(value = "/queryForPage")
    public IdmResDTO<IPage<YfElectricityMeterVO>> queryForPage(@RequestBody YfElectricityMeterDTO vo){
        IPage<YfElectricityMeterVO> page = yfElectricityMeterService.queryForPage(vo);
        return IdmResDTO.success(page);
    }


    /**
     * 电表详情
     * @param idParam IdParam
     * @return YfElectricityMeterVO
     */
    @PostMapping(value = "/queryForDetail")
 
    public IdmResDTO<YfElectricityMeterVO> queryForDetail(@RequestBody IdParam idParam){
        YfElectricityMeterVO detail = yfElectricityMeterService.queryForDetail(idParam);
        return IdmResDTO.success(detail);
    }


    /**
     * 导出电表设备列表
     */
 
    @PostMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public void export(@RequestBody YfElectricityMeterDTO vo) throws IOException {

        ExcelDownloadDto<YfElectricityMeterDTO> downloadDto = ExcelDownloadDto.<YfElectricityMeterDTO>builder()
                .loginInfo(LoginInfoHolder.getCurrentLoginInfo())
                .query(vo)
                .fileName("电表设备导出(" + DateTimeUtil.dateToString(new Date(), "yyyyMMddHHmmss") + ")")
                .build();

        excelExportService.excelExport(downloadDto, YfElectricityMeterVO.class, dto -> yfElectricityMeterService.queryForPage(vo));

    }




    /**
     * 电表用量分页查询
     * @param vo YfElectricityMeterDTO
     * @return YfElectricityMeterVO
     */
    @PostMapping(value = "/amount/queryForPage")
    public IdmResDTO<IPage<YfElectricityMeterStatisticsVO>> queryAmountForPage(@RequestBody YfElectricityMeterDTO vo){
        IPage<YfElectricityMeterStatisticsVO> page = yfElectricityMeterService.queryAmountForPage(vo);
        return IdmResDTO.success(page);
    }


    /**
     * 导出电表统计量
     */
 
    @PostMapping(value = "/amount/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public void amountExport(@RequestBody YfElectricityMeterDTO vo) throws IOException {

        ExcelDownloadDto<YfElectricityMeterDTO> downloadDto = ExcelDownloadDto.<YfElectricityMeterDTO>builder()
                .loginInfo(LoginInfoHolder.getCurrentLoginInfo())
                .query(vo)
                .fileName("电表用量导出(" + DateTimeUtil.dateToString(new Date(), "yyyyMMddHHmmss")+")")
                .build();

        excelExportService.excelExport(downloadDto, YfElectricityMeterStatisticsVO.class, dto -> yfElectricityMeterService.queryAmountForPage(vo));


    }


    @PostMapping(value = "/amount/day/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public void amountDaySync(@RequestBody YfElectricityMeterDTO vo) throws IOException {
        yfWaterElectricityDeviceDataSyncService.electricityDeviceDataSyncDay(vo.getRecordBeginTime(), vo.getRecordEndTime());

    }

    @PostMapping(value = "/amount/real/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public void amountRealSync(@RequestBody YfElectricityMeterDTO vo) throws IOException {
        yfWaterElectricityDeviceDataSyncService.electricityDeviceDataSyncReal();
    }


}
