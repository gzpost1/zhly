package cn.cuiot.dmp.baseconfig.flow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pengjian
 * @since 2024-04-23
 */
@Data
@TableName("tb_work_info")
public class WorkInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.INPUT)
    private Long id;


    /**
     * 业务类型
     */
    private Long businessType;


    /**
     * 所属组织
     */
    private Long orgId;


    /**
     * 工单名称
     */
    private String workName;


    /**
     * 工单来源
     */
    private Byte workSouce;



    /**
     * 状态
     */
    private Byte status;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 创建的用户id
     */
    private Long createUser;


    /**
     * 流程实例id
     */
    private String procInstId;

    /**
     * 超时 0未超时 1超时
     */
    private  Byte timeOut;
}
