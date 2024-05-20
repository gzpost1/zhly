package cn.cuiot.dmp.baseconfig.flow.service;

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.base.infrastructure.xxljob.XxlJobClient;
import cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.*;
import cn.cuiot.dmp.baseconfig.flow.entity.PlanContentEntity;
import cn.cuiot.dmp.baseconfig.flow.entity.PlanWorkExecutionInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkPlanInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.mapper.WorkPlanInfoMapper;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jodd.util.StringUtil;
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
    private XxlJobClient xxlJobClient;

    @Autowired
    private PlanWorkExecutionInfoService planWorkExecutionInfoService;

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    @Autowired
    private PlanContentService planContentService;
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
        entity.setContent(JSONObject.toJSONString(workPlanInfoCreateDto.getStartProcessInstanceDTO()));
        planContentService.save(entity);
        return IdmResDTO.success();
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
            times.add(of);
            //启用循环
            if(workPlanInfoCreateDto.getRecurrentState().intValue()==1){
                //获取循环结束时间
                LocalDateTime overTime = getOverTime(dto.getExecutionDate(), workPlanInfoCreateDto);
                LocalDateTime localDateTime = of.plusMinutes(Math.round(workPlanInfoCreateDto.getPushHour() * 60));
                while (overTime.isAfter(localDateTime)){
                    times.add(localDateTime);
                    localDateTime = localDateTime.plusMinutes(Math.round(workPlanInfoCreateDto.getPushHour() * 60));
                }
            }
        }
    }
    public  void  specifyWeek(WorkPlanInfoDto workPlanInfoCreateDto,LocalDateTime of,ExecutionDateDto dto, List<LocalDateTime> times){
        //获取时间星期几
        int value = of.getDayOfWeek().getValue();
        if(getListStr(workPlanInfoCreateDto.getSpecifyWeek(),of).contains(String.valueOf(value))){
            times.add(of);
            //启用循环
            if(workPlanInfoCreateDto.getRecurrentState().intValue()==1){
                //获取循环结束时间
                LocalDateTime overTime = getOverTime(dto.getExecutionDate(), workPlanInfoCreateDto);
                LocalDateTime localDateTime = of.plusMinutes(Math.round(workPlanInfoCreateDto.getPushHour() * 60));
                while (overTime.isAfter(localDateTime)){
                    times.add(localDateTime);
                    localDateTime = localDateTime.plusMinutes(Math.round(workPlanInfoCreateDto.getPushHour() * 60));
                }
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
                times.add(of);
                //启用循环
                if(workPlanInfoCreateDto.getRecurrentState().intValue()==1){
                    //获取循环结束时间
                    LocalDateTime overTime = getOverTime(dto.getExecutionDate(), workPlanInfoCreateDto);
                    LocalDateTime localDateTime = of.plusMinutes(Math.round(workPlanInfoCreateDto.getPushHour() * 60));
                    while (overTime.isAfter(localDateTime)){
                        times.add(localDateTime);
                        localDateTime = localDateTime.plusMinutes(Math.round(workPlanInfoCreateDto.getPushHour() * 60));
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
                LocalDateTime localDateTime = of.plusMinutes(Math.round(workPlanInfoCreateDto.getPushHour() * 60));
                while (overTime.isAfter(localDateTime)){
                    times.add(localDateTime);
                    localDateTime = localDateTime.plusMinutes(Math.round(workPlanInfoCreateDto.getPushHour() * 60));
                }
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
        LambdaQueryWrapper<WorkPlanInfoEntity> lw = getCondition(dto);
        Page<WorkPlanInfoEntity> workPlanInfoEntityPage = this.getBaseMapper().selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), lw);
        List<WorkPlanInfoEntity> records = workPlanInfoEntityPage.getRecords();
        if(CollectionUtil.isNotEmpty(records)){
            List<Long> userIds = records.stream().map(WorkPlanInfoEntity::getCreateUser).collect(Collectors.toList());
            Map<Long, String> userMap = getUserMap(userIds);
            records.stream().forEach(item->{
                //根据userId获取userName
                item.setCreateName(userMap.get(item.getCreateUser()));
            });
        }
        return IdmResDTO.success(workPlanInfoEntityPage);
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

    public IdmResDTO deleteWorkPlan(QueryWorkPlanInfoDto dto) {
        this.removeById(dto.getId());
        return IdmResDTO.success();
    }

    public IdmResDTO batchBusinessWorkInfo(BatchBusinessWorkDto dto) {
        if(dto.getBusinessType().intValue()==0){
            List<WorkPlanInfoEntity> workPlanInfoEntities = this.getBaseMapper().selectBatchIds(dto.getIds());
            workPlanInfoEntities.stream().forEach(item->item.setState(Byte.parseByte("0")));
            this.saveOrUpdateBatch(workPlanInfoEntities);
        }
        if(dto.getBusinessType().intValue()==1){
            List<WorkPlanInfoEntity> workPlanInfoEntities = this.getBaseMapper().selectBatchIds(dto.getIds());
            workPlanInfoEntities.stream().forEach(item->item.setState(Byte.parseByte("1")));
            this.saveOrUpdateBatch(workPlanInfoEntities);
        }
        if(dto.getBusinessType().intValue()==2){
            this.getBaseMapper().deleteBatchIds(dto.getIds());
        }
        return IdmResDTO.success();
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
        //已生成
        if(dto.getPageType().equals(WorkFlowConstants.RESULT_1)){
            lw.eq(PlanWorkExecutionInfoEntity::getState,WorkFlowConstants.RESULT_1);
        }

        //未生成或生成失败
        if(dto.getPageType().equals(WorkFlowConstants.RESULT_0)){
            if(Objects.nonNull(dto.getState())){
                lw.eq(PlanWorkExecutionInfoEntity::getState,dto.getState());
            }else{
                lw.in(PlanWorkExecutionInfoEntity::getState,Arrays.asList(WorkFlowConstants.RESULT_0, WorkFlowConstants.PARAM_2));
            }

        }
        if(Objects.nonNull(dto.getStartDate())){
            lw.ge(PlanWorkExecutionInfoEntity::getExecutionTime,dto.getStartDate()).le(PlanWorkExecutionInfoEntity::getExecutionTime,dto.getEndDate());
        }
        Page<PlanWorkExecutionInfoEntity> page = planWorkExecutionInfoService.getBaseMapper().
                selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), lw);
        return IdmResDTO.success(page);
    }
}
