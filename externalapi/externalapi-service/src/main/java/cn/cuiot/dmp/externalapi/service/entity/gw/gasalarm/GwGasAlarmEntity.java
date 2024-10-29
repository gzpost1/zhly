package cn.cuiot.dmp.externalapi.service.entity.gw.gasalarm;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.DmpDeviceCreateResp;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 格物燃气报警器
 *
 * @Author: zc
 * @Date: 2024-10-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tb_gw_gas_alarm")
public class GwGasAlarmEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 企业ID
     */
    @TableField(value = "dept_id")
    private Long deptId;

    /**
     * 楼盘id
     */
    @TableField(value = "building_id")
    private Long buildingId;

    /**
     * 设备名称
     */
    @TableField(value = "device_name")
    private String deviceName;

    /**
     * 设备IMEI
     */
    @TableField(value = "imei")
    private String imei;

    /**
     * 鉴权码
     */
    @TableField(value = "auth_code")
    private String authCode;

    /**
     * 详细地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 纬度
     */
    @TableField(value = "latitude")
    private BigDecimal latitude;

    /**
     * 经度
     */
    @TableField(value = "longitude")
    private BigDecimal longitude;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 状态 1-启用 0-停用
     */
    @TableField(value = "`status`")
    private Byte status;

    /**
     * 设备状态 (0: 在线，1: 离线，2: 未激活）
     */
    @TableField(value = "equip_status")
    private String equipStatus;

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
     * 设备平台唯一标识码（接口返回）
     */
    @TableField(value = "iot_id")
    private String iotId;

    /**
     * 设备密钥，设备鉴权登录时使用（接口返回）
     */
    @TableField(value = "device_secret")
    private String deviceSecret;

    private static final long serialVersionUID = 1L;

    /**
     * 构建外部设备信息
     */
    public void buildExternalDeviceInfo(GwGasAlarmEntity entity, DmpDeviceCreateResp device) {
        //第三方设备信息
        entity.setProductKey(device.getProductKey());
        entity.setDeviceKey(device.getDeviceKey());
        entity.setIotId(device.getIotId());
        entity.setDeviceSecret(device.getDeviceSecret());
    }
}