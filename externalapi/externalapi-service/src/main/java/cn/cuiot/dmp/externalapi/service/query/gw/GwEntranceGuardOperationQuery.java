package cn.cuiot.dmp.externalapi.service.query.gw;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 格物门禁-操作记录
 *
 * @Author: zc
 * @Date: 2024-09-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GwEntranceGuardOperationQuery extends PageQuery {

    /**
     * 门禁id
     */
    @NotNull(message = "门禁不能为空")
    private Long entranceGuardId;

    /**
     * 执行状态（0：失败，1：成功，2：执行中）
     */
    private Byte executionStatus;

    /**
     * 操作开始日期
     */
    private LocalDate operationBeginDate;

    /**
     * 操作结束日期
     */
    private LocalDate operationEndDate;

    /**
     * 操作类型（1：开门，2：重启）
     */
    @NotNull(message = "操作类型不能为空")
    private Byte type;

    /**
     * 企业ID（前端不用传）
     */
    private Long companyId;
}
