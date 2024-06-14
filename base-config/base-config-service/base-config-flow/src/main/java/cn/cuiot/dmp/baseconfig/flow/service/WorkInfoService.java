package cn.cuiot.dmp.baseconfig.flow.service;


import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BusinessTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.BusinessTypeRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.baseconfig.flow.constants.WorkOrderConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.*;
import cn.cuiot.dmp.baseconfig.flow.dto.app.AppTransferTaskDto;
import cn.cuiot.dmp.baseconfig.flow.dto.approval.MyApprovalResultDto;
import cn.cuiot.dmp.baseconfig.flow.dto.approval.QueryMyApprovalDto;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.*;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.Properties;
import cn.cuiot.dmp.baseconfig.flow.dto.vo.HandleDataVO;
import cn.cuiot.dmp.baseconfig.flow.dto.work.*;
import cn.cuiot.dmp.baseconfig.flow.dto.work.HandleDataDTO;
import cn.cuiot.dmp.baseconfig.flow.entity.*;
import cn.cuiot.dmp.baseconfig.flow.enums.*;
import cn.cuiot.dmp.baseconfig.flow.feign.SystemToFlowService;
import cn.cuiot.dmp.baseconfig.flow.mapper.WorkInfoMapper;
import cn.cuiot.dmp.baseconfig.flow.vo.HistoryProcessInstanceVO;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.cuiot.dmp.baseconfig.flow.constants.CommonConstants.*;
import static cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants.*;
import static cn.cuiot.dmp.baseconfig.flow.enums.BusinessInfoEnums.BUSINESS_TIME_OUT;
import static cn.cuiot.dmp.baseconfig.flow.utils.BpmnModelUtils.getChildNodeByNodeId;

/**
 * @author pengjian
 * @since 2024-04-23
 */
@Service
@Slf4j
public class WorkInfoService extends ServiceImpl<WorkInfoMapper, WorkInfoEntity>{

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private TbFlowConfigService flowConfigService;

    @Autowired
    private TbFlowCcService tbFlowCcService;
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkBusinessTypeInfoService workBusinessTypeInfoService;
    @Autowired
    private SystemApiFeignService systemApiFeignService;

    @Autowired
    private TbFlowConfigOrgService tbFlowConfigOrgService;
    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private ArchiveFeignService archiveFeignService;

    @Autowired
    private SystemToFlowService systemToFlowService;

    @Autowired
    private NodeTypeService nodeTypeService;

    @Autowired
    private WorkOrgRelService workOrgRelService;

    @Autowired
    private ProcessAndDeptService processAndDeptService;

    @Autowired
    private CommitProcessService commitProcessService;

