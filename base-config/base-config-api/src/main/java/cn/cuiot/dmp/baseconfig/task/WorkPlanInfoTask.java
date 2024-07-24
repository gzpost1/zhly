package cn.cuiot.dmp.baseconfig.task;

import cn.cuiot.dmp.base.application.enums.OrgStatusEnum;
import cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants;
import cn.cuiot.dmp.baseconfig.flow.constants.WorkOrderConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.StartProcessInstanceDTO;
import cn.cuiot.dmp.baseconfig.flow.entity.PlanContentEntity;
import cn.cuiot.dmp.baseconfig.flow.entity.PlanWorkExecutionInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkPlanInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.enums.WorkOrderResultEnums;
import cn.cuiot.dmp.baseconfig.flow.service.PlanContentService;
import cn.cuiot.dmp.baseconfig.flow.service.PlanWorkExecutionInfoService;
import cn.cuiot.dmp.baseconfig.flow.service.WorkInfoService;
import cn.cuiot.dmp.baseconfig.flow.service.WorkPlanInfoService;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.ObjectMapper;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import liquibase.pro.packaged.L;
import liquibase.pro.packaged.P;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.*;

/**
 * 生成工单计划
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

    @Autowired
    private PlanContentService planContentService;

    @XxlJob("createPlanWork")
    public ReturnT<String> createWork(String param){
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.minusMinutes(6);
        LambdaQueryWrapper<PlanWorkExecutionInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.gt(PlanWorkExecutionInfoEntity::getExecutionTime,endDate).le(PlanWorkExecutionInfoEntity::getExecutionTime,startDate)
                .eq(PlanWorkExecutionInfoEntity::getState, WorkFlowConstants.RESULT_0);

        List<PlanWorkExecutionInfoEntity> executionList = planWorkExecutionInfoService.list(lw);
        if(CollectionUtil.isEmpty(executionList)){
            return ReturnT.SUCCESS;
        }
        List<PlanWorkExecutionInfoEntity> exList = new ArrayList<>();
        for(PlanWorkExecutionInfoEntity entity : executionList){
            //查询工单信息
            WorkPlanInfoEntity planEntity = Optional.ofNullable(workPlanInfoService.getById(entity.getPlanWorkId())).orElse(new WorkPlanInfoEntity());
            //停用
            if(Objects.equals(planEntity.getState(),OrgStatusEnum.DISABLE.getCode().intValue())){
                entity.setState(WorkOrderResultEnums.GENERATION_FAILED.getCode());
            }else if (Objects.equals(OrgStatusEnum.ENABLE.getCode(),planEntity.getState().intValue())){
                //启用
                PlanContentEntity planContentEntity = planContentService.getById(entity.getPlanWorkId());
                StartProcessInstanceDTO startProcessInstanceDTO = JSONObject.parseObject(planContentEntity.getContent(), new TypeReference<StartProcessInstanceDTO>() {
                });
                startProcessInstanceDTO.setWorkSource(WorkOrderConstants.WORK_SOURCE_PLAN);
                IdmResDTO start = workInfoService.start(startProcessInstanceDTO);
                Long data =Long.parseLong(String.valueOf(start.getData())) ;
                entity.setProcInstId(data);
                entity.setState( WorkOrderResultEnums.GENERATED.getCode());
            }
            exList.add(entity);
        }
        if(CollectionUtil.isNotEmpty(exList)){
            planWorkExecutionInfoService.updateBatchById(exList);
        }

        return ReturnT.SUCCESS;
    }



}
