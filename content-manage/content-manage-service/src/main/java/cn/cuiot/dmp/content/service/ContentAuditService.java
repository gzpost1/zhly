package cn.cuiot.dmp.content.service;//	模板

import cn.cuiot.dmp.content.dal.entity.ContentAudit;
import cn.cuiot.dmp.content.param.req.AuditReqVo;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/31 15:24
 */
public interface ContentAuditService {
    /**
     * 数据审核
     *
     * @param auditReqVo
     * @return
     */
    Boolean audit(AuditReqVo auditReqVo);

    /**
     * 查询最新的审核记录
     * @param id
     * @return
     */
    ContentAudit getLastAuditResult(Long id);
}
