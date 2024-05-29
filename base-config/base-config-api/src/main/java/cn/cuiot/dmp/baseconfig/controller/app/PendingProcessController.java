package cn.cuiot.dmp.baseconfig.controller.app;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.baseconfig.flow.constants.WorkOrderConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.StartProcessInstanceDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.app.BaseDto;
import cn.cuiot.dmp.baseconfig.flow.dto.app.query.PendingProcessQuery;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import cn.cuiot.dmp.baseconfig.flow.entity.UserEntity;
import cn.cuiot.dmp.baseconfig.flow.service.AppWorkInfoService;
import cn.cuiot.dmp.baseconfig.flow.service.WorkInfoService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
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
@RequestMapping("/app/pend")
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

}
