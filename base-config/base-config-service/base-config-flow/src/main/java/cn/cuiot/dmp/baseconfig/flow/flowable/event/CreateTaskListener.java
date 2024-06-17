package cn.cuiot.dmp.baseconfig.flow.flowable.event;

import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.ChildNode;
import cn.cuiot.dmp.baseconfig.flow.flowable.msg.MsgSendService;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.impl.event.FlowableEntityEventImpl;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants.*;
import static cn.cuiot.dmp.baseconfig.flow.utils.BpmnModelUtils.getChildNode;


/**
 * @Author: 土豆仙
 * @Date: 2021/8/9 17:17
 * @Description: create：在任务创建且所有任务属性设置完成之后才触发。
 * assignment：在任务被分配给某个班里人之后触发，它是在create事件触发前被触发。
 * complete：在配置了监听器的任务完成时触发，也就是说运行期任务删除之前触发。
 * delete：任务删除触发
 */
@Component
@Slf4j
public class CreateTaskListener extends AbstractFlowableEngineEventListener {

    @Autowired
    private ProcessEngine processEngine;
    @Resource
    private RepositoryService repositoryService;
    @Autowired
    private MsgSendService msgSendService;
    @Resource
    private TaskService taskService;

    @Override
    protected void taskCreated(FlowableEngineEntityEvent event) {

        TaskEntity taskEntity = (TaskEntity) ((FlowableEntityEventImpl) event).getEntity();
        Object rollback = processEngine.getTaskService().getVariable(taskEntity.getId(), "rollback");
        String taskDefinitionId = taskEntity.getTaskDefinitionKey();

        if (!StringUtils.equals(taskDefinitionId, "root")) {
            Process mainProcess = repositoryService.getBpmnModel(taskEntity.getProcessDefinitionId()).getMainProcess();
            String dingDing = mainProcess.getAttributeValue(FLOWABLE_NAME_SPACE, FLOWABLE_NAME_SPACE_NAME);
            JSONObject jsonObject = JSONObject.parseObject(dingDing, new TypeReference<JSONObject>() {
            });
            String processJson = jsonObject.getString(VIEW_PROCESS_JSON_NAME);
            ChildNode childNode = JSONObject.parseObject(processJson, new TypeReference<ChildNode>() {
            });
            ChildNode currentNode = getChildNode(childNode, taskEntity.getTaskDefinitionKey());
            taskDefinitionId = currentNode.getType();
        }

        //审批人
        String assignee = taskEntity.getAssignee();
        if (!StringUtils.equals(assignee, DEFAULT_NULL_ASSIGNEE)) {
            msgSendService.sendProcess(taskEntity.getProcessInstanceId(), taskDefinitionId, rollback != null && Boolean.valueOf(rollback.toString()), assignee);
        }


        ProcessEngineConfigurationImpl processEngineConfiguration = CommandContextUtil.getProcessEngineConfiguration();
        taskEntity = processEngineConfiguration.getTaskServiceConfiguration().getTaskService().getTask(taskEntity.getId());
        if (!taskEntity.isDeleted()) {
            processEngine.getTaskService().setVariable(taskEntity.getId(), "rollback", "false");
        }
    }


}
