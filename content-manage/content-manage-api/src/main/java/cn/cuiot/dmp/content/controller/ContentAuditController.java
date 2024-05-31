package cn.cuiot.dmp.content.controller;//	模板

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.content.constant.ContentConstants;
import cn.cuiot.dmp.content.param.req.AuditReqVo;
import cn.cuiot.dmp.content.service.ContentAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/31 15:27
 */
@RestController
@RequestMapping("/contentAudit")
public class ContentAuditController {

    @Autowired
    private ContentAuditService contentAuditService;

    /**
     * 审核
     *
     * @param auditReqVo
     * @return
     */
    @RequiresPermissions
    @PostMapping("/auditApply")
    public Boolean audit(@RequestBody @Valid AuditReqVo auditReqVo) {
        if (auditReqVo.getAuditStatus().equals(ContentConstants.AuditStatus.NOT_PASSED) && auditReqVo.getAuditOpinion() == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "审核意见不能为空");
        }
        return contentAuditService.audit(auditReqVo);
    }
}
