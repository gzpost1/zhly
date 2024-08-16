
package cn.cuiot.dmp.video.service.vendor.bean.resp.vsuap;

import lombok.Data;

/**
 * 云智眼-查询设备列表resp
 *
 * @Author: zc
 * @Date: 2024-03-12
 */
@Data
public class VsuapDeviceListResp {
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
     * 实际在线通道数
     */
    private Integer channelOnlineNum;
    /**
     * 组织ID
     */
    private String layerNodeId;
}
