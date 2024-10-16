package cn.cuiot.dmp.lease.dto.charge;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description app账单
 * @Date 2024/10/10 17:26
 * @Created by libo
 */
@Data
public class AppChargeManagerDto implements ChargeItemNameSet{
    /**
     * 应收编码
     */
    private Long id;

    /**
     * 房屋ID
     */
    private Long houseId;

    /**
     * 收费项目id
     */
    private Long chargeItemId;

    /**
     * 收费项目名称
     */
    private String chargeItemName;

    /**
     * 所属账期-开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodEnd;

    /**
     * 欠收合计 应收金额-本金实收=欠收合计
     */
    private Integer totalOwe = 0;

    /**
     * 缴费时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastPayTime;

    /**
     * 应收金额/本金
     */
    private Integer receivableAmount;

    /**
     * 本金实收
     */
    private Integer receivableAmountReceived = 0;



    public Integer getTotalOwe() {
        return receivableAmount - receivableAmountReceived;
    }

    public void setTotalOwe(Integer totalOwe) {
        this.totalOwe = totalOwe;
    }
}
