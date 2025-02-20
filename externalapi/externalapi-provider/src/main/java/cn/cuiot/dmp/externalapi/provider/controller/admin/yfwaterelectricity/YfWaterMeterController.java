package cn.cuiot.dmp.externalapi.provider.controller.admin.yfwaterelectricity;


import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadCallable;
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
import cn.cuiot.dmp.externalapi.service.query.yfwaterelectricity.YfWaterMeterDTO;
import cn.cuiot.dmp.externalapi.service.service.yfwaterelectricity.IYfWaterMeterService;
import cn.cuiot.dmp.externalapi.service.sync.yfwaterelectricity.YFWaterElectricityDeviceDataSyncService;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.YfWaterMeterStatisticsVO;
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

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * 管理端-智慧物联-宇泛水表
 *
 *
 * @author xiaotao
 * @since 2024-10-10
 */
@RestController
@RequestMapping("/yf/water")
public class YfWaterMeterController extends BaseController {


    @Autowired
    private IYfWaterMeterService yfWaterMeterService;

    @Autowired
    private YFWaterElectricityDeviceDataSyncService yfWaterElectricityDeviceDataSyncService;

    @Autowired
    private ExcelExportService excelExportService;


    /**
     * 新增水表
     *
     * @return Long
     */
    @PostMapping(value = "/create")
    public IdmResDTO<Long> create(@RequestBody @Validated(value = ValidGroup.Crud.Insert.class) YfWaterMeterDTO electricityMeterDTO) {
        Long id = yfWaterMeterService.create(electricityMeterDTO);
        return IdmResDTO.success(id);
    }


    /**
     * 编辑水表
     *
     * @return Long
     */
    @PostMapping(value = "/update")
    public IdmResDTO<Boolean> update(@RequestBody @Validated(value = ValidGroup.Crud.Update.class) YfWaterMeterDTO electricityMeterDTO) {
        Boolean res = yfWaterMeterService.update(electricityMeterDTO);
        return IdmResDTO.success(res);
    }

    /**
     * 删除水表
     *
     * @return Long
     */
    @PostMapping(value = "/delete")

    public IdmResDTO<Boolean> delete(@RequestBody @Valid IdsParam idsParam) {
        Boolean res = yfWaterMeterService.delete(idsParam);
        return IdmResDTO.success(res);
    }


    /**
     * 水表分页查询
     *
     * @param vo YfWaterMeterDTO
     * @return  YfWaterMeterDTO
     */
    @PostMapping(value = "/queryForPage")

    public IdmResDTO<IPage<YfWaterMeterVO>> queryForPage(@RequestBody YfWaterMeterDTO vo) {
        IPage< YfWaterMeterVO> page = yfWaterMeterService.queryForPage(vo);
        return IdmResDTO.success(page);
    }


    /**
     * 水表详情
     *
     * @param idParam IdParam
     * @return  YfWaterMeterDTO
     */
    @PostMapping(value = "/queryForDetail")

    public IdmResDTO< YfWaterMeterVO> queryForDetail(@RequestBody IdParam idParam) {
        YfWaterMeterVO detail = yfWaterMeterService.queryForDetail(idParam);
        return IdmResDTO.success(detail);
    }


    /**
     * 导出水表设备列表
     */

    @PostMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public void export(@RequestBody YfWaterMeterDTO vo) throws IOException {
        ExcelDownloadDto<YfWaterMeterDTO> downloadDto = ExcelDownloadDto.<YfWaterMeterDTO>builder()
                .loginInfo(LoginInfoHolder.getCurrentLoginInfo())
                .query(vo)
                .fileName("水表设备导出(" + DateTimeUtil.dateToString(new Date(), "yyyyMMddHHmmss") + ")")
                .build();

        excelExportService.excelExport(downloadDto, YfWaterMeterVO.class, dto -> yfWaterMeterService.queryForPage(vo));
    }


    /**
     * 水表用量分页查询
     *
     * @param vo YfWaterMeterDTO
     * @return  YfWaterMeterDTO
     */
    @PostMapping(value = "/amount/queryForPage")
    public IdmResDTO<IPage<YfWaterMeterStatisticsVO>> queryAmountForPage(@RequestBody YfWaterMeterDTO vo) {
        IPage<YfWaterMeterStatisticsVO> page = yfWaterMeterService.queryAmountForPage(vo);
        return IdmResDTO.success(page);
    }


    /**
     * 导出水表统计量
     */

    @PostMapping(value = "/amount/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public void amountExport(@RequestBody YfWaterMeterDTO vo) throws IOException {

        ExcelDownloadDto<YfWaterMeterDTO> downloadDto = ExcelDownloadDto.<YfWaterMeterDTO>builder()
                .loginInfo(LoginInfoHolder.getCurrentLoginInfo())
                .query(vo)
                .fileName("水表用量导出(" + DateTimeUtil.dateToString(new Date(), "yyyyMMddHHmmss") + ")")
                .build();

        excelExportService.excelExport(downloadDto, YfWaterMeterStatisticsVO.class, dto -> yfWaterMeterService.queryAmountForPage(vo));

    }


    @PostMapping(value = "/amount/day/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public void amountDaySync(@RequestBody YfWaterMeterDTO vo) throws IOException {
        yfWaterElectricityDeviceDataSyncService.waterDeviceDataSyncDay(vo.getRecordBeginTime(), vo.getRecordEndTime());

    }

    @PostMapping(value = "/amount/real/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public void amountRealSync(@RequestBody YfWaterMeterDTO vo) throws IOException {
        yfWaterElectricityDeviceDataSyncService.waterDeviceDataSyncReal();
    }


}
