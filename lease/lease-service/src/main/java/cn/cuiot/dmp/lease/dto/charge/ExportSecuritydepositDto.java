package cn.cuiot.dmp.lease.dto.charge;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * @author pengjian
 * @create 2024/10/16 10:45
 */
@Data
public class ExportSecuritydepositDto {

    @Excel(name = "押金应收编码", orderNum = "0", width = 20)
    private Long receivableId;


    /**
     * 房屋名称
     */
    @Excel(name = "房屋名称", orderNum = "1", width = 20)
    private String houseName;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称", orderNum = "3", width = 20)
    private String customerUserName;

    /**
     * 收费项目名称
     */
    @Excel(name = "收费项目", orderNum = "4", width = 20)
    private String chargeItemName;

    /**
     * 所属账期-开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "所属账期-开始时间",orderNum = "5",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "所属账期-结束时间",orderNum = "6",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date ownershipPeriodEnd;

    /**
     * 应收日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "应收日期",orderNum = "7",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date dueDate;



    /**
     * 应收金额/本金
     */
    private Integer receivableAmount;
    @Excel(name = "实收", orderNum = "8", width = 20)
    private String receivableAmountName;


    public String getReceivableAmountName(){
        Double num = receivableAmount / 100.0;

        DecimalFormat decimalFormat = new DecimalFormat("#.00"); //定义格式，小数点后两位

        String formattedAmount = decimalFormat.format(num);
        if(formattedAmount.startsWith(".")){
            return "0.00";
        }
        return formattedAmount;
    }

    /**
     * 本金实收
     */
    private Integer receivableAmountReceived;

    @Excel(name = "实收", orderNum = "9", width = 20)
    private String receivableAmountReceivedName;

    public String getReceivableAmountReceivedName(){
        Double num = receivableAmountReceived / 100.0;

        DecimalFormat decimalFormat = new DecimalFormat("#.00"); //定义格式，小数点后两位

        String formattedAmount = decimalFormat.format(num);
        if(formattedAmount.startsWith(".")){
            return "0.00";
        }
        return formattedAmount;
    }


    /**
     * 已退金额
     */
    private Integer returnedAmount;

    @Excel(name = "已退", orderNum = "10", width = 20)
    private String returnedAmountName;

    public String getReturnedAmountName(){
        Double num = returnedAmount / 100.0;

        DecimalFormat decimalFormat = new DecimalFormat("#.00"); //定义格式，小数点后两位

        String formattedAmount = decimalFormat.format(num);
        if(formattedAmount.startsWith(".")){
            return "0.00";
        }
        return formattedAmount;
    }

    /**
     * 未退金额
     */
    private Integer unreturnedAmount;

    @Excel(name = "未退", orderNum = "11", width = 20)
    private Integer unreturnedAmountName;

    public String getUnreturnedAmountName(){
        Double num = unreturnedAmount / 100.0;

        DecimalFormat decimalFormat = new DecimalFormat("#.00"); //定义格式，小数点后两位

        String formattedAmount = decimalFormat.format(num);
        if(formattedAmount.startsWith(".")){
            return "0.00";
        }
        return formattedAmount;
    }

    /**
     * 状态 0未交款 1已交清 2未退完 3 已退完 4作废
     */
    private Byte status;

    /**
     * 状态 0未交款 1已交清 2未退完 3 已退完 4作废
     */
    @Excel(name = "状态", orderNum = "12", width = 20)
    private String statusName;
}
