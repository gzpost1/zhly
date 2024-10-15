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
public class BalanceChangeRecordSysVo extends BalanceChangeRecord {

    /**
     * 房屋名称
     */
    private String houseName;


    /**
     * 操作人名称
     */
    private String createName;
}
