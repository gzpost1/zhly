package cn.cuiot.dmp.baseconfig.task;

import cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.StartProcessInstanceDTO;
import cn.cuiot.dmp.baseconfig.flow.entity.PlanWorkExecutionInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkPlanInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.service.PlanWorkExecutionInfoService;
import cn.cuiot.dmp.baseconfig.flow.service.WorkInfoService;
import cn.cuiot.dmp.baseconfig.flow.service.WorkPlanInfoService;
import cn.cuiot.dmp.baseconfig.flow.utils.JsonUtil;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.ObjectMapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxl.job.core.handler.annotation.XxlJob;
import liquibase.pro.packaged.P;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author pengjian
 * @create 2024/5/7 16:31
 */
@Slf4j
@Component
public class WorkPlanInfoTask {

    @Autowired
    private WorkPlanInfoService workPlanInfoService;
    @Autowired
    private WorkInfoService workInfoService;

    @Autowired
    private PlanWorkExecutionInfoService planWorkExecutionInfoService;

    @XxlJob("createPlanWork")
    public void createWork(String param){
        //查询工单信息
        WorkPlanInfoEntity entity = workPlanInfoService.getById(Long.parseLong(param));
        //判断生效与失效
        if(checkExpire(entity)){
            log.info("任务"+param+"未开始");
            return;
        }
        StartProcessInstanceDTO startProcessInstanceDTO = JsonUtil.readValue(entity.getWorkJosn(), StartProcessInstanceDTO.class);
        IdmResDTO start = workInfoService.start(startProcessInstanceDTO);
        Long data =Long.parseLong(String.valueOf(start.getData())) ;
        if(Objects.nonNull(data)){
            updatePlanWorkExecutionInfo( param, data);
        }
    }

    public void updatePlanWorkExecutionInfo(String param,Long data){
        LambdaQueryWrapper<PlanWorkExecutionInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(PlanWorkExecutionInfoEntity::getPlanWorkId, Long.parseLong(param)).eq(PlanWorkExecutionInfoEntity::getState
                , WorkFlowConstants.RESULT_0).orderByAsc(PlanWorkExecutionInfoEntity::getExecutionTime).last("limit 1");
        List<PlanWorkExecutionInfoEntity> list = planWorkExecutionInfoService.list(lw);
        if(CollectionUtil.isNotEmpty(list)){
            PlanWorkExecutionInfoEntity executionInfo = list.get(0);
            executionInfo.setState(WorkFlowConstants.RESULT_1);
            executionInfo.setProcInstId(data);
            planWorkExecutionInfoService.updateById(executionInfo);
        }
    }
    public boolean checkExpire( WorkPlanInfoEntity entity){
        LocalDateTime date = LocalDateTime.now();
        Instant startInstant = entity.getStartDate().toInstant();
        LocalDateTime startDate = LocalDateTime.ofInstant(startInstant, ZoneId.systemDefault()).minusDays(entity.getPushDay());


        Instant endInstant = entity.getEndDate().toInstant();
        LocalDateTime endDate = LocalDateTime.ofInstant(endInstant, ZoneId.systemDefault()).minusDays(entity.getPushDay());;
        if(date.isAfter(startDate) && date.isBefore(endDate)){
            return false;
        }

        if(!getOverTime(entity).isBefore(date)){
            return false;
        }

        return true;
    }

    public LocalDateTime getOverTime(WorkPlanInfoEntity entity){
        LocalDate currentDate = LocalDate.now();
        //循环结束类型今日
        LocalTime recurrentOverTime = entity.getRecurrentOverTime();
        if(Objects.equals(entity.getRecurrentType(), WorkFlowConstants.RESULT_1)){
            currentDate=currentDate.plusDays(1);
        }

        // 获取新的日期时间
        return LocalDateTime.of(currentDate, recurrentOverTime);
    }

}
