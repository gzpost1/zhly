package cn.cuiot.dmp.baseconfig.flow.service;

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.base.infrastructure.xxljob.XxlJobClient;
import cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.*;
import cn.cuiot.dmp.baseconfig.flow.entity.*;
import cn.cuiot.dmp.baseconfig.flow.mapper.WorkPlanInfoMapper;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jodd.util.StringUtil;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author pengjian
 * @since 2024-05-06
 */
@Service
public class WorkPlanInfoService extends ServiceImpl<WorkPlanInfoMapper, WorkPlanInfoEntity>{


    @Autowired
    private WorkInfoService workInfoService;

    @Autowired
    private PlanWorkExecutionInfoService planWorkExecutionInfoService;

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    @Autowired
    private PlanContentService planContentService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TbFlowConfigService flowConfigService;

    @Autowired
    private TbFlowConfigOrgService tbFlowConfigOrgService;

    @Autowired
    private WorkOrgRelService workOrgRelService;

    @Autowired
    private ProcessAndDeptService processAndDeptService;

    /**
     * 创建工单计划
     * @param workPlanInfoCreateDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO createWordPlan(WorkPlanInfoDto workPlanInfoCreateDto) {


        WorkPlanInfoEntity map = BeanMapper.map(workPlanInfoCreateDto, WorkPlanInfoEntity.class);
        map.setId(IdWorker.getId());
        map.setOrgId(LoginInfoHolder.getCurrentOrgId());

//        //创建定时任务
//        Long xxlJob = xxlJobClient.createXxlJob(workPlanInfoCreateDto.getPlanName(),
//                WorkFlowConstants.JOB_STATUS, WorkFlowConstants.JOB_INVOKETARGET, createCron(workPlanInfoCreateDto),
//                String.valueOf(map.getId()));

//        map.setTaskId(xxlJob);
        this.save(map);

        workPlanInfoCreateDto.setId(map.getId());
        //创建预生成时间
        saveExecutionTime(workPlanInfoCreateDto);

        PlanContentEntity entity = new PlanContentEntity();
        entity.setId(map.getId());
        workPlanInfoCreateDto.getStartProcessInstanceDTO().setCreateUserId(LoginInfoHolder.getCurrentUserId());
        entity.setContent(JSONObject.toJSONString(workPlanInfoCreateDto.getStartProcessInstanceDTO()));
        planContentService.save(entity);

        //保存组织信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(workPlanInfoCreateDto.getStartProcessInstanceDTO().getProcessDefinitionId())
                .singleResult();
        String flowableKey = processDefinition.getKey().replaceAll("[a-zA-Z]", "");
        TbFlowConfig flowConfig = Optional.ofNullable(flowConfigService.getById(Long.parseLong(flowableKey))).
                orElseThrow(()->new RuntimeException("流程配置为空"));

        List<Long> orgIds = orgIds(flowConfig.getId());
//        saveWorkOrg(map.getId(),orgIds);
        saveProcessDefinitionAndOrgIds(workPlanInfoCreateDto.getStartProcessInstanceDTO().getProcessDefinitionId(), orgIds);
        return IdmResDTO.success();
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
     * 保存预生成时间
     * @param workPlanInfoCreateDto
     */

    public void saveExecutionTime(WorkPlanInfoDto workPlanInfoCreateDto){

        Instant instant = workPlanInfoCreateDto.getStartDate().toInstant();
        LocalDate startDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        Instant endTasn = workPlanInfoCreateDto.getEndDate().toInstant();
        LocalDate endDate = endTasn.atZone(ZoneId.systemDefault()).toLocalDate();
        List<ExecutionDateDto> dtos = Optional.ofNullable(planWorkExecutionInfoService.
                queryExecutionTime(String.valueOf(startDate), String.valueOf(endDate) )).
                orElseThrow(()->new RuntimeException("时间周期为空"));
        //工单推送时间 单位分钟
        long round = Math.round(workPlanInfoCreateDto.getPushHour() * 60 + workPlanInfoCreateDto.getPushDay() * 24 * 60);
        List<LocalDateTime> times = new ArrayList<>();
        for(ExecutionDateDto dto : dtos){
            //计划开始时间
            LocalDateTime of = LocalDateTime.of(dto.getExecutionDate(), workPlanInfoCreateDto.getPlanTime());
            //按天
            if(workPlanInfoCreateDto.getExecutionStrategy().intValue()==1){
                //指定的天数
                specifyDay(workPlanInfoCreateDto,of,dto,times);
            }
            //按周
            if(workPlanInfoCreateDto.getExecutionStrategy().intValue()==2){
                //指定的星期
                specifyWeek(workPlanInfoCreateDto,of,dto,times);
            }
            //按月
            if(workPlanInfoCreateDto.getExecutionStrategy().intValue()==3){
                //指定的月份
                specifyMonth(workPlanInfoCreateDto,of,dto,times);
            }
        }
        List<PlanWorkExecutionInfoEntity> entities = new ArrayList<>();
        if(times.size()>0){
            times.stream().forEach(item->{
                PlanWorkExecutionInfoEntity entity = new PlanWorkExecutionInfoEntity();
                entity.setId(IdWorker.getId());
                entity.setExecutionTime(item.minusMinutes(round));
                entity.setPlanWorkId(workPlanInfoCreateDto.getId());
                entities.add(entity);
            });
        }
        if(CollectionUtil.isNotEmpty(entities)){
            planWorkExecutionInfoService.saveBatch(entities);
        }

    }

