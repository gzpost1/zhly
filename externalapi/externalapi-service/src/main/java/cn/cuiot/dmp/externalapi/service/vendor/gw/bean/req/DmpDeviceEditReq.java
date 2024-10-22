package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DmpDeviceEditReq extends DmpDeviceReq {

    private static final long serialVersionUID = -1891460425184742223L;

    /**
     * 设备名称。不填则由平台自动生成。
     */
    private String deviceName;

    /**
     * 设备IMEI号，全局唯一校验
     */
    private String imei;

    /**
     * 设备备注信息
     */
    private String description;

    private Position position;

    @Data
    public static class Position {
        /**
         * 设备的经度信息。
         */
        private String longitude;
        /**
         * 设备的纬度信息。
         */
        private String latitude;
        /**
         * 设备的海拔信息。
         */
        private String altitude;
    }
}