    @Autowired
    private ManagementService managementService;


    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO start(StartProcessInstanceDTO startProcessInstanceDTO) {
        JSONObject formData = startProcessInstanceDTO.getFormData();
        UserInfo startUserInfo = startProcessInstanceDTO.getStartUserInfo();
        Authentication.setAuthenticatedUserId(startUserInfo.getId());
        Map<String,Object> processVariables= new HashMap<>();
        processVariables.put(WorkOrderConstants.FORM_VAR,formData);
        processVariables.put(WorkOrderConstants.PROCESS_STATUS,WorkOrderConstants.BUSINESS_STATUS_1);
        processVariables.put(WorkOrderConstants.START_USER_INFO,JSONObject.toJSONString(startUserInfo));
        processVariables.put(WorkOrderConstants.INITIATOR_ID,startUserInfo.getId());
        ArrayList<UserInfo> userInfos = CollUtil.newArrayList(startUserInfo);
        processVariables.put(WorkOrderConstants.USER_ROOT,JSONObject.toJSONString(userInfos));
        Map<String, List<UserInfo>> processUsers = startProcessInstanceDTO.getProcessUsers();
        if(CollUtil.isNotEmpty(processUsers)){
            Set<String> strings = processUsers.keySet();
            for (String string : strings) {
                List<UserInfo> selectUserInfo = processUsers.get(string);
                List<String> users=new ArrayList<>();
                for (UserInfo userInfo : selectUserInfo) {
                    users.add(userInfo.getId());
                }
                processVariables.put(string,users);
            }
        }

        Map formValue = JSONObject.parseObject(formData.toJSONString(), new TypeReference<Map>() {
        });
        processVariables.putAll(formValue);

        String taskId = startProcessInstanceDTO.getTaskId();
        Task task =null;
        //再次发起
        if(StringUtils.isNotBlank(startProcessInstanceDTO.getTaskId())){
            task = taskService.createTaskQuery().taskId(taskId).singleResult();
            runtimeService.setVariables(task.getProcessInstanceId(),processVariables);
        }else{
            task = startWorkOrder(startProcessInstanceDTO,processVariables);
        }

        if(Objects.nonNull(task)){
            HandleDataDTO handleDataDTO = new HandleDataDTO();
            handleDataDTO.setTaskId(task.getId());
            WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
            workBusinessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_START.getCode());
            workBusinessTypeInfoService.save(workBusinessTypeInfo);
            //保存提交的参数
            saveCommitProcess(workBusinessTypeInfo,startProcessInstanceDTO);
            taskService.complete(task.getId());
        }
        return IdmResDTO.success(task.getProcessInstanceId());
    }

    /**
     * 保存提交的信息
     * @param workBusinessTypeInfo
     * @param startProcessInstanceDTO
     */
    public void saveCommitProcess(WorkBusinessTypeInfoEntity workBusinessTypeInfo,StartProcessInstanceDTO startProcessInstanceDTO){
        CommitProcessEntity commitProcess = startProcessInstanceDTO.getCommitProcess();
        if(Objects.isNull(commitProcess)){
            return;
        }
        CommitProcessEntity entity = new CommitProcessEntity();
        entity.setId(IdWorker.getId());
        entity.setProcInstId(workBusinessTypeInfo.getProcInstId());
        entity.setUserId(LoginInfoHolder.getCurrentUserId());
        entity.setNodeId(workBusinessTypeInfo.getNode());
        entity.setDataId(commitProcess.getDataId());
        entity.setCommitProcess(commitProcess.getCommitProcess());
        entity.setCreateTime(new Date());
        entity.setBusinessTypeId(workBusinessTypeInfo.getId());
        commitProcessService.save(commitProcess);
    }
    /**
     * 启动工单
     * @param startProcessInstanceDTO
     * @param processVariables
     * @return
     */
    public Task startWorkOrder(StartProcessInstanceDTO startProcessInstanceDTO,Map<String,Object> processVariables){
        //新建
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        ProcessInstance processInstance = processInstanceBuilder
                .processDefinitionId(startProcessInstanceDTO.getProcessDefinitionId())
                .variables(processVariables)
                .businessStatus(WorkOrderConstants.BUSINESS_STATUS_1)
                .start();
        //手动完成第一个任务
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(startProcessInstanceDTO.getProcessDefinitionId())
                .singleResult();
        String flowableKey = processDefinition.getKey().replaceAll("[a-zA-Z]", "");
        TbFlowConfig flowConfig = Optional.ofNullable(flowConfigService.getById(Long.parseLong(flowableKey))).
                orElseThrow(()->new RuntimeException("流程配置为空"));
        //保存节点类型
        saveChildNode(processJson(flowConfig.getProcess()),startProcessInstanceDTO.getProcessDefinitionId());
        //保存工单信息
        List<Long> orgIds = orgIds(flowConfig.getId());
        //保存工单信息
        saveWorkInfo(flowConfig,startProcessInstanceDTO,task,orgIds);


//            saveWorkOrg(entity.getId(),orgIds);

            //保存组织与流程的关联关系
            saveProcessDefinitionAndOrgIds(startProcessInstanceDTO.getProcessDefinitionId(),orgIds);

        return task;
    }

    /**
     * 保存流程与组织的关联关系
     * @param processDefinitionId
     * @param orgIds
     */

    public void saveProcessDefinitionAndOrgIds(String processDefinitionId, List<Long> orgIds){
        if(CollectionUtils.isEmpty(orgIds)){
            return;
        }
        //判断当前流程是否已经保存了组织信息，如果保存了组织信息则不再次保存
        LambdaQueryWrapper<ProcessAndDeptEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(ProcessAndDeptEntity::getProcessDefinitionId,processDefinitionId);
        long count = processAndDeptService.count(lw);
        if(count>0){
            return;
        }
        List<ProcessAndDeptEntity> processAndDepts = new ArrayList<>();
        orgIds.stream().forEach(item->{
            ProcessAndDeptEntity entity = new ProcessAndDeptEntity();
            entity.setId(IdWorker.getId());
            entity.setProcessDefinitionId(processDefinitionId);
            entity.setOrgId(item);
            processAndDepts.add(entity);
        });
        if(CollectionUtils.isNotEmpty(processAndDepts)){
            processAndDeptService.saveBatch(processAndDepts);
        }


    }
    /**
     * 保存工单信息
     * @param flowConfig
     * @param startProcessInstanceDTO
     * @param task
     */
    public void saveWorkInfo(TbFlowConfig flowConfig,StartProcessInstanceDTO startProcessInstanceDTO,Task task,List<Long> orgIds){
        WorkInfoEntity entity = new WorkInfoEntity();
        entity.setId(IdWorker.getId());
        entity.setBusinessType(flowConfig.getBusinessTypeId());
        entity.setOrgId(LoginInfoHolder.getCurrentDeptId());
        entity.setCreateTime(new Date());
        entity.setWorkName(flowConfig.getName());
        entity.setWorkSouce(startProcessInstanceDTO.getWorkSource());
        entity.setCreateUser(LoginInfoHolder.getCurrentUserId());
        entity.setProcessDefinitionId(startProcessInstanceDTO.getProcessDefinitionId());
        //处理计划工单
        if(Objects.nonNull(startProcessInstanceDTO.getCreateUserId())){
            entity.setCreateUser(startProcessInstanceDTO.getCreateUserId());
        }
        if(Objects.isNull(startProcessInstanceDTO.getActualUserId())){
            startProcessInstanceDTO.setActualUserId(LoginInfoHolder.getCurrentUserId());
        }
        entity.setCompanyId(flowConfig.getCompanyId());
        entity.setProcInstId(task.getProcessInstanceId());
        entity.setPropertyId(startProcessInstanceDTO.getPropertyId());
        entity.setStatus(WorkOrderStatusEnums.progress.getStatus());
        entity.setOrgIds(orgIds.stream().map(e -> String.valueOf(e)).collect(Collectors.joining(", ")));

        Process mainProcess = repositoryService.getBpmnModel(startProcessInstanceDTO.getProcessDefinitionId()).getMainProcess();

        String dingDing = mainProcess.getAttributeValue(FLOWABLE_NAME_SPACE, FLOWABLE_NAME_SPACE_NAME);
        JSONObject mainJson = JSONObject.parseObject(dingDing, new TypeReference<JSONObject>() {
        });
        String configJosn = mainJson.getString(VIEW_FLOW_CONFIG);

        TbFlowConfig jsonEntity = JsonUtil.readValue(configJosn, TbFlowConfig.class);
        CommonConfigDto configDto = jsonEntity.getCommonConfigDto();
        entity.setRevokeType(configDto.getRevokeType());
        entity.setRevokeNodeId(configDto.getRevokeNodeId());
//            entity.setFlowConfigId(flowConfig.getId());
        this.save(entity);
    }

    public void saveWorkOrg(Long workId,List<Long> orgIds){
        if (CollectionUtils.isEmpty(orgIds)){
            return;
        }
        List<WorkOrgRelEntity> relList = new ArrayList<>();
        orgIds.stream().forEach(item->{
            WorkOrgRelEntity rel = new WorkOrgRelEntity();
            rel.setId(IdWorker.getId());
            rel.setWorkId(workId);
            rel.setOrgId(item);
            relList.add(rel);
        });

        if(CollectionUtils.isNotEmpty(relList)){
            workOrgRelService.saveBatch(relList);
        }

    }

    /**
     * 保存流程节点信息
     * @param childNode
     * @param processDefinitionId
     */
    public void saveChildNode(ChildNode childNode,String processDefinitionId){
        LambdaQueryWrapper<NodeTypeEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(NodeTypeEntity::getProcessDefinitionId,processDefinitionId);
        long count = nodeTypeService.count(lw);
        if(count>0){
            return;
        }
        ChildNode children = childNode.getChildren();
        while (true){
            if(Objects.isNull(children)){
                return;
            }
            NodeTypeEntity entity = new NodeTypeEntity();
            entity.setId(IdWorker.getId());
            entity.setNodeId(children.getId());
            entity.setNodeType(children.getType());
            entity.setProcessDefinitionId(processDefinitionId);
            entity.setProcessNodeType(children.getProcessNodeType());
            nodeTypeService.save(entity);
            children = childNode.getChildren();
        }


    }
    public ChildNode processJson(String processJson) {
        ChildNode childNode = JsonUtil.readValue(processJson, new com.fasterxml.jackson.core.type.TypeReference<ChildNode>() {
        });

        return childNode;
    }

    public List<Long> orgIds(Long configId){
        LambdaQueryWrapper<TbFlowConfigOrg> lw = new LambdaQueryWrapper<>();
        lw.eq(TbFlowConfigOrg::getFlowConfigId,configId);
        List<TbFlowConfigOrg> list = tbFlowConfigOrgService.list(lw);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        return list.stream().map(TbFlowConfigOrg::getOrgId).collect(Collectors.toList());
    }

    /**
     * 同意
     * @param handleDataDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO agree(BatchBusinessDto handleDataDTO) {
        List<WorkBusinessTypeInfoEntity> busiList = new ArrayList<>();
        List<String> taskIds = Optional.ofNullable(handleDataDTO.getTaskIds()).orElse(new ArrayList<>());

        Map<String, List<UserInfo>> processUsers = handleDataDTO.getProcessUsers();
        for (String taskId :taskIds){
            HandleDataDTO dto = new HandleDataDTO();
            dto.setTaskId(taskId);
            dto.setComments(handleDataDTO.getComments());
            dto.setReason(handleDataDTO.getReason());
            WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(dto);
            businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_AGREE.getCode());
            //防止是挂起状态
            updateWorkInfo(WorkOrderStatusEnums.progress.getStatus(), businessTypeInfo.getProcInstId());

            taskService.complete(taskId);
            busiList.add(businessTypeInfo);
            //如果节点存在挂起数据，将挂起数据结束
            WorkBusinessTypeInfoEntity update = queryWorkBusinessById(businessTypeInfo.getNode(), businessTypeInfo.getProcInstId());
            if(Objects.nonNull(update)){
                update.setCloseUserId(LoginInfoHolder.getCurrentUserId());
                update.setEndTime(new Date());
                workBusinessTypeInfoService.updateById(update);
            }
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            Map<String,Object> processVariables= new HashMap<>();
            if(CollUtil.isNotEmpty(processUsers)){
                Set<String> nodes = processUsers.keySet();
                for (String node : nodes) {
                    List<UserInfo> selectUserInfo = processUsers.get(node);
                    List<String> users=new ArrayList<>();
                    for (UserInfo userInfo : selectUserInfo) {
                        users.add(userInfo.getId());
                    }
                    processVariables.put(node,users);
                }
                //保存节点审批人信息
                runtimeService.setVariables(task.getProcessInstanceId(),processVariables);
            }
        }
        if(!CollectionUtils.isEmpty(busiList)){
            workBusinessTypeInfoService.saveBatch(busiList);
        }

        return IdmResDTO.success();
    }

    /**
     * 完成任务
     * @param handleDataDTO
     * @return
     */
    public IdmResDTO complete(HandleDataDTO handleDataDTO) {
        //审批通过
        taskService.complete(handleDataDTO.getTaskId());
        WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
        businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_COMPLETED.getCode());
        //如果节点存在挂起数据，将挂起数据结束
        WorkBusinessTypeInfoEntity update = queryWorkBusinessById(businessTypeInfo.getNode(), businessTypeInfo.getProcInstId());
        if(Objects.nonNull(update)){
            update.setCloseUserId(LoginInfoHolder.getCurrentUserId());
            update.setEndTime(new Date());
            workBusinessTypeInfoService.updateById(update);
        }
        return IdmResDTO.success();
    }
    /**
     * 转办
     * @param handleDataDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO assignee(HandleDataDTO handleDataDTO) {
        //记录转办信息
        WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
        businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_TRANSFER.getCode());
        businessTypeInfo.setDeliver(handleDataDTO.getUserIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
        workBusinessTypeInfoService.save(businessTypeInfo);
        //更新挂起时间
        handleDataDTO.setNodeId(businessTypeInfo.getNode());
        updateBusinessPendingDate(handleDataDTO);
        updateWorkInfo(WorkOrderStatusEnums.progress.getStatus(), businessTypeInfo.getProcInstId());
        assigneeByProcInstId(handleDataDTO);

        //执行加签操作
        AddMultiInstanceUserTaskService multiInstanceUserTaskService = new AddMultiInstanceUserTaskService(managementService);
        for (Long userId : handleDataDTO.getUserIds()) {
            multiInstanceUserTaskService.addMultiInstanceUserTask(handleDataDTO.getTaskId(),String.valueOf(userId));
        }

        return IdmResDTO.success();
    }


    /**
     * 批量转办
     * @param handleDataDTO
     * @return
     */

    public IdmResDTO assigneeByProcInstId(HandleDataDTO handleDataDTO){

        taskService.setAssignee(handleDataDTO.getTaskId(),String.valueOf(handleDataDTO.getUserIds().get(0)));

//        // 查询与流程实例ID相关联的所有任务
//        List<Task> tasks = Optional.ofNullable(taskService.createTaskQuery().processInstanceId(handleDataDTO.getProcessInstanceId()).list())
//                .orElseThrow(()->new RuntimeException("任务信息不存在"));
//        //加签
//        List<Long> assignees = tasks.stream().map(item->Long.parseLong(item.getAssignee())).collect(Collectors.toList());
//        List<Long> countSigeList =handleDataDTO.getUserIds().stream().filter(item->!assignees.contains(item)).collect(Collectors.toList());
//        if(CollectionUtils.isNotEmpty(countSigeList)){
//            countSigeList.stream().forEach(item->{
//                Map<String,Object> variableMap= new HashMap<>();
//                variableMap.put("assigneeName",String.valueOf(item));
//                runtimeService.addMultiInstanceExecution(tasks.get(0).getTaskDefinitionKey(), tasks.get(0).getProcessInstanceId(), variableMap);
//            });
//        }
//
//        if(StringUtils.isNotEmpty(handleDataDTO.getTaskId())){
//            Task task = taskService.createTaskQuery().taskId(handleDataDTO.getTaskId()).singleResult();
//            List<Task> collect = tasks.stream().filter(item -> Objects.equals(handleDataDTO.getTaskId(), item.getId())).collect(Collectors.toList());
//            runtimeService.deleteMultiInstanceExecution(collect.get(0).getExecutionId(),true);
//        }else{
//            //减签
//            tasks.stream().filter(item->!handleDataDTO.getUserIds().contains(Long.parseLong(item.getAssignee())))
//                    .forEach(it->{
//                        runtimeService.deleteMultiInstanceExecution(it.getExecutionId(),true);
//                    });
//        }
        return IdmResDTO.success();
    }
    /**
     * 拒绝
     * @param handleDataDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO refuse(BatchBusinessDto handleDataDTO) {
        List<WorkBusinessTypeInfoEntity> buList = new ArrayList<>();
        for(String tasId : handleDataDTO.getTaskIds()){
            HandleDataDTO dto = new HandleDataDTO();
            dto.setTaskId(tasId);
            dto.setReason(handleDataDTO.getReason());
            dto.setComments(handleDataDTO.getComments());
            WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(dto);
            workBusinessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_REFUSE.getCode());
            buList.add(workBusinessTypeInfo);

            updateWorkInfo(WorkOrderStatusEnums.completed.getStatus(), workBusinessTypeInfo.getProcInstId());
            runtimeService.deleteProcessInstance(String.valueOf(workBusinessTypeInfo.getProcInstId()), handleDataDTO.getComments()); // 终止
            //更新挂起
            dto.setNodeId(workBusinessTypeInfo.getNode());
            updateBusinessPendingDate(dto);

        }
        if(!CollectionUtils.isEmpty(buList)){
            workBusinessTypeInfoService.saveBatch(buList);
        }


        return IdmResDTO.success();
    }

    /**
     * 评论或者督办
     * @param handleDataDTOs
     * @return
     */
    public IdmResDTO commentAndSuper(BatchBusinessDto handleDataDTOs) {
        List<WorkBusinessTypeInfoEntity> businessTypeInfoEntities = new ArrayList<>();
        //督办信息
        for(String instId :handleDataDTOs.getProcessInstanceId() ){
            HandleDataDTO dataDTO = new HandleDataDTO();
            dataDTO.setProcessInstanceId(instId);
            dataDTO.setComments(handleDataDTOs.getComments());
            dataDTO.setReason(handleDataDTOs.getReason());
            WorkBusinessTypeInfoEntity businessTypeInfo =getWorkBusinessTypeInfo(dataDTO);
            businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_HANDLE.getCode());
            businessTypeInfoEntities.add(businessTypeInfo);
        }

        //评论信息
        for(String taskId :handleDataDTOs.getTaskIds() ){
            HandleDataDTO dataDTO = new HandleDataDTO();
            dataDTO.setTaskId(taskId);
            dataDTO.setComments(handleDataDTOs.getComments());
            dataDTO.setReason(handleDataDTOs.getReason());
            WorkBusinessTypeInfoEntity businessTypeInfo =getWorkBusinessTypeInfo(dataDTO);
            businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_COMMENT.getCode());
            businessTypeInfoEntities.add(businessTypeInfo);
        }
        if(CollectionUtils.isNotEmpty(businessTypeInfoEntities)){
            workBusinessTypeInfoService.saveBatch(businessTypeInfoEntities);
        }
        return IdmResDTO.success();
    }


    /**
     * 回退
     * @param handleDataDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO rollback(HandleDataDTO handleDataDTO) {

        //回退留痕
        WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
        workBusinessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_ROLLBACK.getCode());
        workBusinessTypeInfoService.save(workBusinessTypeInfo);
        //更新挂起时间
        updateBusinessPendingDate(handleDataDTO);

        //更新主流程状态
        updateWorkInfo(WorkOrderStatusEnums.progress.getStatus(), workBusinessTypeInfo.getProcInstId());
        List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(handleDataDTO.getProcessInstanceId()).orderByTaskId().desc().list();

        List<String> collect = taskInstances.stream().map(HistoricTaskInstance::getTaskDefinitionKey).distinct().collect(Collectors.toList());

        List<Execution> executions = runtimeService.createExecutionQuery().parentId(handleDataDTO.getProcessInstanceId()).list();
        List<String> executionIds = new ArrayList<>();
        executions.forEach(execution -> executionIds.add(execution.getId()));


        runtimeService.createChangeActivityStateBuilder().moveExecutionsToSingleActivityId(executionIds,
                collect.get(1)).changeState();
        return IdmResDTO.success();

    }

    /**
     * 挂起流程
     * @param handleDataDTOs
     * @return
     */
    public IdmResDTO businessPending(BatchBusinessDto handleDataDTOs) {

       List<WorkBusinessTypeInfoEntity> businessTypeInfoEntities = new ArrayList<>();
       //工单中心挂起
        for(String handleDataDTO : handleDataDTOs.getProcessInstanceId()){
            HandleDataDTO dataDTO = new HandleDataDTO();
            dataDTO.setProcessInstanceId(handleDataDTO);
            dataDTO.setComments(handleDataDTOs.getComments());
            dataDTO.setReason(handleDataDTOs.getReason());
            WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(dataDTO);
            businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_PENDING.getCode());
            businessTypeInfoEntities.add(businessTypeInfo);
           //更新主流程状态
            updateWorkInfo(WorkOrderStatusEnums.Suspended.getStatus(), businessTypeInfo.getProcInstId());
       }
        //审批中心挂起
        for(String taskId : handleDataDTOs.getTaskIds()){
            HandleDataDTO dataDTO = new HandleDataDTO();
            dataDTO.setTaskId(taskId);
            dataDTO.setComments(handleDataDTOs.getComments());
            dataDTO.setReason(handleDataDTOs.getReason());
            WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(dataDTO);
            businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_PENDING.getCode());
            businessTypeInfoEntities.add(businessTypeInfo);
            //更新主流程状态
            updateWorkInfo(WorkOrderStatusEnums.Suspended.getStatus(), businessTypeInfo.getProcInstId());
        }
        if(CollectionUtils.isNotEmpty(businessTypeInfoEntities)){
            workBusinessTypeInfoService.saveBatch(businessTypeInfoEntities);
        }
        return IdmResDTO.success();
    }


    /**
     * 根据实列id获取挂起的任务信息
     * @param procInstId
     * @return
     */
    public boolean querySuspendTaskIds(String procInstId){
        LambdaQueryWrapper<WorkInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(WorkInfoEntity::getProcInstId,procInstId).eq(WorkInfoEntity::getStatus,WorkOrderStatusEnums.Suspended.getStatus());
        Long aLong = this.getBaseMapper().selectCount(lw);
        return aLong>0;
    }

    /**
     * 获取最新的任务信息
     */
    public HistoricTaskInstance getHisTaskInfo(HandleDataDTO handleDataDTO){
        String taskId = handleDataDTO.getTaskId();
        HistoricTaskInstance task = null;
        if(null == taskId){
            //通过流程实例id找最新的taskId
            List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(handleDataDTO.getProcessInstanceId()).orderByTaskId().desc().list();
            if(CollUtil.isNotEmpty(list)){
                task = list.get(0);
            }
            handleDataDTO.setTaskId(task.getId());
        }else {
            task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            handleDataDTO.setProcessInstanceId(task.getProcessInstanceId());
        }
        if(null == task){
            throw new RuntimeException("找不到任务");
        }
        return task;
    }
    /**
     * 查询节点属性数据,并判断是否有权限发起该流程
     * @return
     */
    public IdmResDTO queryFirstFormInfo(FirstFormDto dto, Long userId) {
        ChildNode childNodeByNodeId = getChildNodeByNodeId(dto.getProcessDefinitionId(), WorkOrderConstants.USER_ROOT);
        Properties props = childNodeByNodeId.getProps();

        List<String> ids = props.getAssignedUser().stream().map(UserInfo::getId).collect(Collectors.toList());
        log.info("指定类型"+props.getAssignedType()+"指定人员:"+JSONObject.toJSONString(ids)+"当前人员:"+userId);
        //指定人员
        if(StringUtils.equalsAnyIgnoreCase(props.getAssignedType(), AssigneeTypeEnums.ASSIGN_USER.getTypeName())){
            if(!ids.contains(String.valueOf(userId))){
                return IdmResDTO.error(ResultCode.NO_OPERATION_PERMISSION.getCode(), ResultCode.NO_OPERATION_PERMISSION.getMessage());
            }
        }
        if(StringUtils.equalsAnyIgnoreCase(props.getAssignedType(), AssigneeTypeEnums.DEPT.getTypeName())){
            if(!ids.contains(String.valueOf(LoginInfoHolder.getCurrentDeptId()))){
                return IdmResDTO.error(ResultCode.NO_OPERATION_PERMISSION.getCode(), ResultCode.NO_OPERATION_PERMISSION.getMessage());
            }
        }
        if(StringUtils.equalsAnyIgnoreCase(props.getAssignedType(), AssigneeTypeEnums.ROLE.getTypeName())){
            BaseUserReqDto query = new BaseUserReqDto();
            query.setUserId(LoginInfoHolder.getCurrentUserId());
            IdmResDTO<BaseUserDto> baseUserDtoIdmResDTO = systemApiFeignService.lookUpUserInfo(query);
            BaseUserDto data = baseUserDtoIdmResDTO.getData();
            if(!ids.contains(String.valueOf(data.getRoleId()))){
                return IdmResDTO.error(ResultCode.NO_OPERATION_PERMISSION.getCode(), ResultCode.NO_OPERATION_PERMISSION.getMessage());
            }
        }
        return IdmResDTO.success();
    }

    /**
     * 分页获取工单列表
     * @param dto
     * @return
     */
    public IdmResDTO<IPage<WorkInfoDto>> queryWorkOrderInfo(WorkInfoDto dto) {
        //根据组织id获取本组织与子组织信息
       if(CollectionUtils.isEmpty(dto.getOrgIds())){
           List<DepartmentDto> departs = getDeptIds(dto.getOrg());
           List<Long> deptIds = departs.stream().map(DepartmentDto::getId).collect(Collectors.toList());
           dto.setOrgIds(deptIds);
       }
        IPage<WorkInfoDto> workInfoEntityPage = getBaseMapper().queryWorkOrderInfo(new Page<WorkInfoDto>(dto.getCurrentPage(),dto.getPageSize()),dto);
        List<WorkInfoDto> records = workInfoEntityPage.getRecords();
        if(CollectionUtils.isNotEmpty(records)){
            //企业id 获取业务类型
            List<Long> businessIds = records.stream().map(WorkInfoDto::getBusinessType).collect(Collectors.toList());
            Map<Long, String> busiMap = getBusiMap(businessIds);

            //获取全部userId
            List<Long> userIds = records.stream().map(WorkInfoDto::getCreateUser).collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(userIds);

            //部门ids
            records.stream().forEach(item->{
                //组织名称
                item.setOrgPath(getOrgPath(item.getDeptIds()));
                //用户名称
                item.setUserName(userMap.get(item.getCreateUser()));
               //业务类型
                item.setBusinessTypeName(busiMap.get(item.getBusinessType()));
                //获取流程输入的组织信息
                item.setOrgIds(getOrgIds(item.getDeptIds()));

            });
        }

        return IdmResDTO.success(workInfoEntityPage);
    }

    /**
     * 获取客户工单列表
     * @param dto
     * @return
     */
    public IdmResDTO<IPage<WorkInfoDto>> queryCustomerWorkOrderInfo(WorkInfoDto dto){
        //根据组织id获取本组织与子组织信息
        if(CollectionUtils.isEmpty(dto.getOrgIds())){
            List<DepartmentDto> departs = getDeptIds(dto.getOrg());
            List<Long> deptIds = departs.stream().map(DepartmentDto::getId).collect(Collectors.toList());
            dto.setOrgIds(deptIds);
        }
        IPage<WorkInfoDto> workInfoEntityPage = getBaseMapper().queryCustomerWorkOrderInfo(new Page<WorkInfoDto>(dto.getCurrentPage(),dto.getPageSize()),dto);
        List<WorkInfoDto> records = workInfoEntityPage.getRecords();
        if(CollectionUtils.isNotEmpty(records)){
            //企业id 获取业务类型
            List<Long> businessIds = records.stream().map(WorkInfoDto::getBusinessType).collect(Collectors.toList());
            Map<Long, String> busiMap = getBusiMap(businessIds);

            //获取创建人的userId
            List<Long> userIds = records.stream().map(WorkInfoDto::getCreateUser).collect(Collectors.toList());
            //获取全部报单人的userId
            List<Long> actualIds = records.stream().map(WorkInfoDto::getActualUserId).collect(Collectors.toList());
            userIds.addAll(actualIds);
            userIds=userIds.stream().distinct().collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(userIds);

            //TODO 根据楼盘id获取楼盘名称

            //部门ids
            records.stream().forEach(item->{
                //组织名称
                item.setOrgPath(getOrgPath(item.getDeptIds()));
                //用户名称
                item.setUserName(userMap.get(item.getCreateUser()));
                //业务类型
                item.setBusinessTypeName(busiMap.get(item.getBusinessType()));
                //获取流程输入的组织信息
                item.setOrgIds(getOrgIds(item.getDeptIds()));
                //报单人
                item.setActualUserName(userMap.get(item.getActualUserId()));
                //TODO 组装楼盘名称

            });
        }

        return IdmResDTO.success(workInfoEntityPage);
    }

    /**
     * 根据部门id获取部门路径名称
     * @param deptIds
     * @return
     */
    public String getOrgPath(String deptIds){
        List<Long> orgIds = getOrgIds(deptIds);
        Map<Long, String> deptMap = getDeptMap(orgIds);
        return deptMap.values().stream()
                .collect(Collectors.joining(","));
    }

    /**
     * 将部门字符串转成部门List
     * @param deptIds
     * @return
     */
    public List<Long> getOrgIds(String deptIds){
        String[] stringArray = deptIds.split(",",-1);
        return Arrays.stream(stringArray)
                .map(e->Long.parseLong(e.trim())) // 将String转换为Long
                .collect(Collectors.toList()); // 收集结果到List
    }

    /**
     * 部门id与部门名称
     * @param deptIds
     * @return
     */
    public Map<Long,String> getDeptMap(List<Long> deptIds){
        DepartmentReqDto depeDto = new DepartmentReqDto();
        depeDto.setDeptIdList(deptIds);
        IdmResDTO<List<DepartmentDto>> listIdmResDTO = systemApiFeignService.lookUpDepartmentList(depeDto);
        List<DepartmentDto> deptLsit = Optional.ofNullable(listIdmResDTO.getData()).orElseThrow(()->new RuntimeException("组织信息不存在"));
        return deptLsit.stream().collect(Collectors.toMap(DepartmentDto::getId, DepartmentDto::getPathName));
    }

    public Map<Long,String> getUserMap(List<Long> userIds){
        BaseUserReqDto userReqDto = new BaseUserReqDto();
        userReqDto.setUserIdList(userIds);
        IdmResDTO<List<BaseUserDto>> listIdmResDTO = systemApiFeignService.lookUpUserList(userReqDto);
        List<BaseUserDto> data = Optional.ofNullable(listIdmResDTO.getData()).orElseThrow(()->new RuntimeException("用户信息不存在"));
        return data.stream().collect(Collectors.toMap(BaseUserDto::getId,BaseUserDto::getName ));
    }

    public Map<Long,String> getBusiMap(List<Long> businessIds){
        IdmResDTO<List<BusinessTypeRspDTO>> listIdmResDTO = systemApiFeignService.batchGetBusinessType(BusinessTypeReqDTO.builder()
                .businessTypeIdList(businessIds).build());
        List<BusinessTypeRspDTO> businessInfo = Optional.ofNullable(listIdmResDTO.getData()).orElseThrow(()->new RuntimeException("业务类型不存在"));
        return businessInfo.stream().collect(Collectors.toMap(BusinessTypeRspDTO::getBusinessTypeId, BusinessTypeRspDTO::getTreeName));
    }
    /**
     * 获取自己与子部门的部门信息
     * @param deptId
     * @return
     */
    public List<DepartmentDto> getDeptIds(Long deptId){
        DepartmentReqDto paraDto = new DepartmentReqDto();
        paraDto.setDeptId(LoginInfoHolder.getCurrentDeptId());
        paraDto.setSelfReturn(true);
        IdmResDTO<List<DepartmentDto>> listIdmResDTO = systemApiFeignService.lookUpDepartmentChildList(paraDto);
        return listIdmResDTO.getData();

    }
    public List<WorkBusinessTypeInfoEntity> queryBusinessTypeInfo(BusinessTypeInfoDto dto){
        LambdaQueryWrapper<WorkBusinessTypeInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(Objects.nonNull(dto.getBusinessType()),WorkBusinessTypeInfoEntity::getBusinessType,dto.getBusinessType())
                .eq(Objects.nonNull(dto.getTaskId()),WorkBusinessTypeInfoEntity::getTaskId,dto.getTaskId())
                .eq(Objects.nonNull(dto.getProcInstId()),WorkBusinessTypeInfoEntity::getProcInstId,dto.getProcInstId());
        List<WorkBusinessTypeInfoEntity> list = workBusinessTypeInfoService.list(lw);
        return list;
    }
    /**
     * 获取详情里的基础信息
     * @param dto
     * @return
     */
    public IdmResDTO<WorkInfoDto> queryBasicWorkOrderDetailInfo(WorkProcInstDto dto) {
        Long company = getCompany();
        //获取工单详情
        WorkInfoDto resultDto = getBaseMapper().queryWorkOrderDetailInfo(dto);
        if(Objects.equals(company,resultDto.getCompanyId())){
            return IdmResDTO.error(ResultCode.NO_OPERATION_PERMISSION.getCode(), ResultCode.NO_OPERATION_PERMISSION.getMessage());
        }
        //填充组织名称
        resultDto.setOrgPath(getOrgPath(resultDto.getDeptIds()));
        // 业务类型后返回数据
        Map<Long, String> busiMap = getBusiMap(Arrays.asList(resultDto.getBusinessType()));
        resultDto.setBusinessTypeName(busiMap.get(resultDto.getBusinessType()));
        //获取用户手机号
        BaseUserDto baseUserDto = queryBaseUserInfo(resultDto.getCreateUser());
        resultDto.setPhone(baseUserDto.getPhoneNumber());
        resultDto.setUserName(baseUserDto.getName());
        //获取报单人信息
        BaseUserDto actualUser = queryBaseUserInfo(resultDto.getActualUserId());
        resultDto.setActualUserPhone(actualUser.getPhoneNumber());
        resultDto.setActualUserName(actualUser.getName());

        //获取挂起时间
        BusinessTypeInfoDto businessTypeInfoDto = BusinessTypeInfoDto.builder().businessType(BUSINESS_BYPE_PENDING).
                procInstId(Long.parseLong(resultDto.getProcInstId())).build();
        Long pendTime = calculateSuspensionTime(businessTypeInfoDto);
        //获取流程实例信息
        HistoricProcessInstance historicProcessInstance = Optional.ofNullable(historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(resultDto.getProcInstId())
                .singleResult()).orElseThrow(()->new RuntimeException("未找到流程实例信息"));
        Long time= null;
        if(Objects.nonNull(historicProcessInstance.getEndTime())){
            time =historicProcessInstance.getEndTime().getTime()-historicProcessInstance.getStartTime().getTime()-(Objects.nonNull(pendTime)?pendTime:0);
        }else{
            time =new Date().getTime()-historicProcessInstance.getStartTime().getTime()-(Objects.nonNull(pendTime)?pendTime:0);
        }
        resultDto.setWorkTime(converTime(time));

        queryBusinessTime(resultDto,historicProcessInstance.getEndTime());

        return IdmResDTO.success(resultDto);
    }

    public Long getCompany(){
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        if(Objects.isNull(currentOrgId)){
            //TODO 根据小区id获取企业id
        }
        return currentOrgId;
    }

    public BaseUserDto queryBaseUserInfo(Long userId){
        BaseUserReqDto reqDto = new BaseUserReqDto();
        reqDto.setUserId(userId);
        IdmResDTO<BaseUserDto> baseUserDtoIdmResDTO = systemApiFeignService.lookUpUserInfo(reqDto);
        if(baseUserDtoIdmResDTO == null){
            throw new RuntimeException("用户信息不存在");
        }
        return baseUserDtoIdmResDTO.getData();
    }
    /**
     * 获取操作时间
     * @return
     */

    public void queryBusinessTime( WorkInfoDto resultDto,Date businessTime){
        //完结与终止
        if(resultDto.getStatus().equals(WorkInfoEnums.FINISH.getCode()) || resultDto.getStatus().equals(WorkInfoEnums.TERMINATED.getCode())){
            resultDto.setBusinessTime(businessTime);
        }
        //挂起
        if(resultDto.getStatus().equals(WorkInfoEnums.SUSPEND.getCode())){
            WorkBusinessTypeInfoEntity businessTypeInfo = queryWorkBusinessById(null, Long.parseLong(resultDto.getProcInstId()));
            resultDto.setBusinessTime(businessTypeInfo.getStartTime());
        }
    }
    /**
     * 转换耗时时间
     * @param time
     * @return
     */
    public String converTime(Long time){
        long dayCount = time /(1000*60*60*24);//计算天
        long restTimes = time %(1000*60*60*24);//剩下的时间用于计于小时
        long hourCount = restTimes/(1000*60*60);//小时
        restTimes = restTimes % (1000*60*60);
        long minuteCount = restTimes / (1000*60);
        minuteCount++;
        return dayCount+"天"+hourCount+"小时"+minuteCount+"分";
    }

    /**
     * 计算挂起时间
     * @return
     */
    public Long calculateSuspensionTime(BusinessTypeInfoDto dto){
        List<WorkBusinessTypeInfoEntity> workList = queryBusinessTypeInfo(dto);
        List<Long> times = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(workList)){
            workList.stream().forEach(item->{
                long totalTimes = item.getEndTime()==null?
                        (new Date().getTime()-item.getStartTime().getTime()):
                        (item.getEndTime().getTime()-item.getStartTime().getTime());
                times.add(totalTimes);
            });
        }
        if(CollectionUtils.isNotEmpty(times)){
            return times.stream().reduce(0L,Long::sum);
        }
        return null;
    }


    public IdmResDTO<HandleDataVO> instanceInfo(HandleDataDTO HandleDataDTO) {
        String processInstanceId = HandleDataDTO.getProcessInstanceId();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId)
                .includeProcessVariables().singleResult();
        String processDefinitionKey = historicProcessInstance.getProcessDefinitionKey();

        TbFlowConfig flowConfig = flowConfigService.getById(processDefinitionKey.replace(PROCESS_PREFIX, ""));

        Process mainProcess = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId()).getMainProcess();

        String dingDing = mainProcess.getAttributeValue(FLOWABLE_NAME_SPACE, FLOWABLE_NAME_SPACE_NAME);
        JSONObject mainJson = JSONObject.parseObject(dingDing, new TypeReference<JSONObject>() {
        });
        String processJson = mainJson.getString(VIEW_PROCESS_JSON_NAME);

        Collection<FlowElement> flowElements = mainProcess.getFlowElements();
        String instId = historicProcessInstance.getId();
        String nodeJson = queryCCrecipient(processJson, processInstanceId,historicProcessInstance.getProcessDefinitionId() );
        flowConfig.setProcess(nodeJson);
