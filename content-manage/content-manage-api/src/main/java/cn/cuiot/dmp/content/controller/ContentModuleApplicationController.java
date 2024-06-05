package cn.cuiot.dmp.content.controller;//	模板

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.content.dal.entity.ContentModuleApplication;
import cn.cuiot.dmp.content.param.dto.ModuleApplicationCreateDto;
import cn.cuiot.dmp.content.param.dto.ModuleApplicationUpdateDto;
import cn.cuiot.dmp.content.param.query.ModuleApplicationPageQuery;
import cn.cuiot.dmp.content.service.ContentModuleApplicationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统配置-应用
 *
 * @author hantingyao
 * @Description
 * @data 2024/6/4 15:38
 */
@RestController
@RequestMapping("/moduleApplication")
public class ContentModuleApplicationController {

    @Autowired
    private ContentModuleApplicationService contentModuleApplicationService;


    /**
     * 创建
     *
     * @param moduleBannerCreateDto
     * @return
     */
    @PostMapping("/create")
    @RequiresPermissions
    @LogRecord(operationCode = "createModuleApplication", operationName = "新增应用", serviceType = ServiceTypeConst.CONTENT_MANAGE)
    public Boolean create(@RequestBody @Valid ModuleApplicationCreateDto moduleBannerCreateDto) {
        return contentModuleApplicationService.create(moduleBannerCreateDto);
    }


    /**
     * 更新
     */
    @RequiresPermissions
    @PostMapping("/update")
    @LogRecord(operationCode = "updateModuleApplication", operationName = "更新应用", serviceType = ServiceTypeConst.CONTENT_MANAGE)
    public Boolean update(@RequestBody @Valid ModuleApplicationUpdateDto updateDto) {
        return contentModuleApplicationService.update(updateDto);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @PostMapping("/delete")
    @LogRecord(operationCode = "deleteModuleApplication", operationName = "删除应用", serviceType = ServiceTypeConst.CONTENT_MANAGE)
    public Boolean deleteContentImgText(@RequestBody @Valid IdParam idParam) {
        return contentModuleApplicationService.deleteById(idParam.getId());
    }

    /**
     * 停启用
     *
     * @param updateStatusParam
     * @return
     */
    @PostMapping("/updateStatus")
    @RequiresPermissions
    @LogRecord(operationCode = "updateStatus", operationName = "停启用应用", serviceType = ServiceTypeConst.CONTENT_MANAGE)
    public Boolean updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        return contentModuleApplicationService.updateStatus(updateStatusParam);
    }

    /**
     * 分页查询
     *
     * @param pageQuery
     * @return
     */
    @PostMapping("/queryForPage")
    public IPage<ContentModuleApplication> queryForPage(@RequestBody @Valid ModuleApplicationPageQuery pageQuery) {
        return contentModuleApplicationService.queryForPage(pageQuery);
    }

    /**
     * 列表查询
     *
     * @param pageQuery
     * @return
     */
    @PostMapping("/queryForList")
    public List<ContentModuleApplication> queryForList(@RequestBody @Valid ModuleApplicationPageQuery pageQuery) {
        return contentModuleApplicationService.queryForList(pageQuery);
    }

}
