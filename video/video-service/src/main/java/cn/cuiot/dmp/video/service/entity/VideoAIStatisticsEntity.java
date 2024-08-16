package cn.cuiot.dmp.video.service.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Date;

/**
 * 监控-统计分析
 *
 * @Author: zc
 * @Date: 2024-08-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_video_ai_statistics")
public class VideoAIStatisticsEntity extends YjBaseEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 算法id
     */
    private String methodId;

    /**
     * 统计时间 （小时维度：如：2022-03-23 15:00:00  天维度：2022-03-23）
     */
    private Date billTime;

    /**
     * 统计日期 （小时维度：如：2022-03-23 15:00:00  天维度：2022-03-23）
     */
    private LocalDate billDate;

    /**
     * 统计数目
     */
    private Long count;
}
