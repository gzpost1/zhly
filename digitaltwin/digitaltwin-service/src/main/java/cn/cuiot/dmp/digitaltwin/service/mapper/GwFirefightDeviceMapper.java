package cn.cuiot.dmp.digitaltwin.service.mapper;

import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightDeviceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 格物消防-设备信息 mapper接口
 *
 * @Author: zc
 * @Date: 2024-06-16
 */
public interface GwFirefightDeviceMapper extends BaseMapper<GwFirefightDeviceEntity> {
    /**
     * 根据设备id查询数据
     *
     * @return GwFirefightDeviceEntity
     * @Param deviceId 设备id
     */
    GwFirefightDeviceEntity getDeviceByDeviceId(String deviceId);

    void deleteByDeviceId(String deviceId);
}