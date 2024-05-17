package cn.cuiot.dmp.baseconfig.flow.flowable.listener;

import cn.cuiot.dmp.base.infrastructure.utils.SpringContextHolder;
import cn.cuiot.dmp.baseconfig.flow.enums.TimeLimitHandleEnums;
import cn.cuiot.dmp.baseconfig.flow.enums.WorkBusinessEnums;
import cn.hutool.core.collection.CollUtil;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BoundaryEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.cuiot.dmp.baseconfig.flow.service.WorkBusinessTypeInfoService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cn.cuiot.dmp.baseconfig.flow.entity.WorkBusinessTypeInfoEntity;

import static cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants.*;


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
                .list();

        if (StringUtils.isNotBlank(handlerType) && CollUtil.isNotEmpty(list)) {
            //保存超时信息
            for (Task task : list) {
                workBusinessTypeInfoService.saveBusinessInfo(task, userTask, WorkBusinessEnums.TIMEOUT,null);
            }

            if (StringUtils.equals(handlerType, TimeLimitHandleEnums.DO_NOTHING.getCode())) {
                //什么都不做

            } else if (StringUtils.equals(handlerType, TimeLimitHandleEnums.TO_END.getCode())) {
                //结束流程
                RuntimeService runtimeService = SpringContextHolder.getBean(RuntimeService.class);
                runtimeService.deleteProcessInstance(execution.getProcessInstanceId(), TimeLimitHandleEnums.TO_END.getProcessComment());

            } else if (StringUtils.equals(handlerType, TimeLimitHandleEnums.TO_SUSPEND.getCode())) {

                //挂起流程
                for (Task task : list) {
                    taskService.addComment(task.getId(), execution.getProcessInstanceId(), BUSINESS_PENDING, TimeLimitHandleEnums.TO_SUSPEND.getProcessComment());

                    //保存超时信息和挂起信息
                    workBusinessTypeInfoService.saveBusinessInfo(task, userTask, WorkBusinessEnums.SUSPEND,null);
                }
            } else if (StringUtils.equals(handlerType, TimeLimitHandleEnums.TO_APPROVE.getCode())) {
                //自动通过
                for (Task task : list) {
                    taskService.addComment(execution.getSuperExecutionId(), execution.getProcessInstanceId(), OPINION_COMMENT, TimeLimitHandleEnums.TO_APPROVE.getProcessComment());
                    taskService.complete(task.getId());
                }
            }else {

            }
        }
    }



}
