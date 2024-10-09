package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;

import lombok.Data;

import java.util.List;

/**
 * 门禁状态resp
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikAcsStatesResp {

    /**
     * 页码
     */
    private Integer pageNo;

    /**
     * 每页记录数
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer totalPage;

    /**
     * 资源信息集
     */
    private List<DataItem> list;

    @Data
    public static class DataItem {
        /**
         * 设备型号
         */
        private String deviceType;

        /**
         * 设备唯一编码
         */
        private String deviceIndexCode;

        /**
         * 区域编码
         */
        private String regionIndexCode;

        /**
         * 采集时间
         */
        private String collectTime;

        /**
         * 区域名字
         */
        private String regionName;

        /**
         * 资源唯一编码
         */
        private String indexCode;

        /**
         * 设备名称
         */
        private String cn;

        /**
         * 协议类型，海康私有协议： hiksdk_net，GB/T28181： gb_reg, eHome协议： ehome_reg，大华私有协议： dhsdk_net，ONVIF协议： onvif_net，萤石协议： ezviz_net，级联： cascade
         */
        private String treatyType;

        /**
         * 厂商，hikvision-海康，dahua-大华
         */
        private String manufacturer;

        /**
         * IP地址
         */
        private String ip;

        /**
         * 端口
         */
        private Integer port;

        /**
         * 在线状态，0离线，1在线
         */
        private Integer online;
    }
}
