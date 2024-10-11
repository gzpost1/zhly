package cn.cuiot.dmp.lease.entity.charge;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收费管理-催款计划
 *
 * @author zc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tb_charge_collection_plan",autoResultMap = true,resultMap = "basemap")
public class ChargeCollectionPlanEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 计划名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 通知渠道（1：系统消息；2：短信）
     */
    @TableField(value = "channel",typeHandler = JsonTypeHandler.class)
    private List<String> channel;

    /**
     * 发送日期类型（1:每天，2:每周，3:每月）
     */
    @TableField(value = "cron_type")
    private Byte cronType;

    /**
     * 执行频率-指定日期 1-31
     */
    @TableField(value = "cron_appoint_day")
    private Integer cronAppointDay;

    /**
     * 执行频率-指定周数 1-7
     */
    @TableField(value = "cron_appoint_week")
    private Integer cronAppointWeek;

    /**
     * 执行频率-指定的小时分
     */
    @TableField(value = "cron_time")
    private Date cronTime;

    /**
     * 停启用状态（0停用，1启用）
     */
    @TableField(value = "status")
    private Byte status;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;
}