//        flowConfig.setFormItems(flowConfig.getFormItems());
        HandleDataVO handleDataVO =new HandleDataVO();
        Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();

        handleDataVO.setProcessInstanceId(historicProcessInstance.getId());
        JSONObject jsonObject = (JSONObject) processVariables.get(FORM_VAR);
        handleDataVO.setFormData(jsonObject);
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        if(CollectionUtils.isNotEmpty(taskList)){
            handleDataVO.setCurrentNode(taskList.get(0).getTaskDefinitionKey());
        }
        Map<String,NodeDetailDto> pamaMap = new HashMap<>();
        //已经运行完成的节点
        List<String> endNodes = new ArrayList<>();
        for (FlowElement flowElement : flowElements) {
            String nodeId = flowElement.getId();
            if(nodeId.startsWith(WorkOrderConstants.NODE_START) || StringUtils.equals(WorkOrderConstants.USER_ROOT,nodeId)){
                //没有收集
                if(!pamaMap.containsKey(nodeId)){
                    NodeDetailDto nodeDetailDto = new NodeDetailDto();
                    nodeDetailDto.setNodeId(nodeId);
                    nodeDetailDto.setTimeOut(checkNodeTimeOut(nodeId,
                            instId));
                    nodeDetailDto.setNodeInfos(queryNodeBusiness(nodeId,
                            instId));
                    nodeDetailDto.setBusinessName(getStartUserName(nodeId,
                            instId));
                    nodeDetailDto.setCreateDate(historicProcessInstance.getStartTime());
                    pamaMap.put(nodeDetailDto.getNodeId(),nodeDetailDto);
                }
            }
            if(!endNodes.contains(nodeId) && (nodeId.startsWith(WorkOrderConstants.NODE_START) || StringUtils.equals(WorkOrderConstants.USER_ROOT,nodeId))){
                endNodes.add(nodeId);
            }
        }

        handleDataVO.setProcessTemplates(flowConfig);
        handleDataVO.setEndList(endNodes);
        handleDataVO.setNodeList(pamaMap);
        return IdmResDTO.success(handleDataVO);
    }

    public String queryCCrecipient(String process,String procInstId ,String processDefinitionId){

        String flowConfigId = StringUtils.substringBefore(processDefinitionId, ":").replace(PROCESS_PREFIX, "");
        //装抄送人
        ChildNode childNode = processJson(process);
        ChildNode children = childNode.getChildren();
        while (true){
            if(StringUtils.isEmpty(children.getId())){
                break;
            }
            if(StringUtils.equals("CC",children.getType())){
                CCInfo ccInfo = children.getProps().getCcInfo();
                List<Long> ccUserIds = new ArrayList<>();
                List<String> ids = ccInfo.getCcIds();
                //指定人员证明节点已经有人员数据
                if (Objects.equals(ccInfo.getType(), FlowCCEnums.PERSON.getCode())) {
                    ccUserIds = ccInfo.getCcIds().stream().map(e -> Long.parseLong(e)).collect(Collectors.toList());
                }else if (Objects.equals(ccInfo.getType(), FlowCCEnums.ROLE.getCode())) {
                    List<Long> flowCOnfigDeptIds = tbFlowConfigOrgService.queryOrgIdsByFlowConfigId(Long.valueOf(flowConfigId));
                    AssertUtil.notEmpty(flowCOnfigDeptIds, "该流程暂未配置组织,请重试");
                    ccUserIds = systemToFlowService.getUserIdByRole(ids.stream().map(e -> Long.parseLong(e)).collect(Collectors.toList()),flowCOnfigDeptIds);
                }else if (Objects.equals(ccInfo.getType(), FlowCCEnums.DEPARTMENT.getCode())) {
                    ccUserIds = systemToFlowService.getUserIdByDept(ids.stream().map(e -> Long.parseLong(e)).collect(Collectors.toList()));
                }
                Map<Long, String> userMap = getUserMap(ccUserIds);
                List<UserInfo>  assignedUser = new ArrayList<>();
                ccUserIds.stream().forEach(item->{
                        UserInfo userInfo = new UserInfo();
                        userInfo.setId(String.valueOf(item));
                        userInfo.setName(userMap.get(item));
                        assignedUser.add(userInfo);
                    });
                children.getProps().setAssignedUser(assignedUser);
            }
            children=children.getChildren();
        }
        return JsonUtil.writeValueAsString(childNode);
    }

    /**
     * 判断节点是否超时
     * @return
     */
    public List<WorkBusinessTypeInfoEntity> queryNodeBusiness(String nodeId,String procinstId){
        LambdaQueryWrapper<WorkBusinessTypeInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(WorkBusinessTypeInfoEntity::getNode,nodeId).
                ne(WorkBusinessTypeInfoEntity::getBusinessType,BUSINESS_TIME_OUT.getCode()).
                eq(WorkBusinessTypeInfoEntity::getProcInstId,Long.parseLong(procinstId))
                .orderByAsc(WorkBusinessTypeInfoEntity::getStartTime);
        List<WorkBusinessTypeInfoEntity> list = workBusinessTypeInfoService.list(lw);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        //根据userId获取用户信息与转交信息
        //获取userIds
        List<Long> userIds = list.stream().map(WorkBusinessTypeInfoEntity::getUserId).collect(Collectors.toList());
        Map<Long, String> userMap = getUserMap(userIds);
        list.stream().forEach(item->{
            item.setUserName(userMap.get(item.getUserId()));
            if(StringUtils.isNotEmpty(item.getDeliver())){
                String deliverNames = getDeliverNames(item.getDeliver());
                item.setDeliverName(deliverNames);
            }
        });
        return list;
    }

    public String getStartUserName(String nodeId,String procinstId){
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(procinstId)
                .includeProcessVariables().singleResult();

        Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();
        String userNames =  "";
        if(StringUtils.equals(nodeId, WorkOrderConstants.USER_ROOT)){
            List<UserInfo> userInfos = JSONObject.parseObject(MapUtil.getStr(processVariables, nodeId), new TypeReference<List<UserInfo>>() {
            });
            if(CollectionUtils.isEmpty(userInfos)){
                return "";
            }
            List<Long> userIds = userInfos.stream().map(UserInfo::getId).map(e->Long.parseLong(e)).collect(Collectors.toList());
             userNames = getUserMap(userIds).values().stream().collect(Collectors.joining(","));
        }else{

            List<Long> userInfos = JSONObject.parseObject(MapUtil.getStr(processVariables, nodeId), new TypeReference<List<Long>>() {
            });
            if(Objects.isNull(userInfos)){
                return "";
            }

            userNames = getUserMap(userInfos).values().stream().collect(Collectors.joining(","));
        }

        return  userNames;
    }

    public String getDeliverNames(String deliver){
        List<Long> longList = Arrays.stream(deliver.split(","))
                .map(s -> s.trim()) // 去除空白字符
                .map(Long::valueOf) // 转换为 Long
                .collect(Collectors.toList());
        Map<Long, String> resutMap = getUserMap(longList);
        StringJoiner joiner = new StringJoiner(",");
        for (String value : resutMap.values()) {
            joiner.add(value);
        }

        return  joiner.toString();
    }

    /**
     * 判断节点是否超时
     * @return
     */
    public Byte checkNodeTimeOut(String nodeId,String procinstId){
        LambdaQueryWrapper<WorkBusinessTypeInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(WorkBusinessTypeInfoEntity::getNode,nodeId).eq(WorkBusinessTypeInfoEntity::getProcInstId,Long.parseLong(procinstId))
                .eq(WorkBusinessTypeInfoEntity::getBusinessType, RESULT_1);
        long count = workBusinessTypeInfoService.count(lw);
        Byte timeOut = RESULT_0;
        if(count>0){
            timeOut = RESULT_1;
        }
        return timeOut;
    }

    /**
     * 获取节点数据
     * @param childNode
     * @param childNodeMap
     */
    private  static  void getChildNode(ChildNode childNode,Map<String,ChildNode> childNodeMap){
        childNodeMap.put(childNode.getId(),childNode);
        List<ChildNode> branchs = childNode.getBranchs();
        ChildNode children = childNode.getChildren();
        if(branchs!=null && branchs.size()>0){
            for (ChildNode branch : branchs) {
                if(StringUtils.isNotBlank(branch.getId())){
                    childNodeMap.put(branch.getId(),branch);
                    getChildNode(branch,childNodeMap);
                }
            }
        }

        if(children!=null ){
            childNodeMap.put(children.getId(),children);
            getChildNode(children,childNodeMap);
        }

    }

    /**
     * 终止流程
     * @param handleDataDTOs
     * @return
     */
    public IdmResDTO closeFlow(BatchBusinessDto handleDataDTOs) {
        //保存操作信息
        List<WorkBusinessTypeInfoEntity> businessTypeInfoEntities = new ArrayList<>();
        //工单中心终止流程
        for(String instId:handleDataDTOs.getProcessInstanceId()){
            HandleDataDTO dataDTO = new HandleDataDTO();
            dataDTO.setProcessInstanceId(instId);
            dataDTO.setComments(handleDataDTOs.getComments());
            dataDTO.setReason(handleDataDTOs.getReason());
            WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(dataDTO);
            workBusinessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_CLOSE.getCode());
            businessTypeInfoEntities.add(workBusinessTypeInfo);
            //终止流程
            runtimeService.deleteProcessInstance(instId, handleDataDTOs.getComments()); // 终止
            //更新挂起结束时间
            dataDTO.setNodeId(workBusinessTypeInfo.getNode());
            updateBusinessPendingDate(dataDTO);

            //更新工单状态
            updateWorkInfo(WorkOrderStatusEnums.terminated.getStatus(), workBusinessTypeInfo.getProcInstId());
        }
        //审批中心终止流程
        for(String taskId : handleDataDTOs.getTaskIds()){
            HandleDataDTO dataDTO = new HandleDataDTO();
            dataDTO.setTaskId(taskId);
            dataDTO.setComments(handleDataDTOs.getComments());
            dataDTO.setReason(handleDataDTOs.getReason());
            WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(dataDTO);
            workBusinessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_CLOSE.getCode());
            businessTypeInfoEntities.add(workBusinessTypeInfo);
            //终止流程
            runtimeService.deleteProcessInstance(dataDTO.getProcessInstanceId(), handleDataDTOs.getComments()); // 终止
            //
            dataDTO.setNodeId(workBusinessTypeInfo.getNode());
            updateBusinessPendingDate(dataDTO);

            //更新工单状态
            updateWorkInfo(WorkOrderStatusEnums.terminated.getStatus(), workBusinessTypeInfo.getProcInstId());
        }

        if(CollectionUtils.isNotEmpty(businessTypeInfoEntities)){
            workBusinessTypeInfoService.saveBatch(businessTypeInfoEntities);
        }
        return IdmResDTO.success();
    }


    public void updateWorkInfo(Byte state,Long procInstId){
        LambdaUpdateWrapper<WorkInfoEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(WorkInfoEntity::getProcInstId,procInstId).set(WorkInfoEntity::getStatus,state);
        this.update(wrapper);
    }

    public void updateWorkTimeOutInfo(Byte state,Long procInstId){
        LambdaUpdateWrapper<WorkInfoEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(WorkInfoEntity::getProcInstId,procInstId).set(WorkInfoEntity::getWorkTimeOut,state);
        this.update(wrapper);
    }
    /**
     * 获取操作信息
     * @param handleDataDTO
     * @return
     */
    public WorkBusinessTypeInfoEntity getWorkBusinessTypeInfo(HandleDataDTO handleDataDTO){

        HistoricTaskInstance hisTaskInfo = getHisTaskInfo(handleDataDTO);
        WorkBusinessTypeInfoEntity businessTypeInfo = new WorkBusinessTypeInfoEntity();
        businessTypeInfo.setId(IdWorker.getId());
        if (StringUtils.isEmpty(handleDataDTO.getTaskId())) {
            businessTypeInfo.setTaskId(Long.parseLong(hisTaskInfo.getProcessInstanceId()));
        } else {
            businessTypeInfo.setTaskId(Long.parseLong(handleDataDTO.getTaskId()));
        }
        businessTypeInfo.setProcInstId(Long.parseLong(hisTaskInfo.getProcessInstanceId()));
        businessTypeInfo.setUserId(LoginInfoHolder.getCurrentUserId());
        businessTypeInfo.setNode(hisTaskInfo.getTaskDefinitionKey());
        businessTypeInfo.setStartTime(new Date());
        businessTypeInfo.setComments(handleDataDTO.getComments());
        businessTypeInfo.setReason(handleDataDTO.getReason());
        return businessTypeInfo;
    }

    public WorkBusinessTypeInfoEntity queryWorkBusinessById(String nodeId,Long procInstId){
        LambdaQueryWrapper<WorkBusinessTypeInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(Objects.nonNull(nodeId),WorkBusinessTypeInfoEntity::getNode,nodeId)
                .eq(WorkBusinessTypeInfoEntity::getProcInstId,procInstId)
                .eq(WorkBusinessTypeInfoEntity::getBusinessType,RESULT_0).isNull(WorkBusinessTypeInfoEntity::getEndTime);
        List<WorkBusinessTypeInfoEntity> list = workBusinessTypeInfoService.list(lw);
        return CollectionUtils.isEmpty(list)?null:list.get(0);
    }
    /**
     * 更新挂起结束时间
     * @param handleDataDTO
     */
    public void updateBusinessPendingDate(HandleDataDTO handleDataDTO){
        LambdaUpdateWrapper<WorkBusinessTypeInfoEntity> lw = new LambdaUpdateWrapper<>();
        lw.eq(WorkBusinessTypeInfoEntity::getProcInstId,handleDataDTO.getProcessInstanceId())
                .eq(WorkBusinessTypeInfoEntity::getBusinessType,BUSINESS_BYPE_PENDING)
                .eq(StringUtils.isNotBlank(handleDataDTO.getNodeId()),WorkBusinessTypeInfoEntity::getNode,handleDataDTO.getNodeId())
                .isNull(WorkBusinessTypeInfoEntity::getEndTime).set(WorkBusinessTypeInfoEntity::getEndTime,new Date())
                .set(WorkBusinessTypeInfoEntity::getCloseUserId,LoginInfoHolder.getCurrentUserId());
        workBusinessTypeInfoService.update(lw);
    }

    /**
     * 查询待审批列表
     * @param dto
     * @return
     */
    public IdmResDTO<IPage<MyApprovalResultDto>> queryMyNotApproval(QueryMyApprovalDto dto) {
        dto.setAssignee(LoginInfoHolder.getCurrentUserId());

        Page<MyApprovalResultDto> pages = baseMapper.queryMyNotApproval(new Page<MyApprovalResultDto>(dto.getPageNo()
                ,dto.getPageSize()),dto);

        List<MyApprovalResultDto> records = pages.getRecords();
        if(CollectionUtils.isNotEmpty(records)){
            //根据业务类型获取数据
            List<Long> busiTypes = records.stream().map(MyApprovalResultDto::getBusinessType).collect(Collectors.toList());
            Map<Long, String> busiMap = getBusiMap(busiTypes);
            //userIds
            List<Long> usreIds = records.stream().map(MyApprovalResultDto::getUserId).collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(usreIds);
            records.stream().forEach(item->{
                //根据业务类型id获取业务类型数据
                item.setBusinessTypeName(busiMap.get(item.getBusinessType()));
                //获取所属组织
                item.setOrgPath(getOrgPath(item.getOrgIds()));
                // 发起人
                item.setUserName(userMap.get(item.getUserId()));
                //按钮
                ChildNode childNodeByNodeId = getChildNodeByNodeId(item.getProcDefId(), item.getTaskDefKey());
                List<NodeButton> buttons = childNodeByNodeId.getProps().getButtons();
                item.setButtons(buttons);

            });
        }

        return IdmResDTO.success(pages);
    }

    /**
     * 查询已审批列表
     * @param dto
     * @return
     */
    public IdmResDTO<IPage<MyApprovalResultDto>> queryMyApproval(QueryMyApprovalDto dto) {
        dto.setAssignee(LoginInfoHolder.getCurrentUserId());
        Page<MyApprovalResultDto> page=baseMapper.
                queryMyApproval(new Page<MyApprovalResultDto>(dto.getPageNo(),dto.getPageSize()),dto);
        List<MyApprovalResultDto> records = page.getRecords();
        if(CollectionUtils.isNotEmpty(records)){

            //根据业务类型获取数据
            List<Long> busiTypes = records.stream().map(MyApprovalResultDto::getBusinessType).collect(Collectors.toList());
            Map<Long, String> busiMap = getBusiMap(busiTypes);

            //userIds
            List<Long> usreIds = records.stream().map(MyApprovalResultDto::getUserId).collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(usreIds);
            records.stream().forEach(item->{
                //根据业务类型id获取业务类型数据
                item.setBusinessTypeName(busiMap.get(item.getBusinessType()));
                //获取所属组织
                item.setOrgPath(getOrgPath(item.getOrgIds()));
                // 发起人
                item.setUserName(userMap.get(item.getUserId()));
            });
        }
        return IdmResDTO.success(page);
    }

    /**
     * 获取抄送列表
     * @param dto
     * @return
     */
    public IdmResDTO<IPage<MyApprovalResultDto>> queryMakeApproval(QueryMyApprovalDto dto) {
        dto.setMakeUserId(LoginInfoHolder.getCurrentUserId());
        Page<MyApprovalResultDto> page = baseMapper.queryMakeApproval(new Page<MyApprovalResultDto>(dto.getPageNo(),dto.getPageSize()),
                dto);
        List<MyApprovalResultDto> records = page.getRecords();
        if(CollectionUtils.isNotEmpty(records)){

            //根据业务类型获取数据
            List<Long> busiTypes = records.stream().map(MyApprovalResultDto::getBusinessType).collect(Collectors.toList());
            Map<Long, String> busiMap = getBusiMap(busiTypes);

            //组织ids
//            List<Long> orgIds = records.stream().map(MyApprovalResultDto::getOrgId).collect(Collectors.toList());
//            Map<Long, String> deptMap = getDeptMap(orgIds);

            //userIds
            List<Long> usreIds = records.stream().map(MyApprovalResultDto::getUserId).collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(usreIds);
            records.stream().forEach(item->{
                //根据业务类型id获取业务类型数据
                item.setBusinessTypeName(busiMap.get(item.getBusinessType()));
                //获取所属组织
                item.setOrgPath(getOrgPath(item.getOrgIds()));
                // 发起人
                item.setUserName(userMap.get(item.getUserId()));
            });
        }
        return IdmResDTO.success(page);
    }

    public IdmResDTO<IPage<WorkInfoEntity>> queryMySubmitWorkInfo(QueryMyApprovalDto dto) {
        dto.setCreateUser(LoginInfoHolder.getCurrentUserId());
        Page<WorkInfoEntity> page = baseMapper.queryMySubmitWorkInfo(new Page<WorkInfoEntity>(dto.getPageNo(),dto.getPageSize()),
                dto);
        List<WorkInfoEntity> records = page.getRecords();
        if(CollectionUtils.isNotEmpty(records)){
            //根据业务类型获取数据
            List<Long> busiTypes = records.stream().map(WorkInfoEntity::getBusinessType).collect(Collectors.toList());
            Map<Long, String> busiMap = getBusiMap(busiTypes);
            List<Long> usreIds = records.stream().map(WorkInfoEntity::getCreateUser).collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(usreIds);
            records.stream().forEach(item->{
                //根据业务类型id获取业务类型数据
                item.setBusinessTypeName(busiMap.get(item.getBusinessType()));
                //获取所属组织
                item.setOrgPath(getOrgPath(item.getOrgIds()));
                // 发起人
                item.setUserName(userMap.get(item.getCreateUser()));
                //判断能否撤回
                item.setRevokeType(checkRevokeType(item));
                //重新提交
                item.setResubmit(resubmit(item.getProcInstId()));
            });
        }
        return IdmResDTO.success(page);
    }

    /**
     * 判断是否可以重新提交
     * @param procInstId
     * @return
     */
    public Byte resubmit(String procInstId){
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(String.valueOf(procInstId)).list();
        if (CollectionUtil.isEmpty(taskList)){
            return ButtonBusinessEnums.NOT_BUTTON.getCode();
        }
        //当前节点为root节点则可以重新提交
        if(Objects.equals(taskList.get(0).getTaskDefinitionKey(), WorkOrderConstants.USER_ROOT)){
            return ButtonBusinessEnums.BUTTON.getCode();
        }
        return ButtonBusinessEnums.NOT_BUTTON.getCode();
    }

    /**
     * 判断能否撤回
     * @param workInfoDto
     * @return
     */
    public Byte checkRevokeType(WorkInfoEntity workInfoDto){

        //查询当前任务节点
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(workInfoDto.getProcInstId()).list();
        //不存在任务信息则表示流程已经结束
        if(CollectionUtil.isEmpty(taskList)){
            return ButtonBusinessEnums.NOT_BUTTON.getCode();
        }
        //表示流程不支持撤销
        if(Objects.equals(workInfoDto.getRevokeType(),ButtonBusinessEnums.NOT_BUTTON.getCode())){
            return ButtonBusinessEnums.NOT_BUTTON.getCode();
        }
        //未完成就可以撤回
        if(Objects.equals(workInfoDto.getRevokeType(),ButtonBusinessEnums.BUTTON.getCode())){
            return ButtonBusinessEnums.BUTTON.getCode();
        }
        //同节点可以撤回
        if(Objects.equals(workInfoDto.getRevokeNodeId(),taskList.get(0).getTaskDefinitionKey())){
            return ButtonBusinessEnums.BUTTON.getCode();
        }
        return ButtonBusinessEnums.NOT_BUTTON.getCode();
    }

    /**
     * 获取部门信息
     * @param dto
     * @return
     */
    public List<DepartmentDto> queryDeptList(WorkInfoDto dto) {
        return getDeptIds(LoginInfoHolder.getCurrentDeptId());
    }

    public IdmResDTO queryDataForm(HandleDataDTO handleDataDTO) {
        HistoricVariableInstanceQuery query = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()
                .processInstanceId(handleDataDTO.getProcessInstanceId())
                .variableName("formData");
        HistoricVariableInstanceQuery formData = query.variableName("formData");
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(handleDataDTO.getProcessInstanceId())
                .singleResult();

        String flowableKey = processInstance.getProcessDefinitionKey().replaceAll("[a-zA-Z]", "");
        TbFlowConfig flowConfig = Optional.ofNullable(flowConfigService.getById(Long.parseLong(flowableKey))).
                orElseThrow(()->new RuntimeException("流程配置为空"));


        return IdmResDTO.success();
    }


    /**
     * app端任务转办
     * @param appTransferTaskDto
     * @return
     */
    public IdmResDTO appTransfer(AppTransferTaskDto appTransferTaskDto) {
        List<TaskUserInfoDto> userInfos = Optional.ofNullable(appTransferTaskDto.getTaskUserInfos())
                .orElseThrow(()->new RuntimeException("转办任务数不能为空"));
        //保存转办信息
        HandleDataDTO handleDataDTO = new HandleDataDTO();
        handleDataDTO.setTaskId(String.valueOf(userInfos.get(0).getTaskId()));
        WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
        businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_TRANSFER.getCode());
        businessTypeInfo.setDeliver(userInfos.stream().map(e->String.valueOf(e.getUserId())).collect(Collectors.joining(",")));
        workBusinessTypeInfoService.save(businessTypeInfo);

        handleDataDTO.setNodeId(businessTypeInfo.getNode());
        updateBusinessPendingDate(handleDataDTO);
        updateWorkInfo(WorkOrderStatusEnums.progress.getStatus(), businessTypeInfo.getProcInstId());

        //转办
        userInfos.stream().forEach(item->{
            taskService.setAssignee(String.valueOf(item.getTaskId()),
                    String.valueOf(item.getUserId()));
        });

        return IdmResDTO.success();
    }

    /**
     * 查询客户工单列表
     * @param req
     * @return
     */
    public IdmResDTO<IPage<CustomerWorkOrderDto>> queryCustomerWorkOrder(QueryCustomerWorkOrderDto req) {
        if(CollectionUtils.isEmpty(req.getPropertyIds())){
            //根据当前管理人员信息查询下面的楼盘id
        }
        IPage<CustomerWorkOrderDto> page= baseMapper.queryCustomerWorkOrder(new Page<CustomerWorkOrderDto>(req.getPageNo(),req.getPageSize()),req);
        List<CustomerWorkOrderDto> records = page.getRecords();
        if (CollectionUtils.isNotEmpty(records)){
            //获取业务类型
            List<Long> businessTypeIds = records.stream().map(CustomerWorkOrderDto::getBusinessType).collect(Collectors.toList());
            Map<Long, String> businessMap = getBusiMap(businessTypeIds);

            //获取楼盘信息
            List<Long> propertyIds = records.stream().map(CustomerWorkOrderDto::getPropertyId).collect(Collectors.toList());
            Map<Long,String> propertyMap =getPropertyMap(propertyIds);
            //获取发起人与报单人信息
            List<Long> userIds = records.stream().map(CustomerWorkOrderDto::getCreateUser).collect(Collectors.toList());
            List<Long> actualUserId = records.stream().map(CustomerWorkOrderDto::getActualUserId).collect(Collectors.toList());
            List<Long> combinedList = Stream.concat(userIds.stream(), actualUserId.stream()).distinct()
                    .collect(Collectors.toList());

            Map<Long, String> userMap = getUserMap(combinedList);
            records.stream().forEach(item->{
                //填充业务业务类型
                item.setBusinessTypeName(businessMap.get(item.getBusinessType()));
                //填充楼盘信息
                item.setPropertyName(propertyMap.get(item.getPropertyId()));
                //创建人
                item.setCreateUserName(userMap.get(item.getCreateUser()));
                //填报人
                item.setActualUserName(userMap.get(item.getActualUserId()));
            });
        }
        return IdmResDTO.success(page);
    }

    /**
     * 根据楼盘id获取楼盘信息
     * @param propertyIds
     * @return
     */
    public  Map<Long,String> getPropertyMap(List<Long> propertyIds){
        BuildingArchiveReq req = new BuildingArchiveReq();
        req.setIdList(propertyIds);
        IdmResDTO<List<BuildingArchive>> listIdmResDTO = archiveFeignService.buildingArchiveQueryForList(req);
        List<BuildingArchive> archiveList = Optional.ofNullable(listIdmResDTO.getData()).orElseThrow(()->new BusinessException(ErrorCode.NOT_FOUND.getCode(),
                ErrorCode.NOT_FOUND.getMessage()));
        return archiveList.stream().collect(Collectors.toMap(BuildingArchive::getId,BuildingArchive::getName));
    }
}
