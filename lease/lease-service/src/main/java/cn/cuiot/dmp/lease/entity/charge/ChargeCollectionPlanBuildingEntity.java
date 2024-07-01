package cn.cuiot.dmp.lease.entity.charge;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 收费管理-催款计划（楼盘信息）
 *
 * @author zc
 */
@Data
@TableName(value = "tb_charge_collection_plan_building")
public class ChargeCollectionPlanBuildingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 催款计划id
     */
    @TableField(value = "charge_collection_plan_id")
    private Long chargeCollectionPlanId;

    /**
     * 楼盘id
     */
    @TableField(value = "building_id")
    private Long buildingId;
}