package cn.cuiot.dmp.baseconfig.flow.service;


import cn.cuiot.dmp.baseconfig.flow.constants.WorkOrderConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.AttachmentDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.HandleDataDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.QueryApprovalInfoDto;
import cn.cuiot.dmp.baseconfig.flow.dto.StartProcessInstanceDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.ChildNode;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.FormOperates;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.Properties;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import cn.cuiot.dmp.baseconfig.flow.dto.work.FirstFormDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.WorkInfoDto;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowConfig;
import cn.cuiot.dmp.baseconfig.flow.entity.UserEntity;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.enums.AssigneeTypeEnums;
import cn.cuiot.dmp.baseconfig.flow.mapper.WorkInfoMapper;
import cn.cuiot.dmp.baseconfig.flow.vo.HistoryProcessInstanceVO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.xml.internal.bind.v2.TODO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.baseconfig.flow.constants.CommonConstants.*;
import static cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants.*;
import static cn.cuiot.dmp.baseconfig.flow.utils.BpmnModelUtils.getChildNode;
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
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserService userService;


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
            taskService.complete(task.getId());
        }else{
            //新建
            ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
            ProcessInstance processInstance = processInstanceBuilder
                    .processDefinitionId(startProcessInstanceDTO.getProcessDefinitionId())
                    .variables(processVariables)
                    .businessStatus(WorkOrderConstants.BUSINESS_STATUS_1)
                    .start();
            //手动完成第一个任务
            task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
            if(task!=null){
                taskService.complete(task.getId());
            }
            //保存工单信息
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(startProcessInstanceDTO.getProcessDefinitionId())
                    .singleResult();
            String flowableKey = processDefinition.getKey().replaceAll("[a-zA-Z]", "");
            TbFlowConfig flowConfig = Optional.ofNullable(flowConfigService.getById(Long.parseLong(flowableKey))).
                    orElseThrow(()->new RuntimeException("流程配置为空"));
            //保存工单信息
            WorkInfoEntity entity = new WorkInfoEntity();
            entity.setId(IdWorker.getId());
            entity.setBusinessType(flowConfig.getBusinessTypeId());
            entity.setOrgId(flowConfig.getOrgId());
            entity.setCreateTime(new Date());
            entity.setWorkName(flowConfig.getName());
            entity.setWorkSouce(startProcessInstanceDTO.getWorkSource());
            entity.setCreateUser(LoginInfoHolder.getCurrentUserId());
            entity.setProcInstId(task.getProcessInstanceId());
            this.save(entity);
        }
        taskService.addComment(task.getId(),task.getProcessInstanceId(), START_PROCESS,"启动流程");
        return IdmResDTO.success();
    }


    public IdmResDTO<IPage<WorkInfoEntity>> processList(QueryApprovalInfoDto dto) {
        List<HistoricProcessInstance> historicProcessInstances =
                historyService.createHistoricProcessInstanceQuery()
                        .includeProcessVariables()
                        .startedBy(dto.getUserId())
                        .orderByProcessInstanceStartTime().desc()
                        .listPage((dto.getCurrentPage() - 1) * dto.getPageSize(), dto.getPageSize());
        long count = historyService.createHistoricProcessInstanceQuery()
                .startedBy(dto.getUserId()).count();
        List<String> applyUserIds= new ArrayList<>();
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();
            String id = JSONObject.parseObject(MapUtil.getStr(processVariables, WorkOrderConstants.START_USER_INFO), new TypeReference<UserInfo>() {
            }).getId();
            applyUserIds.add(id);
        }
        Map<Long, UserEntity> collect=new HashMap<>();
        if(CollUtil.isNotEmpty(applyUserIds)){
            LambdaQueryWrapper<UserEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//            lambdaQueryWrapper.in(UserInfo::getId,applyUserIds);
//            List<UserEntity> list = userService.list(lambdaQueryWrapper);
//            collect = list.stream().collect(Collectors.toMap(UserEntity::getUserId, Function.identity()));
        }

        List<HistoryProcessInstanceVO> historyProcessInstanceVOS= new ArrayList<>();
        Page<HistoryProcessInstanceVO> page=new Page<>();
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();
            HistoryProcessInstanceVO historyProcessInstanceVO=new HistoryProcessInstanceVO();
            historyProcessInstanceVO.setProcessInstanceId(historicProcessInstance.getId());
            historyProcessInstanceVO.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
            historyProcessInstanceVO.setStartUser(JSONObject.parseObject(MapUtil.getStr(processVariables,START_USER_INFO),new TypeReference<UserInfo>(){}));
            historyProcessInstanceVO.setUsers(collect.get(Long.valueOf(historyProcessInstanceVO.getStartUser().getId())));
            historyProcessInstanceVO.setStartTime(historicProcessInstance.getStartTime());
            historyProcessInstanceVO.setEndTime(historicProcessInstance.getEndTime());
            Boolean flag= historicProcessInstance.getEndTime() != null;
