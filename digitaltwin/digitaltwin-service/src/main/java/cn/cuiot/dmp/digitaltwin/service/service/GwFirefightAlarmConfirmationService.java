package cn.cuiot.dmp.digitaltwin.service.service;

import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.digitaltwin.service.dto.GwFirefightAlarmConfirmationDto;
import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightAlarmConfirmationEntity;
import cn.cuiot.dmp.digitaltwin.service.mapper.GwFirefightAlarmConfirmationMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 格物消防-报警确认 业务层
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Service
public class GwFirefightAlarmConfirmationService extends ServiceImpl<GwFirefightAlarmConfirmationMapper, GwFirefightAlarmConfirmationEntity> {

    /**
     * 保存
     *
     * @Param dto 参数
     */
    public void save(GwFirefightAlarmConfirmationDto dto) {
        GwFirefightAlarmConfirmationEntity alarmEntity = new GwFirefightAlarmConfirmationEntity();
        BeanUtils.copyProperties(dto, alarmEntity);
        if (StringUtils.isNotBlank(dto.getReportTime())) {
            alarmEntity.setReportTime(DateTimeUtil.stringToDate(dto.getReportTime()));
        }
        save(alarmEntity);
    }

}
