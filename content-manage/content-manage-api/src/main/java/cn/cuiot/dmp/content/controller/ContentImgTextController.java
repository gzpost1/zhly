package cn.cuiot.dmp.content.controller;//	模板

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.content.constant.ContentConstants;
import cn.cuiot.dmp.content.dal.entity.ContentAudit;
import cn.cuiot.dmp.content.param.dto.ContentImgTextCreateDto;
import cn.cuiot.dmp.content.param.dto.ContentImgTextUpdateDto;
import cn.cuiot.dmp.content.param.query.ContentImgTextPageQuery;
import cn.cuiot.dmp.content.param.vo.AuditStatusNumVo;
import cn.cuiot.dmp.content.param.vo.ImgTextVo;
import cn.cuiot.dmp.content.service.ContentAuditService;
import cn.cuiot.dmp.content.service.ContentImgTextService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private ContentAuditService contentAuditService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<ImgTextVo> queryForDetail(@RequestBody @Valid IdParam idParam) {
        ImgTextVo imgTextVo = contentImgTextService.queryForDetail(idParam.getId());
        if (!ContentConstants.AuditStatus.AUDIT_ING.equals(imgTextVo.getAuditStatus())) {
            ContentAudit lastAuditResult = contentAuditService.getLastAuditResult(imgTextVo.getId());
            imgTextVo.setContentAudit(lastAuditResult);
        }
        return IdmResDTO.success(imgTextVo);
    }

    /**
     * 列表
     */
    @PostMapping("/queryForList")
    public List<ImgTextVo> queryForList(@RequestBody @Valid ContentImgTextPageQuery pageQuery) {
        String orgId = getOrgId();
        pageQuery.setCompanyId(Long.valueOf(orgId));
        return contentImgTextService.queryForList(pageQuery);
    }

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public IPage<ImgTextVo> queryForPage(@RequestBody @Valid ContentImgTextPageQuery pageQuery) {
        String orgId = getOrgId();
        pageQuery.setCompanyId(Long.valueOf(orgId));
        return contentImgTextService.queryForPage(pageQuery);
    }

    /**
     * 保存
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveContentImgText", operationName = "保存图文", serviceType = "imgText",serviceTypeName = "图文管理")
    @PostMapping("/create")
    public int saveContentImgText(@RequestBody @Valid ContentImgTextCreateDto createDto) {
        createDto.setDepartments(Collections.singletonList(LoginInfoHolder.getCurrentDeptId()));
        return contentImgTextService.saveContentImgText(createDto);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateContentImgText", operationName = "更新图文", serviceType = "imgText",serviceTypeName = "图文管理")
    @PostMapping("/update")
    public int updateContentImgText(@RequestBody @Valid ContentImgTextUpdateDto updateDto) {
        updateDto.setDepartments(Collections.singletonList(LoginInfoHolder.getCurrentDeptId()));
        return contentImgTextService.updateContentImgText(updateDto);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteContentImgText", operationName = "删除图文", serviceType = ServiceTypeConst.ARCHIVE_CENTER)
    @PostMapping("/delete")
    public Boolean deleteContentImgText(@RequestBody @Valid IdParam idParam) {
        return contentImgTextService.removeById(idParam.getId());
    }

    /**
     * 停启用
     *
     * @param updateStatusParam
     * @return
     */
    @PostMapping("/updateStatus")
    @LogRecord(operationCode = "updateStatusImgText", operationName = "停启用图文", serviceType = "imgText",serviceTypeName = "图文管理")
    public Boolean updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        return contentImgTextService.updateStatus(updateStatusParam);
    }

    /**
     * 获取审核状态数量
     * @return
     */
    @GetMapping("/getAuditStatusNum")
    public List<AuditStatusNumVo> getAuditStatusNum(@RequestParam(value = "typeId",required = false) Long typeId) {
        return contentImgTextService.getAuditStatusNum(typeId);
    }
}
