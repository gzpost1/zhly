package cn.cuiot.dmp.baseconfig.flow.service;


import cn.cuiot.dmp.baseconfig.flow.constants.WorkOrderConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.AttachmentDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.QueryApprovalInfoDto;
import cn.cuiot.dmp.baseconfig.flow.dto.StartProcessInstanceDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.approval.MyApprovalResultDto;
import cn.cuiot.dmp.baseconfig.flow.dto.approval.QueryMyApprovalDto;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.ChildNode;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.FormOperates;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.Properties;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import cn.cuiot.dmp.baseconfig.flow.dto.vo.AttachmentVO;
import cn.cuiot.dmp.baseconfig.flow.dto.vo.CommentVO;
import cn.cuiot.dmp.baseconfig.flow.dto.vo.HandleDataVO;
import cn.cuiot.dmp.baseconfig.flow.dto.vo.TaskDetailVO;
import cn.cuiot.dmp.baseconfig.flow.dto.work.BusinessTypeInfoDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.FirstFormDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.HandleDataDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.work.WorkInfoDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.WorkProcInstDto;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowConfig;
import cn.cuiot.dmp.baseconfig.flow.entity.UserEntity;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkBusinessTypeInfoEntity;
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
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.engine.task.Attachment;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.baseconfig.flow.constants.CommonConstants.*;
import static cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants.*;
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

    @Autowired
    private WorkBusinessTypeInfoService workBusinessTypeInfoService;


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
        //记录转办信息
        WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
        businessTypeInfo.setBusinessType(BUSINESS_TRANSFER);
        workBusinessTypeInfoService.save(businessTypeInfo);
        //更新挂起时间
        updateBusinessPendingDate(handleDataDTO);

        if(StringUtils.isEmpty(handleDataDTO.getTaskId())){
            //批量转办
            return assigneeByProcInstId(handleDataDTO);
        }
        //单个转办
        taskService.setAssignee(handleDataDTO.getTaskId(),handleDataDTO.getTransferUserInfo().getId());

        return IdmResDTO.success();
    }

    /**
     * 批量转办
     * @param handleDataDTO
     * @return
     */

    public IdmResDTO assigneeByProcInstId(HandleDataDTO handleDataDTO){
        // 查询与流程实例ID相关联的所有任务
        List<Task> tasks = Optional.ofNullable(taskService.createTaskQuery().processInstanceId(handleDataDTO.getProcessInstanceId()).list())
                .orElseThrow(()->new RuntimeException("任务信息不存在"));
        //加签
        List<Long> assignees = tasks.stream().map(item->Long.parseLong(item.getAssignee())).collect(Collectors.toList());
        List<Long> countSigeList =handleDataDTO.getUserIds().stream().filter(item->!assignees.contains(item)).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(countSigeList)){
            countSigeList.stream().forEach(item->{
                Map<String,Object> variableMap= new HashMap<>();
                variableMap.put("assigneeName",item);
                runtimeService.addMultiInstanceExecution(tasks.get(0).getTaskDefinitionKey(), tasks.get(0).getProcessInstanceId(), variableMap);
            });
        }

        //减签
        tasks.stream().filter(item->!handleDataDTO.getUserIds().contains(Long.parseLong(item.getAssignee())))
                .forEach(it->{
                    runtimeService.deleteMultiInstanceExecution(it.getExecutionId(),true);
                });

        return IdmResDTO.success();
    }
    /**
     * 驳回
     * @param handleDataDTO
     * @return
     */
    public IdmResDTO refuse(HandleDataDTO handleDataDTO) {
            

//        runtimeService.deleteProcessInstance(task.getProcessInstanceId(),"拒绝");
//        taskService.complete(task.getId());

        return IdmResDTO.success();
    }

    /**
     * 评论或者督办
     * @param handleDataDTOs
     * @return
     */
    public IdmResDTO commentAndSuper(List<HandleDataDTO> handleDataDTOs) {
        List<WorkBusinessTypeInfoEntity> businessTypeInfoEntities = new ArrayList<>();
        for(HandleDataDTO handleDataDTO :handleDataDTOs ){
            String comments = handleDataDTO.getComments();
            String taskId = handleDataDTO.getTaskId();
            HistoricTaskInstance task = getHisTaskInfo(handleDataDTO);
            WorkBusinessTypeInfoEntity businessTypeInfo =getWorkBusinessTypeInfo(handleDataDTO);
            //评论
            if(handleDataDTO.getBusinessType().intValue()==BUSINESS_TYPE_COMMENT.intValue()){
//            taskService.addComment(task.getId(),task.getProcessInstanceId(),COMMENTS_COMMENT,comments);
                businessTypeInfo.setBusinessType(BUSINESS_TYPE_COMMENT);
            }
            //督办
            if(handleDataDTO.getBusinessType().intValue()==BUSINESS_TYPE_SUPER.intValue()){
//            taskService.addComment(task.getId(),task.getProcessInstanceId(),BUSINESS_SUPERVISION,comments);
                businessTypeInfo.setBusinessType(BUSINESS_TYPE_SUPER);
            }
            businessTypeInfoEntities.add(businessTypeInfo);
        }
        if(CollectionUtils.isNotEmpty(businessTypeInfoEntities)){
            workBusinessTypeInfoService.saveBatch(businessTypeInfoEntities);
        }
        return IdmResDTO.success();
    }

    public IdmResDTO rollback(HandleDataDTO handleDataDTO) {
        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
//        Authentication.setAuthenticatedUserIddsacurrentUserInfo.getId());
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
     * @param handleDataDTOs
     * @return
     */
    public IdmResDTO businessPending(List<HandleDataDTO> handleDataDTOs) {
//        UserInfo currentUserInfo = handleDataDTO.getCurrentUserInfo();
//        Authentication.setAuthenticatedUserId(currentUserInfo.getId());
//        //挂起留痕
//        taskService.addComment(handleDataDTO.getTaskId(),handleDataDTO.getProcessInstanceId(),BUSINESS_PENDING,handleDataDTO.getComments());
       List<WorkBusinessTypeInfoEntity> businessTypeInfoEntities = new ArrayList<>();
        for(HandleDataDTO handleDataDTO : handleDataDTOs){
           HistoricTaskInstance taskInstance = getHisTaskInfo(handleDataDTO);
           WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
           businessTypeInfo.setBusinessType(BUSINESS_BYPE_PENDING);
           businessTypeInfoEntities.add(businessTypeInfo);
       }
        if(CollectionUtils.isNotEmpty(businessTypeInfoEntities)){
            workBusinessTypeInfoService.saveBatch(businessTypeInfoEntities);
        }
        return IdmResDTO.success();
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
        }else {
            task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
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
        List<WorkInfoDto> records = workInfoEntityPage.getRecords();
        if(CollectionUtils.isNotEmpty(records)){
            records.stream().forEach(item->{
                //获取是否超时
                BusinessTypeInfoDto build = BusinessTypeInfoDto.builder().businessType(BUSINESS_TYPE_TIME_OUT).
                        procInstId(Long.parseLong(item.getProcInstId())).build();
                List<WorkBusinessTypeInfoEntity> workBusinessTypeInfoEntities = queryBusinessTypeInfo(build);
                if(CollectionUtils.isNotEmpty(workBusinessTypeInfoEntities)){
                    item.setTimeOut(RESULT_1);
                }
                //TODO 填充组织名称与业务类型后返回分页数据
            });
        }


        return IdmResDTO.success(workInfoEntityPage);
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
        //获取工单详情
        WorkInfoDto resultDto = getBaseMapper().queryWorkOrderDetailInfo(dto);
        //TODO 填充组织名称与业务类型后返回数据
        //TODO 获取用户手机号

        //获取挂起时间
        BusinessTypeInfoDto businessTypeInfoDto = BusinessTypeInfoDto.builder().businessType(BUSINESS_BYPE_PENDING).
                procInstId(Long.parseLong(resultDto.getProcInstId())).build();
        Long pendTime = calculateSuspensionTime(businessTypeInfoDto);
        //获取流程实例信息
        HistoricProcessInstance historicProcessInstance = Optional.ofNullable(historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(resultDto.getProcInstId())
                .singleResult()).orElseThrow(()->new RuntimeException("未找到流程实例信息"));

        Long time =historicProcessInstance.getEndTime().getTime()-historicProcessInstance.getStartTime().getTime()-pendTime;
        resultDto.setWorkTime(converTime(time));
        return IdmResDTO.success(resultDto);
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
                        (Calendar.getInstance().getTimeInMillis()-item.getStartTime().getTime()):
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
        String ex = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId()).getMainProcess().
                getAttributeValue(ATTRIBUTE_NAME_SPACE, ATTRIBUTE_NAME);
        HashMap hashMap = JSONObject.parseObject(ex, new TypeReference<HashMap>() {
        });
        String processJson = MapUtil.getStr(hashMap, "processJson");
        String formJson = MapUtil.getStr(hashMap, "formJson");

        TbFlowConfig flowConfig = flowConfigService.getById(processDefinitionKey.replace(PROCESS_PREFIX, ""));
        flowConfig.setProcess(processJson);
        flowConfig.setFormItems(formJson);
        HandleDataVO handleDataVO =new HandleDataVO();
        Map<String, Object> processVariables = historicProcessInstance.getProcessVariables();

        handleDataVO.setProcessInstanceId(historicProcessInstance.getId());
        JSONObject jsonObject = (JSONObject) processVariables.get(FORM_VAR);
        handleDataVO.setFormData(jsonObject);
        ChildNode childNode = JSONObject.parseObject(processJson, new TypeReference<ChildNode>(){});
        ChildNode currentNode=null;
        if(StringUtils.isNotBlank(HandleDataDTO.getTaskId())){
            HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(HandleDataDTO.getTaskId()).singleResult();
            currentNode = getChildNode(childNode, historicTaskInstance.getTaskDefinitionKey());
            List<FormOperates> formPerms = currentNode.getProps().getFormPerms();
            if(CollUtil.isNotEmpty(formPerms)){
                Iterator<FormOperates> iterator = formPerms.iterator();
                while (iterator.hasNext()){
                    FormOperates next = iterator.next();
                    if("H".equals(next.getPerm())){
                        if(jsonObject!=null){
                            jsonObject.remove(next.getId());
                        }
                    }
                }
            }
            handleDataVO.setCurrentNode(currentNode);
            handleDataVO.setTaskId(HandleDataDTO.getTaskId());
        }
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(historicProcessInstance.getId()).list();
        Map<String,List<HistoricActivityInstance>> historicActivityInstanceMap =new HashMap<>();
        for (HistoricActivityInstance historicActivityInstance : list) {
            List<HistoricActivityInstance> historicActivityInstances = historicActivityInstanceMap.get(historicActivityInstance.getActivityId());
            if(historicActivityInstances==null){
                historicActivityInstances =new ArrayList<>();
                historicActivityInstances.add(historicActivityInstance);
                historicActivityInstanceMap.put(historicActivityInstance.getActivityId(),historicActivityInstances);
            }
            else{
                historicActivityInstances.add(historicActivityInstance);
                historicActivityInstanceMap.put(historicActivityInstance.getActivityId(),historicActivityInstances);
            }
        }
        Process mainProcess = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId()).getMainProcess();
        Collection<FlowElement> flowElements = mainProcess.getFlowElements();

        List<String> runningList= new ArrayList<>();
        handleDataVO.setRunningList(runningList);
        List<String> endList=new ArrayList<>();
        handleDataVO.setEndList(endList);
        List<String> noTakeList=new ArrayList<>();
        handleDataVO.setNoTakeList(noTakeList);
        Map<String,List<TaskDetailVO>> deatailMap =new HashMap<>();
        List<Comment> processInstanceComments = taskService.getProcessInstanceComments(historicProcessInstance.getId());
        List<Attachment> processInstanceAttachments = taskService.getProcessInstanceAttachments(historicProcessInstance.getId());

        for (FlowElement flowElement : flowElements) {
            List<TaskDetailVO> detailVOList =new ArrayList<>();
            List<HistoricActivityInstance> historicActivityInstanceList = historicActivityInstanceMap.get(flowElement.getId());
            if(CollUtil.isNotEmpty(historicActivityInstanceList)){
                for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
                    if(historicActivityInstance.getEndTime()!=null){
                        if("startEvent".equalsIgnoreCase(historicActivityInstance.getActivityType()) ||"endEvent".equalsIgnoreCase(historicActivityInstance.getActivityType())){
                            TaskDetailVO taskDetailVO = new TaskDetailVO();
                            taskDetailVO.setActivityId(historicActivityInstance.getActivityId());
                            taskDetailVO.setName(historicActivityInstance.getActivityName());
                            taskDetailVO.setCreateTime(historicActivityInstance.getStartTime());
                            taskDetailVO.setEndTime(historicActivityInstance.getEndTime());
                            detailVOList.add(taskDetailVO);
                            deatailMap.put(historicActivityInstance.getActivityId(),detailVOList);
                            endList.add(historicActivityInstance.getActivityId());
                        }
                        else if ("userTask".equalsIgnoreCase(historicActivityInstance.getActivityType())){
                            List<TaskDetailVO> voList = deatailMap.get(historicActivityInstance.getActivityId());
                            List<HistoricActivityInstance> activityInstanceList = list.stream().filter(h -> h.getActivityId().equals(historicActivityInstance.getActivityId()) &&h.getEndTime()!=null).collect(Collectors.toList());
                            if(voList!=null){
//                                collectUserTaskInfo(processInstanceComments, processInstanceAttachments, historicActivityInstance, voList, activityInstanceList);
                            }else{
                                voList=new ArrayList<>();
                                collectUserTaskInfo(processInstanceComments, processInstanceAttachments, historicActivityInstance, voList, activityInstanceList);
                                deatailMap.put(historicActivityInstance.getActivityId(),voList);
                                endList.add(historicActivityInstance.getActivityId());
                            }

                        }else if("serviceTask".equalsIgnoreCase(historicActivityInstance.getActivityType())){

                        }
                    }else{
                        if ("userTask".equalsIgnoreCase(historicActivityInstance.getActivityType())){
                            List<TaskDetailVO> voList = deatailMap.get(historicActivityInstance.getActivityId());
                            List<HistoricActivityInstance> activityInstanceList = list.stream().filter(h -> h.getActivityId().equals(historicActivityInstance.getActivityId()) &&h.getEndTime()==null).collect(Collectors.toList());
                            if(voList!=null){
//                                collectUserTaskInfo(processInstanceComments, processInstanceAttachments, historicActivityInstance, voList, activityInstanceList);
                            }
                            else{
                                voList=new ArrayList<>();
                                collectUserTaskInfo(processInstanceComments, processInstanceAttachments, historicActivityInstance, voList, activityInstanceList);
                            }
                            deatailMap.put(historicActivityInstance.getActivityId(),voList);
                            if(endList.contains(historicActivityInstance.getActivityId())){
                                endList.remove(historicActivityInstance.getActivityId());
                                runningList.add(historicActivityInstance.getActivityId());
                            }
                            else{
                                runningList.add(historicActivityInstance.getActivityId());
                            }
                        }
                        else if("serviceTask".equalsIgnoreCase(historicActivityInstance.getActivityType())){

                        }
                    }
                }
            }else{
                noTakeList.add(flowElement.getId());
            }
        }
        handleDataVO.setProcessTemplates(flowConfig);
        handleDataVO.setDetailVOList(deatailMap);
        return IdmResDTO.success(handleDataVO);
    }

    private void collectUserTaskInfo(List<Comment> processInstanceComments,
                                     List<Attachment> processInstanceAttachments,
                                     HistoricActivityInstance historicActivityInstance,
                                     List<TaskDetailVO> voList,
                                     List<HistoricActivityInstance> activityInstanceList) {
        for (HistoricActivityInstance activityInstance : activityInstanceList) {
            TaskDetailVO taskDetailVO =new TaskDetailVO();
            taskDetailVO.setTaskId(activityInstance.getTaskId());
            taskDetailVO.setActivityId(activityInstance.getActivityId());
            taskDetailVO.setName(activityInstance.getActivityName());
            taskDetailVO.setCreateTime(activityInstance.getStartTime());
            taskDetailVO.setEndTime(activityInstance.getEndTime());
            Comment signComment = processInstanceComments.stream().filter(h -> h.getTaskId().equals(historicActivityInstance.getTaskId()) && h.getType().equals(SIGN_COMMENT)).findFirst().orElse(null);
            if(signComment!=null){
                taskDetailVO.setSignImage(signComment.getFullMessage());
            }
            List<Attachment> attachments = processInstanceAttachments.stream().filter(h -> h.getTaskId().equals(historicActivityInstance.getTaskId())).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(attachments)){
                List<AttachmentVO> attachmentVOList = new ArrayList<>();
                for (Attachment attachment : attachments) {
                    AttachmentVO attachmentVO = new AttachmentVO();
                    attachmentVO.setId(attachment.getId());
                    attachmentVO.setName(attachment.getName());
                    attachmentVO.setUrl(attachment.getUrl());
                    attachmentVOList.add(attachmentVO);
                }
                taskDetailVO.setAttachmentVOList(attachmentVOList);
            }

//            List<Comment> options = processInstanceComments.stream().filter(h -> h.getTaskId().equals(historicActivityInstance.getTaskId())
//                   ).collect(Collectors.toList());
//            if(CollUtil.isNotEmpty(options)){
//                List<OptionVO> optionVOList =new ArrayList<>();
//                for (Comment option : options) {
//                    OptionVO optionVO = new OptionVO();
//                    optionVO.setComments(option.getFullMessage());
//                    optionVO.setUserId(option.getUserId());
////                                        optionVO.setUserName();
//                    optionVO.setCreateTime(option.getTime());
//                    optionVOList.add(optionVO);
//                }
//                taskDetailVO.setOptionVOList(optionVOList);
//            }
///  && h.getType().equals(COMMENTS_COMMENT)
            List<Comment> comments = processInstanceComments.stream().filter(h -> h.getTaskId().equals(historicActivityInstance.getTaskId())
            ).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(comments)){
                List<CommentVO> commentsVOList =new ArrayList<>();
                for (Comment comment : comments) {
                    CommentVO commentVO = new CommentVO();
                    commentVO.setComments(comment.getFullMessage());
                    commentVO.setUserId(comment.getUserId());
//                                        commentVO.setUserName();
                    commentVO.setCreateTime(comment.getTime());
                    commentsVOList.add(commentVO);
                }
                taskDetailVO.setCommentVOList(commentsVOList);
            }

            voList.add(taskDetailVO);


        }
    }


    public static  ChildNode getChildNode(ChildNode childNode,String nodeId){
        Map<String,ChildNode> childNodeMap =new HashMap<>();
        if(StringUtils.isNotBlank(childNode.getId())){
            getChildNode(childNode,childNodeMap);
        }

        Set<String> set = childNodeMap.keySet();
        for (String s : set) {
            if(StringUtils.isNotBlank(s)){
                if(s.equals(nodeId)){
                    return childNodeMap.get(s);
                }
            }
        }
        return null;
    }

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
    public IdmResDTO closeFlow(List<HandleDataDTO> handleDataDTOs) {
        //保存操作信息
        List<WorkBusinessTypeInfoEntity> businessTypeInfoEntities = new ArrayList<>();
        for(HandleDataDTO handleDataDTO:handleDataDTOs){
            WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
            workBusinessTypeInfo.setBusinessType(BUSINESS_BYTE_CLOSE);
            businessTypeInfoEntities.add(workBusinessTypeInfo);
            //终止流程
            runtimeService.deleteProcessInstance(handleDataDTO.getProcessInstanceId(), handleDataDTO.getComments()); // 终止
        }

        if(CollectionUtils.isNotEmpty(businessTypeInfoEntities)){
            workBusinessTypeInfoService.saveBatch(businessTypeInfoEntities);
        }
        return IdmResDTO.success();
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
        return businessTypeInfo;
    }

    /**
     * 更新挂起结束时间
     * @param handleDataDTO
     */
    public void updateBusinessPendingDate(HandleDataDTO handleDataDTO){
        LambdaUpdateWrapper<WorkBusinessTypeInfoEntity> lw = new LambdaUpdateWrapper<>();
        lw.eq(WorkBusinessTypeInfoEntity::getProcInstId,handleDataDTO.getProcessInstanceId())
                .eq(WorkBusinessTypeInfoEntity::getBusinessType,BUSINESS_BYPE_PENDING)
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

        Page<MyApprovalResultDto> pages = baseMapper.queryMyNotApproval(new Page<MyApprovalResultDto>(dto.getCurrentPage()
                ,dto.getPageSize()),dto);

        List<MyApprovalResultDto> records = pages.getRecords();
        if(CollectionUtils.isNotEmpty(records)){
            records.stream().forEach(item->{
                //TODO 根据业务类型id获取业务类型数据
                //TODO 获取所属组织
                //TODO 发起人
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
        dto.setAssignee(String.valueOf(LoginInfoHolder.getCurrentUserId()));
        dto.setQueryType(QUERY_TYPE_APPROVAL);
        Page<MyApprovalResultDto> page=baseMapper.
                queryMyApproval(new Page<MyApprovalResultDto>(dto.getCurrentPage(),dto.getPageSize()),dto);
        List<MyApprovalResultDto> records = page.getRecords();
        if(CollectionUtils.isNotEmpty(records)){
            records.stream().forEach(item->{
                //TODO 根据业务类型id获取业务类型数据
                //TODO 获取所属组织
                //TODO 发起人
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
        Page<MyApprovalResultDto> page = baseMapper.queryMakeApproval(new Page<MyApprovalResultDto>(dto.getCurrentPage(),dto.getPageSize()),
                dto);
        List<MyApprovalResultDto> records = page.getRecords();
        if(CollectionUtils.isNotEmpty(records)){
            records.stream().forEach(item->{
                //TODO 根据业务类型id获取业务类型数据
                //TODO 获取所属组织
                //TODO 发起人
            });
        }
        return IdmResDTO.success(page);
    }

    public IdmResDTO<IPage<WorkInfoEntity>> queryMySubmitWorkInfo(QueryMyApprovalDto dto) {
        dto.setCreateUser(String.valueOf(LoginInfoHolder.getCurrentUserId()));
        Page<WorkInfoEntity> page = baseMapper.queryMySubmitWorkInfo(new Page<WorkInfoEntity>(dto.getCurrentPage(),dto.getPageSize()),
                dto);
        List<WorkInfoEntity> records = page.getRecords();
        if(CollectionUtils.isNotEmpty(records)){
            records.stream().forEach(item->{
                //TODO 根据业务类型id获取业务类型数据
                //TODO 获取所属组织
                //TODO 发起人
            });
        }
        return IdmResDTO.success(page);
    }

}
