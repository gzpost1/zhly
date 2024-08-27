package cn.cuiot.dmp.externalapi.service.service.video;

import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.externalapi.service.entity.video.VideoDeviceEntity;
import cn.cuiot.dmp.externalapi.service.mapper.video.VideoDeviceMapper;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapDeviceListResp;
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
     * @Param companyId 企业id
     */
    public void disableDevice(String deviceId, Long companyId) {
        LambdaUpdateWrapper<VideoDeviceEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(VideoDeviceEntity::getStatus, EntityConstants.DISABLED);
        wrapper.eq(StringUtils.isNotBlank(deviceId), VideoDeviceEntity::getDeviceId, deviceId);
        wrapper.eq(VideoDeviceEntity::getCompanyId, companyId);

        update(wrapper);
    }

    /**
     * 查询在线的设备
     *
     * @return List<VideoDeviceEntity> 列表
     */
    public IPage<VideoDeviceEntity> queryEnableDevicePage(Page<VideoDeviceEntity> page, Integer state) {
        LambdaQueryWrapper<VideoDeviceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoDeviceEntity::getStatus, EntityConstants.ENABLED);
        wrapper.eq(VideoDeviceEntity::getState, state);

        return page(page, wrapper);
    }

    /**
     * 同步设备数据
     *
     * @Param data 参数
     */
    public void syncDevices(List<VsuapDeviceListResp> data, PlatfromInfoRespDTO platfromInfoRespDTO) {
        // 获取设备id列表
        List<String> deviceIds = data.stream()
                .map(VsuapDeviceListResp::getDeviceId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());

        // 根据设备列表查询数据
        List<VideoDeviceEntity> dbList = list(new LambdaQueryWrapper<VideoDeviceEntity>()
                .eq(VideoDeviceEntity::getCompanyId, platfromInfoRespDTO.getCompanyId())
                .in(VideoDeviceEntity::getDeviceId, deviceIds));
        Map<String, VideoDeviceEntity> dbMap = dbList.stream().collect(Collectors.toMap(VideoDeviceEntity::getDeviceId, e -> e));

        // 构建VideoDeviceEntity
        List<VideoDeviceEntity> collect = data.stream().map(item -> {
            VideoDeviceEntity aDefault = dbMap.getOrDefault(item.getDeviceId(), new VideoDeviceEntity());
            BeanUtils.copyProperties(item, aDefault);
            aDefault.setStatus(EntityConstants.ENABLED);
            aDefault.setCompanyId(platfromInfoRespDTO.getCompanyId());
            aDefault.setSecret(Sm4.encryption(platfromInfoRespDTO.getData()));
            return aDefault;
        }).collect(Collectors.toList());

        saveOrUpdateBatch(collect);
    }
}
