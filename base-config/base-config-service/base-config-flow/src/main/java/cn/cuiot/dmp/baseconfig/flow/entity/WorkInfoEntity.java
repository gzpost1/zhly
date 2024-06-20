package cn.cuiot.dmp.baseconfig.flow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
     * 业务类型名称
     */
    @TableField(exist = false)
    private String businessTypeName;

    /**
     * 所属组织
     */
    private Long orgId;

    /**
     * 组织名称
     */
    @TableField(exist = false)
    private String orgPath;

    /**
     * 楼盘id
     */
    private Long propertyId;


    /**
     * 工单名称
     */
    private String workName;


    /**
     * 工单来源 0 计划生成  1 自查报事2客户提单3代录工单
     */
    private Byte workSouce;



    /**
     * 状态
     */
    private Byte status;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建的用户id
     */
    private Long createUser;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 流程实例id
     */
    private String procInstId;

    /**
     * configId
     */
    private String orgIds;

    /**
     * 流程定义id
     */
    private  String ProcessDefinitionId;

    /**
     * 报单客户id
     */
    private Long actualUserId;

    /**
     * 工单是否超时0未超时1已超时
     */
    private Byte workTimeOut;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 撤销规则 0不可撤回 1 可撤回
     */
    private Byte revokeType;

    /**
     * 撤销的节点id
     */
    private String revokeNodeId;

    /**
     * 1 重新提交
     */
    @TableField(exist = false)
    private Byte resubmit;

    /**
     * 客户id
     */
    private Long customerId;

}
