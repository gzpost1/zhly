package cn.cuiot.dmp.pay.service.service.dto;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 扣缴记录
 * @author wuyongchong
 * @since 2023-11-29
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceChargeRecordQuery extends PageQuery {


    /**
     * 实收id
     */
    private Long receivedId;

    /**
     * 应收id
     */
    private Long receivableId;

    /**
     * 开始时间
     */
    private LocalDate beginDate;

    /**
     * 结束时间
     */
    private LocalDate endDate;


}