    public static void main(String[] args) {
        String dateTimeString = "2024-04-05 12:45:46";

        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 将字符串转换为LocalDateTime对象
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

        System.out.println(dateTime.getDayOfMonth());
    }

    public void specifyMonth(WorkPlanInfoDto workPlanInfoCreateDto,LocalDateTime of,ExecutionDateDto dto, List<LocalDateTime> times){
        //获取月份
        int monthValue = of.getMonthValue();
        //获取日
        int dayOfMonth = of.getDayOfMonth();

        if(getListStr(workPlanInfoCreateDto.getSpecifyMonth(),of).contains(String.valueOf(monthValue)) &&
                getListStr(workPlanInfoCreateDto.getSpecifyDay(),of).contains(String.valueOf(dayOfMonth))){
            //启用循环
            if(workPlanInfoCreateDto.getRecurrentState().intValue()==1){
                //获取循环结束时间
                LocalDateTime overTime = getOverTime(dto.getExecutionDate(), workPlanInfoCreateDto);
                LocalDateTime localDateTime = of;
                while (overTime.isAfter(localDateTime)){
                    times.add(localDateTime);
                    localDateTime = localDateTime.plusMinutes(Math.round(workPlanInfoCreateDto.getRecurrentHour() * 60));
                }
            }else{
                times.add(of);
            }
        }
    }
    public  void  specifyWeek(WorkPlanInfoDto workPlanInfoCreateDto,LocalDateTime of,ExecutionDateDto dto, List<LocalDateTime> times){
        //获取时间星期几
        int value = of.getDayOfWeek().getValue();
        if(getListStr(workPlanInfoCreateDto.getSpecifyWeek(),of).contains(String.valueOf(value))){
            //启用循环
            if(workPlanInfoCreateDto.getRecurrentState().intValue()==1){
                //获取循环结束时间
                LocalDateTime overTime = getOverTime(dto.getExecutionDate(), workPlanInfoCreateDto);
                LocalDateTime localDateTime = of;
                while (overTime.isAfter(localDateTime)){
                    times.add(localDateTime);
                    localDateTime = localDateTime.plusMinutes(Math.round(workPlanInfoCreateDto.getRecurrentHour() * 60));
                }
            }else{
                times.add(of);
            }
        }
    }


