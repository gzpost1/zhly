package cn.cuiot.dmp.content.controller;//	模板

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.content.dal.entity.ContentModuleBanner;
import cn.cuiot.dmp.content.param.dto.ModuleBannerCreateDto;
import cn.cuiot.dmp.content.param.dto.ModuleBannerUpdateDto;
import cn.cuiot.dmp.content.param.query.ModuleBannerPageQuery;
import cn.cuiot.dmp.content.service.ContentModuleBannerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * banner管理
 * @author hantingyao
 * @Description
 * @data 2024/6/4 10:27
 */

@RestController
@RequestMapping("/moduleBanner")
public class ContentModuleBannerController {

    @Autowired
    private ContentModuleBannerService contentModuleBannerService;

    /**
     * 创建banner
     *
     * @param moduleBannerCreateDto
     * @return
     */
    @PostMapping("/create")
    @RequiresPermissions
    @LogRecord(operationCode = "createModuleBanner", operationName = "新增banner", serviceType = ServiceTypeConst.CONTENT_MANAGE)
    public Boolean create(@RequestBody @Valid ModuleBannerCreateDto moduleBannerCreateDto) {
        return contentModuleBannerService.create(moduleBannerCreateDto);
    }


    /**
     * 更新
     */
    @RequiresPermissions
    @PostMapping("/update")
    @LogRecord(operationCode = "updateModuleBanner", operationName = "更新banner", serviceType = ServiceTypeConst.CONTENT_MANAGE)
    public Boolean update(@RequestBody @Valid ModuleBannerUpdateDto updateDto) {
        return contentModuleBannerService.update(updateDto);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @PostMapping("/delete")
    @LogRecord(operationCode = "deleteModuleBanner", operationName = "删除banner", serviceType = ServiceTypeConst.CONTENT_MANAGE)
    public Boolean deleteContentImgText(@RequestBody @Valid IdParam idParam) {
        return contentModuleBannerService.deleteById(idParam.getId());
    }

    /**
     * 停启用
     *
     * @param updateStatusParam
     * @return
     */
    @PostMapping("/updateStatus")
    @LogRecord(operationCode = "updateStatus", operationName = "停启用banner", serviceType = ServiceTypeConst.CONTENT_MANAGE)
    @RequiresPermissions
    public Boolean updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        return contentModuleBannerService.updateStatus(updateStatusParam);
    }

    /**
     * 分页查询
     *
     * @param pageQuery
     * @return
     */
    @PostMapping("/queryForPage")
    public IPage<ContentModuleBanner> queryForPage(@RequestBody @Valid ModuleBannerPageQuery pageQuery) {
        return contentModuleBannerService.queryForPage(pageQuery);
    }

    /**
     * 列表查询
     *
     * @param pageQuery
     * @return
     */
    @PostMapping("/queryForList")
    public List<ContentModuleBanner> queryForList(@RequestBody @Valid ModuleBannerPageQuery pageQuery) {
        return contentModuleBannerService.queryForList(pageQuery);
    }
}
