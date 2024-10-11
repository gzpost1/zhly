package cn.cuiot.dmp.pay.service.service.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付服务标识
 *
 * @author huq
 * @ClassName PayChannelMark
 * @Date 2024/1/29 14:54
 **/
@Data
@NoArgsConstructor
public class PayChannelMark {
    /**
     * 标识
     */
    private String markPrefix;
    /**
     * 支付标识
     */
    private String payMark;
    /**
     * 分账标识
     */
    private String profitMark;
    /**
     * 补差标识
     */
    private String subsidyMark;
    /**
     * 进件标识
     */
    private String applymentMark;

    public PayChannelMark(String markPrefix) {
        this.markPrefix = markPrefix;
        this.payMark = markPrefix+"_pay";
        this.profitMark = markPrefix+"_profit";
        this.subsidyMark = markPrefix+"_subsidy";
        this.applymentMark = markPrefix+"_applyment";
    }
}
