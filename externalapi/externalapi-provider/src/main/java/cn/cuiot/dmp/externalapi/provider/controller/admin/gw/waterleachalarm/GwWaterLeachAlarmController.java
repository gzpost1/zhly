package cn.cuiot.dmp.externalapi.provider.controller.admin.gw.waterleachalarm;

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
import cn.cuiot.dmp.externalapi.service.query.gw.waterleachalarm.GwWaterLeachAlarmPageQuery;
import cn.cuiot.dmp.externalapi.service.query.gw.waterleachalarm.GwWaterLeachAlarmPropertyDto;
import cn.cuiot.dmp.externalapi.service.query.gw.waterleachalarm.GwWaterLeachAlarmCreateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.waterleachalarm.GwWaterLeachAlarmUpdateDto;
import cn.cuiot.dmp.externalapi.service.service.gw.waterleachalarm.GwWaterLeachAlarmService;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwCommonPropertyVo;
import cn.cuiot.dmp.externalapi.service.vo.gw.waterleachalarm.GwWaterLeachAlarmDetailVO;
import cn.cuiot.dmp.externalapi.service.vo.gw.waterleachalarm.GwWaterLeachAlarmPageVO;
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
 * 后台-格物水浸报警器
 *
 * @Author: zc
 * @Date: 2024-09-06
 */
@RestController
@RequestMapping("/gw/waterLeachAlarm")
public class GwWaterLeachAlarmController {

    @Autowired
    private GwWaterLeachAlarmService gwWaterLeachAlarmService;
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
    public IdmResDTO<IPage<GwWaterLeachAlarmPageVO>> queryForPage(@RequestBody GwWaterLeachAlarmPageQuery query) {
        return IdmResDTO.success(gwWaterLeachAlarmService.queryForPage(query));
    }

    /**
     * 创建
     *
     * @Param dto 参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "create", operationName = "创建", serviceType = "gwWaterLeachAlarm", serviceTypeName = "格物水浸报警器")
    @PostMapping("create")
    public IdmResDTO<?> create(@RequestBody @Valid GwWaterLeachAlarmCreateDto dto) {
        gwWaterLeachAlarmService.create(dto);
        return IdmResDTO.success();
    }

    /**
     * 设置
     *
     * @Param dto 参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "update", operationName = "设置", serviceType = "gwWaterLeachAlarm", serviceTypeName = "格物水浸报警器")
    @PostMapping("update")
    public IdmResDTO<?> update(@RequestBody @Valid GwWaterLeachAlarmUpdateDto dto) {
        gwWaterLeachAlarmService.update(dto);
        return IdmResDTO.success();
    }

    /**
     * 详情
     *
     * @return GwWaterLeachAlarmDetailVO
     * @Param id 数据id
     */
    @RequiresPermissions
    @PostMapping("queryForDetail")
    public IdmResDTO<GwWaterLeachAlarmDetailVO> queryForDetail(@RequestBody @Valid IdParam param) {
        return IdmResDTO.success(gwWaterLeachAlarmService.queryForDetail(param.getId()));
    }

    /**
     * 获取设备属性
     *
     * @return GwWaterLeachAlarmPropertyEntity 属性详情
     * @Param id 数据id
     */
    @RequiresPermissions
    @PostMapping("getProperty")
    public IdmResDTO<List<GwCommonPropertyVo>> getProperty(@RequestBody @Valid IdParam param) {
        return IdmResDTO.success(gwWaterLeachAlarmService.getProperty(param.getId()));
    }

    /**
     * 批量设置属性
     *
     * @Param dto 参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchSetProperty", operationName = "批量设置属性", serviceType = "gwWaterLeachAlarm", serviceTypeName = "格物水浸报警器")
    @PostMapping("batchSetProperty")
    public IdmResDTO<?> batchSetProperty(@RequestBody @Valid GwWaterLeachAlarmPropertyDto dto) {
        gwWaterLeachAlarmService.batchSetProperty(dto);
        return IdmResDTO.success();
    }

    /**
     * 批量删除
     *
     * @Param ids 数据id列表
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchDelete", operationName = "批量删除", serviceType = "gwWaterLeachAlarm", serviceTypeName = "格物水浸报警器")
    @PostMapping("batchDelete")
    public IdmResDTO<?> batchDelete(@RequestBody @Valid IdsParam param) {
        gwWaterLeachAlarmService.batchDelete(param.getIds());
        return IdmResDTO.success();
    }

    /**
     * 批量启用
     *
     * @Param ids 数据id列表
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchEnable", operationName = "批量启用", serviceType = "gwWaterLeachAlarm", serviceTypeName = "格物水浸报警器")
    @PostMapping("batchEnable")
    public IdmResDTO<?> batchEnable(@RequestBody @Valid IdsParam param) {
        gwWaterLeachAlarmService.batchEnable(param.getIds());
        return IdmResDTO.success();
    }

    /**
     * 批量禁用
     *
     * @Param ids 数据id列表
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchDisable", operationName = "批量禁用", serviceType = "gwWaterLeachAlarm", serviceTypeName = "格物水浸报警器")
    @PostMapping("batchDisable")
    public IdmResDTO<?> batchDisable(@RequestBody @Valid IdsParam param) {
        gwWaterLeachAlarmService.batchDisable(param.getIds());
        return IdmResDTO.success();
    }

    /**
     * 导出
     *
     * @Param query 参数
     */
    @RequiresPermissions
    @PostMapping("/export")
    public IdmResDTO<?> export(@RequestBody GwWaterLeachAlarmPageQuery query) {

        ExcelDownloadDto<GwWaterLeachAlarmPageQuery> dto = ExcelDownloadDto.<GwWaterLeachAlarmPageQuery>builder()
                .loginInfo(LoginInfoHolder.getCurrentLoginInfo())
                .query(query)
                .fileName("水浸报警器设备导出（" + DateTimeUtil.dateToString(new Date(), "yyyyMMdd") + "）")
                .build();

        excelExportService.excelExport(dto, GwWaterLeachAlarmPageVO.class,
                new ExcelDownloadCallable<GwWaterLeachAlarmPageQuery, GwWaterLeachAlarmPageVO>() {
                    @Override
                    public IPage<GwWaterLeachAlarmPageVO> excute(
                            ExcelDownloadDto<GwWaterLeachAlarmPageQuery> dto) {
                        return gwWaterLeachAlarmService.queryForPage(dto.getQuery());
                    }
                });

        return IdmResDTO.success();
    }
}
