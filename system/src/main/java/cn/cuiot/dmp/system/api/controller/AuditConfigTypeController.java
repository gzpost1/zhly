package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.AuditConfigTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.AuditConfigTypeRspDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.param.dto.AuditConfigTypeDTO;
import cn.cuiot.dmp.system.application.param.dto.AuditConfigTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.AuditConfigTypeStatusDTO;
import cn.cuiot.dmp.system.application.service.AuditConfigService;
import cn.cuiot.dmp.system.application.service.AuditConfigTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统配置-初始化配置-审核配置
 *
 * @author caorui
 * @date 2024/6/11
 */
@RestController
@RequestMapping("/auditConfigType")
public class AuditConfigTypeController {

    @Autowired
    private AuditConfigTypeService auditConfigTypeService;

    @Autowired
    private AuditConfigService auditConfigService;

    /**
     * 根据企业id查询审核配置列表
     */
    @PostMapping("/queryByCompany")
    public List<AuditConfigTypeDTO> queryByCompany(@RequestBody @Valid AuditConfigTypeQueryDTO queryDTO) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        return auditConfigTypeService.queryByCompany(companyId);
    }

    /**
     * 根据条件查询审核配置列表
     */
    @PostMapping("/queryForList")
    public List<AuditConfigTypeRspDTO> queryForList(@RequestBody @Valid AuditConfigTypeReqDTO queryDTO) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        queryDTO.setCompanyId(companyId);
        return auditConfigTypeService.queryForList(queryDTO);
    }

    /**
     * 更新状态
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateAuditConfigStatus", operationName = "更新审核配置状态", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/updateStatus")
    public boolean updateAuditConfigStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        return auditConfigTypeService.updateAuditConfigStatus(updateStatusParam);
    }

    /**
     * 批量更新状态
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchUpdateAuditConfigStatus", operationName = "批量更新审核配置状态", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/batchUpdateStatus")
    public boolean batchUpdateAuditConfigStatus(@RequestBody @Valid AuditConfigTypeStatusDTO auditConfigTypeStatusDTO) {
        return auditConfigService.batchUpdateStatus(auditConfigTypeStatusDTO);
    }

}