    public Set<String> getListStr(String str,LocalDateTime of){
        if (str.contains(WorkFlowConstants.LAST_DAT)) {
            str = str.substring(0,str.length()-3)+","+of.toLocalDate().with(TemporalAdjusters.lastDayOfMonth());
        }
        String[] items = str.split(",",-1);

        return new HashSet<>(Arrays.asList(items));
    }
    /**
     * 指定循环天数
     * @param workPlanInfoCreateDto
     * @param of
     * @param dto
     * @param times
     */
    public void specifyDay(WorkPlanInfoDto workPlanInfoCreateDto,LocalDateTime of,ExecutionDateDto dto, List<LocalDateTime> times){
        if(StringUtil.isNotBlank(workPlanInfoCreateDto.getSpecifyDay())){
            String specifyDay=workPlanInfoCreateDto.getSpecifyDay();
            if(getListStr(specifyDay,of).contains(String.valueOf(of.getDayOfMonth()))){
                //启用循环
                if(workPlanInfoCreateDto.getRecurrentState().intValue()==1){
                    //获取循环结束时间
                    LocalDateTime overTime = getOverTime(dto.getExecutionDate(), workPlanInfoCreateDto);
                    LocalDateTime localDateTime = of;
                    while (overTime.isAfter(localDateTime)){
                        times.add(localDateTime);
                        localDateTime = localDateTime.plusMinutes(Math.round(workPlanInfoCreateDto.getRecurrentHour() * 60));
                    }
                }
            }
        }else{
//            times.add(of);
            //没有指定天数
            //启用循环
            if(workPlanInfoCreateDto.getRecurrentState().intValue()==1){
                //获取循环结束时间
                LocalDateTime overTime = getOverTime(dto.getExecutionDate(), workPlanInfoCreateDto);
//                LocalDateTime localDateTime = of.plusMinutes(Math.round(workPlanInfoCreateDto.getPushHour() * 60));
                LocalDateTime localDateTime =of;
                while (overTime.isAfter(localDateTime)){
                    times.add(localDateTime);
                    localDateTime = localDateTime.plusMinutes(Math.round(workPlanInfoCreateDto.getRecurrentHour() * 60));
                }
            }else{
                times.add(of);
            }
        }

    }
    public LocalDateTime getOverTime(LocalDate currentDate,WorkPlanInfoDto workPlanInfoCreateDto){

        //循环结束类型今日
        LocalTime recurrentOverTime = workPlanInfoCreateDto.getRecurrentOverTime();
        if(Objects.equals(workPlanInfoCreateDto.getRecurrentType(), WorkFlowConstants.RESULT_1)){
            currentDate=currentDate.plusDays(1);
        }

        // 获取新的日期时间
        return LocalDateTime.of(currentDate, recurrentOverTime);
    }
    /**
     * 生成corn表达式
     * @param
     * @return
     */
    public String createCron(WorkPlanInfoDto dto){
        String cron =null;
        //将推送时间的天与小时转成分钟
        getPushTime(dto);
        //减去提前推送的时间
        LocalTime localTime = dto.getPlanTime().minusMinutes(dto.getPushTime());
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        int second = localTime.getSecond();
        if(dto.getExecutionStrategy().intValue()==1){
            //天数
            String specifyDay = "*";
            if(StringUtil.isNotBlank(dto.getSpecifyDay())){
                specifyDay=dto.getSpecifyDay();
            }
            //分钟
            String specifyMinute=String.valueOf(minute);
            //启用循环
            if(dto.getRecurrentState().intValue()==0){
                specifyMinute=specifyMinute+"/"+String.valueOf(Math.round(dto.getRecurrentHour()*60));
            }
             cron =String.format("%d %s %d %s * ? *", second,
                    specifyMinute, hour, specifyDay);
        }
        //按周
        if(dto.getExecutionStrategy().intValue()==2){
            String specifyMinute=String.valueOf(minute);
            //启用循环
            if(dto.getRecurrentState().intValue()==0){
                specifyMinute=specifyMinute+"/"+String.valueOf(Math.round(dto.getRecurrentHour()*60));
            }
            String specifyWeek = dto.getSpecifyWeek();
             cron =String.format("%d %s %d ? * %d *", second,
                    specifyMinute, hour, specifyWeek);
        }
        //按月执行
        if(dto.getExecutionStrategy().intValue()==3){
            String specifyMinute=String.valueOf(minute);
            //启用循环
            if(dto.getRecurrentState().intValue()==0){
                specifyMinute=specifyMinute+"/"+String.valueOf(Math.round(dto.getRecurrentHour()*60));
            }
             cron =String.format("%d %s %d %s %s ? *", second,
                    specifyMinute, hour, dto.getSpecifyDay(),dto.getSpecifyMonth());
        }



        return cron;
    }

    public String getSpecifyDat(String day){
        if(day.contains(WorkFlowConstants.LAST_DAT)){
            return day.substring(0,day.length()-3);
        }
        return "";
    }
    /**
     * 将推送时间转成分钟
     * @param dto
     */
    public void getPushTime(WorkPlanInfoDto dto){
        //dto.getPushDay()*24*60*60+
        dto.setPushTime(Math.round(dto.getPushHour()*60));
    }



    /**
     * 查询计划任务列表
     * @param dto
     * @return
     */
    public IdmResDTO<IPage<WorkPlanInfoEntity>>  queryWordPlanInfo(QueryWorkPlanInfoDto dto) {

        //根据组织id获取自己及下属的组织id
        if(CollectionUtil.isEmpty(dto.getOrgIds())){
            List<DepartmentDto> deptList = getDeptIds(LoginInfoHolder.getCurrentDeptId());
            List<Long> deptIds = deptList.stream().map(DepartmentDto::getId).collect(Collectors.toList());
            dto.setOrgIds(deptIds);
        }
        Page<WorkPlanInfoEntity> workPlanInfoEntityPage = this.getBaseMapper().queryWordPlanInfo(new Page<>(dto.getCurrentPage(), dto.getPageSize()), dto);
        List<WorkPlanInfoEntity> records = workPlanInfoEntityPage.getRecords();
        if(CollectionUtil.isNotEmpty(records)){
            List<Long> userIds = records.stream().map(WorkPlanInfoEntity::getCreateUser).collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(userIds);
            List<Long> ids = records.stream().map(WorkPlanInfoEntity::getId).collect(Collectors.toList());
            Map<Long, String> contentMap = getContent(ids);
            records.stream().forEach(item->{
                //根据userId获取userName
                item.setCreateName(userMap.get(item.getCreateUser()));
                item.setStartProcessInstanceDTO(contentMap.get(item.getId()));
            });
        }
        return IdmResDTO.success(workPlanInfoEntityPage);
    }

