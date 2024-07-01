package cn.cuiot.dmp.lease.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.lease.dto.contractTemplate.ContractTemplateCreateDTO;
import cn.cuiot.dmp.lease.dto.contractTemplate.ContractTemplateDTO;
import cn.cuiot.dmp.lease.dto.contractTemplate.ContractTemplatePageQueryDTO;
import cn.cuiot.dmp.lease.dto.contractTemplate.ContractTemplateUpdateDTO;
import cn.cuiot.dmp.lease.service.ContractTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 租赁管理-合同管理-合同模板
 *
 * @author caorui
 * @date 2024/6/28
 */
@RestController
@RequestMapping("/contractTemplate")
public class ContractTemplateController {

    @Autowired
    private ContractTemplateService contractTemplateService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public ContractTemplateDTO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return contractTemplateService.queryForDetail(idParam.getId());
    }

    /**
     * 查询列表
     */
    @PostMapping("/queryForList")
    public List<ContractTemplateDTO> queryForList(@RequestBody @Valid ContractTemplatePageQueryDTO queryDTO) {
        return contractTemplateService.queryForList(queryDTO);
    }

    /**
     * 查询分页列表
     */
    @PostMapping("/queryForPage")
    public PageResult<ContractTemplateDTO> queryForPage(@RequestBody @Valid ContractTemplatePageQueryDTO queryDTO) {
        return contractTemplateService.queryForPage(queryDTO);
    }

    /**
     * 保存
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveContractTemplate", operationName = "保存合同模板", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/save")
    public boolean saveContractTemplate(@RequestBody @Valid ContractTemplateCreateDTO createDTO) {
        return contractTemplateService.saveContractTemplate(createDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateContractTemplate", operationName = "更新合同模板", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/update")
    public boolean updateContractTemplate(@RequestBody @Valid ContractTemplateUpdateDTO updateDTO) {
        return contractTemplateService.updateContractTemplate(updateDTO);
    }

    /**
     * 复制新增
     */
    @RequiresPermissions
    @LogRecord(operationCode = "copyContractTemplate", operationName = "复制合同模板", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/copy")
    public boolean copyContractTemplate(@RequestBody @Valid IdParam idParam) {
        return contractTemplateService.copyContractTemplate(idParam.getId());
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteContractTemplate", operationName = "删除合同模板", serviceType = ServiceTypeConst.CLUE_MANAGEMENT)
    @PostMapping("/delete")
    public boolean deleteContractTemplate(@RequestBody @Valid IdParam idParam) {
        return contractTemplateService.deleteContractTemplate(idParam.getId());
    }

}
