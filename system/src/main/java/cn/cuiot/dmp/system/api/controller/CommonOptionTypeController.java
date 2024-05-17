package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionTypeCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionTypeUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.CommonOptionTypeVO;
import cn.cuiot.dmp.system.application.param.vo.CommonOptionTypeTreeNodeVO;
import cn.cuiot.dmp.system.application.param.vo.CommonOptionVO;
import cn.cuiot.dmp.system.application.service.CommonOptionTypeService;
import cn.cuiot.dmp.system.domain.aggregate.CommonOptionPageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 企业账号-系统配置-初始化配置-常用选项分类
 *
 * @author caorui
 * @date 2024/4/28
 */
@RestController
@RequestMapping("/commonOptionType")
public class CommonOptionTypeController {

    @Autowired
    private CommonOptionTypeService commonOptionTypeService;

    /**
     * 根据id获取详情
     */
    @RequiresPermissions
    @PostMapping("/queryForDetail")
    public CommonOptionTypeVO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return commonOptionTypeService.queryForDetail(idParam.getId());
    }

    /**
     * 编辑时查询类型列表（排除当前节点）
     */
    @RequiresPermissions
    @PostMapping("/queryExcludeChild")
    public List<CommonOptionTypeTreeNodeVO> queryExcludeChild(@RequestBody @Valid CommonOptionTypeQueryDTO queryDTO) {
        return commonOptionTypeService.queryExcludeChild(queryDTO);
    }

    /**
     * 根据条件查询企业的表单配置详情
     */
    @RequiresPermissions
    @PostMapping("/queryByCompany")
    public List<CommonOptionTypeTreeNodeVO> queryByCompany(@RequestBody @Valid CommonOptionTypeQueryDTO queryDTO) {
        return commonOptionTypeService.queryByCompany(queryDTO);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @PostMapping("/create")
    public int create(@RequestBody @Valid CommonOptionTypeCreateDTO createDTO) {
        return commonOptionTypeService.saveCommonOptionType(createDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateCommonOptionType", operationName = "更新常用选项分类", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/update")
    public int update(@RequestBody @Valid CommonOptionTypeUpdateDTO updateDTO) {
        return commonOptionTypeService.updateCommonOptionType(updateDTO);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteCommonOptionType", operationName = "删除常用选项分类", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/delete")
    public int delete(@RequestBody @Valid CommonOptionTypeQueryDTO queryDTO) {
        return commonOptionTypeService.deleteCommonOptionType(queryDTO);
    }

    /**
     * 根据常用选项分类查询常用选项列表
     */
    @RequiresPermissions
    @PostMapping("/queryCommonOptionByType")
    public PageResult<CommonOptionVO> queryCommonOptionByType(@RequestBody @Valid CommonOptionPageQuery pageQuery) {
        return commonOptionTypeService.queryCommonOptionByType(pageQuery);
    }
    
}
