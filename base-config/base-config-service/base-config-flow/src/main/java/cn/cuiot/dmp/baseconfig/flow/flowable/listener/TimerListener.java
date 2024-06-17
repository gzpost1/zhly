package cn.cuiot.dmp.baseconfig.flow.flowable.listener;

import cn.cuiot.dmp.base.infrastructure.utils.SpringContextHolder;
import cn.cuiot.dmp.baseconfig.flow.enums.TimeLimitHandleEnums;
import cn.cuiot.dmp.baseconfig.flow.enums.WorkBusinessEnums;
import cn.cuiot.dmp.baseconfig.flow.enums.WorkOrderStatusEnums;
import cn.cuiot.dmp.baseconfig.flow.flowable.msg.MsgSendService;
import cn.cuiot.dmp.baseconfig.flow.service.WorkBusinessTypeInfoService;
import cn.cuiot.dmp.baseconfig.flow.service.WorkInfoService;
import cn.cuiot.dmp.common.constant.MsgDataType;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BoundaryEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants.TIME_HANDLER_TYPE;


/**
 * @author LoveMyOrange
 * @create 2022-10-15 19:47
 */
@Component
@Slf4j
public class TimerListener implements ExecutionListener {
    @Resource
    private TaskService taskService;
    @Autowired
    private WorkBusinessTypeInfoService workBusinessTypeInfoService;
    @Autowired
    private WorkInfoService workInfoService;
    @Autowired
    private MsgSendService msgSendService;

    @Override
    public void notify(DelegateExecution execution) {
        log.info("TimerListener超时限制执行========");
        //获取当前节点信息
        String handlerType = (String) execution.getVariable(TIME_HANDLER_TYPE);
        log.info("当前节点超时限制设置信息：{}", handlerType);

        //获取当前节点信息下的所有任务
        BoundaryEvent currentFlowElement = (BoundaryEvent) execution.getCurrentFlowElement();
        UserTask userTask = (UserTask) currentFlowElement.getAttachedToRef();

        //找出没有处理过的任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionId(execution.getProcessDefinitionId())
                .taskDefinitionKey(userTask.getId())
                .processInstanceId(execution.getProcessInstanceId())
                .list();

        boolean nodeSupend = workInfoService.querySuspendTaskIds(execution.getProcessInstanceId());

        if (StringUtils.isNotBlank(handlerType) && CollUtil.isNotEmpty(list) && !nodeSupend) {
            //保存超时信息
            workBusinessTypeInfoService.saveBusinessInfo(list.get(0), userTask, WorkBusinessEnums.TIMEOUT,null);

            //更新工单主表超时信息
            workInfoService.updateWorkTimeOutInfo(WorkBusinessEnums.TIMEOUT.getCode(),Long.valueOf(execution.getProcessInstanceId()));
            if (StringUtils.equals(handlerType, TimeLimitHandleEnums.DO_NOTHING.getCode())) {
                //什么都不做

            } else if (StringUtils.equals(handlerType, TimeLimitHandleEnums.TO_END.getCode())) {
                //结束流程
                RuntimeService runtimeService = SpringContextHolder.getBean(RuntimeService.class);
                runtimeService.deleteProcessInstance(execution.getProcessInstanceId(), TimeLimitHandleEnums.TO_END.getProcessComment());
                workBusinessTypeInfoService.saveBusinessInfo(list.get(0), userTask, WorkBusinessEnums.CLOSE,null);
                //更新工单信息
                workInfoService.updateWorkInfo(WorkOrderStatusEnums.terminated.getStatus(), Long.valueOf(execution.getProcessInstanceId()));

                //发送消息
                msgSendService.sendProcess(execution.getProcessInstanceId(), MsgDataType.WORK_INFO_CANCEL);

            } else if (StringUtils.equals(handlerType, TimeLimitHandleEnums.TO_SUSPEND.getCode())) {

                //保存超时信息和挂起信息
                workBusinessTypeInfoService.saveBusinessInfo(list.get(0), userTask, WorkBusinessEnums.SUSPEND,null);

                //更新工单信息
                workInfoService.updateWorkInfo(WorkOrderStatusEnums.Suspended.getStatus(), Long.valueOf(execution.getProcessInstanceId()));

            } else if (StringUtils.equals(handlerType, TimeLimitHandleEnums.TO_APPROVE.getCode())) {

                ProcessEngineConfigurationImpl processEngineConfiguration = CommandContextUtil.getProcessEngineConfiguration();

                //自动通过
                for (Task task : list) {
                    TaskEntity taskEntity = processEngineConfiguration.getTaskServiceConfiguration().getTaskService().getTask(task.getId());
                    if(!taskEntity.isDeleted()){
                        taskService.complete(task.getId());
                    }
                }

                workBusinessTypeInfoService.saveBusinessInfo(list.get(0), userTask, WorkBusinessEnums.BUSINESS_AGREE,null);
            }else {

            }
        }
    }



}
