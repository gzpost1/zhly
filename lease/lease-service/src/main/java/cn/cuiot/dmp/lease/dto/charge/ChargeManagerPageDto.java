package cn.cuiot.dmp.lease.dto.charge;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.cuiot.dmp.base.infrastructure.utils.MathTool;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @Description 收费管理-收银台-缴费管理分页详情
 * @Date 2024/6/12 17:28
 * @Created by libo
 */
@Data
public class ChargeManagerPageDto implements ChargeItemNameSet {
    /**
     * 应收编码
     */
    @Excel(name = "应收编码", orderNum = "0", width = 20)
    private Long id;

    /**
     * 房屋ID
     */
    private Long houseId;

    /**
     * 房屋名称
     */
    @Excel(name = "房屋名称", orderNum = "3", width = 20)
    private String houseName;

    /**
     * 房屋编号
     */
    @Excel(name = "房屋编码", orderNum = "4", width = 20)
    private String houseCode;

    /**
     * 客户id
     */
    private Long customerUserId;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称", orderNum = "5", width = 20)
    private String customerUserName;

    /**
     * 客户手机号
     */
    private String customerUserPhone;

    /**
     * 客户身份
     */
    @Excel(name = "客户身份", orderNum = "6", width = 20)
    private String customerUserRoleName;

    /**
     * 所属类型 0手动创建  1自动生成
     */
    private Byte createType;

    /**
     * 所属名称 手动创建  自动生成
     */
    @Excel(name = "所属类型", orderNum = "1", width = 20)
    private String createTypeName;

    /**
     * 自动生成计划编码
     */
    @Excel(name = "自动生成计划编码", orderNum = "2", width = 20)
    private Long receivblePlanId;

    /**
     * 收费项目id
     */
    private Long chargeItemId;

    /**
     * 收费项目名称
     */
    @Excel(name = "收费项目", orderNum = "7", width = 20)
    private String chargeItemName;

    /**
     * 应收金额/本金
     */
    private Integer receivableAmount;
    @Excel(name = "应收金额", orderNum = "8", width = 20)
    private String receivableAmountName;


    /**
     * 所属账期-开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "所属账期-开始时间",orderNum = "9",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "所属账期-结束时间",orderNum = "10",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date ownershipPeriodEnd;

    /**
     * 应收日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "应收日期",orderNum = "11",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date dueDate;

    /**
     * 应收状态 0未交款 1已交款 2已交清
     */
    private Byte receivbleStatus;

    /**
     * 应收状态名称 未交款 已交款 已交清
     */
    @Excel(name = "应收状态", orderNum = "12", width = 20)
    private String receivbleStatusName;

    /**
     * 挂起状态 0未挂起 1已挂起
     */
    private Byte hangUpStatus;

    @Excel(name = "挂起状态", orderNum = "14", width = 20)
    private String hangUpStatusName;

    /**
     * 作废状态 0正常 1已作废
     */
    private Byte abrogateStatus;

    /**
     * 状态
     */
    @Excel(name = "状态", orderNum = "15", width = 20)
    private String abrogateStatusName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "创建时间",orderNum = "13",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 本金实收
     */
    private Integer receivableAmountReceived = 0;

    /**
     * 欠收合计 应收金额-本金实收=欠收合计
     */
    private Integer totalOwe = 0;

    /**
     * 违约金税额 单次税额：违约金*税率=违约金税额，计算累计的违约金税额
     */
    private Integer liquidatedDamagesTax = 0;


    /**
     * 本金税率
     */
    private BigDecimal receivableAmountRate;

    /**
     * 本金税额	应收金额*税率=本金税额
     */
    private Integer receivableAmountTax = 0;

    public Integer getTotalOwe() {
        return receivableAmount - receivableAmountReceived;
    }

    public void setTotalOwe(Integer totalOwe) {
        this.totalOwe = totalOwe;
    }

    public Integer getReceivableAmountTax() {
        if(Objects.isNull(receivableAmountRate)){
            return 0;
        }
        return MathTool.percentCalculate(getReceivableAmount(), receivableAmountRate);
    }


    public String getReceivableAmountName(){
        Double num = receivableAmount / 100.0;

        DecimalFormat decimalFormat = new DecimalFormat("#.00"); //定义格式，小数点后两位

        String formattedAmount = decimalFormat.format(num);
        return formattedAmount;
    }
}
