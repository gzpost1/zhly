package cn.cuiot.dmp.baseconfig.controller.app;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.baseconfig.flow.dto.AppAssigneeDto;
import cn.cuiot.dmp.baseconfig.flow.dto.StartProcessInstanceDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.app.*;
import cn.cuiot.dmp.baseconfig.flow.dto.app.query.PendingProcessQuery;
import cn.cuiot.dmp.baseconfig.flow.dto.app.query.RepairReportQuery;
import cn.cuiot.dmp.baseconfig.flow.dto.app.query.UserSubmitDataDto;
import cn.cuiot.dmp.baseconfig.flow.dto.app.query.WorkOrderSuperQuery;
import cn.cuiot.dmp.baseconfig.flow.dto.approval.QueryMyApprovalDto;
import cn.cuiot.dmp.baseconfig.flow.dto.vo.HandleDataVO;
import cn.cuiot.dmp.baseconfig.flow.dto.work.*;
import cn.cuiot.dmp.baseconfig.flow.enums.WorkSourceEnums;
import cn.cuiot.dmp.baseconfig.flow.service.AppWorkInfoService;
import cn.cuiot.dmp.baseconfig.flow.service.WorkInfoService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 2.3.【管理端-待办】
 * @author pengjian
 * @create 2024/5/27 11:33
 */

@RestController
@RequestMapping("/app/work")
public class AppWorkOrderController {

    @Autowired
    private AppWorkInfoService appWorkInfoService;

    @Autowired
    private WorkInfoService workInfoService;

    /**
     * 3.3代办工单-待审批
     * @return
     */
    @RequestMapping("queryPendProcess")
    public IdmResDTO<BaseDto> queryPendProcess(@RequestBody PendingProcessQuery query){
        return appWorkInfoService.queryMyNotApprocalCount(query);

    }

    /**
     * 3.3待处理
     * @param query
     * @return
     */
    @RequestMapping("queryPendProcessList")
    public IdmResDTO<List<BaseDto>> queryPendProcessList(@RequestBody PendingProcessQuery query){
        return appWorkInfoService.queryPendProcessList(query);
    }

