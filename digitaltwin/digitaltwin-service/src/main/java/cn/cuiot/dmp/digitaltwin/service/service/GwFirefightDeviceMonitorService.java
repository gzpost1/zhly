package cn.cuiot.dmp.digitaltwin.service.service;

import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.digitaltwin.service.dto.GwFirefightDeviceMonitorDto;
import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightDeviceMonitorEntity;
import cn.cuiot.dmp.digitaltwin.service.mapper.GwFirefightDeviceMonitorMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 格物消防-设备监测 业务层
 *
 * @Author: zc
 * @Date: 2024-06-16
 */
@Service
public class GwFirefightDeviceMonitorService extends ServiceImpl<GwFirefightDeviceMonitorMapper, GwFirefightDeviceMonitorEntity> {

    /**
     * 保存
     *
     * @Param dto 参数
     */
    public void save(GwFirefightDeviceMonitorDto dto) {
        GwFirefightDeviceMonitorEntity monitorEntity = new GwFirefightDeviceMonitorEntity();
        BeanUtils.copyProperties(dto, monitorEntity);
        if (StringUtils.isNotBlank(dto.getReportTime())) {
            monitorEntity.setReportTime(DateTimeUtil.stringToDate(dto.getReportTime()));
        }
        save(monitorEntity);
    }
}
