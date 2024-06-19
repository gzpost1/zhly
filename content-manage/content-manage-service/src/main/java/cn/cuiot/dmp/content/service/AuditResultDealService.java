package cn.cuiot.dmp.content.service;//	模板

import cn.cuiot.dmp.content.param.dto.AuditResultDto;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/31 15:48
 */
public interface AuditResultDealService {

    Boolean dealAuditResult(AuditResultDto auditResultDto);
}
