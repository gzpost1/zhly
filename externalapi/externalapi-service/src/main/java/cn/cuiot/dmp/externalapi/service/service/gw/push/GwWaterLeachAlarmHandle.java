package cn.cuiot.dmp.externalapi.service.service.gw.push;

import cn.cuiot.dmp.externalapi.service.constant.GwBusinessTypeConstant;
import cn.cuiot.dmp.externalapi.service.entity.gw.waterleachalarm.GwWaterLeachAlarmFaultRecordEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.push.DeviceWaterLeachAlarmEventParams;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.DataItem;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.DeviceServiceOutParams;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.GwHead;
import cn.cuiot.dmp.externalapi.service.service.gw.waterleachalarm.GwWaterLeachAlarmFaultRecordService;
import cn.cuiot.dmp.externalapi.service.utils.GwPushUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 格物水浸报警器-推送数据统一处理
 *
 * @Author: zc
 * @Date: 2024-09-12
 */
@Slf4j
@Component
public class GwWaterLeachAlarmHandle implements GwBusinessStrategy{

    @Autowired
    private GwWaterLeachAlarmFaultRecordService waterLeachAlarmFaultRecordService;
    @Override
    public void serviceHandle(GwHead head, DeviceServiceOutParams<Object> obj, Long dataId) {

    }

    @Override
    public void eventHandle(List<DataItem<Object>> params, Long dataId) {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        DeviceWaterLeachAlarmEventParams body = GwPushUtil.getBody(params, DeviceWaterLeachAlarmEventParams.class);
        if (Objects.nonNull(body)) {
            GwWaterLeachAlarmFaultRecordEntity recordEntity = new GwWaterLeachAlarmFaultRecordEntity();
            recordEntity.setErrorCode(body.getErrorCode());
            recordEntity.setDeviceId(dataId);

            waterLeachAlarmFaultRecordService.save(recordEntity);
        }
    }

    @Override
    public String getBusinessType() {
        return GwBusinessTypeConstant.WATER_LEACH_ALARM;
    }
}
