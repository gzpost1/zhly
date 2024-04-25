package cn.cuiot.dmp.baseconfig.flow.flowable.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: 定时器监听器
 * @author LoveMyOrange
 * @create 2022-10-15 19:47
 */
@Component
@Slf4j
public class TimerListener implements ExecutionListener {
    @Resource
    private RepositoryService repositoryService;

    /**
     * 自行编写 触发逻辑既可
     * @param execution
     */
    @Override
    public void notify(DelegateExecution execution) {
        log.info("========");
        System.err.println("exection触发了");
    }
}
