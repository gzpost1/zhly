package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.param.dto.FormConfigTypeCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.FormConfigTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.FormConfigTypeUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeTreeNodeVO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeVO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigVO;
import cn.cuiot.dmp.system.application.service.FormConfigTypeService;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigPageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 企业账号-系统配置-初始化配置-表单配置分类
 *
 * @author caorui
 * @date 2024/4/28
 */
@RestController
@RequestMapping("/formConfigType")
public class FormConfigTypeController {

    @Autowired
    private FormConfigTypeService formConfigTypeService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public FormConfigTypeVO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return formConfigTypeService.queryForDetail(idParam.getId());
    }

    /**
     * 根据条件获取表单配置类型列表
     */
    @PostMapping("/queryForList")
    public List<FormConfigTypeVO> queryForList(@RequestBody @Valid FormConfigTypeQueryDTO queryDTO) {
        return formConfigTypeService.queryForList(queryDTO);
    }

    /**
     * 编辑时查询类型列表（排除当前节点）
     */
    @PostMapping("/queryExcludeChild")
    public List<FormConfigTypeTreeNodeVO> queryExcludeChild(@RequestBody @Valid FormConfigTypeQueryDTO queryDTO) {
        return formConfigTypeService.queryExcludeChild(queryDTO);
    }

    /**
     * 根据条件查询企业的表单配置详情
     */
    @PostMapping("/")
    public List<FormConfigTypeTreeNodeVO> queryByCompany(@RequestBody @Valid FormConfigTypeQueryDTO queryDTO) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        queryDTO.setCompanyId(companyId);
        return formConfigTypeService.queryByCompany(queryDTO);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @LogRecord(operationCode = "createFormConfigType", operationName = "创建表单配置分类", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/create")
    public int create(@RequestBody @Valid FormConfigTypeCreateDTO FormConfigTypeCreateDTO) {
        return formConfigTypeService.saveFormConfigType(FormConfigTypeCreateDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateFormConfigType", operationName = "更新表单配置分类", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/update")
    public int update(@RequestBody @Valid FormConfigTypeUpdateDTO FormConfigTypeUpdateDTO) {
        return formConfigTypeService.updateFormConfigType(FormConfigTypeUpdateDTO);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteFormConfigType", operationName = "删除表单配置分类", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/delete")
    public int delete(@RequestBody @Valid FormConfigTypeQueryDTO queryDTO) {
        return formConfigTypeService.deleteFormConfigType(queryDTO);
    }

    /**
     * 根据表单分类查询表单配置列表
     */
    @PostMapping("/queryFormConfigByType")
    public PageResult<FormConfigVO> queryFormConfigByType(@RequestBody @Valid FormConfigPageQuery pageQuery) {
        return formConfigTypeService.queryFormConfigByType(pageQuery);
    }

}
