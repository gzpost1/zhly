package cn.cuiot.dmp.externalapi.service.service.gw.push;

import cn.cuiot.dmp.common.bean.external.GWCurrencyBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.service.constant.GwBusinessTypeConstant;
import cn.cuiot.dmp.externalapi.service.constant.GwEntranceGuardServiceKeyConstant;
import cn.cuiot.dmp.externalapi.service.entity.gw.*;
import cn.cuiot.dmp.externalapi.service.query.gw.push.DeviceEventParams;
import cn.cuiot.dmp.externalapi.service.query.gw.push.DeviceEventSmogParams;
import cn.cuiot.dmp.externalapi.service.query.gw.push.DeviceParametersParams;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.DataItem;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.DeviceServiceOutParams;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.GwHead;
import cn.cuiot.dmp.externalapi.service.service.gw.*;
import cn.cuiot.dmp.externalapi.service.utils.GwPushUtil;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.InvokeDeviceServiceReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.dmp.DmpDeviceRemoteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
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
