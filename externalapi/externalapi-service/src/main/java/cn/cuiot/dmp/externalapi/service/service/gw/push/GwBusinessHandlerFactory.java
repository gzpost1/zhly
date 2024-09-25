package cn.cuiot.dmp.externalapi.service.service.gw.push;

import cn.cuiot.dmp.externalapi.service.query.gw.push.base.DataItem;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.DeviceServiceOutParams;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.GwHead;
import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: zc
 * @Date: 2024-09-12
 */
@Component
public class GwBusinessHandlerFactory implements ApplicationContextAware {

    private final static Map<String, GwBusinessStrategy> MAP = Maps.newHashMap();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, GwBusinessStrategy> beanTypeMap = applicationContext.getBeansOfType(GwBusinessStrategy.class);
        beanTypeMap.values().forEach(strategyObj -> MAP.put(strategyObj.getBusinessType(), strategyObj));
    }

    /**
     * 对外统一暴露的服务调用
     */
    public void serviceHandle(GwHead head, DeviceServiceOutParams<Object> params, String type, Long dataId) {
        GwBusinessStrategy strategy = MAP.get(type);
        if (Objects.isNull(strategy)) {
            throw new RuntimeException("业务类型错误");
        }
        strategy.serviceHandle(head, params, dataId);
    }

    /**
     * 对外统一暴露的服务事件
     */
    public void eventHandle(List<DataItem<Object>> params, String type, Long dataId) {
        GwBusinessStrategy strategy = MAP.get(type);
        if (Objects.isNull(strategy)) {
            throw new RuntimeException("业务类型错误");
        }
        strategy.eventHandle(params, dataId);
    }

    /**
     * 静态内部类创建单例工厂对象
     */
    private static class CreateFactorySingleton {
        private static GwBusinessHandlerFactory factory = new GwBusinessHandlerFactory();
    }

    public static GwBusinessHandlerFactory getInstance() {
        return CreateFactorySingleton.factory;
    }
}