//            historyProcessInstanceVO.setCurrentActivityName(getCurrentName(historicProcessInstance.getId(),flag,historicProcessInstance.getProcessDefinitionId()));
            historyProcessInstanceVO.setBusinessStatus(MapUtil.getStr(processVariables,PROCESS_STATUS));


            long totalTimes = historicProcessInstance.getEndTime()==null?
                    (Calendar.getInstance().getTimeInMillis()-historicProcessInstance.getStartTime().getTime()):
                    (historicProcessInstance.getEndTime().getTime()-historicProcessInstance.getStartTime().getTime());
            long dayCount = totalTimes /(1000*60*60*24);//计算天
            long restTimes = totalTimes %(1000*60*60*24);//剩下的时间用于计于小时
            long hourCount = restTimes/(1000*60*60);//小时
            restTimes = restTimes % (1000*60*60);
            long minuteCount = restTimes / (1000*60);

            String spendTimes = dayCount+"天"+hourCount+"小时"+minuteCount+"分";
            historyProcessInstanceVO.setDuration(spendTimes);
            historyProcessInstanceVOS.add(historyProcessInstanceVO);
        }
        page.setRecords(historyProcessInstanceVOS);
//        page.setCurrent(applyDTO.getPageNo());
//        page.setSize(applyDTO.getPageSize());
        page.setTotal(count);
        return null;
    }

    /**
     * 转办
     * @param handleDataDTO
     * @return
     */
    public IdmResDTO delegateTask(HandleDataDTO handleDataDTO) {
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        List<AttachmentDTO> attachments = handleDataDTO.getAttachments();
        String comments = handleDataDTO.getComments();
        JSONObject formData = handleDataDTO.getFormData();
        String taskId = handleDataDTO.getTaskId();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String,Object> map=new HashMap<>();
        if(formData!=null &&formData.size()>0){
            Map formValue = JSONObject.parseObject(formData.toJSONString(), new TypeReference<Map>() {
            });
            map.putAll(formValue);
            map.put(FORM_VAR,formData);
        }

        runtimeService.setVariables(task.getProcessInstanceId(),map);
        if(StringUtils.isNotBlank(comments)){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),OPINION_COMMENT,comments);
        }
        if(attachments!=null && attachments.size()>0){
            for (AttachmentDTO attachment : attachments) {
                taskService.createAttachment(OPTION_COMMENT,taskId,task.getProcessInstanceId(),attachment.getName(),attachment.getName(),attachment.getUrl());
            }
        }

        if(StringUtils.isNotBlank(handleDataDTO.getSignInfo())){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),SIGN_COMMENT,handleDataDTO.getSignInfo());
        }

        UserInfo delegateUserInfo = handleDataDTO.getDelegateUserInfo();
        taskService.delegateTask(task.getId(),delegateUserInfo.getId());

        return IdmResDTO.success();
    }

    /**
     * 同意
     * @param handleDataDTO
     * @return
     */
    public IdmResDTO agree(HandleDataDTO handleDataDTO) {
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
        //审批通过
        List<AttachmentDTO> attachments = handleDataDTO.getAttachments();
        String comments = handleDataDTO.getComments();
        JSONObject formData = handleDataDTO.getFormData();
        String taskId = handleDataDTO.getTaskId();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(DelegationState.PENDING.equals(task.getDelegationState())){
            return IdmResDTO.error("委派人不可以点击同意按钮,而应该点击 委派人完成按钮");
        }
        Map<String,Object> map=new HashMap<>();
        if(formData!=null &&formData.size()>0){
            Map formValue = JSONObject.parseObject(formData.toJSONString(), new TypeReference<Map>() {
            });
            map.putAll(formValue);
            map.put(FORM_VAR,formData);
        }

        runtimeService.setVariables(task.getProcessInstanceId(),map);
        //审批通过留痕
        taskService.addComment(task.getId(),task.getProcessInstanceId(),OPINION_COMMENT,comments);

        if(attachments!=null && attachments.size()>0){
            for (AttachmentDTO attachment : attachments) {
                taskService.createAttachment(OPTION_COMMENT,taskId,task.getProcessInstanceId(),attachment.getName(),attachment.getName(),attachment.getUrl());
            }
        }

        if(StringUtils.isNotBlank(handleDataDTO.getSignInfo())){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),SIGN_COMMENT,handleDataDTO.getSignInfo());
        }

        taskService.complete(task.getId());
        return IdmResDTO.success();
    }

    /**
     * 转办
     * @param handleDataDTO
     * @return
     */
    public IdmResDTO assignee(HandleDataDTO handleDataDTO) {

        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        List<AttachmentDTO> attachments = handleDataDTO.getAttachments();
        String comments = handleDataDTO.getComments();
        JSONObject formData = handleDataDTO.getFormData();
        String taskId = handleDataDTO.getTaskId();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String,Object> map=new HashMap<>();
        if(formData!=null &&formData.size()>0){
            Map formValue = JSONObject.parseObject(formData.toJSONString(), new TypeReference<Map>() {
            });
            map.putAll(formValue);
            map.put(FORM_VAR,formData);
        }
        map.put(PROCESS_STATUS,BUSINESS_STATUS_1);
        runtimeService.setVariables(task.getProcessInstanceId(),map);

        //转办留痕
        taskService.addComment(task.getId(),task.getProcessInstanceId(),BUSINESS_TRANSFER,comments);

        if(attachments!=null && attachments.size()>0){
            for (AttachmentDTO attachment : attachments) {
                taskService.createAttachment(OPTION_COMMENT,taskId,task.getProcessInstanceId(),attachment.getName(),attachment.getName(),attachment.getUrl());
            }
        }

        if(StringUtils.isNotBlank(handleDataDTO.getSignInfo())){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),SIGN_COMMENT,handleDataDTO.getSignInfo());
        }
        taskService.setAssignee(taskId,handleDataDTO.getTransferUserInfo().getId());

        //转办留痕
        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
        taskService.addComment(task.getId(),task.getProcessInstanceId(),COMMENTS_COMMENT,comments);
        return IdmResDTO.success();
    }

    /**
     * 驳回
     * @param handleDataDTO
     * @return
     */
    public IdmResDTO refuse(HandleDataDTO handleDataDTO) {
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
        List<AttachmentDTO> attachments = handleDataDTO.getAttachments();
        String comments = handleDataDTO.getComments();
        JSONObject formData = handleDataDTO.getFormData();
        String taskId = handleDataDTO.getTaskId();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String,Object> map=new HashMap<>();
        if(formData!=null &&formData.size()>0){
            Map formValue = JSONObject.parseObject(formData.toJSONString(), new TypeReference<Map>() {
            });
            map.putAll(formValue);
            map.put(FORM_VAR,formData);
        }
        map.put(PROCESS_STATUS,BUSINESS_STATUS_3);
        runtimeService.setVariables(task.getProcessInstanceId(),map);

        taskService.addComment(task.getId(),task.getProcessInstanceId(),BUSINESS_REFUSE,comments);

        if(attachments!=null && attachments.size()>0){
            for (AttachmentDTO attachment : attachments) {
                taskService.createAttachment(OPTION_COMMENT,taskId,task.getProcessInstanceId(),attachment.getName(),attachment.getName(),attachment.getUrl());
            }
        }

//        runtimeService.deleteProcessInstance(task.getProcessInstanceId(),"拒绝");
        taskService.complete(task.getId());

        return IdmResDTO.success();
    }

    /**
     * 评论或者督办
     * @param handleDataDTO
     * @return
     */
    public IdmResDTO commentAndSuper(HandleDataDTO handleDataDTO) {
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
        List<AttachmentDTO> attachments = handleDataDTO.getAttachments();
        String comments = handleDataDTO.getComments();
        JSONObject formData = handleDataDTO.getFormData();
        String taskId = handleDataDTO.getTaskId();
        HistoricTaskInstance task = null;
        if(null == taskId){
            //通过流程实例id找最新的taskId
            List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(handleDataDTO.getProcessInstanceId()).orderByTaskId().desc().list();
            if(CollUtil.isNotEmpty(list)){
                task = list.get(0);
            }
        }else {
            task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        }
        if(null == task){
            return IdmResDTO.error("找不到任务");
        }

        Map<String,Object> map=new HashMap<>();
        if(formData!=null &&formData.size()>0){
            Map formValue = JSONObject.parseObject(formData.toJSONString(), new TypeReference<Map>() {
            });
            map.putAll(formValue);
            map.put(FORM_VAR,formData);
        }
        //评论
        if(handleDataDTO.getBusinessType().intValue()==BUSINESS_TYPE_COMMENT){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),COMMENTS_COMMENT,comments);
        }
        //督办
        if(handleDataDTO.getBusinessType().intValue()==BUSINESS_TYPE_SUPER){
            taskService.addComment(task.getId(),task.getProcessInstanceId(),BUSINESS_SUPERVISION,comments);
        }

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(handleDataDTO.getProcessInstanceId()).singleResult();
        if(historicProcessInstance.getEndTime()==null) {
            runtimeService.setVariables(task.getProcessInstanceId(), map);
            if (attachments != null && attachments.size() > 0) {
                for (AttachmentDTO attachment : attachments) {
                    taskService.createAttachment(OPTION_COMMENT, taskId, task.getProcessInstanceId(), attachment.getName(), attachment.getName(), attachment.getUrl());
                }
            }

            if (StringUtils.isNotBlank(handleDataDTO.getSignInfo())) {
                taskService.addComment(task.getId(), task.getProcessInstanceId(), SIGN_COMMENT, handleDataDTO.getSignInfo());
            }


        }
        return IdmResDTO.success();
    }

    public IdmResDTO rollback(HandleDataDTO handleDataDTO) {
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
        //回退留痕
        taskService.addComment(handleDataDTO.getTaskId(),handleDataDTO.getProcessInstanceId(),BUSINESS_BACK,handleDataDTO.getComments());

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
     * @param handleDataDTO
     * @return
     */
    public IdmResDTO businessPending(HandleDataDTO handleDataDTO) {
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
        //挂起留痕
        taskService.addComment(handleDataDTO.getTaskId(),handleDataDTO.getProcessInstanceId(),BUSINESS_PENDING,handleDataDTO.getComments());

        return IdmResDTO.success();
    }

    /**
     * 查询节点属性数据,并判断是否有权限发起该流程
     * @return
     */
    public IdmResDTO<Properties> queryFirstFormInfo(FirstFormDto dto, String userId) {
        ChildNode childNodeByNodeId = getChildNodeByNodeId(dto.getProcessDefinitionId(), dto.getTaskDefinitionKey());
        Properties props = childNodeByNodeId.getProps();

        List<String> ids = props.getAssignedUser().stream().map(UserInfo::getId).collect(Collectors.toList());
        log.info("指定类型"+props.getAssignedType());
        //指定人员
        if(StringUtils.equals(props.getAssignedType(), AssigneeTypeEnums.ASSIGN_USER.getTypeName())){
            AssertUtil.isTrue(ids.contains(userId),"没有权限发起该流程");
        }

        //TODO 根据userId获取部门信息
        if(StringUtils.equals(props.getAssignedType(),AssigneeTypeEnums.DEPT.getTypeName())){
            AssertUtil.isTrue(ids.contains(userId),"没有权限发起该流程");
        }
        //TODO 根据userId获取角色信息
        if(StringUtils.equals(props.getAssignedType(),AssigneeTypeEnums.ROLE.getTypeName())){
            AssertUtil.isTrue(ids.contains(userId),"没有权限发起该流程");
        }

        return IdmResDTO.success(props);
    }

    public IdmResDTO<IPage<WorkInfoDto>> queryWorkOrderInfo(WorkInfoDto dto) {
        //TODO 根据组织id获取本组织与子组织信息
        List<Long> orgIds = new ArrayList<>();
        dto.setOrgIds(orgIds);
        Page<WorkInfoDto> workInfoEntityPage = getBaseMapper().queryWorkOrderInfo(new Page<WorkInfoDto>(dto.getCurrentPage(),dto.getPageSize()),dto);
        //TODO 填充组织名称与业务类型后返回分页数据

        return IdmResDTO.success(workInfoEntityPage);
    }

}
