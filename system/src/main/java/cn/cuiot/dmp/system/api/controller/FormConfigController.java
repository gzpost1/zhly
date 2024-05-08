package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.system.application.param.dto.BatchFormConfigDTO;
import cn.cuiot.dmp.system.application.param.dto.FormConfigCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.FormConfigUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigVO;
import cn.cuiot.dmp.system.application.service.FormConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
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
     * 保存
     */
    @PostMapping("/save")
    public int saveFormConfig(@RequestBody @Valid FormConfigCreateDTO createDTO) {
        return formConfigService.saveFormConfig(createDTO);
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    public int updateFormConfig(@RequestBody @Valid FormConfigUpdateDTO updateDTO) {
        return formConfigService.updateFormConfig(updateDTO);
    }

    /**
     * 更新状态
     */
    @PostMapping("/updateStatus")
    public int updateFormConfigStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        return formConfigService.updateFormConfigStatus(updateStatusParam);
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public int deleteFormConfig(@RequestBody @Valid IdParam idParam) {
        return formConfigService.deleteFormConfig(idParam.getId());
    }

    /**
     * 批量移动
     */
    @PostMapping("/batchMove")
    public int batchMoveFormConfig(@RequestBody @Valid BatchFormConfigDTO batchFormConfigDTO) {
        return formConfigService.batchMoveFormConfig(batchFormConfigDTO);
    }

    /**
     * 批量更新状态
     */
    @PostMapping("/batchUpdateStatus")
    public int batchUpdateFormConfigStatus(@RequestBody @Valid BatchFormConfigDTO batchFormConfigDTO) {
        return formConfigService.batchUpdateFormConfigStatus(batchFormConfigDTO);
    }

    /**
     * 批量删除
     */
    @PostMapping("/batchDelete")
    public int batchDeleteFormConfig(@RequestBody @Valid BatchFormConfigDTO batchFormConfigDTO) {
        return formConfigService.batchDeleteFormConfig(batchFormConfigDTO.getIdList());
    }

}
