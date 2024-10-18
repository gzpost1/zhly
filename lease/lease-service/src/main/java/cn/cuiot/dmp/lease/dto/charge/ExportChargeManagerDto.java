package cn.cuiot.dmp.lease.dto.charge;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * @author pengjian
 * @create 2024/10/16 10:05
 */
@Data
public class ExportChargeManagerDto {

    /**
     * 应收编码
     */
    @Excel(name = "应收编码", orderNum = "0", width = 20)
    private Long id;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称", orderNum = "1", width = 20)
    private String customerUserName;

    /**
     * 手机号
     */
    @Excel(name = "手机号", orderNum = "2", width = 20)
    private String customerUserPhone;

    /**
     * 客户身份
     */
    @Excel(name = "客户身份", orderNum = "3", width = 20)
    private String customerUserRoleName;


    /**
     * 所属类型 0手动创建  1自动生成
     */
    private Byte createType;

    /**
     * 所属名称 手动创建  自动生成
     */
    @Excel(name = "所属类型", orderNum = "4", width = 20)
    private String createTypeName;

    /**
     * 自动生成计划编码
     */
    @Excel(name = "自动生成计划编码", orderNum = "5", width = 20)
    private Long receivblePlanId;

    /**
     * 收费项目名称
     */
    @Excel(name = "收费项目", orderNum = "6", width = 20)
    private String chargeItemName;

    /**
     * 应收金额
     */
    private Integer receivableAmount;

    /**
     * 应收金额/本金
     */
    @Excel(name = "应收金额", orderNum = "7", width = 20)
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
     * 所属账期-开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "所属账期-开始时间",orderNum = "8",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "所属账期-结束时间",orderNum = "9",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date ownershipPeriodEnd;

    /**
     * 应收日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "应收日期",orderNum = "10",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date dueDate;
    /**
     * 应收状态 0未交款 1已交款 2已交清
     */
    private Byte receivbleStatus;

    /**
     * 应收状态名称 未交款 已交款 已交清
     */
    @Excel(name = "应收状态", orderNum = "11", width = 20)
    private String receivbleStatusName;



    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "创建时间",orderNum = "12",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 挂起状态 0未挂起 1已挂起
     */
    private Byte hangUpStatus;

    @Excel(name = "挂起状态", orderNum = "13", width = 20)
    private String hangUpStatusName;
}
