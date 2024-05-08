package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.query.PageResult;
import cn.cuiot.dmp.system.application.param.dto.FormConfigTypeCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.FormConfigTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.FormConfigTypeUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeTreeNodeVO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeVO;
import cn.cuiot.dmp.system.application.service.FormConfigTypeService;
import cn.cuiot.dmp.system.domain.aggregate.FormConfig;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigPageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 企业账号-系统配置-初始化配置-表单配置
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
     * 编辑时查询类型列表（排除当前节点）
     */
    @RequiresPermissions
    @PostMapping("/queryExcludeChild")
    public List<FormConfigTypeTreeNodeVO> queryExcludeChild(@RequestBody @Valid FormConfigTypeQueryDTO queryDTO) {
        return formConfigTypeService.queryExcludeChild(queryDTO);
    }

    /**
     * 根据条件查询企业的表单配置详情
     */
    @RequiresPermissions
    @PostMapping("/queryByCompany")
    public List<FormConfigTypeTreeNodeVO> queryByCompany(@RequestBody @Valid FormConfigTypeQueryDTO queryDTO) {
        return formConfigTypeService.queryByCompany(queryDTO);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @PostMapping("/create")
    public int create(@RequestBody @Valid FormConfigTypeCreateDTO FormConfigTypeCreateDTO) {
        return formConfigTypeService.saveFormConfigType(FormConfigTypeCreateDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @PostMapping("/update")
    public int update(@RequestBody @Valid FormConfigTypeUpdateDTO FormConfigTypeUpdateDTO) {
        return formConfigTypeService.updateFormConfigType(FormConfigTypeUpdateDTO);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @PostMapping("/delete")
    public int delete(@RequestBody @Valid FormConfigTypeQueryDTO queryDTO) {
        return formConfigTypeService.deleteFormConfigType(queryDTO);
    }

    /**
     * 根据表单分类查询表单配置列表
     */
    @RequiresPermissions
    @PostMapping("/queryFormConfigByType")
    public PageResult<FormConfig> queryFormConfigByType(@RequestBody @Valid FormConfigPageQuery pageQuery) {
        return formConfigTypeService.queryFormConfigByType(pageQuery);
    }

}
