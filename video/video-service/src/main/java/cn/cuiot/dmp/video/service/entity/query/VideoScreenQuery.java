package cn.cuiot.dmp.video.service.entity.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 数字孪生-大屏 query
 *
 * @Author: zc
 * @Date: 2024-08-14
 */
@Data
public class VideoScreenQuery {
    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 开始日期（前端不用传）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginDate;

    /**
     * 结束日期（前端不用传）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
