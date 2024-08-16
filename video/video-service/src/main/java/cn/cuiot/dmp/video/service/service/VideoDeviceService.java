package cn.cuiot.dmp.video.service.service;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.video.service.entity.VideoDeviceEntity;
import cn.cuiot.dmp.video.service.mapper.VideoDeviceMapper;
import cn.cuiot.dmp.video.service.vendor.bean.resp.vsuap.VsuapDeviceListResp;
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
import java.util.stream.Collectors;

/**
 * 监控-设备信息 业务层
 *
 * @Author: zc
 * @Date: 2024-08-12
 */
@Service
public class VideoDeviceService extends ServiceImpl<VideoDeviceMapper, VideoDeviceEntity> {

    /**
     * 停用设备（不填deviceId修改全部设备）
     *
     * @Param deviceId 第三方设备id
     */
    public void disableDevice(String deviceId) {
        LambdaUpdateWrapper<VideoDeviceEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(VideoDeviceEntity::getStatus, EntityConstants.DISABLED);
        wrapper.eq(StringUtils.isNotBlank(deviceId), VideoDeviceEntity::getDeviceId, deviceId);

        update(wrapper);
    }

    /**
     * 查询在线的设备
     *
     * @return List<VideoDeviceEntity> 列表
     */
    public IPage<VideoDeviceEntity> queryEnableDevicePage(Page<VideoDeviceEntity> page, Integer state) {
        LambdaQueryWrapper<VideoDeviceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoDeviceEntity::getStatus, EntityConstants.DISABLED);
        wrapper.eq(VideoDeviceEntity::getState, state);

        return page(page, wrapper);
    }

    /**
     * 同步设备数据
     *
     * @Param data 参数
     */
    public void syncDevices(List<VsuapDeviceListResp> data) {
        // 获取设备id列表
        List<String> deviceIds = data.stream()
                .map(VsuapDeviceListResp::getDeviceId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());

        // 根据设备列表查询数据
        List<VideoDeviceEntity> dbList = list(new LambdaQueryWrapper<VideoDeviceEntity>()
                .in(VideoDeviceEntity::getDeviceId, deviceIds));
        Map<String, VideoDeviceEntity> dbMap = dbList.stream().collect(Collectors.toMap(VideoDeviceEntity::getDeviceId, e -> e));

        // 构建VideoDeviceEntity
        List<VideoDeviceEntity> collect = data.stream().map(item -> {
            VideoDeviceEntity aDefault = dbMap.getOrDefault(item.getDeviceId(), new VideoDeviceEntity());
            BeanUtils.copyProperties(item, aDefault);
            aDefault.setStatus(EntityConstants.ENABLED);
            return aDefault;
        }).collect(Collectors.toList());

        saveOrUpdateBatch(collect);
    }
}
