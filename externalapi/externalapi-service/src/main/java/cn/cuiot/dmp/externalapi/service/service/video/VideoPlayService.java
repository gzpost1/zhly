package cn.cuiot.dmp.externalapi.service.service.video;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.externalapi.service.entity.video.VideoPlayEntity;
import cn.cuiot.dmp.externalapi.service.entity.video.query.VideoScreenQuery;
import cn.cuiot.dmp.externalapi.service.mapper.video.VideoPlayMapper;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapPlayOnFlvHlsResp;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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
     */
    public void syncPlay(VsuapPlayOnFlvHlsResp resp) {
        List<VideoPlayEntity> list = list(new LambdaQueryWrapper<VideoPlayEntity>()
                .eq(VideoPlayEntity::getChannelCodeId, resp.getChannelCodeId())
                .eq(VideoPlayEntity::getDeviceId, resp.getDeviceId())
                .eq(VideoPlayEntity::getStreamId, resp.getStreamId()));

        VideoPlayEntity videoPlayEntity = CollectionUtils.isNotEmpty(list) ? list.get(0) : new VideoPlayEntity();
        BeanUtils.copyProperties(resp, videoPlayEntity);
        videoPlayEntity.setStatus(EntityConstants.ENABLED);

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
                .last(" Limit 1 "));
    }
}
