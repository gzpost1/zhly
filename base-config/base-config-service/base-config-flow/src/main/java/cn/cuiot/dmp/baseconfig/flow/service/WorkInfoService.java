package cn.cuiot.dmp.baseconfig.flow.service;


import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BusinessTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.BusinessTypeRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.baseconfig.flow.constants.WorkOrderConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.AttachmentDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.NodeDetailDto;
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
import cn.cuiot.dmp.baseconfig.flow.enums.WorkOrderStatusEnums;
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
import org.apache.poi.hssf.record.DVALRecord;
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
import static cn.cuiot.dmp.baseconfig.flow.constants.WorkOrderConstants.USER_TASK;
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
    @Autowired
    private SystemApiFeignService systemApiFeignService;


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
            entity.setOrgId(LoginInfoHolder.getCurrentDeptId());
            entity.setCreateTime(new Date());
            entity.setWorkName(flowConfig.getName());
            entity.setWorkSouce(startProcessInstanceDTO.getWorkSource());
            entity.setCreateUser(LoginInfoHolder.getCurrentUserId());
            entity.setProcInstId(task.getProcessInstanceId());
            entity.setStatus(WorkOrderStatusEnums.completed.getStatus());
            this.save(entity);
        }
        return IdmResDTO.success(task.getProcessInstanceId());
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
        //审批通过

        taskService.complete(handleDataDTO.getTaskId());
        WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
        businessTypeInfo.setBusinessType(RESULT_0);

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
    public IdmResDTO assignee(HandleDataDTO handleDataDTO) {
        //记录转办信息
        WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
        businessTypeInfo.setBusinessType(BUSINESS_TRANSFER);
        workBusinessTypeInfoService.save(businessTypeInfo);
        //更新挂起时间
        handleDataDTO.setNodeId(businessTypeInfo.getNode());
        updateBusinessPendingDate(handleDataDTO);

        if(CollectionUtils.isNotEmpty(handleDataDTO.getUserIds())){
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
     * 拒绝
     * @param handleDataDTO
     * @return
     */
    public IdmResDTO refuse(HandleDataDTO handleDataDTO) {

        WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
//        runtimeService.deleteProcessInstance(task.getProcessInstanceId(),"拒绝");
        workBusinessTypeInfo.setBusinessType(BUSINESS_TYPE_REFUSR);
        workBusinessTypeInfoService.save(workBusinessTypeInfo);
        //更新挂起
        handleDataDTO.setNodeId(workBusinessTypeInfo.getNode());
        updateBusinessPendingDate(handleDataDTO);
        taskService.complete(handleDataDTO.getTaskId());

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
        WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
        workBusinessTypeInfo.setBusinessType(BUSINESS_BACK);
        workBusinessTypeInfoService.save(workBusinessTypeInfo);

        updateBusinessPendingDate(handleDataDTO);

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
           WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
           businessTypeInfo.setBusinessType(BUSINESS_BYPE_PENDING);
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
    public IdmResDTO<Properties> queryFirstFormInfo(FirstFormDto dto, Long userId) {
        ChildNode childNodeByNodeId = getChildNodeByNodeId(dto.getProcessDefinitionId(), WorkOrderConstants.USER_ROOT);
        Properties props = childNodeByNodeId.getProps();

        List<String> ids = props.getAssignedUser().stream().map(UserInfo::getId).collect(Collectors.toList());
        log.info("指定类型"+props.getAssignedType());
        //指定人员
        if(StringUtils.equals(props.getAssignedType(), AssigneeTypeEnums.ASSIGN_USER.getTypeName())){
            AssertUtil.isTrue(ids.contains(userId),"没有权限发起该流程");
        }

        return IdmResDTO.success(props);
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
            List<Long> orgIds = records.stream().map(WorkInfoDto::getOrg).collect(Collectors.toList());
            Map<Long, String> orgMap = getDeptMap(orgIds);
            records.stream().forEach(item->{
                //组织名称
                item.setOrgPath(orgMap.get(item.getOrg()));
                //用户名称
                item.setUserName(userMap.get(item.getCreateUser()));
               //业务类型
                item.setBusinessTypeName(busiMap.get(item.getBusinessType()));
            });
        }


        return IdmResDTO.success(workInfoEntityPage);
    }

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
        //获取工单详情
        WorkInfoDto resultDto = getBaseMapper().queryWorkOrderDetailInfo(dto);
        //填充组织名称
        Map<Long, String> deptMap = getDeptMap(Arrays.asList(resultDto.getOrg()));
        resultDto.setOrgPath(deptMap.get(resultDto.getOrg()));
        // 业务类型后返回数据
        Map<Long, String> busiMap = getBusiMap(Arrays.asList(resultDto.getBusinessType()));
        resultDto.setBusinessTypeName(busiMap.get(resultDto.getBusinessType()));
        //获取用户手机号
        BaseUserReqDto reqDto = new BaseUserReqDto();
        reqDto.setUserId(resultDto.getCreateUser());
        IdmResDTO<BaseUserDto> baseUserDtoIdmResDTO = systemApiFeignService.lookUpUserInfo(reqDto);
        if(baseUserDtoIdmResDTO == null){
            throw new RuntimeException("用户信息不存在");
        }
        resultDto.setPhone(baseUserDtoIdmResDTO.getData().getPhoneNumber());
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

        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(historicProcessInstance.getId()).orderByHistoricActivityInstanceStartTime().asc().list();
        if(CollectionUtils.isEmpty(list)){
            return IdmResDTO.success();
        }
        Map<String,NodeDetailDto> pamaMap = new HashMap<>();
        //已经运行完成的节点
        List<String> endNodes = new ArrayList<>();
        for (HistoricActivityInstance historicActivityInstance : list) {

            if(StringUtils.equals(historicActivityInstance.getActivityType(),USER_TASK)){
                //没有收集
                if(!pamaMap.containsKey(historicActivityInstance.getActivityId())){
                    NodeDetailDto nodeDetailDto = new NodeDetailDto();
                    nodeDetailDto.setNodeId(historicActivityInstance.getActivityId());
                    nodeDetailDto.setTimeOut(checkNodeTimeOut(historicActivityInstance.getActivityId(),
                            historicActivityInstance.getProcessInstanceId()));
                    nodeDetailDto.setNodeInfos(queryNodeBusiness(historicActivityInstance.getActivityId(),
                            historicActivityInstance.getProcessInstanceId()));
                    pamaMap.put(nodeDetailDto.getNodeId(),nodeDetailDto);
                }
            }
            if(!endNodes.contains(historicActivityInstance.getActivityType())){
                endNodes.add(historicActivityInstance.getActivityType());
            }
        }

        handleDataVO.setProcessTemplates(flowConfig);
        handleDataVO.setEndList(endNodes);
        handleDataVO.setNodeList(pamaMap);
//        handleDataVO.setDetailVOList(deatailMap);
        return IdmResDTO.success(handleDataVO);
    }

    /**
     * 判断节点是否超时
     * @return
     */
    public List<WorkBusinessTypeInfoEntity> queryNodeBusiness(String nodeId,String procinstId){
        LambdaQueryWrapper<WorkBusinessTypeInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(WorkBusinessTypeInfoEntity::getNode,nodeId).eq(WorkBusinessTypeInfoEntity::getProcInstId,Long.parseLong(procinstId))
                .eq(WorkBusinessTypeInfoEntity::getBusinessType, RESULT_1).orderByAsc(WorkBusinessTypeInfoEntity::getStartTime);
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
            //
            handleDataDTO.setNodeId(workBusinessTypeInfo.getNode());
            updateBusinessPendingDate(handleDataDTO);

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
        lw.eq(WorkBusinessTypeInfoEntity::getNode,nodeId).eq(WorkBusinessTypeInfoEntity::getProcInstId,procInstId)
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

        Page<MyApprovalResultDto> pages = baseMapper.queryMyNotApproval(new Page<MyApprovalResultDto>(dto.getCurrentPage()
                ,dto.getPageSize()),dto);

        List<MyApprovalResultDto> records = pages.getRecords();
        if(CollectionUtils.isNotEmpty(records)){
            //根据业务类型获取数据
            List<Long> busiTypes = records.stream().map(MyApprovalResultDto::getBusinessType).collect(Collectors.toList());
            Map<Long, String> busiMap = getBusiMap(busiTypes);

            //组织ids
            List<Long> orgIds = records.stream().map(MyApprovalResultDto::getOrgId).collect(Collectors.toList());
            Map<Long, String> deptMap = getDeptMap(orgIds);

            //userIds
            List<Long> usreIds = records.stream().map(MyApprovalResultDto::getUserId).collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(usreIds);

            records.stream().forEach(item->{
                //根据业务类型id获取业务类型数据
                item.setBusinessTypeName(busiMap.get(item.getBusinessType()));
                //获取所属组织
                item.setOrgPath(deptMap.get(item.getOrgId()));
                // 发起人
                item.setUserName(userMap.get(item.getUserId()));
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

            //根据业务类型获取数据
            List<Long> busiTypes = records.stream().map(MyApprovalResultDto::getBusinessType).collect(Collectors.toList());
            Map<Long, String> busiMap = getBusiMap(busiTypes);

            //组织ids
            List<Long> orgIds = records.stream().map(MyApprovalResultDto::getOrgId).collect(Collectors.toList());
            Map<Long, String> deptMap = getDeptMap(orgIds);

            //userIds
            List<Long> usreIds = records.stream().map(MyApprovalResultDto::getUserId).collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(usreIds);
            records.stream().forEach(item->{
                //根据业务类型id获取业务类型数据
                item.setBusinessTypeName(busiMap.get(item.getBusinessType()));
                //获取所属组织
                item.setOrgPath(deptMap.get(item.getOrgId()));
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
        Page<MyApprovalResultDto> page = baseMapper.queryMakeApproval(new Page<MyApprovalResultDto>(dto.getCurrentPage(),dto.getPageSize()),
                dto);
        List<MyApprovalResultDto> records = page.getRecords();
        if(CollectionUtils.isNotEmpty(records)){

            //根据业务类型获取数据
            List<Long> busiTypes = records.stream().map(MyApprovalResultDto::getBusinessType).collect(Collectors.toList());
            Map<Long, String> busiMap = getBusiMap(busiTypes);

            //组织ids
            List<Long> orgIds = records.stream().map(MyApprovalResultDto::getOrgId).collect(Collectors.toList());
            Map<Long, String> deptMap = getDeptMap(orgIds);

            //userIds
            List<Long> usreIds = records.stream().map(MyApprovalResultDto::getUserId).collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(usreIds);
            records.stream().forEach(item->{
                //根据业务类型id获取业务类型数据
                item.setBusinessTypeName(busiMap.get(item.getBusinessType()));
                //获取所属组织
                item.setOrgPath(deptMap.get(item.getOrgId()));
                // 发起人
                item.setUserName(userMap.get(item.getUserId()));
            });
        }
        return IdmResDTO.success(page);
    }

    public IdmResDTO<IPage<WorkInfoEntity>> queryMySubmitWorkInfo(QueryMyApprovalDto dto) {
        dto.setCreateUser(LoginInfoHolder.getCurrentUserId());
        Page<WorkInfoEntity> page = baseMapper.queryMySubmitWorkInfo(new Page<WorkInfoEntity>(dto.getCurrentPage(),dto.getPageSize()),
                dto);
        List<WorkInfoEntity> records = page.getRecords();
        if(CollectionUtils.isNotEmpty(records)){
            //根据业务类型获取数据
            List<Long> busiTypes = records.stream().map(WorkInfoEntity::getBusinessType).collect(Collectors.toList());
            Map<Long, String> busiMap = getBusiMap(busiTypes);

            //组织ids
            List<Long> orgIds = records.stream().map(WorkInfoEntity::getOrgId).collect(Collectors.toList());
            Map<Long, String> deptMap = getDeptMap(orgIds);

            //userIds
            List<Long> usreIds = records.stream().map(WorkInfoEntity::getCreateUser).collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(usreIds);
            records.stream().forEach(item->{
                //根据业务类型id获取业务类型数据
                item.setBusinessTypeName(busiMap.get(item.getBusinessType()));
                //获取所属组织
                item.setOrgPath(deptMap.get(item.getOrgId()));
                // 发起人
                item.setUserName(userMap.get(item.getCreateUser()));
            });
        }
        return IdmResDTO.success(page);
    }

    /**
     * 获取部门信息
     * @param dto
     * @return
     */
    public List<DepartmentDto> queryDeptList(WorkInfoDto dto) {
        return getDeptIds(LoginInfoHolder.getCurrentDeptId());
    }
}