    public Map<Long, String> getContent(List<Long> ids){
        List<PlanContentEntity> planContentEntities = planContentService.getBaseMapper().selectBatchIds(ids);
        return planContentEntities.stream().collect(Collectors.toMap(PlanContentEntity::getId,PlanContentEntity::getContent ));
    }
    public Map<Long,String> getUserMap(List<Long> userIds){
        BaseUserReqDto userReqDto = new BaseUserReqDto();
        userReqDto.setUserIdList(userIds);
        IdmResDTO<List<BaseUserDto>> listIdmResDTO = systemApiFeignService.lookUpUserList(userReqDto);
        List<BaseUserDto> data = Optional.ofNullable(listIdmResDTO.getData()).orElseThrow(()->new RuntimeException("用户信息不存在"));
        return data.stream().collect(Collectors.toMap(BaseUserDto::getId,BaseUserDto::getName ));
    }

    public List<DepartmentDto> getDeptIds(Long deptId){
        DepartmentReqDto paraDto = new DepartmentReqDto();
        paraDto.setDeptId(deptId);
        paraDto.setSelfReturn(true);
        IdmResDTO<List<DepartmentDto>> listIdmResDTO = systemApiFeignService.lookUpDepartmentChildList(paraDto);
        return listIdmResDTO.getData();

    }
    public LambdaQueryWrapper getCondition(QueryWorkPlanInfoDto dto){
        LambdaQueryWrapper<WorkPlanInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.like(Objects.nonNull(dto.getId()),WorkPlanInfoEntity::getId,dto.getId())
                .eq(Objects.nonNull(dto.getState()),WorkPlanInfoEntity::getState,dto.getState())
        .like(Objects.nonNull(dto.getPlanName()),WorkPlanInfoEntity::getPlanName,dto.getPlanName())
         .in(CollectionUtil.isNotEmpty(dto.getOrgIds()),WorkPlanInfoEntity::getOrgId,dto.getOrgIds());
        if(Objects.nonNull(dto.getStartDate())){
            lw.le(WorkPlanInfoEntity::getStartDate,dto.getEndDate()).ge(WorkPlanInfoEntity::getStartDate,dto.getStartDate());
        }
        if(Objects.nonNull(dto.getPlanState())){
            //未生效
            if(dto.getPlanState().intValue()==0){
                lw.gt(WorkPlanInfoEntity::getStartDate,new Date());
            }
            //已生效
            if(dto.getPlanState().intValue()==1){
                lw.lt(WorkPlanInfoEntity::getStartDate,new Date()).ge(WorkPlanInfoEntity::getEndDate,new Date());
            }
            //已失效
            if(dto.getPlanState().intValue()==2){
                lw.lt(WorkPlanInfoEntity::getEndDate,new Date());
            }

        }
        return lw;
    }

    /**
     * 更新状态
     * @param dto
     * @return
     */
    public IdmResDTO updateState(QueryWorkPlanInfoDto dto) {
        WorkPlanInfoEntity byId = this.getById(dto.getId());
        byId.setState(dto.getState());
        this.updateById(byId);
        return IdmResDTO.success();
    }


    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO deleteWorkPlan(QueryWorkPlanInfoDto dto) {
        this.removeById(dto.getId());

        LambdaQueryWrapper<PlanWorkExecutionInfoEntity> exLw = new LambdaQueryWrapper<>();
        exLw.eq(PlanWorkExecutionInfoEntity::getPlanWorkId,dto.getId());

        List<PlanWorkExecutionInfoEntity> exList = planWorkExecutionInfoService.list(exLw);
        List<Long> procIds = exList.stream().filter(e->Objects.nonNull(e.getProcInstId())).map(PlanWorkExecutionInfoEntity::getProcInstId).collect(Collectors.toList());

        //删除工单信息,单个删除时只有一个id
        if(CollectionUtils.isNotEmpty(procIds)){
            deleteWorkInfo(procIds);
        }

        //删除对应的时间信息
        planWorkExecutionInfoService.remove(exLw);

        //删除对应的组织信息
        deleteOrgs(dto.getId());
        return IdmResDTO.success();
    }


