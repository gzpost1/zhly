package cn.cuiot.dmp.baseconfig.flow.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author pengjian
 * @since 2024-05-06
 */
@Data
@TableName("tb_work_plan_info")
public class WorkPlanInfoEntity extends YjBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;


    /**
     * 计划名称
     */
    private String planName;


    /**
     * 计划说明
     */
    private String planDesc;

    /**
     * 计划开始时间
     */
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private LocalTime planTime;

    /**
     * 流程id
     */
    private String flowId;


    /**
     * 流程key
     */
    private String flowKey;


    /**
     * 计划有效期开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;


    /**
     * 计划有效期结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;


//    /**
//     * 工单推送日期，单位分钟
//     */
//    private Integer pushTime;


    /**
     * 推送天
     */
    private Integer pushDay;
    /**
     * 推送小时
     */
    private Double pushHour;

    /**
     * 执行策略1按天2按周3按月
     */
    private Byte executionStrategy;


    /**
     * 0每天1指定
     */
    private Byte strategyType;


    /**
     * 指定的天
     */
    private String specifyDay;


    /**
     * 指定的星期
     */
    private String specifyWeek;


    /**
     * 指定的月份
     */
    private String specifyMonth;


    /**
     * 时循环设置1启用0不启用
     */
    private Byte recurrentState;


    /**
     * 循环小时
     */
    private Double recurrentHour;


    /**
     * 循环结束类型0今日1次日
     */
    private Byte recurrentType;


    /**
     * 循环结束时间
     */
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private LocalTime recurrentOverTime;


    /**
     * 任务id
     */
    private Long taskId;


    /**
     * 组织id
     */
    private Long orgId;


    /**
     * 状态0停用1启用
     */
    private Byte state;


    /**
     * 备用字段
     */
    private String ext;


    /**
     * 创建人名称
     */
    @TableField(exist = false)
    private  String createName;

    @TableField(exist = false)
    private String startProcessInstanceDTO;
}
