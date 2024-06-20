package cn.cuiot.dmp.baseconfig.flow.service;

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BusinessTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.BusinessTypeRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.base.infrastructure.utils.SpringContextHolder;
import cn.cuiot.dmp.baseconfig.flow.constants.WorkOrderConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.AppAssigneeDto;
import cn.cuiot.dmp.baseconfig.flow.dto.CommonConfigDto;
import cn.cuiot.dmp.baseconfig.flow.dto.StartProcessInstanceDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.app.*;
import cn.cuiot.dmp.baseconfig.flow.dto.app.query.*;
import cn.cuiot.dmp.baseconfig.flow.dto.approval.QueryMyApprovalDto;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.ChildNode;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.NodeButton;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import cn.cuiot.dmp.baseconfig.flow.dto.work.*;
import cn.cuiot.dmp.baseconfig.flow.entity.*;
import cn.cuiot.dmp.baseconfig.flow.enums.*;
import cn.cuiot.dmp.baseconfig.flow.flowable.msg.MsgSendService;
import cn.cuiot.dmp.baseconfig.flow.mapper.WorkInfoMapper;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.MsgDataType;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
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
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants.*;

/**
 * @author pengjian
 * @create 2024/5/27 16:50
 */
@Service
@Slf4j
public class AppWorkInfoService extends ServiceImpl<WorkInfoMapper, WorkInfoEntity> {


    @Autowired
    private SystemApiFeignService systemApiFeignService;

    @Autowired
    private WorkBusinessTypeInfoService workBusinessTypeInfoService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private WorkOrderRelService workOrderRelService;

    @Autowired
    private CommitProcessService commitProcessService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TbFlowConfigService flowConfigService;
    @Autowired
    private NodeTypeService nodeTypeService;

    @Autowired
    private ProcessAndDeptService processAndDeptService;

    @Autowired
    private TbFlowConfigOrgService tbFlowConfigOrgService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private MsgSendService msgSendService;

    /**
     * APP 获取待审批的数据
     * @param query
     * @return
     */
    public IdmResDTO<BaseDto> queryMyNotApprocalCount (PendingProcessQuery query){
        query.setAssignee(LoginInfoHolder.getCurrentUserId());
        BaseDto dto = getBaseMapper().queryMyNotApprocalCount(query);
        return IdmResDTO.success(dto);

    }

    /**
     * 获取待处理的列表
     * @param query
     * @return
     */
    public IdmResDTO<List<BaseDto>> queryPendProcessList(PendingProcessQuery query) {
        query.setAssignee(LoginInfoHolder.getCurrentUserId());
        List<BaseDto> dtos = getBaseMapper().queryPendProcessList(query);
        return IdmResDTO.success(dtos);
    }

    /**
     * 获取转交时，任务原有的
     * @param dto
     * @return
     */

    public IdmResDTO<List<TaskUserInfoDto>> queryTaskUserInfo(QueryTaskUserInfoDto dto) {

        List<TaskUserInfoDto> users = getBaseMapper().queryTaskUserInfo(dto);
        if(CollectionUtil.isEmpty(users)){
            return IdmResDTO.success();
        }
        List<Long> userIds = users.stream().filter(e -> Objects.nonNull(e.getUserId())).
                map(TaskUserInfoDto::getUserId).collect(Collectors.toList());
        Map<Long, String> userMap = getUserMap(userIds);
        users.stream().forEach(item->{
            item.setUserName(userMap.get(item.getUserId()));
        });

        return IdmResDTO.success(users);
    }

    public Map<Long,String> getUserMap(List<Long> userIds){
        BaseUserReqDto userReqDto = new BaseUserReqDto();
        userReqDto.setUserIdList(userIds);
        IdmResDTO<List<BaseUserDto>> listIdmResDTO = systemApiFeignService.lookUpUserList(userReqDto);
        List<BaseUserDto> data = Optional.ofNullable(listIdmResDTO.getData()).orElseThrow(()->new RuntimeException("用户信息不存在"));
        return data.stream().collect(Collectors.toMap(BaseUserDto::getId,BaseUserDto::getName ));
    }

    /**
     * app端获取我提交的工单信息
     * @param dto
     * @return
     */
    public IdmResDTO<IPage<AppWorkInfoDto>> queryAppMySubmitWorkInfo(QueryMyApprovalDto dto) {
        dto.setAssignee(LoginInfoHolder.getCurrentUserId());
        Page<AppWorkInfoDto>  page = baseMapper.queryAppMySubmitWorkInfo(new Page<AppWorkInfoDto>(dto.getPageNo(),dto.getPageSize()),dto);

        List<AppWorkInfoDto> records = page.getRecords();
        if(CollectionUtil.isEmpty(records)){
            return IdmResDTO.success();
        }
        List<Long> userIds = records.stream().map(AppWorkInfoDto::getCreateUser).collect(Collectors.toList());
        Map<Long, String> userMap = getUserMap(userIds);
        records.stream().forEach(item->{
            item.setCreateUserName(userMap.get(item.getCreateUser()));
        });

        return IdmResDTO.success(page);
    }

    /**
     * 工单监管-获取查询列表
     * @param query
     * @return
     */
    public IdmResDTO<IPage<AppWorkInfoDto>> queryWorkOrderSuper(WorkOrderSuperQuery query) {
        List<Long> deptIds = getDeptIds().stream().map(DepartmentDto::getId).collect(Collectors.toList());
        //校验
        checkOrgIds(query.getOrgIds(),deptIds);
        if(CollectionUtil.isEmpty(query.getOrgIds())){
            query.setOrgIds(deptIds);
        }

       IPage<AppWorkInfoDto> workInfoDtoPage = baseMapper.queryWorkOrderSuper(new Page<>(query.getPageNo(),query.getPageSize()),query);
        List<AppWorkInfoDto> workInfoDtos = workInfoDtoPage.getRecords();
        if(CollectionUtil.isNotEmpty(workInfoDtos)){
            List<Long> userIds = workInfoDtos.stream().map(AppWorkInfoDto::getCreateUser).collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(userIds);
            workInfoDtos.stream().forEach(item->{
                item.setCreateUserName(userMap.get(item.getCreateUser()));
            });
        }
       return IdmResDTO.success(workInfoDtoPage);
    }

    /**
     * 校验查询的组织是不是本组织及下属组织
     * @param orgIds
     * @param deptIds
     */
    public void checkOrgIds(List<Long> orgIds,List<Long> deptIds){
        if(CollectionUtils.isNotEmpty(orgIds) && CollectionUtils.isNotEmpty(deptIds)){
            AssertUtil.isTrue(deptIds.containsAll(orgIds),ResultCode.NO_OPERATION_PERMISSION.getMessage());
        }
    }
    /**
     * 获取详情里的基础信息
     * @param dto
     * @return
     */
    public IdmResDTO<WorkInfoDto> queryBasicWorkOrderDetailInfo(WorkProcInstDto dto) {
        WorkInfoDto workInfoDto = queryWorkInfoDto(dto);
        return IdmResDTO.success(workInfoDto);
    }