    /**
     * 3.6.1启动工单-重新提交
     * @param startProcessInstanceDTO
     * @return
     */
    @PostMapping("start")
    @LogRecord(operationCode = "appStartWork", operationName = "app启动工单", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    @RequiresPermissions
    public IdmResDTO start(@RequestBody StartProcessInstanceDTO startProcessInstanceDTO){
        startProcessInstanceDTO.setWorkSource(WorkSourceEnums.WORK_SOURCE_MAKE.getCode());
        return appWorkInfoService.start(startProcessInstanceDTO);
    }



    /**
     * 工单监管--3.6.2列表查询
     * @return
     */
    @PostMapping("queryWorkOrderSuper")
    public IdmResDTO<IPage<AppWorkInfoDto>> queryWorkOrderSuper(@RequestBody WorkOrderSuperQuery query){
        return appWorkInfoService.queryWorkOrderSuper(query);
    }

    /**
     * 工单详情-3.6.2基本信息
     * @param dto
     * @return
     */
    @PostMapping("queryBasicWorkOrderDetailInfo")
    public IdmResDTO<WorkInfoDto> queryBasicWorkOrderDetailInfo(@RequestBody @Valid WorkProcInstDto dto){
        return appWorkInfoService.queryBasicWorkOrderDetailInfo(dto);
    }


    /**
     * 工单进度
     * @param
     * @return
     */
    @PostMapping("instanceInfo")
    public IdmResDTO<HandleDataVO>  instanceInfo(@RequestBody HandleDataDTO HandleDataDTO){
        return workInfoService.instanceInfo(HandleDataDTO);
    }



    /**
     * 工单监管-工单进度-转办获取当前任务的用户数
     * @param dto
     * @return
     */
    @PostMapping("queryTaskUserInfo")
    public IdmResDTO<List<TaskUserInfoDto>> queryTaskUserInfo(@RequestBody QueryTaskUserInfoDto dto){
        return appWorkInfoService.queryTaskUserInfo(dto);
    }
    /**
     * 工单监管-工单进度-转办
     * @param appTransferTaskDto
     * @return
     */
    @PostMapping("appTransfer")
    @RequiresPermissions
    @LogRecord(operationCode = "appTransfer", operationName = "app工单转办", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO appTransfer(@RequestBody AppTransferTaskDto appTransferTaskDto){
        return workInfoService.appTransfer(appTransferTaskDto);
    }


    /**
     * 工单监管-3.6.2 督办
     * @param
     * @return
     */
    @PostMapping("workOrderSuper")
    @RequiresPermissions
    public IdmResDTO workOrderSuper(@RequestBody @Valid ProcessBusinessDto processBusinessDto){
        return appWorkInfoService.workOrderSuper(processBusinessDto);
    }

    /**
     * 小程序 - 3.6.3我提交的
     * @param dto
     * @return
     */
    @PostMapping("queryAppMySubmitWorkInfo")
    public IdmResDTO<IPage<AppWorkInfoDto>> queryAppMySubmitWorkInfo(@RequestBody QueryMyApprovalDto dto){
        return appWorkInfoService.queryAppMySubmitWorkInfo(dto);
    }

    /**
     * 小程序-3.6.3我提交的基本信息
     * @param dto
     * @return
     */
    @PostMapping("queryMySubmitWorkOrderDetailInfo")
    public IdmResDTO<WorkInfoDto> queryMySubmitWorkOrderDetailInfo(@RequestBody WorkProcInstDto dto){
        return appWorkInfoService.queryMySubmitWorkOrderDetailInfo(dto);
    }

    /**
     * 小程序-3.6.3撤回
     * @param businessDto
     * @return
     */
    @PostMapping("revokeWorkOrder")
    @RequiresPermissions()
    @LogRecord(operationCode = "revokeWorkOrder", operationName = "app工单撤回", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO revokeWorkOrder(@RequestBody ProcessBusinessDto businessDto){
        return appWorkInfoService.revokeWorkOrder(businessDto);
    }

    /**
     * 小程序 - 3.6.5 我处理的-查询节点类型
     * @param
     * @return
     */
    @PostMapping("queryNodeType")
    public IdmResDTO<NodeTypeDto> queryNodeType(@RequestBody PendingProcessQuery query){
       return appWorkInfoService.queryNodeType(query);
    }


    /**
     * 3.6.4-我审批的-列表查询
     * @param query
     * @return
     */
    @PostMapping("queryMyApprove")
    public IdmResDTO<IPage<AppWorkInfoDto>> queryMyApprove(@RequestBody WorkOrderSuperQuery query){
        return appWorkInfoService.queryMyApprove(query);
    }

    /**
     * 3.6.4-我审批的-基本信息
     * @param dto
     * @return
     */
    @PostMapping("queryMyapproveBasicInfo")
    public IdmResDTO<WorkInfoDto> queryMyapproveBasicInfo(@RequestBody WorkProcInstDto dto){
        return appWorkInfoService.queryBasicInfo(dto);
    }

    /**
     * 3.6.4-我的审批-审批同意
     * @param approvalDto
     * @return
     */
    @PostMapping("approvalConsent")
    @RequiresPermissions()
    @LogRecord(operationCode = "approvalConsent", operationName = "app审批同意", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO approvalConsent(@RequestBody @Valid ApprovalDto approvalDto){
        return appWorkInfoService.approvalConsent(approvalDto);
    }

    /**
     * 3.6.4-我的审批-审批驳回
     * @param approvalDto
     * @return
     */
    @PostMapping("approvalRejection")
    @RequiresPermissions()
    @LogRecord(operationCode = "approvalRejection", operationName = "app审批驳回", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO approvalRejection(@RequestBody @Valid ApprovalDto approvalDto){
        return appWorkInfoService.approvalRejection(approvalDto);
    }
    /**
     * 小程序 - 3.6.5 我处理的-列表查询
     * @param
     * @return
     */
    @PostMapping("queryMyHandleInfo")
    public IdmResDTO<IPage<AppWorkInfoDto>> queryMyHandleInfo(@RequestBody WorkOrderSuperQuery query){
        return appWorkInfoService.queryMyHandleInfo(query);
    }



    /**
     * 我处理的-3.6.5 基本信息
     * 因为此处需要返回配置的操作所以单独提出来
     * @param dto
     * @return
     */
    @PostMapping("queryBasicInfo")
    public IdmResDTO<WorkInfoDto> queryBasicInfo(@RequestBody WorkProcInstDto dto){
        return appWorkInfoService.queryBasicInfo(dto);
    }
    /**
     * 3.6.5-我处理的-去处理
     * @param dto
     * @return
     */
    @PostMapping("queryUserSubmitData")
    public IdmResDTO<ProcessResultDto> queryUserSubmitData(@RequestBody @Valid UserSubmitDataDto dto) throws FlowException {
        return appWorkInfoService.queryUserSubmitData(dto);
    }
    /**
     * 3.6.5更新或者保存用户提交的对象数据
     * @param commitId
     * @return
     */
    @PostMapping("saveOrUpdateSubmitData")
    @LogRecord(operationCode = "saveOrUpdateSubmitData", operationName = "app完成对象任务", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    @RequiresPermissions()
    public IdmResDTO saveOrUpdateSubmitData(@RequestBody @Valid CommitProcessDto commitId){
        return appWorkInfoService.saveSubmitData(commitId);
    }

    /**
     * 3.6.5完成任务
     * @param taskDto
     * @return
     */
    @PostMapping("completeTask")
    @LogRecord(operationCode = "completeTask", operationName = "app完成任务", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO  completeTask(@RequestBody CompleteTaskDto taskDto){
        return appWorkInfoService.completeTask(taskDto);
    }

    /**
     *小程序- 退回
     * @param
     * @return
     */
    @PostMapping("clientBack")
    @RequiresPermissions()
    @LogRecord(operationCode = "clientBack", operationName = "app任务退回", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO clientBack(@RequestBody @Valid ClientOperationDto operationDto){
        return appWorkInfoService.clientBack(operationDto);
    }

    /**
     * 小程序-转办
     * @param
     * @return
     */
    @PostMapping("assignee")
    @RequiresPermissions
    @LogRecord(operationCode = "appAssignee", operationName = "app转办", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO appAssignee(@RequestBody AppAssigneeDto assigneeDto){
        return appWorkInfoService.appAssignee(assigneeDto);
    }

    /**
     * 终止
     * @param operationDto
     * @return
     */
    @PostMapping("clientCloseFlow")
    @RequiresPermissions
    @LogRecord(operationCode = "clientCloseFlow", operationName = "app终止", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO clientCloseFlow(@RequestBody ClientOperationDto operationDto){
        return appWorkInfoService.clientCloseFlow(operationDto);
    }


    /**
     * 挂起
     * @param operationDto
     * @return
     */
    @PostMapping("clientBusinessPending")
    @RequiresPermissions
    @LogRecord(operationCode = "clientBusinessPending", operationName = "app挂起", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO clientBusinessPending(@RequestBody ClientOperationDto operationDto){
        return appWorkInfoService.clientBusinessPending(operationDto);
    }

    /**
     * 评论
     * @return
     */
    @PostMapping("clientComment")
    public IdmResDTO clientComment(@RequestBody TaskBusinessDto dto){
        return appWorkInfoService.clientComment(dto);
    }


    /**
     * 3.6.6-抄送我的-列表查询
     * @param query
     * @return
     */
    @PostMapping("queryMyMake")
    public IdmResDTO<IPage<AppWorkInfoDto>> queryMyMake(@RequestBody WorkOrderSuperQuery query){
        return appWorkInfoService.queryMyMake(query);
    }

    /**
     * 3.11.6-报事报修-重新提交
     * @param startProcessInstanceDTO
     * @return
     */
    @PostMapping("reportRepairsStart")
    @RequiresPermissions("allowUserType=client")
    public IdmResDTO reportRepairsStart(@RequestBody StartProcessInstanceDTO startProcessInstanceDTO){
        return appWorkInfoService.reportRepairsStart(startProcessInstanceDTO);
    }

    /**
     * 3.11.5-我的报修
     * @param query
     * @return
     */
    @PostMapping("queryReportRepairs")
    @RequiresPermissions("allowUserType=client")
    public IdmResDTO<IPage<RepairReportDto>> queryReportRepairs(@RequestBody @Valid RepairReportQuery query){
        return appWorkInfoService.queryReportRepairs(query);
    }

    /**
     * 3.11.5-工单详情
     * @param dto
     * @return
     */
    @PostMapping("queryRepairReportDetail")
    public IdmResDTO<RepairReportDetailDto> queryRepairReportDetail(@RequestBody @Valid ProcessBusinessDto dto){
        return appWorkInfoService.queryRepairReportDetail(dto);
    }

    /**
     * 3.11.5-评价
     * @param taskDto
     * @return
     */
    @PostMapping("evaluate")
    public IdmResDTO  evaluate(@RequestBody CompleteTaskDto taskDto){
        return appWorkInfoService.evaluate(taskDto);
    }
}
