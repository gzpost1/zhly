package cn.cuiot.dmp.baseconfig.flow.flowable.event;

import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEventDispatcher;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @Author: 土豆仙
 * @Date: 2021/7/12 15:34
 * @Description: flowable全局事件配置
 */
@Configuration
public class FlowableGlobListenerConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private SpringProcessEngineConfiguration configuration;

    @Autowired
    private CreateTaskListener createTaskListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        FlowableEventDispatcher dispatcher = configuration.getEventDispatcher();
        dispatcher.addEventListener(createTaskListener, FlowableEngineEventType.TASK_CREATED);
    }
}