    public WorkInfoDto queryWorkInfoDto(WorkProcInstDto dto){
       checkWorkOrder(dto.getProcInstId());
        //获取工单详情
        WorkInfoDto resultDto = getBaseMapper().queryWorkOrderDetailInfo(dto);
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
        //获取关联的工单信息
        resultDto.setWorkOrderIds(getWordOrderIds(Long.parseLong(resultDto.getProcInstId())));
        return resultDto;
    }

    /**
     * 校验工单信息
     * @param processInstanceId
     */
    public void checkWorkOrder(String processInstanceId){
        if(Objects.nonNull(processInstanceId)){
            WorkInfoEntity workInfo = getWorkInfo(processInstanceId);
            AssertUtil.isTrue(Objects.equals(workInfo.getCompanyId(),LoginInfoHolder.getCurrentOrgId()),ResultCode.NO_OPERATION_PERMISSION.getMessage());
        }
    }

    /**
     * 获取工单信息
     * @param processInstanceId
     * @return
     */
    public WorkInfoEntity getWorkInfo(String processInstanceId){
        LambdaQueryWrapper<WorkInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(WorkInfoEntity::getProcInstId,processInstanceId);
        List<WorkInfoEntity> workInfoEntities = baseMapper.selectList(lw);
        return CollectionUtils.isNotEmpty(workInfoEntities)?workInfoEntities.get(0):new WorkInfoEntity();
    }
    /**
     * 获取关联的工单id
     * @param procInstId
     * @return
     */
    public List<Long> getWordOrderIds(Long procInstId){
        LambdaQueryWrapper<WorkOrderRelEntity> relLw = new LambdaQueryWrapper<>();
        relLw.eq(WorkOrderRelEntity::getOldWorkOrderId,procInstId).or().eq(WorkOrderRelEntity::getNewWorkOrderId,procInstId);
        List<WorkOrderRelEntity> workOrderRelEntityList = workOrderRelService.list(relLw);
        List<Long> workOrders = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(workOrderRelEntityList)){
            List<Long> oldList = workOrderRelEntityList.stream().map(WorkOrderRelEntity::getOldWorkOrderId).collect(Collectors.toList());
            List<Long> newList = workOrderRelEntityList.stream().map(WorkOrderRelEntity::getNewWorkOrderId).collect(Collectors.toList());
            workOrders.addAll(oldList);
            workOrders.addAll(newList);
        }
        return workOrders;
    }
    public Long getCompany(){
        Long currentOrgId = LoginInfoHolder.getCurrentOrgId();
        if(Objects.isNull(currentOrgId)){
            //TODO 根据小区id获取企业id
        }
        return currentOrgId;
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

    public WorkBusinessTypeInfoEntity queryWorkBusinessById(String nodeId,Long procInstId){
        LambdaQueryWrapper<WorkBusinessTypeInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(Objects.nonNull(nodeId),WorkBusinessTypeInfoEntity::getNode,nodeId)
                .eq(WorkBusinessTypeInfoEntity::getProcInstId,procInstId)
                .eq(WorkBusinessTypeInfoEntity::getBusinessType, BusinessInfoEnums.BUSINESS_PENDING.getCode())
                .isNull(WorkBusinessTypeInfoEntity::getEndTime);
        List<WorkBusinessTypeInfoEntity> list = workBusinessTypeInfoService.list(lw);
        return CollectionUtils.isEmpty(list)?null:list.get(0);
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
    public Map<Long,String> getBusiMap(List<Long> businessIds){
        IdmResDTO<List<BusinessTypeRspDTO>> listIdmResDTO = systemApiFeignService.batchGetBusinessType(BusinessTypeReqDTO.builder()
                .businessTypeIdList(businessIds).build());
        List<BusinessTypeRspDTO> businessInfo = Optional.ofNullable(listIdmResDTO.getData()).orElseThrow(()->new RuntimeException("业务类型不存在"));
        return businessInfo.stream().collect(Collectors.toMap(BusinessTypeRspDTO::getBusinessTypeId, BusinessTypeRspDTO::getTreeName));
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

    /**
     * 获取自己与子部门的部门信息
     * @param
     * @return
     */
    public List<DepartmentDto> getDeptIds(){
        DepartmentReqDto paraDto = new DepartmentReqDto();
        paraDto.setDeptId(LoginInfoHolder.getCurrentDeptId());
        paraDto.setSelfReturn(true);
        IdmResDTO<List<DepartmentDto>> listIdmResDTO = systemApiFeignService.lookUpDepartmentChildList(paraDto);
        return listIdmResDTO.getData();

    }

    /**
     * 查询待处理的节点类型
     * @param query
     * @return
     */
    public IdmResDTO<NodeTypeDto> queryNodeType(PendingProcessQuery query) {
        query.setAssignee(LoginInfoHolder.getCurrentUserId());
        List<String> types = baseMapper.queryNodeType(query);
        NodeTypeDto dto = new NodeTypeDto();
        dto.setProcessNodeType(types);
        return IdmResDTO.success(dto);
    }

    /**
     * 查询待审批信息
     * @param query
     * @return
     */
    public IdmResDTO<IPage<AppWorkInfoDto>> queryMyApprove(WorkOrderSuperQuery query) {
        query.setAssignee(LoginInfoHolder.getCurrentUserId());
        IPage<AppWorkInfoDto> pageDto  = baseMapper.queryMyApprove(new Page<AppWorkInfoDto>(query.getPageNo(),query.getPageSize()),query);
        List<AppWorkInfoDto> records = pageDto.getRecords();
        if(CollectionUtil.isNotEmpty(records)){
            List<Long> userIds = records.stream().map(AppWorkInfoDto::getCreateUser).collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(userIds);
            records.stream().forEach(item->{
                item.setCreateUserName(userMap.get(item.getCreateUser()));
            });
        }
        return IdmResDTO.success(pageDto);
    }
    /**
     * 获取我处理的列表数据
     * @param query
     * @return
     */
    public IdmResDTO<IPage<AppWorkInfoDto>> queryMyHandleInfo(WorkOrderSuperQuery query) {
        query.setAssignee(LoginInfoHolder.getCurrentUserId());
        IPage<AppWorkInfoDto> pageDto  = baseMapper.queryMyHandleInfo(new Page<AppWorkInfoDto>(query.getPageNo(),query.getPageSize()),query);
        List<AppWorkInfoDto> records = pageDto.getRecords();
        if(CollectionUtil.isNotEmpty(records)){
            List<Long> userIds = records.stream().map(AppWorkInfoDto::getCreateUser).collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(userIds);
            records.stream().forEach(item->{
                item.setCreateUserName(userMap.get(item.getCreateUser()));
            });
        }
        return IdmResDTO.success(pageDto);
    }

    /**
     * 更新或者保存用户提交的对象数据
     * @param dto
     * @return
     */
    public IdmResDTO saveSubmitData(CommitProcessDto dto) {
        if(Objects.isNull(dto.getId())){
            dto.setId(IdWorker.getId());
        }

        CommitProcessEntity processEntity = BeanMapper.map(dto, CommitProcessEntity.class);
        processEntity.setUserId(LoginInfoHolder.getCurrentUserId());
        processEntity.setCreateTime(new Date());
        commitProcessService.saveOrUpdate(processEntity);
        return IdmResDTO.success();
    }

    /**
     * 查询待处理信息
     * @param dto
     * @return
     */
    public IdmResDTO<ProcessResultDto> queryUserSubmitData(UserSubmitDataDto dto) throws FlowException {
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(String.valueOf(dto.getProcInstId())).list();
        if(CollectionUtil.isEmpty(taskList)){
            throw new FlowException("任务信息不存在");
        }
        Process mainProcess = repositoryService.getBpmnModel(taskList.get(0).getProcessDefinitionId()).getMainProcess();
        String dingDing = mainProcess.getAttributeValue(FLOWABLE_NAME_SPACE, FLOWABLE_NAME_SPACE_NAME);
        JSONObject mainJson = JSONObject.parseObject(dingDing, new TypeReference<JSONObject>() {
        });
        String processJson = mainJson.getString(VIEW_PROCESS_JSON_NAME);

        LambdaQueryWrapper<CommitProcessEntity> processLw = new LambdaQueryWrapper<>();
        processLw.eq(CommitProcessEntity::getProcInstId,dto.getProcInstId()).eq(CommitProcessEntity::getNodeId,dto.getNodeId())
                .eq(CommitProcessEntity::getUserId,LoginInfoHolder.getCurrentUserId());
        List<CommitProcessEntity> processList = commitProcessService.list(processLw);

        ProcessResultDto resultDto = new ProcessResultDto();
        resultDto.setProcess(processJson);
        resultDto.setCommitProcess(processList);
        resultDto.setNodeCount(taskList.size());
        return IdmResDTO.success(resultDto);
    }

    /**
     * 完成任务
     * @param taskDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO completeTask(CompleteTaskDto taskDto) {

        Task task = taskService.createTaskQuery().taskId(String.valueOf(taskDto.getTaskId())).singleResult();
        if(StringUtils.isNotEmpty(taskDto.getCompletionRatio())){
            //未达到完成比列不允许提交
            Assert.isTrue(checkCompletionRatio(taskDto,task),() -> new BusinessException(ResultCode.COMPLETE_RATIO_ERROR));

        }
        //保存节点自选人信息
        Map<String, List<UserInfo>> processUsers = taskDto.getProcessUsers();
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
        //保存操作记录
        HandleDataDTO dto = new HandleDataDTO();
        dto.setTaskId(String.valueOf(taskDto.getTaskId()));
        WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(dto);
        businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_COMPLETED.getCode());
        workBusinessTypeInfoService.save(businessTypeInfo);
        //更新主流程防止是挂起状态
        updateWorkInfo(WorkOrderStatusEnums.progress.getStatus(), businessTypeInfo.getProcInstId());
        //保存提交的表单信息
        CommitProcessEntity commitProcess = taskDto.getCommitProcess();
        if(Objects.nonNull(commitProcess)){
            commitProcess.setId(IdWorker.getId());
            commitProcess.setProcInstId(Long.parseLong(task.getProcessInstanceId()));
            commitProcess.setUserId(LoginInfoHolder.getCurrentUserId());
            commitProcess.setBusinessTypeId(businessTypeInfo.getId());
            commitProcess.setCreateTime(new Date());
            commitProcessService.save(commitProcess);
        }else{
            //由于任务前面已经保存，这个地方直接更新就行
            LambdaUpdateWrapper<CommitProcessEntity> lw =new LambdaUpdateWrapper<>();
            lw.eq(CommitProcessEntity::getProcInstId,task.getProcessInstanceId()).eq(CommitProcessEntity::getNodeId,task.getTaskDefinitionKey())
                    .eq(CommitProcessEntity::getUserId,LoginInfoHolder.getCurrentUserId()).
                    set(CommitProcessEntity::getBusinessTypeId,businessTypeInfo.getId());
            commitProcessService.update(lw);
        }
        //如果节点存在挂起数据，将挂起数据结束
        WorkBusinessTypeInfoEntity update = queryWorkBusinessById(businessTypeInfo.getNode(), businessTypeInfo.getProcInstId());
        if(Objects.nonNull(update)){
            update.setCloseUserId(LoginInfoHolder.getCurrentUserId());
            update.setEndTime(new Date());
            workBusinessTypeInfoService.updateById(update);
        }

        taskService.complete(String.valueOf(taskDto.getTaskId()));
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
     * 校验是否达到完成比例
     * @return
     */
    public boolean checkCompletionRatio(CompleteTaskDto taskDto, Task task){

        ChildNode childNodeByNodeId = getChildNodeByNodeId(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
        //设置的比例信息
        Integer rate = childNodeByNodeId.getFormTaskAccessRate()*100;
        //实际比例
        Integer ratio = (int) (Double.parseDouble(taskDto.getCompletionRatio())*100);
        if(rate>ratio){
            return false;
        }
        return true;
    }

    /**
     * 获取流程提交的json
     * @param processDefinitionId
     * @param currentActivityId
     * @return
     */
    public static ChildNode getChildNodeByNodeId(String processDefinitionId,String currentActivityId){
        RepositoryService repositoryService = SpringContextHolder.getBean(RepositoryService.class);
        Process mainProcess = repositoryService.getBpmnModel(processDefinitionId).getMainProcess();
        UserTask userTask = (UserTask) mainProcess.getFlowElement(currentActivityId);
        String dingDing = mainProcess.getAttributeValue(FLOWABLE_NAME_SPACE, FLOWABLE_NAME_SPACE_NAME);
        JSONObject jsonObject = JSONObject.parseObject(dingDing, new TypeReference<JSONObject>() {
        });
        String processJson = jsonObject.getString(VIEW_PROCESS_JSON_NAME);
        ChildNode childNode = JSONObject.parseObject(processJson, new TypeReference<ChildNode>(){});
        return getChildNode(childNode, currentActivityId);
    }

    /**
     * 获取节点信息
     * @param childNode
     * @param nodeId
     * @return
     */
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

    /**
     * 获取子节点信息
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
     * 回退
     * @param operationDto
     * @return
     */
    public IdmResDTO clientBack(ClientOperationDto operationDto) {
        //回退留痕
        HandleDataDTO handleDataDTO = new HandleDataDTO();
        handleDataDTO.setTaskId(String.valueOf(operationDto.getTaskId()));
        handleDataDTO.setReason(operationDto.getReason());
        handleDataDTO.setComments(operationDto.getComments());
        WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
        workBusinessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_ROLLBACK.getCode());
        workBusinessTypeInfoService.save(workBusinessTypeInfo);

        //更新挂起时间
        updateBusinessPendingDate(handleDataDTO);

        //更新主流程状态
        updateWorkInfo(WorkOrderStatusEnums.progress.getStatus(), workBusinessTypeInfo.getProcInstId());

        Task task = taskService.createTaskQuery().taskId(String.valueOf(operationDto.getTaskId())).singleResult();
        String rollBackNode = queryRollBackNode(task);
        runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId()).moveActivityIdTo(task.getTaskDefinitionKey(),
                rollBackNode).changeState();

        //设置回退标识
        taskService.setVariable(String.valueOf(operationDto.getTaskId()),"rollback","true");

        return IdmResDTO.success();
    }

    /**
     * 获取回退节点
     * @param task
     * @return
     */
    public String queryRollBackNode(Task task){

        ChildNode childNodeByNodeId = getChildNodeByNodeId(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
        String nodeId = childNodeByNodeId.getParentId();
        while (true){
            ChildNode parentIdNode = getChildNodeByNodeId(task.getProcessDefinitionId(), nodeId);
            nodeId=parentIdNode.getParentId();
            if(WorkOrderConstants.nodes.contains(parentIdNode.getType())){
                return parentIdNode.getId();
            }
        }
    }
    /**
     * 更新挂起时间
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
     * 可操作的功能查询工单详情
     * @param dto
     * @return
     */
    public IdmResDTO<WorkInfoDto> queryBasicInfo(WorkProcInstDto dto) {

        checkWorkOrder(dto.getProcInstId());
        //获取工单详情
        WorkInfoDto resultDto = getBaseMapper().queryWorkOrderDetailInfo(dto);

        List<Task> taskList = taskService.createTaskQuery().processInstanceId(dto.getProcInstId()).list();
        if(CollectionUtil.isEmpty(taskList)){
            return IdmResDTO.error(ResultCode.OBJECT_NOT_EXIST.getCode(), ResultCode.OBJECT_NOT_EXIST.getMessage());
        }
        //获取节点按钮信息
        ChildNode childNode = getChildNodeByNodeId(taskList.get(0).getProcessDefinitionId(), taskList.get(0).getTaskDefinitionKey());
        List<NodeButton> buttons = childNode.getProps().getButtons();
        resultDto.setButtons(buttons);
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
        //获取关联的工单信息
        resultDto.setWorkOrderIds(getWordOrderIds(Long.parseLong(resultDto.getProcInstId())));
        return IdmResDTO.success(resultDto);
    }

    /**
     * 转办
     * @param assigneeDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO appAssignee(AppAssigneeDto assigneeDto) {

        HandleDataDTO handleDataDTO = new HandleDataDTO();
        handleDataDTO.setTaskId(String.valueOf(assigneeDto.getTaskId()));
        WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
        businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_TRANSFER.getCode());
        businessTypeInfo.setDeliver(assigneeDto.getUserIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
        workBusinessTypeInfoService.save(businessTypeInfo);

        //更新挂起时间
        handleDataDTO.setNodeId(businessTypeInfo.getNode());
        updateBusinessPendingDate(handleDataDTO);
        updateWorkInfo(WorkOrderStatusEnums.progress.getStatus(), businessTypeInfo.getProcInstId());
        //单个转办
        if(StringUtils.isNotEmpty(assigneeDto.getTaskId())){
            taskService.setAssignee(String.valueOf(assigneeDto.getTaskId()),String.valueOf(assigneeDto.getUserIds().get(0)));
        }

        //查出当前节点的所有人员信息

        List<Task> tasks = Optional.ofNullable(taskService.createTaskQuery().processInstanceId(handleDataDTO.getProcessInstanceId()).list())
                .orElseThrow(()->new BusinessException(ErrorCode.NOT_FOUND.getCode(),ErrorCode.NOT_FOUND.getMessage()));
        //查询该节点下所有任务信息
        List<String> hisTaskIds = baseMapper.queryHistoricTask( tasks.get(0).getTaskDefinitionKey(),tasks.get(0).getProcessInstanceId());
        //执行加签操作
        AddMultiInstanceUserTaskService multiInstanceUserTaskService = new AddMultiInstanceUserTaskService(managementService);
        Integer instance = 1;
        for (Long userId : handleDataDTO.getUserIds()) {
            multiInstanceUserTaskService.addMultiInstanceUserTask(tasks.get(0).getId(),String.valueOf(userId), instance);
            instance++;
        }

        taskService.complete(tasks.get(0).getId());

        //删除原来的任务id
        hisTaskIds.stream().forEach(item->{
            taskService.deleteTask(item, true);
        });
        return IdmResDTO.success();
    }

    /**
     * 终止
     * @param operationDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO clientCloseFlow(ClientOperationDto operationDto) {
        HandleDataDTO dataDTO = new HandleDataDTO();
        dataDTO.setProcessInstanceId(String.valueOf(operationDto.getProcessInstanceId()));
        dataDTO.setComments(operationDto.getComments());
        dataDTO.setReason(operationDto.getReason());
        WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(dataDTO);
        workBusinessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_CLOSE.getCode());
        workBusinessTypeInfoService.save(workBusinessTypeInfo);
        //终止流程
        runtimeService.deleteProcessInstance(String.valueOf(operationDto.getProcessInstanceId()), operationDto.getComments()); // 终止

        //更新挂起结束时间
        dataDTO.setNodeId(workBusinessTypeInfo.getNode());
        updateBusinessPendingDate(dataDTO);

        //更新工单状态
        updateWorkInfo(WorkOrderStatusEnums.terminated.getStatus(), workBusinessTypeInfo.getProcInstId());

        //发送消息
        msgSendService.sendProcess(operationDto.getProcessInstanceId().toString(), MsgDataType.WORK_INFO_CANCEL);

        return IdmResDTO.success();
    }

    /**
     * 小程序端挂起
     * @param operationDto
     * @return
     */
    public IdmResDTO clientBusinessPending(ClientOperationDto operationDto) {
        //校验任务是否已挂起
        WorkBusinessTypeInfoEntity entity = queryWorkBusinessById(null, operationDto.getProcessInstanceId());
        if(Objects.nonNull(entity)){
            return IdmResDTO.error(ErrorCode.SUSPENDED.getCode(), ErrorCode.SUSPENDED.getMessage());
        }
        HandleDataDTO dataDTO = new HandleDataDTO();
        dataDTO.setProcessInstanceId(String.valueOf(operationDto.getProcessInstanceId()));
        dataDTO.setComments(operationDto.getComments());
        dataDTO.setReason(operationDto.getReason());
        WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(dataDTO);
        businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_PENDING.getCode());
        workBusinessTypeInfoService.save(businessTypeInfo);
        //更新主流程状态
        updateWorkInfo(WorkOrderStatusEnums.Suspended.getStatus(), businessTypeInfo.getProcInstId());
        return IdmResDTO.success();
    }

    /**
     * 评论
     * @param dto
     * @return
     */
    public IdmResDTO clientComment(TaskBusinessDto dto) {
        HandleDataDTO dataDTO = new HandleDataDTO();
        dataDTO.setTaskId(String.valueOf(dto.getTaskId()));
        dataDTO.setComments(dto.getComments());
        dataDTO.setReason(dto.getReason());
        WorkBusinessTypeInfoEntity businessTypeInfo =getWorkBusinessTypeInfo(dataDTO);
        businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_COMMENT.getCode());
        workBusinessTypeInfoService.save(businessTypeInfo);
        return IdmResDTO.success();
    }

    /**
     * 我的提交查询工单基本信息
     * @param dto
     * @return
     */
    public IdmResDTO<WorkInfoDto> queryMySubmitWorkOrderDetailInfo(WorkProcInstDto dto) {

        WorkInfoDto workInfoDto = queryWorkInfoDto(dto);
        //判断是否可撤回
        workInfoDto.setRevokeType(checkRevokeType(workInfoDto));
        //判断是否可以重新提交
        workInfoDto.setResubmit(resubmit(workInfoDto.getProcInstId()));
        return IdmResDTO.success(workInfoDto);
    }
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
     * 判断是否可撤回
     * @param workInfoDto
     * @return
     */
    public Byte checkRevokeType(WorkInfoDto workInfoDto){

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
            return ButtonBusinessEnums.NOT_BUTTON.getCode();
        }
        //同节点可以撤回
        if(Objects.equals(workInfoDto.getRevokeNodeId(),taskList.get(0).getTaskDefinitionKey())){
            return ButtonBusinessEnums.BUTTON.getCode();
        }
        return ButtonBusinessEnums.NOT_BUTTON.getCode();
    }

    /**
     * 启动任务
     * @param startProcessInstanceDTO
     * @return
     */
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
        if(StringUtils.isNotBlank(startProcessInstanceDTO.getProcessInstanceId())){
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(startProcessInstanceDTO.getProcessInstanceId()).list();
            if(Objects.isNull(tasks)){
                throw new BusinessException(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage());
            }
            //工单创建人与当前重新发起人不一致，不能重新发起
            List<WorkInfoEntity> work = queryWorkInfo(Long.parseLong(startProcessInstanceDTO.getProcessInstanceId()));
            if(Objects.equals(work.get(0).getCreateUser(),LoginInfoHolder.getCurrentUserId())){
                throw new BusinessException(ErrorCode.NOT_OPERATION.getCode(), ErrorCode.NOT_OPERATION.getMessage());
            }
            //当前不是root节点不能够重新发起
            if(Objects.equals(WorkOrderConstants.USER_ROOT,tasks.get(0).getTaskDefinitionKey())){
                throw new BusinessException(ErrorCode.NOT_OPERATION.getCode(), ErrorCode.NOT_OPERATION.getMessage());
            }
            runtimeService.setVariables(task.getProcessInstanceId(),processVariables);
            HandleDataDTO handleDataDTO = new HandleDataDTO();
            handleDataDTO.setTaskId(task.getId());
            //保存操作信息
            WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
            workBusinessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_START.getCode());
            workBusinessTypeInfoService.save(workBusinessTypeInfo);
            //保存提交的参数
            saveCommitProcess(workBusinessTypeInfo,startProcessInstanceDTO);
        }else{
            //新建,发起流程
            task =initiateProcess(startProcessInstanceDTO,processVariables);
        }

        if(task!=null){
            taskService.complete(task.getId());
        }
        return IdmResDTO.success(task.getProcessInstanceId());
    }

    /**
     * 发起流程任务
     * @param startProcessInstanceDTO
     * @param processVariables
     * @return
     */
    public Task initiateProcess(StartProcessInstanceDTO startProcessInstanceDTO, Map<String,Object> processVariables){
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
        //获取组织信息
        List<Long> orgIds = orgIds(flowConfig.getId());
        //保存工单信息
        saveWorkInfo(flowConfig,startProcessInstanceDTO,task,orgIds);
        //保存组织与流程的关联关系
        saveProcessDefinitionAndOrgIds(startProcessInstanceDTO.getProcessDefinitionId(),orgIds);

//        HandleDataDTO handleDataDTO = new HandleDataDTO();
//        handleDataDTO.setTaskId(task.getId());
//        //保存操作信息
//        WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
//        workBusinessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_START.getCode());
//        workBusinessTypeInfoService.save(workBusinessTypeInfo);
//
//        //保存提交的表单信息
//        saveCommitProcess(workBusinessTypeInfo,startProcessInstanceDTO);

        return task;
    }


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
     * 获取组织信息
     * @param configId
     * @return
     */
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
        //代报工单
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
     * 解析节点数据
     * @param processJson
     * @return
     */

    public ChildNode processJson(String processJson) {
        ChildNode childNode = JsonUtil.readValue(processJson, new com.fasterxml.jackson.core.type.TypeReference<ChildNode>() {
        });

        return childNode;
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

    /**
     * 工单督办
     * @param processBusinessDto
     * @return
     */
    public IdmResDTO workOrderSuper(ProcessBusinessDto processBusinessDto) {
        HandleDataDTO dataDTO = new HandleDataDTO();
        dataDTO.setProcessInstanceId(String.valueOf(processBusinessDto.getProcessInstanceId()));
        dataDTO.setComments(processBusinessDto.getComments());
        dataDTO.setReason(processBusinessDto.getReason());
        WorkBusinessTypeInfoEntity businessTypeInfo =getWorkBusinessTypeInfo(dataDTO);
        businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_HANDLE.getCode());
        workBusinessTypeInfoService.save(businessTypeInfo);
        return IdmResDTO.success();
    }

    /**
     * 撤销工单
     * @param businessDto
     * @return
     */
    public IdmResDTO revokeWorkOrder(ProcessBusinessDto businessDto) {
        //获取工单信息
        List<WorkInfoEntity> works = queryWorkInfo(businessDto.getProcessInstanceId());
        if(Objects.isNull(works)){
            return IdmResDTO.error(ErrorCode.NOT_FOUND.getCode(),ErrorCode.NOT_FOUND.getMessage());
        }
        //判断是否是本人撤销，不是本人撤销提示没有权限
        if(!Objects.equals(LoginInfoHolder.getCurrentUserId(),works.get(0).getCreateUser())){
            return IdmResDTO.error(ResultCode.NO_OPERATION_PERMISSION.getCode(), ResultCode.NO_OPERATION_PERMISSION.getMessage());
        }
        //判断流程是否可以撤销,不可以撤销提示没有权限
        WorkInfoDto workDto = BeanMapper.map(works.get(0), WorkInfoDto.class);
        Byte businessType = checkRevokeType(workDto);
        if(Objects.equals(businessType, ButtonBusinessEnums.NOT_BUTTON)){
            return IdmResDTO.error(ResultCode.NO_OPERATION_PERMISSION.getCode(), ResultCode.NO_OPERATION_PERMISSION.getMessage());
        }
        //记录撤销
        HandleDataDTO handleDataDTO =new HandleDataDTO();
        handleDataDTO.setProcessInstanceId(String.valueOf(businessDto.getProcessInstanceId()));
        handleDataDTO.setComments(businessDto.getComments());
        handleDataDTO.setReason(businessDto.getReason());
        WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(handleDataDTO);
        businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_REVOKE.getCode());
        workBusinessTypeInfoService.save(businessTypeInfo);

        //更新主流程为已撤销
        //更新挂起结束时间
        handleDataDTO.setNodeId(businessTypeInfo.getNode());
        updateBusinessPendingDate(handleDataDTO);

        //更新工单状态为撤销
        updateWorkInfo(WorkOrderStatusEnums.revoke.getStatus(), businessTypeInfo.getProcInstId());
        //撤销流程
        runtimeService.deleteProcessInstance(String.valueOf(businessDto.getProcessInstanceId()),"撤销");
        return IdmResDTO.success();
    }

    /**
     * 根据工单id获取工单信息
     * @param procInstId
     * @return
     */
    public List<WorkInfoEntity> queryWorkInfo(Long procInstId){
        LambdaQueryWrapper<WorkInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(WorkInfoEntity::getProcInstId,String.valueOf(procInstId));
        return baseMapper.selectList(lw);
    }

    /**
     * 审批同意
     * @param approvalDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO approvalConsent(ApprovalDto approvalDto) {
        //保存节点自选人信息
        Map<String, List<UserInfo>> processUsers = approvalDto.getProcessUsers();
        Task task = taskService.createTaskQuery().taskId(String.valueOf(approvalDto.getTaskId())).singleResult();
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

        //保存操作信息
        HandleDataDTO dto = new HandleDataDTO();
        dto.setTaskId(String.valueOf(approvalDto.getTaskId()));
        dto.setComments(approvalDto.getComments());
        dto.setReason(approvalDto.getReason());
        WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(dto);
        businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_AGREE.getCode());
        workBusinessTypeInfoService.save(businessTypeInfo);
        //防止是挂起状态
        updateWorkInfo(WorkOrderStatusEnums.progress.getStatus(), businessTypeInfo.getProcInstId());

        //如果节点存在挂起数据，将挂起数据结束
        WorkBusinessTypeInfoEntity update = queryWorkBusinessById(businessTypeInfo.getNode(), businessTypeInfo.getProcInstId());
        if(Objects.nonNull(update)){
            update.setCloseUserId(LoginInfoHolder.getCurrentUserId());
            update.setEndTime(new Date());
            workBusinessTypeInfoService.updateById(update);
        }
        //完成审批任务
        taskService.complete(String.valueOf(approvalDto.getTaskId()));
        return IdmResDTO.success();
    }

    /**
     * 审批驳回
     * @param approvalDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO approvalRejection(ApprovalDto approvalDto) {
        HandleDataDTO dto = new HandleDataDTO();
        dto.setTaskId(String.valueOf(approvalDto.getTaskId()));
        dto.setReason(approvalDto.getReason());
        dto.setComments(approvalDto.getComments());
        WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(dto);
        workBusinessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_REFUSE.getCode());
        //保存操作信息
        workBusinessTypeInfoService.save(workBusinessTypeInfo);
        //更新主流程
        updateWorkInfo(WorkOrderStatusEnums.completed.getStatus(), workBusinessTypeInfo.getProcInstId());
        // 终止流程
        runtimeService.deleteProcessInstance(String.valueOf(workBusinessTypeInfo.getProcInstId()), approvalDto.getComments());
        //更新挂起
        dto.setNodeId(workBusinessTypeInfo.getNode());
        updateBusinessPendingDate(dto);

        //发送消息
        msgSendService.sendProcess(workBusinessTypeInfo.getProcInstId().toString(), MsgDataType.WORK_INFO_TURNDOWN);
        return IdmResDTO.success();
    }

    /**
     *获取抄送我的列表
     * @param query
     * @return
     */
    public IdmResDTO<IPage<AppWorkInfoDto>> queryMyMake(WorkOrderSuperQuery query) {
        query.setAssignee(LoginInfoHolder.getCurrentUserId());
        Page<AppWorkInfoDto> page = baseMapper.queryMyMake(new Page<>(query.getPageNo(),query.getPageSize()),query);
        List<AppWorkInfoDto> records = page.getRecords();
            if(CollectionUtil.isNotEmpty(records)){
                List<Long> userIds = records.stream().map(AppWorkInfoDto::getCreateUser).collect(Collectors.toList());
                Map<Long, String> userMap = getUserMap(userIds);
                records.stream().forEach(item->{
                    item.setCreateUserName(userMap.get(item.getCreateUser()));
                });
            }

        return IdmResDTO.success(page);
    }

    /**
     * 报事报修启动
     * @param startProcessInstanceDTO
     * @return
     */
    public IdmResDTO reportRepairsStart(StartProcessInstanceDTO startProcessInstanceDTO) {
        JSONObject formData = startProcessInstanceDTO.getFormData();
        UserInfo startUserInfo = startProcessInstanceDTO.getStartUserInfo();
        Authentication.setAuthenticatedUserId(startUserInfo.getId());
        Map<String,Object> processVariables= new HashMap<>();
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
        Task task =null;
        //再次发起
        if(StringUtils.isNotBlank(startProcessInstanceDTO.getProcessInstanceId())){
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(startProcessInstanceDTO.getProcessInstanceId()).list();
            if(Objects.isNull(tasks)){
                throw new BusinessException(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage());
            }
            //工单创建人与当前重新发起人不一致，不能重新发起
            List<WorkInfoEntity> work = queryWorkInfo(Long.parseLong(startProcessInstanceDTO.getProcessInstanceId()));
            if(Objects.equals(work.get(0).getCreateUser(),LoginInfoHolder.getCurrentUserId())){
                throw new BusinessException(ErrorCode.NOT_OPERATION.getCode(), ErrorCode.NOT_OPERATION.getMessage());
            }
            //当前不是root节点不能够重新发起
            if(Objects.equals(WorkOrderConstants.USER_ROOT,tasks.get(0).getTaskDefinitionKey())){
                throw new BusinessException(ErrorCode.NOT_OPERATION.getCode(), ErrorCode.NOT_OPERATION.getMessage());
            }
            runtimeService.setVariables(task.getProcessInstanceId(),processVariables);

        }else{
            //新建,发起流程
            task =initiateProcess(startProcessInstanceDTO,processVariables);
        }
        if(task!=null){
            HandleDataDTO handleDataDTO = new HandleDataDTO();
            handleDataDTO.setTaskId(task.getId());
            //保存操作信息
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
     * 查询我的报修
     * @param query
     * @return
     */
    public IdmResDTO<IPage<RepairReportDto>> queryReportRepairs(RepairReportQuery query) {
        query.setUserId(LoginInfoHolder.getCurrentUserId());
        IPage<RepairReportDto> report = baseMapper.queryReportRepairs(new Page<>(query.getPageNo(),query.getPageSize()),query);

        return IdmResDTO.success(report);
    }

    /**
     * 报事报修查看工单详情
     * @param dto
     * @return
     */
    public IdmResDTO<RepairReportDetailDto> queryRepairReportDetail(ProcessBusinessDto dto) {
        //查询工单信息
        List<WorkInfoEntity> workInfoEntities = queryWorkInfo(dto.getProcessInstanceId());
        if(Objects.isNull(workInfoEntities)){
            throw new BusinessException(ErrorCode.NOT_FOUND.getCode(),ErrorCode.NOT_FOUND.getMessage());
        }
        WorkInfoEntity workInfoEntity = workInfoEntities.get(0);
        RepairReportDetailDto resultDto = new RepairReportDetailDto();
        resultDto.setWorkName(workInfoEntity.getWorkName());
        resultDto.setCreateTime(workInfoEntity.getCreateTime());
        resultDto.setProcessInstanceId(Long.parseLong(workInfoEntities.get(0).getProcInstId()));
        //填充节点信息与提交的信息
        queryRepairReport(resultDto,workInfoEntity);
        //查询流程状态与按钮信息
        queryButtonStatus(resultDto,workInfoEntity);

        return IdmResDTO.success(resultDto);
    }

    /**
     * 查询流程的状态与填充按钮信息
     * @param resultDto
     * @param entity
     */
    public void queryButtonStatus(RepairReportDetailDto resultDto,WorkInfoEntity entity){
        //状态为已完成
        if(Objects.equals(entity.getStatus(), WorkOrderStatusEnums.completed.getStatus())){
            //已完成中包含拒绝
            WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(Long.parseLong(entity.getProcInstId()),
                    Arrays.asList(BusinessInfoEnums.BUSINESS_REFUSE.getCode()));
            //存在被驳回的记录
            if(Objects.nonNull(workBusinessTypeInfo)){
                //驳回原因
                String message = SupplementExplanationEnum.getSupplementExplanation(BusinessInfoEnums.BUSINESS_REFUSE.getCode()).getMessage();
                resultDto.setContent(message+workBusinessTypeInfo.getComments());
            }
            resultDto.setStatus(WorkInfoEnums.FINISH.getCode());
        }
        //状态为已终止
        if(Objects.equals(entity.getStatus(), WorkOrderStatusEnums.terminated.getStatus())){
            //终止有超时自动终止与手动终止两种
            WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(Long.parseLong(entity.getProcInstId()),
                    Arrays.asList(BusinessInfoEnums.BUSINESS_TIME_OUT.getCode(),BusinessInfoEnums.BUSINESS_CLOSE.getCode()));
            //当最后一调是超时时，则表示时超时自动终止
            if(Objects.equals(workBusinessTypeInfo.getBusinessType(),BusinessInfoEnums.BUSINESS_TIME_OUT.getCode())){
                String message = SupplementExplanationEnum.getSupplementExplanation(BusinessInfoEnums.BUSINESS_TIME_OUT.getCode()).getMessage();
                resultDto.setContent(message);
            }
            //当不是超时自动终止则表示是手动终止
            if(Objects.equals(workBusinessTypeInfo.getBusinessType(),BusinessInfoEnums.BUSINESS_CLOSE.getCode())){
                String message = SupplementExplanationEnum.getSupplementExplanation(BusinessInfoEnums.BUSINESS_CLOSE.getCode()).getMessage();
                resultDto.setContent(message+workBusinessTypeInfo.getComments());
            }
            resultDto.setStatus(WorkInfoEnums.FINISH.getCode());
        }

        //待处理  进行中与已挂起
        if(Objects.equals(entity.getStatus(), WorkOrderStatusEnums.progress.getStatus()) ||
                Objects.equals(entity.getStatus(), WorkOrderStatusEnums.Suspended.getStatus())){
            resultDto.setStatus(WorkInfoEnums.PROCESSING.getCode());
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(entity.getProcInstId()).list();
            if(CollectionUtils.isEmpty(tasks)){
                throw new BusinessException(ErrorCode.NOT_FOUND.getCode(),ErrorCode.NOT_FOUND.getMessage());
            }
            resultDto.setTaskId(Long.parseLong(tasks.get(0).getId()));
           //当前节点处于root表示被退回了
            if(Objects.equals(tasks.get(0).getTaskDefinitionKey(),WorkOrderConstants.USER_ROOT)){
                String message = SupplementExplanationEnum.getSupplementExplanation(BusinessInfoEnums.BUSINESS_ROLLBACK.getCode()).getMessage();
                //可以重新提交
                resultDto.setResubmit(ButtonBusinessEnums.BUTTON.getCode());
                //查询被退回的原因
                WorkBusinessTypeInfoEntity workBusinessTypeInfo = getWorkBusinessTypeInfo(Long.parseLong(entity.getProcInstId()),
                        Arrays.asList(BusinessInfoEnums.BUSINESS_TIME_OUT.getCode(),BusinessInfoEnums.BUSINESS_ROLLBACK.getCode()));
                if(Objects.nonNull(workBusinessTypeInfo)){
                    resultDto.setContent(String.format(message,workBusinessTypeInfo.getComments()));
                }
            }
            //判断是否展示评价按钮
            NodeTypeEntity nodeInfo = getNodeInfo(entity.getProcessDefinitionId(), tasks.get(0).getTaskDefinitionKey());
            if(Objects.equals(nodeInfo.getNodeType(), WorkOrderConstants.COMMENT_NODE_TYPE)){
                resultDto.setEvaluate(ButtonBusinessEnums.BUTTON.getCode());
            }
            //判断是否可以撤回
            //未完成就可以撤回
            if(Objects.equals(entity.getRevokeType(),ButtonBusinessEnums.BUTTON.getCode())){
                resultDto.setRevokeType(ButtonBusinessEnums.BUTTON.getCode());
            }
            //同节点可以撤回
            if(Objects.equals(entity.getRevokeNodeId(),tasks.get(0).getTaskDefinitionKey())){
                resultDto.setRevokeType(ButtonBusinessEnums.BUTTON.getCode());
            }

            //当前所处的节点
            resultDto.setCurrentNode(tasks.get(0).getTaskDefinitionKey());
        }

        //已撤销
        if(Objects.equals(entity.getStatus(), WorkOrderStatusEnums.revoke.getStatus())){
            resultDto.setStatus(WorkOrderStatusEnums.revoke.getStatus());
        }
    }

    /**
     * 根据流程定义id与节点id获取流程节点信息
     * @param processDefinitionId
     * @param nodeId
     * @return
     */
    public NodeTypeEntity getNodeInfo(String processDefinitionId, String nodeId){
        LambdaQueryWrapper<NodeTypeEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(NodeTypeEntity::getProcessDefinitionId,processDefinitionId).eq(NodeTypeEntity::getNodeId,nodeId);
        List<NodeTypeEntity> nodes = nodeTypeService.list(lw);
        return CollectionUtils.isEmpty(nodes)?null:nodes.get(0);
    }


    /**
     * 根据工单id与操作类型获取操作信息
     * @param procInstId
     * @param businessTypes
     * @return
     */
    public WorkBusinessTypeInfoEntity getWorkBusinessTypeInfo(Long procInstId,List<Byte> businessTypes){
        LambdaQueryWrapper<WorkBusinessTypeInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(WorkBusinessTypeInfoEntity::getProcInstId,procInstId).in(WorkBusinessTypeInfoEntity::getBusinessType,businessTypes)
                .orderByDesc(WorkBusinessTypeInfoEntity::getStartTime);
        List<WorkBusinessTypeInfoEntity> business = workBusinessTypeInfoService.list(lw);
        return CollectionUtils.isEmpty(business)?null:business.get(0);
    }
    /**
     * 查询节点信息
     * @param resultDto
     * @param entity
     */
    public void queryRepairReport(RepairReportDetailDto resultDto,WorkInfoEntity entity){

        //查询流程的节点信息
        Process mainProcess = repositoryService.getBpmnModel(entity.getProcessDefinitionId()).getMainProcess();
        String dingDing = mainProcess.getAttributeValue(FLOWABLE_NAME_SPACE, FLOWABLE_NAME_SPACE_NAME);
        JSONObject mainJson = JSONObject.parseObject(dingDing, new TypeReference<JSONObject>() {
        });
        String processJson = mainJson.getString(VIEW_PROCESS_JSON_NAME);
        ChildNode childNode = JSONObject.parseObject(processJson, new TypeReference<ChildNode>() {
        });

        while (true){
            if(Objects.equals(childNode.getId(), WorkOrderConstants.USER_ROOT)){
                childNode.setCreateTime(entity.getCreateTime());
                continue;
            }
            WorkBusinessTypeInfoEntity businessTypeInfo = queryWorkBusinessTypeInfo(Long.parseLong(entity.getProcInstId()), childNode.getId());
            if(Objects.nonNull(businessTypeInfo)){
                childNode.setCreateTime(businessTypeInfo.getStartTime());
            }
            if(Objects.isNull(childNode.getChildren())){
                break;
            }
            childNode = childNode.getChildren();
        }
        resultDto.setProcess(JsonUtil.writeValueAsString(childNode));

        //查询当前所处的节点
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(String.valueOf(entity.getProcInstId())).list();
        if(CollectionUtils.isNotEmpty(tasks)){
            resultDto.setCurrentNode(tasks.get(0).getTaskDefinitionKey());
        }
        CommitProcessEntity processEntity = queryCommitProcess(Long.parseLong(entity.getProcInstId()), WorkOrderConstants.USER_ROOT);
        if(Objects.nonNull(processEntity)){
            resultDto.setCommitProcess(processEntity.getCommitProcess());
        }
    }

    /**
     * 查询root节点提交的数据信息
     * @param procInstId
     * @param nodeId
     * @return
     */
    public CommitProcessEntity queryCommitProcess(Long procInstId,String nodeId){
        LambdaQueryWrapper<CommitProcessEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(CommitProcessEntity::getProcInstId,procInstId).eq(CommitProcessEntity::getNodeId,nodeId).orderByDesc(CommitProcessEntity::getCreateTime);
        List<CommitProcessEntity> processEntities = commitProcessService.list(lw);
        return CollectionUtils.isEmpty(processEntities)?null:processEntities.get(0);
    }
    /**
     * 根据订单id与节点id获取到订单的操作信息
     * @param procInstId
     * @param nodeId
     * @return
     */
    public WorkBusinessTypeInfoEntity queryWorkBusinessTypeInfo(Long procInstId,String nodeId){
        LambdaQueryWrapper<WorkBusinessTypeInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(WorkBusinessTypeInfoEntity::getProcInstId,procInstId).eq(WorkBusinessTypeInfoEntity::getNode,nodeId)
                .orderByDesc(WorkBusinessTypeInfoEntity::getStartTime);
        List<WorkBusinessTypeInfoEntity> business = workBusinessTypeInfoService.list(lw);
        return CollectionUtils.isEmpty(business)?null:business.get(0);
    }

    /**
     * 评价节点
     * @param taskDto
     * @return
     */
    public IdmResDTO evaluate(CompleteTaskDto taskDto) {
        Task task = taskService.createTaskQuery().taskId(String.valueOf(taskDto.getTaskId())).singleResult();
        //保存操作记录
        HandleDataDTO dto = new HandleDataDTO();
        dto.setTaskId(String.valueOf(taskDto.getTaskId()));
        WorkBusinessTypeInfoEntity businessTypeInfo = getWorkBusinessTypeInfo(dto);
        businessTypeInfo.setBusinessType(BusinessInfoEnums.BUSINESS_COMPLETED.getCode());
        workBusinessTypeInfoService.save(businessTypeInfo);

        CommitProcessEntity commitProcess = taskDto.getCommitProcess();
        if(Objects.nonNull(commitProcess)){
            commitProcess.setId(IdWorker.getId());
            commitProcess.setProcInstId(Long.parseLong(task.getProcessInstanceId()));
            commitProcess.setUserId(LoginInfoHolder.getCurrentUserId());
            commitProcess.setBusinessTypeId(businessTypeInfo.getId());
            commitProcess.setCreateTime(new Date());
            commitProcessService.save(commitProcess);
        }

        taskService.complete(String.valueOf(taskDto.getTaskId()));
        return IdmResDTO.success();
    }
}
