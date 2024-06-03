package cn.cuiot.dmp.baseconfig.controller.app;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.baseconfig.flow.constants.WorkOrderConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.StartProcessInstanceDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.app.AppTransferTaskDto;
import cn.cuiot.dmp.baseconfig.flow.dto.app.BaseDto;
import cn.cuiot.dmp.baseconfig.flow.dto.app.AppWorkInfoDto;
import cn.cuiot.dmp.baseconfig.flow.dto.app.query.PendingProcessQuery;
import cn.cuiot.dmp.baseconfig.flow.dto.approval.QueryMyApprovalDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.QueryTaskUserInfoDto;
import cn.cuiot.dmp.baseconfig.flow.dto.work.TaskUserInfoDto;
import cn.cuiot.dmp.baseconfig.flow.service.AppWorkInfoService;
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
 * 2.3.【管理端-待办】
 * @author pengjian
 * @create 2024/5/27 11:33
 */

@RestController
@RequestMapping("/app/work")
public class PendingProcessController {

    @Autowired
    private AppWorkInfoService appWorkInfoService;

    @Autowired
    private WorkInfoService workInfoService;

    /**
     * 代办工单-待审批
     * @return
     */
    @RequestMapping("queryPendProcess")
    public IdmResDTO<BaseDto> queryPendProcess(@RequestBody PendingProcessQuery query){
        return appWorkInfoService.queryMyNotApprocalCount(query);

    }

    /**
     * 待处理
     * @param query
     * @return
     */
    @RequestMapping("queryPendProcessList")
    public IdmResDTO<List<BaseDto>> queryPendProcessList(@RequestBody PendingProcessQuery query){
        return appWorkInfoService.queryPendProcessList(query);
    }

    /**
     * 启动工单
     * @param startProcessInstanceDTO
     * @return
     */
    @PostMapping("start")
    @LogRecord(operationCode = "appStartWork", operationName = "app启动工单", serviceType = ServiceTypeConst.WORK_BASE_CONFIG)
    public IdmResDTO start(@RequestBody StartProcessInstanceDTO startProcessInstanceDTO){
        startProcessInstanceDTO.setWorkSource(WorkOrderConstants.WORK_SOURCE_MAKE);
        return workInfoService.start(startProcessInstanceDTO);
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
     * 单监管-工单进度-转办
     * @param appTransferTaskDto
     * @return
     */
    @PostMapping("appTransfer")
    public IdmResDTO appTransfer(@RequestBody AppTransferTaskDto appTransferTaskDto){
        return workInfoService.appTransfer(appTransferTaskDto);
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



}
