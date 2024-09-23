package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
public class DmpDeviceBatchCreateReq implements Serializable {

    private static final long serialVersionUID = -1891460425184742223L;

    /**
     * 产品的productKey。创建产品时，雁飞·格物DMP平台为该产品颁发的全局唯一标识。
     */
    private String productKey;

    /**
     * 设备的deviceKey，由用户在添加设备时指定但需要满足该账户下唯一。
     */
    private List<DeviceInfo> deviceList;

    @Data
    public static class DeviceInfo{
        /**
         * 设备的deviceKey。
         */
        private String deviceKey;

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
    }
}
