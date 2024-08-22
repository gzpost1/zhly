package cn.cuiot.dmp.externalapi.service.entity.video;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 监控-播放信息
 *
 * @Author: zc
 * @Date: 2024-08-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_video_play")
public class VideoPlayEntity  extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业ID
     */
    private Long companyId;

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
    private String pushUrl;

    /**
     * 流ID
     */
    private String streamId;

    /**
     * 启停用状态（0：停用；1：启用），默认启用，第三方不推送设备信息时状态设置为停用
     */
    private Byte status;
}
