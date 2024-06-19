package cn.cuiot.dmp.content.controller;//	模板

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.content.dal.entity.ContentModule;
import cn.cuiot.dmp.content.service.ContentModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序配置
 *
 * @author hantingyao
 * @Description
 * @data 2024/6/3 17:45
 */

@RestController
@RequestMapping("/applicationConfig")
public class ContentModuleController extends BaseController {

    @Autowired
    private ContentModuleService contentModuleService;

    /**
     * 查询模块列表
     *
     * @param systemModule
     * @return
     */
    @GetMapping("/queryForList")
    public List<ContentModule> queryForList(@RequestParam String systemModule) {
        return contentModuleService.queryForList(getOrgId(), systemModule);
    }

    /**
     * 更新显示状态
     *
     * @param statusParam
     * @return
     */
    @PostMapping("/updateShow")
    @RequiresPermissions
    @LogRecord(operationCode = "updateShow", operationName = "更新显示状态", serviceType = "contentModule", serviceTypeName = "模块管理")
    public Boolean updateShow(@RequestBody UpdateStatusParam statusParam) {
        return contentModuleService.updateShow(statusParam);
    }

    /**
     * 更新模块
     * @param contentModule
     * @return
     */
    @PostMapping("/update")
    @RequiresPermissions
    @LogRecord(operationCode = "updateContentModule", operationName = "更新模块", serviceType = "contentModule", serviceTypeName = "模块管理")
    public Boolean updateContentModule(@RequestBody ContentModule contentModule) {
        return contentModuleService.update(contentModule);
    }
}
