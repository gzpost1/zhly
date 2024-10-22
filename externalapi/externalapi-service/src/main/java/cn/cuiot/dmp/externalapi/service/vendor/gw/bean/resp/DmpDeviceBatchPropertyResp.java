package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp;

import lombok.Data;

import java.util.List;

/**
 * @Author: zc
 * @Date: 2024-10-22
 */
@Data
public class DmpDeviceBatchPropertyResp {

    /**
     * 该次请求中设置属性成功下发设备总数。
     */
    private Integer success;

    /**
     * 该次请求中设置属性失败下发失败总数。
     */
    private Integer fail;

    /**
     * 该次请求中设置属性命令的设备总数。
     */
    private Integer total;

    /**
     * 成功详情列表，列表详情见successInfo。
     */
    private List<SuccessDataItem> successList;

    /**
     * 错误详情列表，列表详情见failInfo。
     */
    private List<FailDataItem> failList;

    @Data
    public static class SuccessDataItem {
        /**
         * 产品的productKey。创建产品时，雁飞·格物DMP平台为该产品颁发的全局唯一标识。
         */
        private String productKey;
        /**
         * 设备的deviceKey，由用户在添加设备时指定
         */
        private String deviceKey;
        /**
         * 设备平台唯一标识码
         */
        private String iotId;
    }

    @Data
    public static class FailDataItem {
        /**
         * 产品的productKey。创建产品时，雁飞·格物DMP平台为该产品颁发的全局唯一标识。
         */
        private String productKey;
        /**
         * 设备的deviceKey，由用户在添加设备时指定
         */
        private String deviceKey;
        /**
         * 设备平台唯一标识码
         */
        private String iotId;
        /**
         * 错误信息描述。
         */
        private String errorMessage;
    }
}
