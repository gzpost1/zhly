package cn.cuiot.dmp.baseconfig.flow.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 工单关联关系
 * @author pengjian
 * @since 2024-06-03
 */
@Data
@TableName("tb_work_order_rel")
public class WorkOrderRelEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;


    /**
     * 原工单id
     */
    private Long oldWorkOrderId;


    /**
     * 新工单id
     */
    private Long newWorkOrderId;



}
