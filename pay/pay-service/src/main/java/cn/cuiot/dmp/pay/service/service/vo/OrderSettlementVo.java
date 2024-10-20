package cn.cuiot.dmp.pay.service.service.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 结算报表
 */
@Data
public class OrderSettlementVo {




    /**
     * 应收编码
     */
    @Excel(name = "应收编码", orderNum = "0", width = 20)
    private Long receivableId;



    /**
     * 实收编码
     */
    @Excel(name = "实收编码", orderNum = "1", width = 20)
    private Long paidUpId;

    /**
     * 交易单号
     */
    @Excel(name = "交易单号", orderNum = "2", width = 20)
    private String transactionNo;

    @Excel(name = "所属楼盘", orderNum = "3", width = 20)
    private String loupanName;


    /**
     * 房屋名称
     */
    @Excel(name = "房屋名称", orderNum = "4", width = 20)
    private String houseName;

    /**
     * 房屋id
     */
    @Excel(name = "房屋id", orderNum = "5", width = 20)
    private Long houseId;

    /**
     * 收支类型 0收入 1支出
     */
    private Byte incomeType;

    @Excel(name = "收支类型", orderNum = "6", width = 20)
    private String incomeTypeName;

    public String getIncomeTypeName() {
        if(Objects.equals(incomeType,(byte)0)){
            return "收入";
        }else if(Objects.equals(incomeType,(byte)1)){
            return "支出";
        }
        return null;
    }

    /**
     * 交易渠道 0平台 1人工
     */
    private Byte paymentMode;

    @Excel(name = "交易渠道", orderNum = "7", width = 20)
    private String paymentModeName;


    public String getPaymentModeName() {
        if(Objects.equals(paymentMode,(byte)0)){
            return "平台";
        }else if(Objects.equals(paymentMode,(byte)1)){
            return "人工";
        }
        return null;
    }

    /**
     * 收费项目名称
     */
    @Excel(name = "收费项目", orderNum = "8", width = 20)
    private String chargeItemName;

    /**
     * 支出项目 0支付手续费
     */
    @Excel(name = "支出项目", orderNum = "9", width = 20)
    private Byte expenditureType;

    /**
     * 交易方式名称
     */
    @Excel(name = "交易方式名称", orderNum = "10", width = 20)
    private String transactionModeName;

    /**
     * 金额
     */
    private Integer payAmount;

    /**
     * 金额
     */
    @Excel(name = "金额", orderNum = "11", width = 20)
    private double payAmountYuan;

    private double getPayAmountYuan(){
        if(Objects.isNull(payAmount)){
            return 0;
        }
        return BigDecimal.valueOf(payAmount).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 结算时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date settlementTime;


    /**
     * 结算时间
     */
    @Excel(name = "结算时间", orderNum = "12", width = 20)
    private String settlementTimeStr;

    public String getSettlementTimeStr(){
        if(Objects.isNull(settlementTime)){
            return null;
        }
        return DateTimeUtil.dateToString(settlementTime,"yyyy-MM-dd HH:mm:ss");
    }

}