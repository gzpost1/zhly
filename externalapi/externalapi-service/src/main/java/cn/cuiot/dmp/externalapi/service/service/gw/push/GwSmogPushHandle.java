package cn.cuiot.dmp.externalapi.service.service.gw.push;

import cn.cuiot.dmp.externalapi.service.constant.GwBusinessTypeConstant;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwSmogEventEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.push.DeviceEventSmogParams;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.DataItem;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.DeviceServiceOutParams;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.GwHead;
import cn.cuiot.dmp.externalapi.service.service.gw.GwSmogEventService;
import cn.cuiot.dmp.externalapi.service.utils.GwPushUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 格物门禁-推送数据统一处理
 *
 * @Author: zc
 * @Date: 2024-09-12
 */
@Slf4j
@Component
public class GwSmogPushHandle implements GwBusinessStrategy {

    @Autowired
    private GwSmogEventService gwSmogEventService;


    @Override
    public void serviceHandle(GwHead head, DeviceServiceOutParams<Object> obj, Long dataId) {
    }

    @Override
    public void eventHandle(List<DataItem<Object>> params, Long dataId) {
        GwHead head = GwPushUtil.getHead(params);
        DeviceEventSmogParams body = GwPushUtil.getBody(params, DeviceEventSmogParams.class);
        GwSmogEventEntity gwSmogEventEntity = new GwSmogEventEntity();
        gwSmogEventEntity.setDeviceId(dataId);
        gwSmogEventEntity.setAlarmCode(Optional.ofNullable(body).map(vo->vo.getAlarm()).map(vo->vo.getAlarmCode()).orElse(null));
        gwSmogEventEntity.setAlarmData(Optional.ofNullable(body).map(vo->vo.getAlarm()).map(vo->vo.getAlarmData()).orElse(null));
        gwSmogEventEntity.setBattery(body.getBattery());
        gwSmogEventEntity.setConnectivity(body.getConnectivity());
        gwSmogEventEntity.setDeviceInfo(body.getDeviceInfo());
        gwSmogEventService.save(gwSmogEventEntity);
    }


    @Override
    public String getBusinessType() {
        return GwBusinessTypeConstant.SMOG_ALARM;
    }
}
