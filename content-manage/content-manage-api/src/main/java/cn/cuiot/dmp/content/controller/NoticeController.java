package cn.cuiot.dmp.content.controller;//	模板

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.content.constant.ContentConstants;
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
    @RequiresPermissions
    @PostMapping("/queryForDetail")
    public IdmResDTO<NoticeVo> queryForDetail(@RequestBody @Valid IdParam idParam) {
        NoticeVo noticeVo = noticeService.queryForDetail(idParam.getId());
        if (!ContentConstants.AuditStatus.AUDIT_ING.equals(noticeVo.getAuditStatus())) {
            ContentAudit lastAuditResult = contentAuditService.getLastAuditResult(noticeVo.getId());
            noticeVo.setContentAudit(lastAuditResult);
        }
        return IdmResDTO.success(noticeVo);
    }

    /**
     * 列表
     */
    @RequiresPermissions
    @PostMapping("/queryForList")
    public List<NoticeVo> queryForList(@RequestBody @Valid NoticPageQuery pageQuery) {
        String orgId = getOrgId();
        pageQuery.setCompanyId(Long.valueOf(orgId));
        return noticeService.queryForList(pageQuery);
    }

    /**
     * 分页列表
     */
    @RequiresPermissions
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
    @LogRecord(operationCode = "saveNotice", operationName = "保存公告", serviceType = "notice", serviceTypeName = "公告管理")
    @PostMapping("/create")
    public int saveNotice(@RequestBody @Valid NoticeCreateDto createDTO) {
        String orgId = getOrgId();
        createDTO.setCompanyId(Long.valueOf(orgId));
        return noticeService.saveNotice(createDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateNotice", operationName = "更新公告", serviceType = "notice", serviceTypeName = "公告管理")
    @PostMapping("/update")
    public int updateNotice(@RequestBody @Valid NoticeUpdateDto updateDtO) {
        return noticeService.updateNotice(updateDtO);
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteNotice", operationName = "删除公告", serviceType = "notice", serviceTypeName = "公告管理")
    @PostMapping("/delete")
    public Boolean deleteNotice(@RequestBody @Valid IdParam idParam) {
        return noticeService.removeById(idParam.getId());
    }

    /**
     * 发布公告
     *
     * @param publishReqVo
     * @return
     */
    @RequiresPermissions
    @LogRecord(operationCode = "publishNotice", operationName = "发布公告", serviceType = "notice", serviceTypeName = "公告管理")
    @PostMapping("/publish")
    public Boolean publish(@RequestBody @Valid PublishReqVo publishReqVo) {
        return noticeService.publish(publishReqVo);
    }


    /**
     * 停启用
     *
     * @param updateStatusParam
     * @return
     */
    @PostMapping("/updateStatus")
    public Boolean updateStatus(@RequestBody UpdateStatusParam updateStatusParam) {
        ContentNoticeEntity contentNoticeEntity = noticeService.getById(updateStatusParam.getId());
        if (contentNoticeEntity == null) {
            return false;
        }
        contentNoticeEntity.setStatus(updateStatusParam.getStatus());
        return noticeService.updateById(contentNoticeEntity);
    }

    /**
     * 获取公告列表
     *
     * @return
     */
    @PostMapping("/getNoticeList")
    public IPage<NoticeVo> getAppNoticePage(@RequestBody @Valid NoticPageQuery pageQuery) {
        return noticeService.getAppNoticePage(pageQuery);
    }

    /**
     * 消息里通过id查详情
     *
     * @param idParam
     * @return
     */
    @PostMapping("getByIdMsg")
    public IdmResDTO<NoticeVo> getByIdMsg(@RequestBody @Valid IdParam idParam) {
        NoticeVo noticeVo = noticeService.queryForDetail(idParam.getId());
        if (!EntityConstants.NORMAL.equals(noticeVo.getEffectiveStatus())) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        if (!EntityConstants.ENABLED.equals(noticeVo.getStatus())) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        if (!ContentConstants.AuditStatus.AUDIT_PASSED.equals(noticeVo.getAuditStatus())) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        if (!ContentConstants.PublishStatus.PUBLISHED.equals(noticeVo.getPublishStatus())) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        return IdmResDTO.success(noticeVo);
    }

    /**
     * 导出
     *
     * @param pageQuery
     * @return
     * @throws Exception
     */
    @PostMapping("export")
    public IdmResDTO export(@RequestBody @Valid NoticPageQuery pageQuery) {
        noticeService.export(pageQuery);
        return IdmResDTO.success();
    }

}
