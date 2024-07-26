package cn.cuiot.dmp.digitaltwin.service.service;

import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.digitaltwin.service.dto.GwFirefightRealTimeAlarmDto;
import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightRealTimeAlarmEntity;
import cn.cuiot.dmp.digitaltwin.service.mapper.GwFirefightRealTimeAlarmMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 格物消防-实时报警 业务层
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Service
public class GwFirefightRealTimeAlarmService extends ServiceImpl<GwFirefightRealTimeAlarmMapper, GwFirefightRealTimeAlarmEntity> {

    /**
     * 保存
     *
     * @Param dto 参数
     */
    public void save(GwFirefightRealTimeAlarmDto dto) {
        GwFirefightRealTimeAlarmEntity alarmEntity = new GwFirefightRealTimeAlarmEntity();
        BeanUtils.copyProperties(dto, alarmEntity);
        if (StringUtils.isNotBlank(dto.getReportTime())) {
            alarmEntity.setReportTime(DateTimeUtil.stringToDate(dto.getReportTime()));
        }
        save(alarmEntity);
    }
}
