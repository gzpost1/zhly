package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.param.dto.*;
import cn.cuiot.dmp.system.application.param.vo.FormConfigVO;
import cn.cuiot.dmp.system.application.service.FormConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 企业账号-系统配置-初始化配置-表单配置
 *
 * @author caorui
 * @date 2024/4/28
 */
@RestController
@RequestMapping("/formConfig")
public class FormConfigController {

    @Autowired
    private FormConfigService formConfigService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public FormConfigVO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return formConfigService.queryForDetail(idParam.getId());
    }

    /**
     * 根据名称获取详情
     */
    @PostMapping("/queryForDetailByName")
    public FormConfigVO queryForDetailByName(@RequestBody @Valid FormConfigDTO formConfigDTO) {
        Long orgId = LoginInfoHolder.getCurrentOrgId();
        formConfigDTO.setCompanyId(orgId);
        return formConfigService.queryForDetailByName(formConfigDTO);
    }

    /**
     * 保存
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveFormConfig", operationName = "保存表单配置", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/save")
    public int saveFormConfig(@RequestBody @Valid FormConfigCreateDTO createDTO) {
        return formConfigService.saveFormConfig(createDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateFormConfig", operationName = "更新表单配置", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/update")
    public int updateFormConfig(@RequestBody @Valid FormConfigUpdateDTO updateDTO) {
        return formConfigService.updateFormConfig(updateDTO);
    }

    /**
     * 更新状态
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateFormConfigStatus", operationName = "更新表单配置状态", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/updateStatus")
    public int updateFormConfigStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        return formConfigService.updateFormConfigStatus(updateStatusParam);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteFormConfig", operationName = "删除表单配置", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/delete")
    public int deleteFormConfig(@RequestBody @Valid IdParam idParam) {
        return formConfigService.deleteFormConfig(idParam.getId());
    }

    /**
     * 批量移动
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchMoveFormConfig", operationName = "批量移动表单配置", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/batchMove")
    public int batchMoveFormConfig(@RequestBody @Valid BatchFormConfigDTO batchFormConfigDTO) {
        return formConfigService.batchMoveFormConfig(batchFormConfigDTO);
    }

    /**
     * 批量更新状态
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchUpdateFormConfigStatus", operationName = "批量更新表单配置状态", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/batchUpdateStatus")
    public int batchUpdateFormConfigStatus(@RequestBody @Valid BatchFormConfigDTO batchFormConfigDTO) {
        return formConfigService.batchUpdateFormConfigStatus(batchFormConfigDTO);
    }

    /**
     * 批量删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchDeleteFormConfig", operationName = "批量删除表单配置", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/batchDelete")
    public int batchDeleteFormConfig(@RequestBody @Valid BatchFormConfigDTO batchFormConfigDTO) {
        return formConfigService.batchDeleteFormConfig(batchFormConfigDTO.getIdList());
    }

    /**
     * 从缓存获取表单配置内容
     */
    @RequiresPermissions
    @PostMapping("/getFormConfigFromCache")
    public String getFormConfigFromCache(@RequestBody @Valid FormConfigCacheDTO cacheDTO) {
        return formConfigService.getFormConfigFromCache(cacheDTO);
    }

    /**
     * 写入表单配置内容到缓存
     */
    @RequiresPermissions
    @PostMapping("/setFormConfig2Cache")
    public IdmResDTO<Object> setFormConfig2Cache(@RequestBody @Valid FormConfigCacheDTO cacheDTO) {
        formConfigService.setFormConfig2Cache(cacheDTO);
        return IdmResDTO.success();
    }

    /**
     * 从缓存删除表单配置内容
     */
    @RequiresPermissions
    @PostMapping("/deleteFormConfigFromCache")
    public IdmResDTO<Object> deleteFormConfigFromCache(@RequestBody @Valid FormConfigCacheDTO cacheDTO) {
        formConfigService.deleteFormConfigFromCache(cacheDTO);
        return IdmResDTO.success();
    }

}
