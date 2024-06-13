package cn.cuiot.dmp.baseconfig.controller.workorder;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.baseconfig.flow.constants.WorkOrderConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.StartProcessInstanceDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.work.BatchBusinessDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.HandleDataDTO;
import cn.cuiot.dmp.baseconfig.flow.service.WorkInfoService;
import cn.cuiot.dmp.common.constant.IdmResDTO;

import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    @RequiresPermissions
    @LogRecord(operationCode = "startWork", operationName = "启动工单", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO start(@RequestBody StartProcessInstanceDTO startProcessInstanceDTO){
        startProcessInstanceDTO.setWorkSource(WorkOrderConstants.WORK_SOURCE_MAKE);
        return workInfoService.start(startProcessInstanceDTO);
    }


    /**
     * 完成
     * @param handleDataDTO
     * @return
     */
    @PostMapping("complete")
    @RequiresPermissions
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
    @RequiresPermissions
    @LogRecord(operationCode = "agree", operationName = "审批同意", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO agree(@RequestBody BatchBusinessDto handleDataDTO){
        return workInfoService.agree(handleDataDTO);
    }
    /**
     * 转办
     * @param handleDataDTO
     * @return
     */
    @PostMapping("assignee")
    @RequiresPermissions
    @LogRecord(operationCode = "assignee", operationName = "转办", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO assignee(@RequestBody HandleDataDTO handleDataDTO){
        return workInfoService.assignee(handleDataDTO);
    }

    /**
     * 驳回/拒绝
     * @param handleDataDTO
     * @return
     */
    @PostMapping("refuse")
    @RequiresPermissions
    @LogRecord(operationCode = "refuse", operationName = "拒绝", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO refuse(@RequestBody BatchBusinessDto handleDataDTO){

        return workInfoService.refuse(handleDataDTO);
    }

    /**
     * 终止
     * @param handleDataDTO
     * @return
     */
    @PostMapping("closeFlow")
    @RequiresPermissions
    @LogRecord(operationCode = "closeFlow", operationName = "终止", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO closeFlow(@RequestBody BatchBusinessDto handleDataDTO){
        return workInfoService.closeFlow(handleDataDTO);
    }

    /**
     * 评论和督办
     * @param handleDataDTO
     * @return
     */
    @PostMapping("commentAndSuper")
    @RequiresPermissions
    @LogRecord(operationCode = "commentAndSuper", operationName = "评论和督办", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO commentAndSuper(@RequestBody BatchBusinessDto handleDataDTO){
        return workInfoService.commentAndSuper(handleDataDTO);
    }

    /**
     * 回退
     * @param handleDataDTO
     * @return
     */
    @PostMapping("rollback")
    @RequiresPermissions
    @LogRecord(operationCode = "rollback", operationName = "回退", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO rollback(@RequestBody HandleDataDTO handleDataDTO){
        return workInfoService.rollback(handleDataDTO);
    }

    /**
     * 挂起
     * @param handleDataDTO
     * @return
     */
    @PostMapping("businessPending")
    @RequiresPermissions
    @LogRecord(operationCode = "businessPending", operationName = "挂起", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO businessPending(@RequestBody BatchBusinessDto handleDataDTO){
        return workInfoService.businessPending(handleDataDTO);
    }

}
