package cn.cuiot.dmp.baseconfig.flow.service;

import cn.cuiot.dmp.baseconfig.flow.dto.BatchBusinessWorkDto;
import cn.cuiot.dmp.baseconfig.flow.dto.QueryWorkPlanInfoDto;
import cn.cuiot.dmp.baseconfig.flow.dto.WorkPlanInfoDto;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkPlanInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.mapper.WorkPlanInfoMapper;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jodd.util.StringUtil;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author pengjian
 * @since 2024-05-06
 */
@Service
public class WorkPlanInfoService extends ServiceImpl<WorkPlanInfoMapper, WorkPlanInfoEntity>{

    /**
     * 创建工单计划
     * @param workPlanInfoCreateDto
     * @return
     */
    public IdmResDTO createWordPlan(WorkPlanInfoDto workPlanInfoCreateDto) {
        WorkPlanInfoEntity map = BeanMapper.map(workPlanInfoCreateDto, WorkPlanInfoEntity.class);
        map.setId(IdWorker.getId());
        this.save(map);
        return IdmResDTO.success();
    }

    /**
     * 生成corn表达式
     * @param
     * @return
     */
    public String createCron(WorkPlanInfoDto dto){
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
                specifyMinute=specifyMinute+"/"+String.valueOf(dto.getRecurrentHour());
            }
            String cron =String.format("%d %d %d %d * ?", second,
                    specifyMinute, hour, specifyDay);
        }
        //按周
        if(dto.getExecutionStrategy().intValue()==2){
            String specifyMinute=String.valueOf(minute);
            //启用循环
            if(dto.getRecurrentState().intValue()==0){
                specifyMinute=specifyMinute+"/"+String.valueOf(dto.getRecurrentHour());
            }
            String specifyWeek = dto.getSpecifyWeek();
            String cron =String.format("%d %d %d * * %d", second,
                    specifyMinute, hour, specifyWeek);
        }
        //按月执行
        if(dto.getExecutionStrategy().intValue()==3){
            String specifyMinute=String.valueOf(minute);
            //启用循环
            if(dto.getRecurrentState().intValue()==0){
                specifyMinute=specifyMinute+"/"+String.valueOf(dto.getRecurrentHour());
            }
            String cron =String.format("%d %d %d %d %d ?", second,
                    specifyMinute, hour, dto.getSpecifyDay(),dto.getSpecifyMonth());
        }



        return null;
    }


    /**
     * 查询计划任务列表
     * @param dto
     * @return
     */
    public IdmResDTO<IPage<WorkPlanInfoEntity>>  queryWordPlanInfo(QueryWorkPlanInfoDto dto) {

        //TODO 根据组织id获取自己及下属的组织id
        LambdaQueryWrapper<WorkPlanInfoEntity> lw = getCondition(dto);
        Page<WorkPlanInfoEntity> workPlanInfoEntityPage = this.getBaseMapper().selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), lw);
        List<WorkPlanInfoEntity> records = workPlanInfoEntityPage.getRecords();
        if(CollectionUtil.isNotEmpty(records)){
            records.stream().forEach(item->{
                //TODO根据userId获取userName
            });
        }
        return IdmResDTO.success(workPlanInfoEntityPage);
    }
    public LambdaQueryWrapper getCondition(QueryWorkPlanInfoDto dto){
        LambdaQueryWrapper<WorkPlanInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.like(Objects.nonNull(dto.getId()),WorkPlanInfoEntity::getId,dto.getId())
        .like(Objects.nonNull(dto.getPlanName()),WorkPlanInfoEntity::getPlanName,dto.getPlanName())
         .in(CollectionUtil.isNotEmpty(dto.getOrgIds()),WorkPlanInfoEntity::getOrgId,dto.getOrgIds());
        if(Objects.nonNull(dto.getPlanDate())){
            lw.le(WorkPlanInfoEntity::getStartDate,dto.getPlanDate()).ge(WorkPlanInfoEntity::getEndDate,dto.getPlanDate());
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
}