    /**
     * 删除计划工单关联的组织信息
     * @param ids
     */
    private void deleteOrgs(Long ids){
        LambdaQueryWrapper<WorkOrgRelEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(WorkOrgRelEntity::getWorkId,ids);
        workOrgRelService.remove(lw);

    }
    /**
     * 删除对应的工单信息
     * @param prodInstId
     */
    public void deleteWorkInfo(List<Long> prodInstId){
        LambdaQueryWrapper<WorkInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.in(WorkInfoEntity::getProcInstId,prodInstId);
        workInfoService.remove(lw);
    }
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO batchBusinessWorkInfo(BatchBusinessWorkDto dto) {
        if(dto.getBusinessType().intValue()==0){
            List<WorkPlanInfoEntity> workPlanInfoEntities = this.getBaseMapper().selectBatchIds(dto.getIds());
            workPlanInfoEntities.stream().forEach(item->item.setState(Byte.parseByte("0")));
            this.updateBatchById(workPlanInfoEntities);
        }
        if(dto.getBusinessType().intValue()==1){
            List<WorkPlanInfoEntity> workPlanInfoEntities = this.getBaseMapper().selectBatchIds(dto.getIds());
            workPlanInfoEntities.stream().forEach(item->item.setState(Byte.parseByte("1")));
            this.updateBatchById(workPlanInfoEntities);
        }
        if(dto.getBusinessType().intValue()==2){
            this.getBaseMapper().deleteBatchIds(dto.getIds());

            BatchDetelePlanRel(dto.getIds());
        }
        return IdmResDTO.success();
    }


    /**
     * 删除计划工单关联的信息
     * @param ids
     */
    public void BatchDetelePlanRel(List<Long> ids){

        ids.stream().forEach(item->{
            LambdaQueryWrapper<PlanWorkExecutionInfoEntity> exLw = new LambdaQueryWrapper<>();
            exLw.eq(PlanWorkExecutionInfoEntity::getPlanWorkId,item);

            List<PlanWorkExecutionInfoEntity> exList = planWorkExecutionInfoService.list(exLw);
            List<Long> procIds = exList.stream().filter(e->Objects.nonNull(e.getProcInstId())).map(PlanWorkExecutionInfoEntity::getProcInstId).collect(Collectors.toList());

            //删除工单信息,单个删除时只有一个id
            if(CollectionUtils.isNotEmpty(procIds)){
                deleteWorkInfo(procIds);
            }

            //删除对应的时间信息
            planWorkExecutionInfoService.remove(exLw);

            //删除对应的组织信息
            deleteOrgs(item);
        });

    }
    /**
     * cha
     * @param dto
     * @return
     */
    public IdmResDTO<IPage<PlanWorkExecutionInfoEntity>> queryExecutionInfo(QueryPlanExecutionDto dto) {

        LambdaQueryWrapper<PlanWorkExecutionInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(PlanWorkExecutionInfoEntity::getPlanWorkId,dto.getPlanId()).like(Objects.nonNull(dto.getProcInstId()),
                PlanWorkExecutionInfoEntity::getProcInstId,dto.getProcInstId());

        if(Objects.nonNull(dto.getStartDate())){
            lw.ge(PlanWorkExecutionInfoEntity::getExecutionTime,dto.getStartDate()).le(PlanWorkExecutionInfoEntity::getExecutionTime,dto.getEndDate());
        }
        //已生成
        if(dto.getPageType().equals(WorkFlowConstants.RESULT_1)){
            lw.eq(PlanWorkExecutionInfoEntity::getState,WorkFlowConstants.RESULT_1);
            lw.orderByDesc(PlanWorkExecutionInfoEntity::getExecutionTime);
        }

        //未生成或生成失败
        if(dto.getPageType().equals(WorkFlowConstants.RESULT_0)){
            if(Objects.nonNull(dto.getState())){
                lw.eq(PlanWorkExecutionInfoEntity::getState,dto.getState());
            }else{
                lw.in(PlanWorkExecutionInfoEntity::getState,Arrays.asList(WorkFlowConstants.RESULT_0, WorkFlowConstants.PARAM_2));
            }
            lw.orderByAsc(PlanWorkExecutionInfoEntity::getExecutionTime);
        }

        Page<PlanWorkExecutionInfoEntity> page = planWorkExecutionInfoService.getBaseMapper().
                selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), lw);
        return IdmResDTO.success(page);
    }
}
