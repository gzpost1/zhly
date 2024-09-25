package cn.cuiot.dmp.baseconfig.controller.workorder;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.baseconfig.flow.dto.BatchBusinessWorkDto;
import cn.cuiot.dmp.baseconfig.flow.dto.QueryPlanExecutionDto;
import cn.cuiot.dmp.baseconfig.flow.dto.QueryWorkPlanInfoDto;
import cn.cuiot.dmp.baseconfig.flow.dto.WorkPlanInfoDto;
import cn.cuiot.dmp.baseconfig.flow.entity.PlanWorkExecutionInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkPlanInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.service.WorkPlanInfoService;
import cn.cuiot.dmp.baseconfig.task.WorkPlanInfoTask;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工单计划信息
 * @author pengjian
 * @create 2024/5/6 14:59
 */
@RestController
@RequestMapping("/workPlan")
public class WorkPlanInfoController {
    @Autowired
    private WorkPlanInfoService workPlanInfoService;

    /**
     * 创建工单计划
     * @param workPlanInfoCreateDto
     * @return
     */
    @PostMapping("createWordPlan")
    @RequiresPermissions
    @LogRecord(operationCode = "createWordPlan", operationName = "创建定时计划", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO createWordPlan(@RequestBody WorkPlanInfoDto workPlanInfoCreateDto){
        return workPlanInfoService.createWordPlan(workPlanInfoCreateDto);
    }


    /**
     * 查询工单列表
     * @param dto
     * @return
     */
    @RequiresPermissions
    @PostMapping("queryWordPlanInfo")
    public IdmResDTO<IPage<WorkPlanInfoEntity>> queryWordPlanInfo(@RequestBody QueryWorkPlanInfoDto dto){
        return workPlanInfoService.queryWordPlanInfo(dto);
    }

    /**
     * 启停用
     * @param dto
     * @return
     */
    @PostMapping("updateState")
    @RequiresPermissions
    public IdmResDTO updateState(@RequestBody QueryWorkPlanInfoDto dto){
        return workPlanInfoService.updateState(dto);
    }

    /**
     * 删除
     * @param dto
     * @return
     */
    @PostMapping("deleteWorkPlan")
    @RequiresPermissions
    public IdmResDTO deleteWorkPlan(@RequestBody QueryWorkPlanInfoDto dto){
        return workPlanInfoService.deleteWorkPlan(dto);
    }

    /**
     * 批量操作
     * @param dto
     * @return
     */
    @PostMapping("batchBusinessWorkInfo")
    @RequiresPermissions
    public IdmResDTO batchBusinessWorkInfo(@RequestBody BatchBusinessWorkDto dto){
        return workPlanInfoService.batchBusinessWorkInfo(dto);
    }

    /**
     * 获取已生成或预生成列表
     * @param dto
     * @return
     */
    @RequiresPermissions
    @PostMapping("queryExecutionInfo")
    public IdmResDTO<IPage<PlanWorkExecutionInfoEntity>> queryExecutionInfo(@RequestBody QueryPlanExecutionDto dto){
        return workPlanInfoService.queryExecutionInfo(dto);
    }

    @Autowired
    private WorkPlanInfoTask workPlanInfoTask;
    @PostMapping("test")
    public IdmResDTO test(){
        workPlanInfoTask.createWork("");
        return IdmResDTO.success();
    }
}
