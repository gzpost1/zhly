package cn.cuiot.dmp.externalapi.service.service.gw.push;

import cn.cuiot.dmp.externalapi.service.query.gw.push.base.DataItem;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.DeviceServiceOutParams;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.GwHead;

import java.util.List;

/**
 * @Author: zc
 * @Date: 2024-09-12
 */
public interface GwBusinessStrategy {

    /**
     * 服务调用数据处理
     *
     * @param head   头部参数
     * @param obj    参数
     * @param dataId 业务数据id
     */
    void serviceHandle(GwHead head, DeviceServiceOutParams<Object> obj, Long dataId);

    /**
     * 事件处理
     *
     * @param params 参数
     * @param dataId 业务数据id
     */
    void eventHandle(List<DataItem<Object>> params, Long dataId);

    /**
     * 返回该处理器支持的业务类型
     *
     * @return String 业务类型
     */
    String getBusinessType();
}
