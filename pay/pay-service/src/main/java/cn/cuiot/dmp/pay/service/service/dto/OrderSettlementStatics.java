package cn.cuiot.dmp.pay.service.service.dto;

import lombok.Data;

/**
 * @Description 交易报表总计
 * @Date 2024/10/15 16:34
 * @Created by libo
 */
@Data
public class OrderSettlementStatics {
    /**
     * 收入合计
     */
    private Integer incomeTotal;

    /**
     * 支出合计
     */
    private Integer expenditureTotal;
}
