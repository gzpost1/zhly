package cn.cuiot.dmp.lease.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 收费管理-催款计划vo
 *
 * @author zc
 */
@Data
public class ChargeCollectionPlanVo {
    /**
     * id
     */
    private Long id;

    /**
     * 计划名称
     */
    private String name;

    /**
     * 通知渠道（1:短信，2:微信）
     */
    private Byte channel;

    /**
     * 发送日期类型（1:每天，2:每周，3:每月）
     */
    private Byte cronType;

    /**
     * 执行频率-指定日期 1-31
     */
    private Integer cronAppointDay;

    /**
     * 执行频率-指定周数 1-7
     */
    private Integer cronAppointWeek;

    /**
     * 执行频率-指定的小时分
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date cronTime;

    /**
     * 停启用状态（0停用，1启用）
     */
    private Byte status;

    /**
     * 楼盘id列表
     */
    private List<Long> buildings;

    /**
     * 收费项目列表
     */
    private List<Long> chargeItems;
}