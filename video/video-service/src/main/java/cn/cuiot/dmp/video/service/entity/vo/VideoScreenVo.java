package cn.cuiot.dmp.video.service.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 数字孪生-大屏 vo
 *
 * @Author: zc
 * @Date: 2024-08-14
 */
@Data
public class VideoScreenVo {

    /**
     * id
     */
    private Long id;

    /**
     * AI 事件
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
     * 排序
     */
    private Integer sort;
}
