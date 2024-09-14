package cn.cuiot.dmp.externalapi.service.entity.gw;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 格物设备数据关联表
 *
 * @Author: zc
 * @Date: 2024-09-13
 */
@Data
@TableName(value = "tb_gw_device_relation")
public class GwDeviceRelationEntity implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 设备productKey（接口返回）
     */
    @TableField(value = "product_key")
    private String productKey;

    /**
     * 设备deviceKey（接口返回）
     */
    @TableField(value = "device_key")
    private String deviceKey;

    /**
     * 业务类型（查看代码）
     */
    @TableField(value = "business_type")
    private String businessType;

    /**
     * 设备数据id（系统创建）
     */
    @TableField(value = "data_id")
    private Long dataId;

    private static final long serialVersionUID = 1L;
}