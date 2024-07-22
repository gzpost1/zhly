package cn.cuiot.dmp.content.controller.app;//	模板

import cn.cuiot.dmp.base.application.annotation.ResolveExtData;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.content.constant.ContentConstants;
import cn.cuiot.dmp.content.param.query.NoticPageQuery;
import cn.cuiot.dmp.content.param.vo.NoticeVo;
import cn.cuiot.dmp.content.service.NoticeService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.domain.types.enums.UserTypeEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

/**
 * app-公告
 *
 * @author hantingyao
 * @Description
 * @data 2024/6/7 15:41
 */
@RestController
@RequestMapping("/app/notice")
@ResolveExtData
public class AppNoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 获取公告列表
     *
     * @return
     */
    @PostMapping("/getNoticeList")
    public IPage<NoticeVo> getAppNoticePage(@RequestBody @Valid NoticPageQuery pageQuery) {
        if (ContentConstants.PublishSource.MANAGE.equals(pageQuery.getPublishSource())) {
            pageQuery.setDepartments(Collections.singletonList(LoginInfoHolder.getCurrentDeptId()));
        } else if (ContentConstants.PublishSource.APP.equals(pageQuery.getPublishSource())) {
            pageQuery.setBuildings(Collections.singletonList(LoginInfoHolder.getCommunityId()));
        }
        pageQuery.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        return noticeService.getAppNoticePage(pageQuery);
    }


    /**
     * 获取公告详情
     *
     * @param idParam
     * @return
     */
    @PostMapping("/getNoticeDetail")
    public NoticeVo getNoticeDetail(@RequestBody @Valid IdParam idParam) {
        return noticeService.queryForAppDetail(idParam.getId());
    }

    /**
     * 获取本人可看的公告
     *
     * @return
     */
    @GetMapping("/getMyNotice")
    public void getMyNotice() {
        if (!UserTypeEnum.OWNER.getValue().equals(LoginInfoHolder.getCurrentUserType())) {
            return;
        }
        if (LoginInfoHolder.getCommunityId() == null) {
            return;
        }
        noticeService.getMyNotice(LoginInfoHolder.getCommunityId());
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
}
