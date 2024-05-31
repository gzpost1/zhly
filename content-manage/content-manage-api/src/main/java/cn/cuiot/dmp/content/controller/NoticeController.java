package cn.cuiot.dmp.content.controller;//	模板

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.content.constant.ContentConstants;
import cn.cuiot.dmp.content.conver.NoticeConvert;
import cn.cuiot.dmp.content.dal.entity.ContentAudit;
import cn.cuiot.dmp.content.dal.entity.ContentNoticeEntity;
import cn.cuiot.dmp.content.param.dto.NoticeCreateDto;
import cn.cuiot.dmp.content.param.dto.NoticeUpdateDto;
import cn.cuiot.dmp.content.param.query.NoticPageQuery;
import cn.cuiot.dmp.content.param.req.PublishReqVo;
import cn.cuiot.dmp.content.param.vo.NoticeVo;
import cn.cuiot.dmp.content.service.ContentAuditService;
import cn.cuiot.dmp.content.service.NoticeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 公告管理
 *
 * @author hantingyao
 * @Description
 * @data 2024/5/28 14:52
 */
@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController {

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private ContentAuditService contentAuditService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<NoticeVo> queryForDetail(@RequestBody @Valid IdParam idParam) {
        ContentNoticeEntity contentNoticeEntity = noticeService.getById(idParam.getId());
        NoticeVo noticeVo = NoticeConvert.INSTANCE.convert(contentNoticeEntity);
        if (!ContentConstants.AuditStatus.AUDIT_ING.equals(contentNoticeEntity.getAuditStatus())) {
            ContentAudit lastAuditResult = contentAuditService.getLastAuditResult(contentNoticeEntity.getId());
            noticeVo.setContentAudit(lastAuditResult);
        }
        return IdmResDTO.success(noticeVo);
    }

    /**
     * 列表
     */
    @PostMapping("/queryForList")
    public List<NoticeVo> queryForList(@RequestBody @Valid NoticPageQuery pageQuery) {
        String orgId = getOrgId();
        pageQuery.setCompanyId(Long.valueOf(orgId));
        return noticeService.queryForList(pageQuery);
    }

    /**
     * 分页列表
     */
    @PostMapping("/queryForPage")
    public IPage<NoticeVo> queryForPage(@RequestBody @Valid NoticPageQuery pageQuery) {
        String orgId = getOrgId();
        pageQuery.setCompanyId(Long.valueOf(orgId));
        return noticeService.queryForPage(pageQuery);
    }

    /**
     * 保存
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveNotice", operationName = "保存公告", serviceType = ServiceTypeConst.CONTENT_MANAGE)
    @PostMapping("/save")
    public int saveNotice(@RequestBody @Valid NoticeCreateDto createDTO) {
        String orgId = getOrgId();
        createDTO.setCompanyId(Long.valueOf(orgId));
        return noticeService.saveNotice(createDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateNotice", operationName = "更新公告", serviceType = ServiceTypeConst.CONTENT_MANAGE)
    @PostMapping("/update")
    public int updateContentImgText(@RequestBody @Valid NoticeUpdateDto updateDtO) {
        return noticeService.updateNotice(updateDtO);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteNotice", operationName = "删除公告", serviceType = ServiceTypeConst.CONTENT_MANAGE)
    @PostMapping("/delete")
    public Boolean deleteContentImgText(@RequestBody @Valid IdParam idParam) {
        return noticeService.removeById(idParam.getId());
    }

    @RequiresPermissions
    @LogRecord(operationCode = "publishNotice", operationName = "发布公告", serviceType = ServiceTypeConst.CONTENT_MANAGE)
    @PostMapping("/publish")
    public Boolean publish(@RequestBody @Valid PublishReqVo publishReqVo) {
        return noticeService.publish(publishReqVo);
    }

}
