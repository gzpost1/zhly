package cn.cuiot.dmp.externalapi.service.service.video;

import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.externalapi.service.entity.video.VideoAIStatisticsEntity;
import cn.cuiot.dmp.externalapi.service.mapper.video.VideoAIStatisticsMapper;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapAIStatisticsResp;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 监控-统计分析 业务层
 *
 * @Author: zc
 * @Date: 2024-08-13
 */
@Service
public class VideoAIStatisticsService extends ServiceImpl<VideoAIStatisticsMapper, VideoAIStatisticsEntity> {

    /**
     * 同步统计数据
     *
     * @Param resp 参数
     * @Param methodId 算法id
     */
    public void syncAIStatistics(List<VsuapAIStatisticsResp> resp, String methodId, Long companyId) {
        List<Date> dates = resp.stream()
                .map(e -> DateTimeUtil.stringToDate(e.getBillTime()))
                .collect(Collectors.toList());

        List<VideoAIStatisticsEntity> list = list(
                new LambdaQueryWrapper<VideoAIStatisticsEntity>()
                        .eq(VideoAIStatisticsEntity::getMethodId, methodId)
                        .eq(VideoAIStatisticsEntity::getCompanyId, companyId)
                        .in(VideoAIStatisticsEntity::getBillTime, dates));
        Map<Date, VideoAIStatisticsEntity> map = list.stream().collect(Collectors.toMap(VideoAIStatisticsEntity::getBillTime, e -> e));

        List<VideoAIStatisticsEntity> collect = resp.stream().map(item -> {
            Date billTime = DateTimeUtil.stringToDate(item.getBillTime());
            VideoAIStatisticsEntity aDefault = map.getOrDefault(billTime, new VideoAIStatisticsEntity());
            BeanUtils.copyProperties(item, aDefault);
            aDefault.setMethodId(methodId);
            aDefault.setBillTime(billTime);
            aDefault.setBillDate(DateTimeUtil.dateToLocalDate(billTime));
            aDefault.setCompanyId(companyId);
            return aDefault;
        }).collect(Collectors.toList());

        saveOrUpdateBatch(collect);
    }
}
