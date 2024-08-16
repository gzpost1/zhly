package cn.cuiot.dmp.video.service.vendor.bean.resp.vsuap;

import lombok.Data;

/**
 * 云智眼-查询统计类AI分析结果resp
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@Data
public class VsuapAIStatisticsResp {
    /**
     * 统计时间 （小时维度：如：2022-03-23 15:00:00  天维度：2022-03-23）
     */
    private String billTime;
    /**
     * 统计数目
     */
    private Long count;
}
