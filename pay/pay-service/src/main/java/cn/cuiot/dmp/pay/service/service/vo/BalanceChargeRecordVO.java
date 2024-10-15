package cn.cuiot.dmp.pay.service.service.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "实收编码", orderNum = "0", width = 20)
    private Long receivedId;

    /**
     * 应收id
     */
    @Excel(name = "应收编码", orderNum = "1", width = 20)
    private Long receivableId;

    /**
     * 房屋id
     */
    private Long houseId;

    /**
     * 房屋名称
     */
    @Excel(name = "房屋名称", orderNum = "2", width = 20)
    private String houseName;

    /**
     * 收费项目
     */
    private Long chargeItemId;

    /**
     * 收费项目
     */
    @Excel(name = "收费项目", orderNum = "3", width = 20)
    private String chargeItemName;

    /**
     * 收费标准
     */
    private Long chargeStandard;

    /**
     * 收费标准
     */
    @Excel(name = "收费标准", orderNum = "4", width = 20)
    private String chargeStandardName;



    /**
     * 账期
     */
    @Excel(name = "账期开始时间", orderNum = "5", width = 20)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodBegin;

    /**
     * 账期
     */
    @Excel(name = "账期结束时间", orderNum = "6", width = 20)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodEnd;

    /**
     * 扣缴金额
     */
    @Excel(name = "扣缴金额", orderNum = "7", width = 20)
    private Integer balance;

    /**
     * 扣费时间
     */
    @Excel(name = "扣费时间", orderNum = "8", width = 20)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Integer createTime;

}
