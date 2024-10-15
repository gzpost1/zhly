package cn.cuiot.dmp.pay.service.service.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

/**
 * 扣缴记录
 * @author wuyongchong
 * @since 2023-11-29
 */
@Getter
@Setter
public class BalanceChargeRecordVO  {


    /**
     * 实收id
     */
    private Long receivedId;

    /**
     * 应收id
     */
    private Long receivableId;

    /**
     * 房屋id
     */
    private Long houseId;

    /**
     * 房屋名称
     */
    private String houseName;

    /**
     * 收费项目
     */
    private Long chargeItemId;

    /**
     * 收费项目
     */
    private String chargeItemName;

    /**
     * 收费标准
     */
    private Long chargeStandard;

    /**
     * 收费标准
     */
    private String chargeStandardName;



    /**
     * 账期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodBegin;

    /**
     * 账期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodEnd;

    /**
     * 扣缴金额
     */
    private Integer balance;

    /**
     * 扣费时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Integer createTime;

}
