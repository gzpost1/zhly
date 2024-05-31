package cn.cuiot.dmp.content.param.req;//	模板

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/31 14:55
 */
@Data
public class AuditReqVo {

    @NotNull(message = "数据id不能为空")
    private Long dataId;

    /**
     * 审核结果
     */
    @NotNull(message = "审核结果不能为空")
    private Byte auditStatus;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 数据类型
     */
    @NotNull(message = "数据类型不能为空")
    private Byte dataType;
}
