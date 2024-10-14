package cn.cuiot.dmp.baseconfig.flow.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "计划id", orderNum = "1", width = 20)
    private Long id;


    /**
     * 计划名称
     */
    @Excel(name = "计划名称", orderNum = "0", width = 20)
    private String planName;


    /**
     * 计划说明
     */
    @Excel(name = "计划说明", orderNum = "2", width = 20)
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
    @Excel(name = "计划开始时间",orderNum = "3",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date startDate;


    /**
     * 计划有效期结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "计划结束时间",orderNum = "4",  width = 20,exportFormat = "yyyy-MM-dd")
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

    @Excel(name = "启用状态", orderNum = "7", width = 20)
    @TableField(exist = false)
    private String stateName;

    @Excel(name = "计划状态", orderNum = "6", width = 20)
    @TableField(exist = false)
    private String planState;

    /**
     * 备用字段
     */
    private String ext;


    /**
     * 创建人名称
     */
    @TableField(exist = false)
    @Excel(name = "创建人", orderNum = "6", width = 20)
    private  String createName;

    @TableField(exist = false)
    private String startProcessInstanceDTO;
}
