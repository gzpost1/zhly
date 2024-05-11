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
    @TableField(value = "company_id")
    private String businessTypeName;

    /**
     * 所属组织
     */
    private Long orgId;

    /**
     * 组织名称
     */
    @TableField(value = "company_id")
    private String orgPath;

    /**
     * 企业ID
     */
    private Long companyId;


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


}
