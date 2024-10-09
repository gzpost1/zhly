package cn.cuiot.dmp.lease.entity.charge;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收费管理-应收管理-自动生成计划
 */
@Data
@TableName(value = "tb_charge_plain")
public class TbChargePlain extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 本金税率
     */
    @TableField(value = "receivable_amount_rate")
    private BigDecimal receivableAmountRate;

    /**
     * 收费项目id
     */
    @TableField(value = "charge_item_id")
    private Long chargeItemId;

    /**
     * 收费标准 0自定义金额
     */
    @TableField(value = "charge_standard")
    private Long chargeStandard;

    /**
     * 应收金额/本金
     */
    @TableField(value = "receivable_amount")
    private Integer receivableAmount;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 执行频率 0每月 1每日 2指定日期
     */
    @TableField(value = "cron_type")
    private Byte cronType;

    /**
     * 执行频率-指定日期-开始时间
     */
    @TableField(value = "cron_begin_date")
    private String cronBeginDate;

    /**
     * 执行频率-指定日期-结束时间
     */
    @TableField(value = "cron_end_date")
    private String cronEndDate;

    /**
     * 执行频率-指定日期 1-31
     */
    @TableField(value = "cron_appoint_date")
    private Integer cronAppointDate;

    /**
     * 执行频率-指定的小时分
     */
    @TableField(value = "cron_time")
    private Date cronTime;

    /**
     * 状态 0停用 1启用
     */
    @TableField(value = "`status`")
    private Byte status;

    /**
     * 收费对象
     */
    private Long receivableObj;

    /**
     * 应收日期
     */
    private Integer dueDateNum;
}