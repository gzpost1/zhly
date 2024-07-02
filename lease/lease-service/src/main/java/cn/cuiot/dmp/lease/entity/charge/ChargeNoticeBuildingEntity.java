package cn.cuiot.dmp.lease.entity.charge;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 收费管理-通知单（楼盘信息）
 *
 * @author zc
 */
@Data
@TableName("tb_charge_notice_building")
public class ChargeNoticeBuildingEntity implements Serializable {
    /**
     * 客户通知单id
     */
    @TableField(value = "charge_notice_id")
    private Long chargeNoticeId;

    /**
     * 楼盘id
     */
    @TableField(value = "building_id")
    private Long buildingId;
}