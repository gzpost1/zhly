package cn.cuiot.dmp.lease.dto.charge;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * @Description 自动生成计划
 * @Date 2024/6/20 10:24
 * @Created by libo
 */
@Data
public class ChargePlainInsertDto {

    /**
     * 本金税率
     */
    private BigDecimal receivableAmountRate;

    /**
     * 收费项目id
     */
    private Long chargeItemId;

    /**
     * 收费标准 0自定义金额
     */
    private Long chargeStandard;

    /**
     * 应收金额/本金
     */
    private Integer receivableAmount;

    /**
     * 执行频率 0每月 1每日 2指定日期
     */
    private Byte cronType;

    /**
     * 执行频率-指定日期-开始时间
     */
    private String cronBeginDate;

    /**
     * 执行频率-指定日期-结束时间
     */
    private String cronEndDate;

    /**
     * 执行频率-指定日期 1-31
     */
    private Integer cronAppointDate;

    /**
     * 执行频率-指定的小时分
     */
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private LocalTime cronTime;

    /**
     * 收费对象
     */
    private Long receivableObj;

    /**
     * 应收日期
     */
    private Integer dueDateNum;
}
