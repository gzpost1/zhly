package cn.cuiot.dmp.baseconfig.flow.flowable.listener;

import cn.cuiot.dmp.base.infrastructure.utils.SpringContextHolder;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants.*;


/**
 * @author LoveMyOrange
 * @create 2022-10-15 14:51
 * @desc 精髓代码实现2 ,看下如何实现的审批人为空自动通过的逻辑
 */
@Component
public class TaskCreatedListener implements TaskListener {
    @Resource
    private TaskService taskService;
    @Override
    public void notify(DelegateTask delegateTask) {
            if(DEFAULT_NULL_ASSIGNEE.equals(delegateTask.getAssignee())){
                Object autoRefuse = delegateTask.getVariable(AUTO_REFUSE_STR);
                if(autoRefuse==null){
                    taskService.addComment(delegateTask.getId(),delegateTask.getProcessInstanceId(),OPINION_COMMENT,"审批人为空,自动通过");
                    taskService.complete(delegateTask.getId());
                }
                else{
                    taskService.addComment(delegateTask.getId(),delegateTask.getProcessInstanceId(),OPINION_COMMENT,"审批人为空,自动驳回");
                    RuntimeService runtimeService = SpringContextHolder.getBean(RuntimeService.class);
                    runtimeService.deleteProcessInstance(delegateTask.getProcessInstanceId(),"审批人为空,自动驳回");
                }
            }
    }
}
