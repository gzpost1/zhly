package cn.cuiot.dmp.baseconfig.flow.service;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import org.flowable.bpmn.model.Activity;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.MultiInstanceLoopCharacteristics;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.Serializable;

/**
 * @author pengjian
 * @create 2024/6/13 16:08
 */
public class AddMultiInstanceUserTaskCmd implements Command, Serializable {

    //多实例总数
    protected static final String NUMBER_OF_INSTANCES = "nrOfInstances";
    //当前活动的实例数，即尚未完成的实例数。对于串行多实 例，这个值总是1。
    protected static final String NUMBER_OF_ACTIVE_INSTANCES = "nrOfActiveInstances";

    protected String taskId;
    protected String assignee;
    protected Integer instance ;

    public AddMultiInstanceUserTaskCmd(String taskId, String assignee,Integer instance) {
        this.taskId = taskId;
        this.assignee = assignee;
        this.instance=instance;
    }
    @Override
    public Object execute(CommandContext commandContext) {
        //查询当前任务实例
        TaskEntity taskEntity =  CommandContextUtil.getTaskService(commandContext).getTask(this.taskId);
        if (taskEntity == null) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION,"id为" + this.taskId + "的任务不存在");
        }
        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager(commandContext);
        //查询当前任务实例所对应的执行实例
        ExecutionEntity executionEntity = executionEntityManager.findById(taskEntity.getExecutionId());
        //查询多实例的根执行实例
        ExecutionEntity miExecution = executionEntityManager.findFirstMultiInstanceRoot(executionEntity);
        if (miExecution == null) {
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION,"节点 " + taskEntity.getTaskDefinitionKey() + "不是多实例任务");
        }

        BpmnModel bpmnModel = ProcessDefinitionUtil.getBpmnModel(miExecution.getProcessDefinitionId());
        Activity miActivityElement = (Activity) bpmnModel.getFlowElement(miExecution.getActivityId());
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = miActivityElement.getLoopCharacteristics();
        //解析多实例activiti:collection配置的表达式中的变量
        String collectionKey = getCollectionKey(multiInstanceLoopCharacteristics);
        //查询多实例activiti:collection配置的表达式中的变量的值
        List<String> collectionValue =  (List)miExecution.getVariable(collectionKey);
//        if (collectionValue.contains(assignee)) {
//            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION,"加签用户 " + assignee + "已经在审批名单中");
//        }
        //往变量中加入加签用户
        collectionValue.add(assignee);
        miExecution.setVariable(collectionKey, collectionValue);

        //更新nrOfInstances变量
        Integer currentNumberOfInstances = (Integer) miExecution.getVariable(NUMBER_OF_INSTANCES);
        miExecution.setVariableLocal(NUMBER_OF_INSTANCES, instance);

        //如果是并行多实例还需要做额外操作
        if (!multiInstanceLoopCharacteristics.isSequential()) {
            //更新nrOfActiveInstances变量
            Integer nrOfActiveInstances = (Integer) miExecution.getVariable(NUMBER_OF_ACTIVE_INSTANCES);
            miExecution.setVariableLocal(NUMBER_OF_ACTIVE_INSTANCES, instance);

            //创建加签任务的执行实例
            ExecutionEntity childExecution = executionEntityManager.createChildExecution(miExecution);
            childExecution.setCurrentFlowElement(miActivityElement);
            //设置加签任务执行实例的局部变量
            Map executionVariables = new HashMap();
            executionVariables.put(multiInstanceLoopCharacteristics.getElementVariable(), assignee);
            ParallelMultiInstanceBehavior miBehavior = (ParallelMultiInstanceBehavior) miActivityElement.getBehavior();
            executionVariables.put(miBehavior.getCollectionElementIndexVariable(),currentNumberOfInstances);
            childExecution.setVariablesLocal(executionVariables);
            //向operations中压入继续流程的操作类
            CommandContextUtil.getAgenda().planContinueMultiInstanceOperation(childExecution, miExecution, currentNumberOfInstances);
        }
        return null;
    }

    //解析出多实例activiti:collection配置的表达式中的变量
    protected String getCollectionKey(MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics) {
        String collectionKey = multiInstanceLoopCharacteristics.getInputDataItem();
        String regex = "\\$\\{(.*?)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(collectionKey);
        if (matcher.find()) {
            collectionKey = matcher.group(1);
        }
        return collectionKey;
    }
}
