package cn.cuiot.dmp.externalapi.service.service.video;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.externalapi.service.entity.video.VideoChannelEntity;
import cn.cuiot.dmp.externalapi.service.entity.video.VideoDeviceEntity;
import cn.cuiot.dmp.externalapi.service.mapper.video.VideoChannelMapper;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapChannelResp;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 监控-通道消息 业务层
 *
 * @Author: zc
 * @Date: 2024-08-12
 */
@Service
public class VideoChannelService extends ServiceImpl<VideoChannelMapper, VideoChannelEntity> {

    /**
     * 停用设备通道（不填channelCodeId修改全部通道）
     *
     * @Param deviceId 第三方通道id
     * @Param companyId 企业id
     */
    public void disableChannel(String deviceId, Long companyId) {
        LambdaUpdateWrapper<VideoChannelEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(VideoChannelEntity::getStatus, EntityConstants.DISABLED);
        wrapper.eq(StringUtils.isNotBlank(deviceId), VideoChannelEntity::getDeviceId, deviceId);
        wrapper.eq(VideoChannelEntity::getCompanyId, companyId);

        update(wrapper);
    }

    /**
     * 查询在线的通道
     *
     * @return List<VideoDeviceEntity> 列表
     */
    public IPage<VideoChannelEntity> queryEnableChannelPage(Page<VideoChannelEntity> page, Integer state, Long companyId) {
        LambdaQueryWrapper<VideoChannelEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(companyId), VideoChannelEntity::getCompanyId, companyId);
        wrapper.eq(VideoChannelEntity::getState, state);
        wrapper.eq(VideoChannelEntity::getStatus, EntityConstants.ENABLED);

        return page(page, wrapper);
    }

    /**
     * 同步通道
     *
     * @Param data 参数
     * @Param deviceId 设备id
     * @Param companyId 企业id
     */
    public void syncChannel(List<VsuapChannelResp> data, VideoDeviceEntity device) {
        // 获取设备id列表
        List<String> channelIds = data.stream()
                .map(VsuapChannelResp::getChannelCodeId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());

        // 根据设备列表查询数据
        List<VideoChannelEntity> dbList = list(new LambdaQueryWrapper<VideoChannelEntity>()
                .eq(VideoChannelEntity::getDeviceId, device.getDeviceId())
                .eq(VideoChannelEntity::getCompanyId, device.getCompanyId())
                .in(VideoChannelEntity::getChannelCodeId, channelIds));
        Map<String, VideoChannelEntity> dbMap = dbList.stream().collect(Collectors.toMap(VideoChannelEntity::getChannelCodeId, e -> e));

        // 构建VideoChannelEntity
        List<VideoChannelEntity> collect = data.stream().map(item -> {
            VideoChannelEntity aDefault = dbMap.getOrDefault(item.getChannelCodeId(), new VideoChannelEntity());
            BeanUtils.copyProperties(item, aDefault);
            aDefault.setStatus(EntityConstants.ENABLED);
            aDefault.setDeviceId(device.getDeviceId());
            aDefault.setCompanyId(device.getCompanyId());
            aDefault.setSecret(device.getSecret());
            return aDefault;
        }).collect(Collectors.toList());

        saveOrUpdateBatch(collect);
    }
}
