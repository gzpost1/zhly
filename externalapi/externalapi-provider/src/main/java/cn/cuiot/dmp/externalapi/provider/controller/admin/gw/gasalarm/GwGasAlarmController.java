package cn.cuiot.dmp.externalapi.provider.controller.admin.gw.gasalarm;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadCallable;
import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.query.gw.gasalarm.GwGasAlarmCreateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.gasalarm.GwGasAlarmPageQuery;
import cn.cuiot.dmp.externalapi.service.query.gw.gasalarm.GwGasAlarmPropertyDto;
import cn.cuiot.dmp.externalapi.service.query.gw.gasalarm.GwGasAlarmUpdateDto;
import cn.cuiot.dmp.externalapi.service.service.gw.gasalarm.GwGasAlarmService;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwCommonPropertyVo;
import cn.cuiot.dmp.externalapi.service.vo.gw.gasalarm.GwGasAlarmDetailVO;
import cn.cuiot.dmp.externalapi.service.vo.gw.gasalarm.GwGasAlarmPageVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 后台-格物燃气报警器
 *
 * @Author: zc
 * @Date: 2024-09-06
 */
@RestController
@RequestMapping("/gw/gasAlarm")
public class GwGasAlarmController {

    @Autowired
    private GwGasAlarmService gwGasAlarmService;
    @Autowired
    private ExcelExportService excelExportService;

    /**
     * 分页
     *
     * @return IPage
     * @Param query 参数
     */
    @RequiresPermissions
    @PostMapping("queryForPage")
    public IdmResDTO<IPage<GwGasAlarmPageVO>> queryForPage(@RequestBody GwGasAlarmPageQuery query) {
        return IdmResDTO.success(gwGasAlarmService.queryForPage(query));
    }

    /**
     * 创建
     *
     * @Param dto 参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "create", operationName = "创建", serviceType = "gwGasAlarm", serviceTypeName = "格物燃气报警器")
    @PostMapping("create")
    public IdmResDTO<?> create(@RequestBody @Valid GwGasAlarmCreateDto dto) {
        gwGasAlarmService.create(dto);
        return IdmResDTO.success();
    }

    /**
     * 设置
     *
     * @Param dto 参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "update", operationName = "设置", serviceType = "gwGasAlarm", serviceTypeName = "格物燃气报警器")
    @PostMapping("update")
    public IdmResDTO<?> update(@RequestBody @Valid GwGasAlarmUpdateDto dto) {
        gwGasAlarmService.update(dto);
        return IdmResDTO.success();
    }

    /**
     * 详情
     *
     * @return GwGasAlarmDetailVO
     * @Param id 数据id
     */
    @RequiresPermissions
    @PostMapping("queryForDetail")
    public IdmResDTO<GwGasAlarmDetailVO> queryForDetail(@RequestBody @Valid IdParam param) {
        return IdmResDTO.success(gwGasAlarmService.queryForDetail(param.getId()));
    }

    /**
     * 获取设备属性
     *
     * @return GwGasAlarmPropertyEntity 属性详情
     * @Param id 数据id
     */
    @RequiresPermissions
    @PostMapping("getProperty")
    public IdmResDTO<List<GwCommonPropertyVo>> getProperty(@RequestBody @Valid IdParam param) {
        return IdmResDTO.success(gwGasAlarmService.getProperty(param.getId()));
    }

    /**
     * 批量设置属性
     *
     * @Param dto 参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchSetProperty", operationName = "批量设置属性", serviceType = "gwGasAlarm", serviceTypeName = "格物燃气报警器")
    @PostMapping("batchSetProperty")
    public IdmResDTO<?> batchSetProperty(@RequestBody @Valid GwGasAlarmPropertyDto dto) {
        gwGasAlarmService.batchSetProperty(dto);
        return IdmResDTO.success();
    }

    /**
     * 批量删除
     *
     * @Param ids 数据id列表
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchDelete", operationName = "批量删除", serviceType = "gwGasAlarm", serviceTypeName = "格物燃气报警器")
    @PostMapping("batchDelete")
    public IdmResDTO<?> batchDelete(@RequestBody @Valid IdsParam param) {
        gwGasAlarmService.batchDelete(param.getIds());
        return IdmResDTO.success();
    }

    /**
     * 批量启用
     *
     * @Param ids 数据id列表
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchEnable", operationName = "批量启用", serviceType = "gwGasAlarm", serviceTypeName = "格物燃气报警器")
    @PostMapping("batchEnable")
    public IdmResDTO<?> batchEnable(@RequestBody @Valid IdsParam param) {
        gwGasAlarmService.batchEnable(param.getIds());
        return IdmResDTO.success();
    }

    /**
     * 批量禁用
     *
     * @Param ids 数据id列表
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchDisable", operationName = "批量禁用", serviceType = "gwGasAlarm", serviceTypeName = "格物燃气报警器")
    @PostMapping("batchDisable")
    public IdmResDTO<?> batchDisable(@RequestBody @Valid IdsParam param) {
        gwGasAlarmService.batchDisable(param.getIds());
        return IdmResDTO.success();
    }

    /**
     * 导出
     *
     * @Param query 参数
     */
    @RequiresPermissions
    @PostMapping("/export")
    public IdmResDTO<?> export(@RequestBody GwGasAlarmPageQuery query) {

        ExcelDownloadDto<GwGasAlarmPageQuery> dto = ExcelDownloadDto.<GwGasAlarmPageQuery>builder()
                .loginInfo(LoginInfoHolder.getCurrentLoginInfo())
                .query(query)
                .fileName("燃气报警器设备导出（" + DateTimeUtil.dateToString(new Date(), "yyyyMMdd") + "）")
                .build();

        excelExportService.excelExport(dto, GwGasAlarmPageVO.class,
                new ExcelDownloadCallable<GwGasAlarmPageQuery, GwGasAlarmPageVO>() {
                    @Override
                    public IPage<GwGasAlarmPageVO> excute(
                            ExcelDownloadDto<GwGasAlarmPageQuery> dto) {
                        return gwGasAlarmService.queryForPage(dto.getQuery());
                    }
                });

        return IdmResDTO.success();
    }
}
