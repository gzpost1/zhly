package cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 物联网水表（山东科德）下发阀控指令请求
 *
 * @date 2024/8/21 14:13
 * @author gxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class WaterMeterCommandControlReq implements Serializable {
    private static final long serialVersionUID = -8527541751522467489L;

    /**
     * 设备的imei号
     */
    private String imei;

    /**
     * 对设备执行的阀门操作 00开阀 01关阀
     */
    private String valveControlType;

}
