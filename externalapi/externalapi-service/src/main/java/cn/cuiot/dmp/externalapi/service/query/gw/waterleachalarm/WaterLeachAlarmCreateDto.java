package cn.cuiot.dmp.externalapi.service.query.gw.waterleachalarm;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 格物-水浸报警器 创建dto
 *
 * @Author: zc
 * @Date: 2024-10-22
 */
@Data
public class WaterLeachAlarmCreateDto {

    /**
     * 楼盘id
     */
    @NotNull(message = "所属楼盘不能为空")
    private Long buildingId;

    /**
     * 设备名称
     */
    @NotNull(message = "设备名称不能为空")
    @Length(min = 1, max = 32, message = "设备名称限32字")
    private String deviceName;

    /**
     * 设备IMEI
     */
    @NotNull(message = "设备IMEI不能为空")
    @Length(min = 1, max = 15, message = "设备IMEI限15字")
    private String imei;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 备注
     */
    private String remark;
}
