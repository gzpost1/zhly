package cn.cuiot.dmp.baseconfig.flow.flowable.listener;

import cn.cuiot.dmp.baseconfig.flow.entity.WorkInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.enums.WorkInfoEnums;
import cn.cuiot.dmp.baseconfig.flow.service.WorkInfoService;
import cn.cuiot.dmp.common.constant.MsgDataType;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.cuiot.dmp.baseconfig.flow.flowable.msg.MsgSendService;

import static cn.cuiot.dmp.baseconfig.flow.constants.CommonConstants.BUSINESS_STATUS_4;
import static cn.cuiot.dmp.baseconfig.flow.constants.CommonConstants.PROCESS_STATUS;


/**
 * @author LoveMyOrange
 * @Description 流程正常结束监听器
 * @create 2022-10-15 19:47
 */
@Slf4j
@Component
public class ProcessListener implements ExecutionListener {
    @Autowired
    private WorkInfoService workInfoService;
    @Autowired
    private MsgSendService msgSendService;


    @Override
    public void notify(DelegateExecution execution) {
        //更新流程实例运行状态
        execution.setVariable(PROCESS_STATUS, BUSINESS_STATUS_4);
        LambdaUpdateWrapper<WorkInfoEntity> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(WorkInfoEntity::getProcInstId, execution.getProcessInstanceId());
        updateWrapper.set(WorkInfoEntity::getStatus, WorkInfoEnums.FINISH.getCode());
        boolean update = workInfoService.update(updateWrapper);

        //发送消息
        msgSendService.sendProcess(execution.getProcessInstanceId(), MsgDataType.WORK_INFO_COMPLETED);
    }
}
