package cn.cuiot.dmp.baseconfig.flow.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author pengjian
 * @since 2024-04-26
 */
@Data
@TableName("tb_work_business_type_info")
public class WorkBusinessTypeInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;


    /**
     * 任务id
     */
    private Long taskId;


    /**
     * 实例id
     */
    private Long procInstId;


    /**
     * 用户id(超时为空)
     */
    private Long userId;

   @TableField(exist = false)
    private String userName;

    /**
     * 更新人的id
     */
    private Long closeUserId;


    /**
     * 操作时间
     */
    private Date startTime;


    /**
     * 结束时间
     */
    private Date endTime;


    /**
     * 操作类型0挂起1超时
     */
    private Byte businessType;


    /**
     * 原因
     */
    private String comments;

    /**
     * 补充说明
     */
    private String reason;

    /**
     * 节点
     */
    private String node;

    /**
     * 转交人id
     */
    private String deliver;

    /**
     * 转交人名称
     */
    @TableField(exist = false)
    private String deliverName;
}
