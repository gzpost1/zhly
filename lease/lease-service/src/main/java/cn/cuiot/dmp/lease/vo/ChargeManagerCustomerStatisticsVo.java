package cn.cuiot.dmp.lease.vo;

import lombok.Data;

/**
 * 收费管理-应收管理-客户统计
 *
 * @author zc
 */
@Data
public class ChargeManagerCustomerStatisticsVo {

    /**
     * 应收金额/本金
     */
    private Integer receivableAmount = 0;

    /**
     * 本金实收
     */
    private Integer receivableAmountReceived = 0;

    /**
     * 欠收合计 应收金额-本金实收=欠收合计
     */
    private Integer totalOwe = 0;

    public Integer getTotalOwe() {
        return receivableAmount - receivableAmountReceived;
    }

    public void setTotalOwe(Integer totalOwe) {
        this.totalOwe = totalOwe;
    }
}