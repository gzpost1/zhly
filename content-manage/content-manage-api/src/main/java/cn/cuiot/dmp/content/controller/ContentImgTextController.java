package cn.cuiot.dmp.content.controller;//	模板

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.content.param.dto.ContentImgTextCreateDto;
import cn.cuiot.dmp.content.param.dto.ContentImgTextUpdateDto;
import cn.cuiot.dmp.content.param.query.ContentImgTextPageQuery;
import cn.cuiot.dmp.content.param.vo.ContentImgTextVo;
import cn.cuiot.dmp.content.service.ContentImgTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 图文管理
 *
 * @author hantingyao
 * @Description
 * @data 2024/5/28 14:50
 */
@RestController
@RequestMapping("/imgText")
public class ContentImgTextController extends BaseController {

    @Autowired
    private ContentImgTextService contentImgTextService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<ContentImgTextVo> queryForDetail(@RequestBody @Valid IdParam idParam) {
        ContentImgTextVo contentImgTextVo = contentImgTextService.queryForDetail(idParam.getId());
        return IdmResDTO.success(contentImgTextVo);
    }

    /**
     * 列表
     */
    @PostMapping("/queryForList")
    public List<ContentImgTextVo> queryForList(@RequestBody @Valid ContentImgTextPageQuery pageQuery) {
        String orgId = getOrgId();
        pageQuery.setCompanyId(Long.valueOf(orgId));
        return contentImgTextService.queryForList(pageQuery);
    }

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public PageResult<ContentImgTextVo> queryForPage(@RequestBody @Valid ContentImgTextPageQuery pageQuery) {
        String orgId = getOrgId();
        pageQuery.setCompanyId(Long.valueOf(orgId));
        return contentImgTextService.queryForPage(pageQuery);
    }

    /**
     * 保存
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveContentImgText", operationName = "保存图文", serviceType = ServiceTypeConst.CONTENT_MANAGE)
    @PostMapping("/save")
    public int saveContentImgText(@RequestBody @Valid ContentImgTextCreateDto createDTO) {
        return contentImgTextService.saveContentImgText(createDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateContentImgText", operationName = "更新图文", serviceType = ServiceTypeConst.CONTENT_MANAGE)
    @PostMapping("/update")
    public int updateContentImgText(@RequestBody @Valid ContentImgTextUpdateDto updateDTO) {
        return contentImgTextService.updateContentImgText(updateDTO);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteContentImgText", operationName = "删除图文", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/delete")
    public int deleteContentImgText(@RequestBody @Valid IdParam idParam) {
        return contentImgTextService.deleteContentImgText(idParam.getId());
    }
}
