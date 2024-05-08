package cn.cuiot.dmp.baseconfig.flow.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author pengjian
 * @since 2024-05-08
 */
@Data
@TableName("tb_plan_work_execution_info")
public class PlanWorkExecutionInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;


    /**
     * 执行时间
     */
    private LocalDateTime executionTime;


    /**
     * 计划工单id
     */
    private Long planWorkId;


    /**
     * 0未生成1已生成
     */
    private Byte state;


    /**
     * 流程实例id
     */
    private Long procInstId;



}
