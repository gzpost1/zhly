package cn.cuiot.dmp.externalapi.service.service.video;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.externalapi.service.entity.video.VideoPlayEntity;
import cn.cuiot.dmp.externalapi.service.query.video.VideoScreenQuery;
import cn.cuiot.dmp.externalapi.service.mapper.video.VideoPlayMapper;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapPlayOnFlvHlsResp;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 监控-播放信息 业务层
 *
 * @Author: zc
 * @Date: 2024-08-12
 */
@Service
public class VideoPlayService extends ServiceImpl<VideoPlayMapper, VideoPlayEntity> {

    /**
     * 同步播放消息
     *
     * @Param resp 参数
     * @Param companyId 企业id
     */
    public void syncPlay(VsuapPlayOnFlvHlsResp resp, Long companyId) {
        List<VideoPlayEntity> list = list(new LambdaQueryWrapper<VideoPlayEntity>()
                .eq(VideoPlayEntity::getChannelCodeId, resp.getChannelCodeId())
                .eq(VideoPlayEntity::getCompanyId, companyId)
                .eq(VideoPlayEntity::getDeviceId, resp.getDeviceId())
                .eq(VideoPlayEntity::getStreamId, resp.getStreamId()));

        VideoPlayEntity videoPlayEntity = CollectionUtils.isNotEmpty(list) ? list.get(0) : new VideoPlayEntity();
        BeanUtils.copyProperties(resp, videoPlayEntity);
        videoPlayEntity.setStatus(EntityConstants.ENABLED);
        videoPlayEntity.setCompanyId(companyId);

        saveOrUpdate(videoPlayEntity);
    }

    /**
     * 根据设备id查询
     *
     * @return VideoPlayEntity
     * @Param query 参数
     */
    public VideoPlayEntity queryByDeviceId(VideoScreenQuery query) {
        if (StringUtils.isBlank(query.getDeviceId())) {
            return null;
        }
        return getOne(new LambdaQueryWrapper<VideoPlayEntity>()
                .eq(VideoPlayEntity::getDeviceId, query.getDeviceId())
                .eq(Objects.nonNull(query.getCompanyId()), VideoPlayEntity::getCompanyId, query.getCompanyId())
                .last(" Limit 1 "));
    }

    /**
     * 根据设备id查询flv
     *
     * @Param deviceIds 设备id列表
     * @Param companyId 企业id
     */
    public List<VideoPlayEntity> queryFlvByDeviceIds(List<String> deviceIds, Long companyId) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Lists.newArrayList();
        }
        return list(new LambdaQueryWrapper<VideoPlayEntity>()
                .eq(VideoPlayEntity::getCompanyId, companyId)
                .in(VideoPlayEntity::getDeviceId, deviceIds));
    }
}
