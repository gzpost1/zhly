package cn.cuiot.dmp.lease.entity.charge;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 收费管理-通知单（收费项目）
 *
 * @author zc
 */
@Data
@TableName("tb_charge_notice_item")
public class ChargeNoticeItemEntity implements Serializable {
    /**
     * 客户通知单id
     */
    @TableField(value = "charge_notice_id")
    private Long chargeNoticeId;

    /**
     * 收费项目id
     */
    @TableField(value = "charge_item_id")
    private Long chargeItemId;
}