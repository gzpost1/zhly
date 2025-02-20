
package cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap;

import lombok.Data;

/**
 * 云智眼-查询设实时视频HLS信息resp
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@Data
public class VsuapPlayOnFlvHlsResp {
    /**
     * 通道编码ID
     */
    private String channelCodeId;
    /**
     * 视频流编码类型
     * H.264: H264格式流
     * H.265: H.265格式流
     */
    private String codeType;
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 视频流 HTTP-HLS 格式播放地址
     */
    private String hls;
    /**
     * 视频流HTTP-FLV格式播放地址
     */
    private String flv;
    /**
     * 推流地址, RTMP设备具备
     */
    private Object pushUrl;
    /**
     * 流ID
     */
    private String streamId;

}
