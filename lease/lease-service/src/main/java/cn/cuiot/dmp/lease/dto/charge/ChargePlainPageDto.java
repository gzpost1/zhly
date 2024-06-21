package cn.cuiot.dmp.lease.dto.charge;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

/**
 * @Description 自动生成计划分页对象
 * @Date 2024/6/20 10:45
 * @Created by libo
 */
@Data
public class ChargePlainPageDto {
    /**
     * id
     */
    private Long id;

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
    private Byte chargeStandard;

    /**
     * 应收金额/本金
     */
    private Integer receivableAmount;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 执行频率 0每月 1每日 2指定日期
     */
    private Byte cronType;

    /**
     * 执行频率-指定日期-开始时间
     */
    private Integer cronBeginDate;

    /**
     * 执行频率-指定日期-结束时间
     */
    private Integer cronEndDate;

    /**
     * 执行频率-指定日期 1-31
     */
    private Integer cronAppointDate;

    /**
     * 执行频率-指定的小时分
     */
    private LocalTime cronTime;

    /**
     * 状态 0停用 1启用
     */
    private Byte status;
}
