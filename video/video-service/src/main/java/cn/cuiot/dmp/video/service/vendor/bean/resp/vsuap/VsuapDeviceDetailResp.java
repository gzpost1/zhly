
package cn.cuiot.dmp.video.service.vendor.bean.resp.vsuap;

import lombok.Data;

import java.util.List;

/**
 * 云智眼-查询设备详情resp
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@Data
public class VsuapDeviceDetailResp {
    /**
     * 组织ID
     */
    private String layerNodeId;
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备描述
     */
    private String deviceDescription;
    /**
     * 设备状态
     * 1: 未注册
     * 2: 在线
     * 3: 离线
     */
    private Integer state;
    /**
     * 启用状态, 默认启用
     * 0: 停用
     * 1: 启用
     */
    private Integer enableState;
    /**
     * 设备类型
     * 1:国标摄像头
     * 2: RTMP摄像头
     * 3: 国标平台
     * 4: NVR设备
     */
    private Integer deviceType;
    /**
     * 是否注册启动拉流
     * 0: 否
     * 1: 是
     */
    private Integer autoStart;
    /**
     * 是否开启视频加密
     * 0: 关闭
     * 1: 开启
     */
    private Integer playEncrypt;
    /**
     * 设置的最大通道数
     */
    private Integer channelAllocate;
    /**
     * 实际接入通道数
     */
    private Integer channelNum;
    /**
     * SIP服务器ID
     */
    private String domainGbId;
    /**
     * SIP服务器IP
     */
    private String sipIp;
    /**
     * SIP服务器Port
     */
    private String sipPort;
    /**
     * 设备国标ID
     */
    private String gbId;
    /**
     * SIP用户ID
     */
    private String accountName;
    /**
     * SIP认证密码
     */
    private String accountPassword;
    /**
     * 设备厂商
     */
    private Object manufacturer;
    /**
     * 最近注册时间
     */
    private Long registerTime;
    /**
     * 推流地址, RTMP设备具备
     */
    private String pushUrl;
    /**
     * 套餐包信息
     */
    private List<DataItem> comboList;

    @Data
    public static class DataItem {
        /**
         * 套餐包Id
         */
        private String comboId;
        /**
         * 套餐包名称
         */
        private String comboName;
        /**
         * 套餐包状态
         */
        private Integer comboState;
        /**
         * 套餐包过期时间, 13位时间戳
         */
        private Long orderExpireTime;
    }

}
