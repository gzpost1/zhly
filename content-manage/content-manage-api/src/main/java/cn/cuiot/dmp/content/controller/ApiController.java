package cn.cuiot.dmp.content.controller;//	模板

import cn.cuiot.dmp.base.application.annotation.InternalApi;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.content.constant.ContentConstants;
import cn.cuiot.dmp.content.dal.entity.ContentAudit;
import cn.cuiot.dmp.content.param.req.NoticeStatisInfoReqVo;
import cn.cuiot.dmp.content.param.vo.ContentNoticeVo;
import cn.cuiot.dmp.content.param.vo.NoticeVo;
import cn.cuiot.dmp.content.service.ContentAuditService;
import cn.cuiot.dmp.content.service.ContentModuleService;
import cn.cuiot.dmp.content.service.ContentModuleSyncService;
import cn.cuiot.dmp.content.service.NoticeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/19 11:40
 */
@RequestMapping("/api")
@InternalApi
@RestController
public class ApiController {

    @Autowired
    private ContentModuleService contentModuleService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private ContentAuditService contentAuditService;
    @Autowired
    private ContentModuleSyncService contentModuleSyncService;


    /**
     * 初始化模块
     *
     * @param orgId
     * @return
     */
    @PostMapping("/initModule")
    public Boolean initModule(@RequestParam("orgId") Long orgId) {
        contentModuleService.initModule(orgId);
        return true;
    }

    /**
     * 公告统计
     * @param dto NoticeStatisInfoReqDTO
     * @return ContentNoticeResDTO
     */
    @PostMapping("/queryContentNotice")
    IdmResDTO<IPage<ContentNoticeVo>> queryContentNotice(@RequestBody NoticeStatisInfoReqVo dto){
        return IdmResDTO.success(noticeService.queryContentNoticeStatistic(dto));
    }


    /**
     * 根据id获取详情
     */
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
     * 用于企业初始化同步小程序配置
     */
    @PostMapping(value = "/syncData", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<?> syncData(@RequestBody @Valid SyncCompanyDTO dto) {
        contentModuleSyncService.syncData(dto);
        return IdmResDTO.success();
    }

    /**
     * 用于清空企业初始化同步小程序配置
     */
    @PostMapping(value = "/cleanSyncData", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO<?> cleanSyncData(@RequestBody @Valid SyncCompanyDTO dto) {
        contentModuleSyncService.cleanSyncData(dto);
        return IdmResDTO.success();
    }
}
