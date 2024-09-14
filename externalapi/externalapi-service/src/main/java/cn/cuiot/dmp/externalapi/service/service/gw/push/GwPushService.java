package cn.cuiot.dmp.externalapi.service.service.gw.push;

import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwDeviceRelationEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.*;
import cn.cuiot.dmp.externalapi.service.service.gw.GwDeviceRelationService;
import cn.cuiot.dmp.externalapi.service.utils.GwPushUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * DMP 格物数据推送
 *
 * @Author: zc
 * @Date: 2024-09-11
 */
@Service
public class GwPushService {

    @Autowired
    private GwDeviceRelationService gwDeviceRelationService;
    @Autowired
    private RedisUtil redisUtil;

    private static final String CACHE_KEY = "gw:";

    /**
     * 数据处理
     */
    public void invokeDeviceService(DeviceServiceResponse<Object> query) {
        //设备服务输出参数
        DeviceServiceOutParams<Object> params = query.getDeviceServiceOutParams();
        //产品key
        String productKey = query.getProductKey();
        //设备key
        String deviceKey = query.getDeviceKey();
        //获取head
        GwHead head = GwPushUtil.getHead(params.getData());
        if (Objects.nonNull(head)) {
            GwDeviceRelationEntity deviceRelation = getBusinessType(query.getProductKey(), query.getDeviceKey());
            if (Objects.isNull(deviceRelation)) {
                throw new BusinessException(ResultCode.ERROR, "数据格物转发数据异常,无关联设备数据.....productKey：{}，deviceKey{}", productKey, deviceKey);
            }

            //cachekey = gw:业务类型：产品key_设备Key:物模型key:messageId
            String cacheKey = CACHE_KEY + deviceRelation.getBusinessType() + ":" + productKey + "_" + deviceKey + ":" + query.getDeviceServiceKey() + ":" + params.getMessageId();
            String dataIdStr = redisUtil.get(cacheKey);

            if (StringUtils.isNotBlank(dataIdStr)) {
                //业务处理
                long dataId = Long.parseLong(dataIdStr);
                GwBusinessHandlerFactory.getInstance().serviceHandle(head, params, deviceRelation.getBusinessType(), dataId);

                redisUtil.del(cacheKey);
            }
        }
    }

    /**
     * 数据处理
     */
    public void deviceEvent(DeviceEventResponse<Object> query) {
        //设备服务输出参数
        List<DataItem<Object>> list = query.getDeviceEventOutParams();
        //产品key
        String productKey = query.getProductKey();
        //设备key
        String deviceKey = query.getDeviceKey();

        GwDeviceRelationEntity deviceRelation = getBusinessType(query.getProductKey(), query.getDeviceKey());
        if (Objects.isNull(deviceRelation)) {
            throw new BusinessException(ResultCode.ERROR, "数据格物转发数据异常,无关联设备数据.....productKey：{}，deviceKey{}", productKey, deviceKey);
        }

        GwBusinessHandlerFactory.getInstance().eventHandle(list, deviceRelation.getBusinessType(), deviceRelation.getId());
    }

    /**
     * 根据产品key和设备key获取业务类型
     *
     * @return String 业务类型
     * @Param productKey 产品key
     * @Param deviceKey 设备key
     */
    private GwDeviceRelationEntity getBusinessType(String productKey, String deviceKey) {
        GwDeviceRelationEntity relation = new GwDeviceRelationEntity();
        relation.setProductKey(productKey);
        relation.setDeviceKey(deviceKey);
        return gwDeviceRelationService.gwDeviceRelation(relation);
    }
}
