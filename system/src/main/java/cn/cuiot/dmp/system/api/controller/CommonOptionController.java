package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.system.application.param.dto.BatchCommonOptionDTO;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionDTO;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.CommonOptionVO;
import cn.cuiot.dmp.system.application.service.CommonOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 系统配置-初始化配置-常用选项-自定义选项名称
 *
 * @author caorui
 * @date 2024/4/28
 */
@RestController
@RequestMapping("/commonOption")
public class CommonOptionController {

    @Autowired
    private CommonOptionService commonOptionService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public CommonOptionVO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return commonOptionService.queryForDetail(idParam.getId());
    }

    /**
     * 根据名称获取详情
     */
    @PostMapping("/queryForDetailByName")
    public CommonOptionVO queryForDetailByName(@RequestBody @Valid CommonOptionDTO commonOptionDTO) {
        return commonOptionService.queryForDetailByName(commonOptionDTO);
    }

    /**
     * 保存
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveCommonOption", operationName = "保存常用选项", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/save")
    public int saveCommonOption(@RequestBody @Valid CommonOptionCreateDTO createDTO) {
        return commonOptionService.saveCommonOption(createDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateCommonOption", operationName = "更新常用选项", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/update")
    public int updateCommonOption(@RequestBody @Valid CommonOptionUpdateDTO updateDTO) {
        return commonOptionService.updateCommonOption(updateDTO);
    }

    /**
     * 更新状态
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateCommonOptionStatus", operationName = "更新常用选项状态", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/updateStatus")
    public int updateCommonOptionStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        return commonOptionService.updateCommonOptionStatus(updateStatusParam);
    }

    /**
     * 删除预校验
     */
    @PostMapping("/checkBeforeDelete")
    public void checkBeforeDelete(@RequestBody @Valid IdParam idParam) {
        commonOptionService.checkDeleteStatus(idParam.getId());
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteCommonOption", operationName = "删除常用选项", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/delete")
    public int deleteCommonOption(@RequestBody @Valid IdParam idParam) {
        return commonOptionService.deleteCommonOption(idParam.getId());
    }

    /**
     * 批量移动
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchMoveCommonOption", operationName = "批量移动常用选项", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/batchMove")
    public int batchMoveCommonOption(@RequestBody @Valid BatchCommonOptionDTO batchCommonOptionDTO) {
        return commonOptionService.batchMoveCommonOption(batchCommonOptionDTO);
    }

    /**
     * 批量更新状态
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchUpdateCommonOptionStatus", operationName = "批量更新常用选项状态", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/batchUpdateStatus")
    public int batchUpdateCommonOptionStatus(@RequestBody @Valid BatchCommonOptionDTO batchCommonOptionDTO) {
        return commonOptionService.batchUpdateCommonOptionStatus(batchCommonOptionDTO);
    }

    /**
     * 批量删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchDeleteCommonOption", operationName = "批量删除常用选项", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/batchDelete")
    public int batchDeleteCommonOption(@RequestBody @Valid BatchCommonOptionDTO batchCommonOptionDTO) {
        return commonOptionService.batchDeleteCommonOption(batchCommonOptionDTO.getIdList());
    }

}
