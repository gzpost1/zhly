package cn.cuiot.dmp.lease.dto.charge;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description 收费管理-收银台-缴费管理分页详情
 * @Date 2024/6/12 17:28
 * @Created by libo
 */
@Data
public class ChargeManagerPageDto implements ChargeItemNameSet{
    /**
     * 应收编码
     */
    private Long id;

    /**
     * 房屋ID
     */
    private Long houseId;

    /**
     * 房屋名称
     */
    private String houseName;

    /**
     * 房屋编号
     */
    private String houseCode;

    /**
     * 客户id
     */
    private Long customerUserId;

    /**
     * 客户名称
     */
    private String customerUserName;

    /**
     * 客户手机号
     */
    private String customerUserPhone;

    /**
     * 客户身份
     */
    private String customerUserRoleName;

    /**
     * 所属类型 0手动创建  1自动生成
     */
    private Byte createType;

    /**
     * 自动生成计划编码
     */
    private Long receivblePlanId;

    /**
     * 收费项目id
     */
    private Long chargeItemId;

    /**
     * 收费项目名称
     */
    private String chargeItemName;

    /**
     * 应收金额/本金
     */
    private Integer receivableAmount;

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
     * 应收日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dueDate;

    /**
     * 应收状态 0未交款 1已交款 2已交清
     */
    private Byte receivbleStatus;

    /**
     * 挂起状态 0未挂起 1已挂起
     */
    private Byte hangUpStatus;

    /**
     * 作废状态 0正常 1已作废
     */
    private Byte abrogateStatus;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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

    public Integer getTotalOwe() {
        return receivableAmount - receivableAmountReceived;
    }

    public void setTotalOwe(Integer totalOwe) {
        this.totalOwe = totalOwe;
    }
}
