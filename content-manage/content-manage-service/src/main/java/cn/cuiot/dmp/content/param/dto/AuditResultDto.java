package cn.cuiot.dmp.content.param.dto;//	模板

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/31 15:49
 */
@Data
@Accessors(chain = true)
public class AuditResultDto {

    private Long id;

    /**
     * 审核结果
     */
    private Byte auditStatus;
}
