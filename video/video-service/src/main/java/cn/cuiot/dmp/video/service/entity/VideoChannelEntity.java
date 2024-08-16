package cn.cuiot.dmp.video.service.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 监控-通道消息
 *
 * @Author: zc
 * @Date: 2024-08-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_video_channel")
public class VideoChannelEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 设备ID
     */
    private String deviceId;

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

    /**
     * 启停用状态（0：停用；1：启用），默认启用，第三方不推送设备信息时状态设置为停用
     */
    private Byte status;
}
