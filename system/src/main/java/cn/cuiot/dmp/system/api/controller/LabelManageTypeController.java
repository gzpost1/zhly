package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.LabelManageTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.LabelManageTypeRspDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.param.dto.LabelManageCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.LabelManageTypeDTO;
import cn.cuiot.dmp.system.application.param.dto.LabelManageUpdateDTO;
import cn.cuiot.dmp.system.application.service.LabelManageService;
import cn.cuiot.dmp.system.application.service.LabelManageTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统配置-初始化配置-标签管理
 *
 * @author caorui
 * @date 2024/7/1
 */
@RestController
@RequestMapping("/labelManageType")
public class LabelManageTypeController {

    @Autowired
    private LabelManageTypeService labelManageTypeService;

    @Autowired
    private LabelManageService labelManageService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public LabelManageTypeDTO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return labelManageTypeService.queryForDetail(idParam.getId());
    }

    /**
     * 根据条件查询标签管理列表
     */
    @PostMapping("/queryForList")
    public List<LabelManageTypeRspDTO> queryForList(@RequestBody @Valid LabelManageTypeReqDTO queryDTO) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        queryDTO.setCompanyId(companyId);
        return labelManageTypeService.queryForList(queryDTO);
    }

    /**
     * 根据企业id查询标签管理列表
     */
    @PostMapping("/queryByCompany")
    public List<LabelManageTypeDTO> queryByCompany() {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        return labelManageTypeService.queryByCompany(companyId);
    }

    /**
     * 新增标签
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveLabel", operationName = "创建标签", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/saveLabel")
    public boolean saveLabelManage(@RequestBody @Valid LabelManageCreateDTO createDTO) {
        return labelManageService.saveLabelManage(createDTO);
    }

    /**
     * 更新标签
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateLabel", operationName = "更新标签", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/updateLabel")
    public boolean updateLabelManage(@RequestBody @Valid LabelManageUpdateDTO updateDTO) {
        return labelManageService.updateLabelManage(updateDTO);
    }

    /**
     * 删除标签
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteLabelManage", operationName = "删除标签", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/deleteLabel")
    public boolean deleteLabelManage(@RequestBody @Valid IdParam idParam) {
        return labelManageService.deleteLabelManage(idParam.getId());
    }

}
