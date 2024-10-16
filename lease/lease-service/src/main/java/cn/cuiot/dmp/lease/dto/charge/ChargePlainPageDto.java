package cn.cuiot.dmp.lease.dto.charge;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.Date;

/**
 * @Description 自动生成计划分页对象
 * @Date 2024/6/20 10:45
 * @Created by libo
 */
@Data
public class ChargePlainPageDto implements ChargeItemNameSet {
    /**
     * id
     */
    @Excel(name = "生成计划编码", orderNum = "0", width = 20)
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
     * 收费项目名称
     */
    @Excel(name = "收费项目", orderNum = "2", width = 20)
    private String chargeItemName;

    /**
     * 收费标准 0自定义金额
     */
    private Long chargeStandard;

    /**
     * 应收金额/本金
     */
    private Integer receivableAmount;

    @Excel(name = "应收金额", orderNum = "3", width = 20)
    private String receivableAmountName;

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
    @Excel(name = "创建人", orderNum = "8", width = 20)
    private String createUserName;

    /**
     * 执行频率 0每月 1每日 2指定日期
     */
    private Byte cronType;

    @Excel(name = "执行频率", orderNum = "4", width = 20)
    private String cornTypeName;

    /**
     * 执行频率-指定日期-开始时间
     */
    @Excel(name = "执行开始时间", orderNum = "5", width = 20)
    private String cronBeginDate;

    /**
     * 执行频率-指定日期-结束时间
     */
    @Excel(name = "执行结束时间", orderNum = "6", width = 20)
    private String cronEndDate;

    /**
     * 执行频率-指定日期 1-31
     */
    @Excel(name = "执行日期", orderNum = "7", width = 20)
    private Integer cronAppointDate;

    /**
     * 执行频率-指定的小时分
     */
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private LocalTime cronTime;

    /**
     * 状态 0停用 1启用
     */
    private Byte status;

    @Excel(name = "状态", orderNum = "9", width = 20)
    private String statusName;
    /**
     * 收费对象
     */
    private Long receivableObj;

    /**
     * 收费对象名称
     */
    @Excel(name = "收费对象", orderNum = "1", width = 20)
    private String receivableObjName;

    /**
     * 应收日期
     */
    private Integer dueDateNum;

    public String getReceivableAmountName(){
        Double num = receivableAmount / 100.0;

        DecimalFormat decimalFormat = new DecimalFormat("#.00"); //定义格式，小数点后两位

        String formattedAmount = decimalFormat.format(num);
        return formattedAmount;
    }
}
