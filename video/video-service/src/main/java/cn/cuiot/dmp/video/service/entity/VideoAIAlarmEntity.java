package cn.cuiot.dmp.video.service.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

/**
 * 监控-报警分析
 *
 * @Author: zc
 * @Date: 2024-08-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_video_ai_alarm")
public class VideoAIAlarmEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 算法ID
     */
    private String methodId;

    /**
     * 算法名称
     */
    private String methodName;

    /**
     * 报警时间, 13位时间戳
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date analysisTime;

    /**
     * 报警日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate analysisDate;

    /**
     * 报警图片
     */
    private String image;

    /**
     * 报警消息
     */
    private String message;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 通道ID
     */
    private String channelCodeId;

    /**
     * 组织ID
     */
    private String layerNodeId;

    /**
     * 流水号
     */
    private String serialNumber;
}
