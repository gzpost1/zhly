package cn.cuiot.dmp.pay.service.service.vo;

import cn.cuiot.dmp.pay.service.service.entity.BalanceChangeRecord;
import lombok.Data;

/**
 * <p>
 * 余额明细变动表
 * </p>
 *
 * @author wuyongchong
 * @since 2023-11-29
 */
@Data
public class BalanceChangeRecordVo extends BalanceChangeRecord {

    /**
     * 应收id
     */
    private Long receivableId;

    /**
     * 实收id
     */
    private Long receivedId;
}
