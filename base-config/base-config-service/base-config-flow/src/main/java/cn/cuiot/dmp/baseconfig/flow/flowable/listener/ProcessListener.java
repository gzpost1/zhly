package cn.cuiot.dmp.baseconfig.flow.flowable.listener;

import cn.cuiot.dmp.baseconfig.flow.entity.WorkInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.enums.WorkInfoEnums;
import cn.cuiot.dmp.baseconfig.flow.service.WorkInfoService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cn.cuiot.dmp.baseconfig.flow.constants.CommonConstants.BUSINESS_STATUS_4;
import static cn.cuiot.dmp.baseconfig.flow.constants.CommonConstants.PROCESS_STATUS;


/**
 * @Description 流程正常结束监听器
 * @author LoveMyOrange
 * @create 2022-10-15 19:47
 */
@Component
public class ProcessListener implements ExecutionListener {
    @Autowired
    private WorkInfoService workInfoService;

    @Override
    public void notify(DelegateExecution execution) {
        execution.setVariable(PROCESS_STATUS, BUSINESS_STATUS_4);
        LambdaUpdateWrapper<WorkInfoEntity> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(WorkInfoEntity::getProcInstId, Long.parseLong(execution.getProcessInstanceId()));
        updateWrapper.set(WorkInfoEntity::getStatus, WorkInfoEnums.FINISH.getCode());
        workInfoService.update(updateWrapper);
    }
}
