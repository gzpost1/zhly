package cn.cuiot.dmp.externalapi.service.entity.video;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 监控-设备信息
 *
 * @Author: zc
 * @Date: 2024-08-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_video_device")
public class VideoDeviceEntity extends YjBaseEntity {
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

    /**
     * 启停用状态（0：停用；1：启用），默认启用，第三方不推送设备信息时状态设置为停用
     */
    private Byte status;
}
