
package cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap;

import lombok.Data;

/**
 * 云智眼-查询设备通道列表resp
 *
 * @Author: zc
 * @Date: 2024-03-12
 */
@Data
public class VsuapChannelResp {
    /**
     * 通道编码ID
     */
    private String channelCodeId;
    /**
     * 通道名称
     */
    private String channelName;
    /**
     * 通道注册ID
     */
    private String channelGbId;
    /**
     * 设备国标ID
     */
    private String domainGbId;
    /**
     * 通道状态
     * 0: 未注册
     * 2: 在线
     * 3: 离线
     */
    private Integer state;

}
