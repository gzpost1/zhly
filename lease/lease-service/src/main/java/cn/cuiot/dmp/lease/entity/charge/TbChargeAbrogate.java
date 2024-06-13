package cn.cuiot.dmp.lease.entity.charge;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 收费管理-收银台-作废明细
 */
@Data
@TableName(value = "tb_charge_abrogate" , autoResultMap = true)
public class TbChargeAbrogate {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 作废时间
     */
    @TableField(value = "abrogate_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date abrogateTime;

    /**
     * 作废原因
     */
    @TableField(value = "abrogate_desc")
    private String abrogateDesc;

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
     * 数据类型 0缴费 1押金
     */
    @TableField(value = "data_type")
    private Byte dataType;

    /**
     * 操作人名称
     */
    @TableField(exist = false)
    private String operatorName;
}