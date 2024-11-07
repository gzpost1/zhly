package cn.cuiot.dmp.baseconfig.flow.flowable.listener;

import cn.cuiot.dmp.base.infrastructure.utils.SpringContextHolder;
import cn.cuiot.dmp.baseconfig.flow.enums.WorkBusinessEnums;
import cn.cuiot.dmp.baseconfig.flow.flowable.msg.MsgSendService;
import cn.cuiot.dmp.baseconfig.flow.service.WorkBusinessTypeInfoService;
import cn.cuiot.dmp.common.constant.MsgDataType;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private MsgSendService msgSendService;
    @Autowired
    private WorkBusinessTypeInfoService workBusinessTypeInfoService;

    @Override
    public void notify(DelegateTask delegateTask) {
            if(DEFAULT_NULL_ASSIGNEE.equals(delegateTask.getAssignee())){
                Object autoRefuse = delegateTask.getVariable(AUTO_REFUSE_STR);
                if(autoRefuse==null){
                    taskService.addComment(delegateTask.getId(),delegateTask.getProcessInstanceId(),OPINION_COMMENT,"审批人为空,自动通过");
                    taskService.complete(delegateTask.getId());

                    UserTask userTask = new UserTask();
                    userTask.setId(delegateTask.getTaskDefinitionKey());

                    TaskEntityImpl taskEntity = new TaskEntityImpl();
                    taskEntity.setId(delegateTask.getId());
                    taskEntity.setProcessInstanceId(delegateTask.getProcessInstanceId());
                    workBusinessTypeInfoService.saveBusinessInfo(taskEntity, userTask, WorkBusinessEnums.AUTOMATIC_PASS,"审批人为空,自动通过");
                }
                else{
                    taskService.addComment(delegateTask.getId(),delegateTask.getProcessInstanceId(),OPINION_COMMENT,"审批人为空,自动驳回");
                    RuntimeService runtimeService = SpringContextHolder.getBean(RuntimeService.class);
                    runtimeService.deleteProcessInstance(delegateTask.getProcessInstanceId(),"审批人为空,自动驳回");

                    //发送消息
                    msgSendService.sendProcess(delegateTask.getProcessInstanceId(), MsgDataType.WORK_INFO_TURNDOWN);
                }
            }else if(DEFAULT_NULL_SUSPEND.equals(delegateTask.getAssignee())){
                //保存超时信息和挂起信息
                UserTask userTask = new UserTask();
                userTask.setId(delegateTask.getTaskDefinitionKey());

                TaskEntityImpl taskEntity = new TaskEntityImpl();
                taskEntity.setId(delegateTask.getId());
                taskEntity.setProcessInstanceId(delegateTask.getProcessInstanceId());
                workBusinessTypeInfoService.saveBusinessInfo(taskEntity, userTask, WorkBusinessEnums.SUSPEND,"审批人为空,自动挂起");
            }
    }
}
