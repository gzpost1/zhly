package cn.cuiot.dmp.digitaltwin.service.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 数字孪生平台-报警消息列表
 *
 * @Author: zc
 * @Date: 2024-08-14
 */
@Data
public class GwFirefightAlarmConfirmationVo {

    /**
     * id
     */
    private Long id;

    /**
     * 处理类型
     */
    private String dealType;

    /**
     * 处理结果
     */
    private String dealResult;

    /**
     * 火警/故障处理状态
     */
    private String alarmStatus;

    /**
     * 上报时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reportTime;

    /**
     * 是否已通知（默认都是通知了）
     */
    private Byte isNotified = 1;

    /**
     * 排序
     */
    private Integer sort;
}
