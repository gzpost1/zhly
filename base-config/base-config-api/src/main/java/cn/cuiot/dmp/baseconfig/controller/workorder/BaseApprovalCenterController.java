package cn.cuiot.dmp.baseconfig.controller.workorder;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.baseconfig.flow.constants.WorkOrderConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.QueryApprovalInfoDto;
import cn.cuiot.dmp.baseconfig.flow.dto.StartProcessInstanceDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.work.BatchBusinessDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.HandleDataDTO;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.service.WorkInfoService;
import cn.cuiot.dmp.common.constant.IdmResDTO;

import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 操作按钮
 * @author pengjian
 * @create 2024/4/23 16:12
 */
@RestController
@RequestMapping("/baseconfig/workorder")
public class BaseApprovalCenterController extends BaseController {

    @Autowired
    private WorkInfoService workInfoService;

    /**
     * 启动流程
     * @param startProcessInstanceDTO
     * @return
     */
    @PostMapping("start")
    @LogRecord(operationCode = "startWork", operationName = "启动工单", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO start(@RequestBody StartProcessInstanceDTO startProcessInstanceDTO){
        startProcessInstanceDTO.setWorkSource(WorkOrderConstants.WORK_SOURCE_MAKE);
        return workInfoService.start(startProcessInstanceDTO);
    }

    /**
     *
     * @param dto
     * @return
     */
    public IdmResDTO<IPage<WorkInfoEntity>> processList(@RequestBody QueryApprovalInfoDto dto){

        return workInfoService. processList(dto);
    }


    /**
     * 审批同意
     * @param handleDataDTO
     * @return
     */
    @PostMapping("complete")
    @LogRecord(operationCode = "complete", operationName = "完成任务", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO complete(@RequestBody HandleDataDTO handleDataDTO){
        return workInfoService.complete(handleDataDTO);
    }


    /**
     * 审批同意
     * @param handleDataDTO
     * @return
     */
    @PostMapping("agree")
    @LogRecord(operationCode = "agree", operationName = "审批同意", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO agree(@RequestBody HandleDataDTO handleDataDTO){
        return workInfoService.agree(handleDataDTO);
    }
    /**
     * 转办
     * @param handleDataDTO
     * @return
     */
    @PostMapping("assignee")
    public IdmResDTO assignee(@RequestBody HandleDataDTO handleDataDTO){
        return workInfoService.assignee(handleDataDTO);
    }

    /**
     * 驳回
     * @param handleDataDTO
     * @return
     */
    @PostMapping("refuse")
    public IdmResDTO refuse(@RequestBody HandleDataDTO handleDataDTO){

        return workInfoService.refuse(handleDataDTO);
    }

    /**
     * 终止
     * @param handleDataDTO
     * @return
     */
    @PostMapping("closeFlow")
    public IdmResDTO closeFlow(@RequestBody BatchBusinessDto handleDataDTO){
        return workInfoService.closeFlow(handleDataDTO);
    }

    /**
     * 评论和督办
     * @param handleDataDTO
     * @return
     */
    @PostMapping("commentAndSuper")
    public IdmResDTO commentAndSuper(@RequestBody BatchBusinessDto handleDataDTO){
        return workInfoService.commentAndSuper(handleDataDTO);
    }

    /**
     * 回退
     * @param handleDataDTO
     * @return
     */
    @PostMapping("rollback")
    public IdmResDTO rollback(@RequestBody HandleDataDTO handleDataDTO){
        return workInfoService.rollback(handleDataDTO);
    }

    /**
     * 挂起
     * @param handleDataDTO
     * @return
     */
    @PostMapping("businessPending")
    public IdmResDTO businessPending(@RequestBody BatchBusinessDto handleDataDTO){
        return workInfoService.businessPending(handleDataDTO);
    }

}
