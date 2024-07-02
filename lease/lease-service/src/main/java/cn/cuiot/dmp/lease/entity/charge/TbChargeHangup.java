package cn.cuiot.dmp.lease.entity.charge;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 收费管理-收银台-挂起
 */
@Data
@TableName(value = "tb_charge_hangup",autoResultMap = true)
public class TbChargeHangup {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    /**
     * 挂起/解挂时间
     */
    @TableField(value = "hangup_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date hangupTime;

    /**
     * 挂起/解挂原因
     */
    @TableField(value = "hangup_desc")
    private String hangupDesc;

    /**
     * 创建人
     */
    @TableField(value = "create_user")
    private Long createUser;

    /**
     * 数据Id
     */
    @TableField(value = "data_id")
    private Long dataId;

    /**
     * 数据类型 0解挂 1挂起
     */
    @TableField(value = "data_type")
    private Byte dataType;

    /**
     * 操作人名称
     */
    @TableField(exist = false)
    private String operatorName;
}