package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.system.application.param.dto.CustomConfigCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.CustomConfigDTO;
import cn.cuiot.dmp.system.application.param.dto.CustomConfigUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.CustomConfigVO;
import cn.cuiot.dmp.system.application.service.CustomConfigService;
import cn.cuiot.dmp.system.domain.aggregate.CustomConfigPageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 系统配置-初始化配置-常用选项-系统选项名称
 *
 * @author caorui
 * @date 2024/5/22
 */
@RestController
@RequestMapping("/customConfig")
public class CustomConfigController extends BaseController {

    @Autowired
    private CustomConfigService customConfigService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public CustomConfigVO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return customConfigService.queryForDetail(idParam.getId());
    }

    /**
     * 根据名称获取详情
     */
    @PostMapping("/queryForDetailByName")
    public CustomConfigVO queryForDetailByName(@RequestBody @Valid CustomConfigDTO customConfigDTO) {
        String orgId = getOrgId();
        customConfigDTO.setCompanyId(Long.valueOf(orgId));
        return customConfigService.queryForDetailByName(customConfigDTO);
    }

    /**
     * 保存
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveCustomConfig", operationName = "保存自定义配置", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/save")
    public int saveCustomConfig(@RequestBody @Valid CustomConfigCreateDTO createDTO) {
        return customConfigService.saveCustomConfig(createDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateCustomConfig", operationName = "更新自定义配置", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/update")
    public int updateCustomConfig(@RequestBody @Valid CustomConfigUpdateDTO updateDTO) {
        return customConfigService.updateCustomConfig(updateDTO);
    }

    /**
     * 更新状态
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateCustomConfigStatus", operationName = "更新自定义配置", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/updateStatus")
    public int updateCustomConfigStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        return customConfigService.updateCustomConfigStatus(updateStatusParam);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteCustomConfig", operationName = "删除自定义配置", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/delete")
    public int deleteCustomConfig(@RequestBody @Valid IdParam idParam) {
        return customConfigService.deleteCustomConfig(idParam.getId());
    }

    /**
     * 根据档案类型查询自定义配置列表
     */
    @PostMapping("/queryByType")
    public PageResult<CustomConfigVO> queryCustomConfigByType(@RequestBody @Valid CustomConfigPageQuery pageQuery) {
        String orgId = getOrgId();
        pageQuery.setCompanyId(Long.valueOf(orgId));
        return customConfigService.queryCustomConfigByType(pageQuery);
    }

}
