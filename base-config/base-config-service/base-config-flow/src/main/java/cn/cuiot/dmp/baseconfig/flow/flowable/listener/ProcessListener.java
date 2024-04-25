package cn.cuiot.dmp.baseconfig.flow.flowable.listener;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static cn.cuiot.dmp.baseconfig.flow.constants.CommonConstants.BUSINESS_STATUS_4;
import static cn.cuiot.dmp.baseconfig.flow.constants.CommonConstants.PROCESS_STATUS;


/**
 * @author LoveMyOrange
 * @create 2022-10-15 19:47
 */
@Component
public class ProcessListener implements ExecutionListener {
    @Resource
    private RepositoryService repositoryService;
    @Override
    public void notify(DelegateExecution execution) {
        execution.setVariable(PROCESS_STATUS,BUSINESS_STATUS_4);
    }
}
