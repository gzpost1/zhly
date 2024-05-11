package cn.cuiot.dmp.baseconfig.controller.workorder;

import cn.cuiot.dmp.baseconfig.flow.dto.BatchBusinessWorkDto;
import cn.cuiot.dmp.baseconfig.flow.dto.QueryPlanExecutionDto;
import cn.cuiot.dmp.baseconfig.flow.dto.QueryWorkPlanInfoDto;
import cn.cuiot.dmp.baseconfig.flow.dto.WorkPlanInfoDto;
import cn.cuiot.dmp.baseconfig.flow.entity.PlanWorkExecutionInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkPlanInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.service.WorkPlanInfoService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
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
    public IdmResDTO createWordPlan(@RequestBody WorkPlanInfoDto workPlanInfoCreateDto){
        return workPlanInfoService.createWordPlan(workPlanInfoCreateDto);
    }


    /**
     * 查询工单列表
     * @param dto
     * @return
     */
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
    public IdmResDTO updateState(@RequestBody QueryWorkPlanInfoDto dto){
        return workPlanInfoService.updateState(dto);
    }

    /**
     * 删除
     * @param dto
     * @return
     */
    @PostMapping("deleteWorkPlan")
    public IdmResDTO deleteWorkPlan(@RequestBody QueryWorkPlanInfoDto dto){
        return workPlanInfoService.deleteWorkPlan(dto);
    }

    /**
     * 批量操作
     * @param dto
     * @return
     */
    @PostMapping("batchBusinessWorkInfo")
    public IdmResDTO batchBusinessWorkInfo(@RequestBody BatchBusinessWorkDto dto){
        return workPlanInfoService.batchBusinessWorkInfo(dto);
    }

    /**
     * 获取已生成或预生成列表
     * @param dto
     * @return
     */
    @PostMapping("queryExecutionInfo")
    public IdmResDTO<IPage<PlanWorkExecutionInfoEntity>> queryExecutionInfo(@RequestBody QueryPlanExecutionDto dto){
        return workPlanInfoService.queryExecutionInfo(dto);
    }
}
