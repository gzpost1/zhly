package cn.cuiot.dmp.externalapi.provider.controller.admin.gw.gasalarm;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadCallable;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.query.gw.gasalarm.GwGasAlarmFaultRecordQuery;
import cn.cuiot.dmp.externalapi.service.service.gw.gasalarm.GwGasAlarmFaultRecordService;
import cn.cuiot.dmp.externalapi.service.vo.gw.gasalarm.GwGasAlarmFaultRecordVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 后台-格物燃气报警器告警记录
 *
 * @Author: zc
 * @Date: 2024-09-06
 */
@RestController
@RequestMapping("/gw/GasAlarm/fault")
public class GwGasAlarmFaultRecordController {

    @Autowired
    private GwGasAlarmFaultRecordService faultRecordService;
    @Autowired
    private ExcelExportService excelExportService;

    /**
     * 报警记录分页
     *
     * @return IPage
     * @Param query 参数
     */
    @RequiresPermissions
    @PostMapping("queryRecordForPage")
    public IdmResDTO<IPage<GwGasAlarmFaultRecordVO>> queryRecordForPage(@RequestBody GwGasAlarmFaultRecordQuery query) {
        return IdmResDTO.success(faultRecordService.queryRecordForPage(query));
    }

    /**
     * 导出
     *
     * @Param query 参数
     */
    @RequiresPermissions
    @PostMapping("/export")
    public IdmResDTO<?> export(@RequestBody GwGasAlarmFaultRecordQuery query) {

        ExcelDownloadDto<GwGasAlarmFaultRecordQuery> dto = ExcelDownloadDto.<GwGasAlarmFaultRecordQuery>builder()
                .loginInfo(LoginInfoHolder.getCurrentLoginInfo())
                .query(query)
                .fileName("燃气报警器设备告警导出（" + DateTimeUtil.dateToString(new Date(), "yyyyMMdd") + "）")
                .build();

        excelExportService.excelExport(dto, GwGasAlarmFaultRecordVO.class,
                new ExcelDownloadCallable<GwGasAlarmFaultRecordQuery, GwGasAlarmFaultRecordVO>() {
                    @Override
                    public IPage<GwGasAlarmFaultRecordVO> excute(
                            ExcelDownloadDto<GwGasAlarmFaultRecordQuery> dto) {
                        return faultRecordService.queryRecordForPage(dto.getQuery());
                    }
                });

        return IdmResDTO.success();
    }
}
