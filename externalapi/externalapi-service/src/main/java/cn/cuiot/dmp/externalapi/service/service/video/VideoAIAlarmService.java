package cn.cuiot.dmp.externalapi.service.service.video;

import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.externalapi.service.entity.video.VideoAIAlarmEntity;
import cn.cuiot.dmp.externalapi.service.mapper.video.VideoAIAlarmMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import cn.cuiot.dmp.externalapi.service.query.video.VideoScreenQuery;
import cn.cuiot.dmp.externalapi.service.vo.video.VideoScreenVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapAIAlarmListResp;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 监控-报警分析 业务层
 *
 * @Author: zc
 * @Date: 2024-08-13
 */
@Service
public class VideoAIAlarmService extends ServiceImpl<VideoAIAlarmMapper, VideoAIAlarmEntity> {

    /**
     * 同步监控AI警告列表
     *
     * @Param data 参数
     * @Param companyId 企业id
     */
    public void syncAIAlarm(List<VsuapAIAlarmListResp> data, Long companyId) {
        //流水号列表
        List<String> serialNumbers = data.stream()
                .map(VsuapAIAlarmListResp::getSerialNumber)
                .collect(Collectors.toList());

        List<VideoAIAlarmEntity> list = list(new LambdaQueryWrapper<VideoAIAlarmEntity>()
                .eq(VideoAIAlarmEntity::getCompanyId, companyId)
                .in(VideoAIAlarmEntity::getSerialNumber, serialNumbers));
        Map<String, VideoAIAlarmEntity> map = list.stream().collect(Collectors.toMap(VideoAIAlarmEntity::getSerialNumber, e -> e));

        List<VideoAIAlarmEntity> collect = data.stream().map(item -> {
            VideoAIAlarmEntity aDefault = map.getOrDefault(item.getSerialNumber(), new VideoAIAlarmEntity());
            BeanUtils.copyProperties(item, aDefault);
            aDefault.setCompanyId(companyId);
            if (Objects.nonNull(aDefault.getAnalysisTime())) {
                aDefault.setAnalysisDate(DateTimeUtil.dateToLocalDate(aDefault.getAnalysisTime()));
            }
            return aDefault;
        }).collect(Collectors.toList());

        saveOrUpdateBatch(collect);
    }

    /**
     * 查询AI报警列表
     *
     * @return List<VideoScreenVo>
     * @Param query 参数
     */
    public List<VideoScreenVo> queryVideoAIList(VideoScreenQuery query) {
        if (StringUtils.isBlank(query.getDeviceId())) {
            return Lists.newArrayList();
        }
        LocalDate now = LocalDate.now();
        query.setBeginDate(now.minusDays(6));
        query.setEndDate(now);

        List<VideoScreenVo> vos = baseMapper.queryVideoAIList(query);
        if (CollectionUtils.isNotEmpty(vos)) {
            AtomicInteger sort = new AtomicInteger(1);
            vos.forEach(item -> item.setSort(sort.getAndIncrement()));
        }
        return vos;
    }
}
