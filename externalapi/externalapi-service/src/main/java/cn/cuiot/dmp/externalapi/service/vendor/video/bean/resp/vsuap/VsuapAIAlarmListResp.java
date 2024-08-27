package cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 云智眼-查询告警类AI分析结果resp
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@Data
public class VsuapAIAlarmListResp {
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
