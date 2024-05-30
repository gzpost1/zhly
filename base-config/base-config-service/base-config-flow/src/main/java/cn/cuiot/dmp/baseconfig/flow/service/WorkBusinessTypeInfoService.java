package cn.cuiot.dmp.baseconfig.flow.service;

import cn.cuiot.dmp.baseconfig.flow.entity.WorkBusinessTypeInfoEntity;
import cn.cuiot.dmp.baseconfig.flow.enums.TimeLimitHandleEnums;
import cn.cuiot.dmp.baseconfig.flow.enums.WorkBusinessEnums;
import cn.cuiot.dmp.baseconfig.flow.mapper.WorkBusinessTypeInfoMapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.UserTask;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @author pengjian
 * @since 2024-04-26
 */
@Service
public class WorkBusinessTypeInfoService extends ServiceImpl<WorkBusinessTypeInfoMapper, WorkBusinessTypeInfoEntity> {

    /**
     * 保存超时/挂起信息
     *
     * @param task
     * @param userTask
     * @param workBusinessEnums
     */
    public void saveBusinessInfo(Task task, UserTask userTask, WorkBusinessEnums workBusinessEnums, String comment) {
        WorkBusinessTypeInfoEntity workBusinessTypeInfoEntity = new WorkBusinessTypeInfoEntity();
        workBusinessTypeInfoEntity.setId(IdWorker.getId());
        workBusinessTypeInfoEntity.setTaskId(Long.valueOf(task.getId()));
        workBusinessTypeInfoEntity.setProcInstId(Long.valueOf(task.getProcessInstanceId()));

        workBusinessTypeInfoEntity.setUserId(2L);

        workBusinessTypeInfoEntity.setStartTime(new Date());
        workBusinessTypeInfoEntity.setBusinessType(workBusinessEnums.getCode());

        if (Objects.equals(workBusinessEnums, WorkBusinessEnums.SUSPEND)) {
            if (StringUtils.isNotBlank(comment)) {
                workBusinessTypeInfoEntity.setComments(comment);
            } else {
                workBusinessTypeInfoEntity.setComments(TimeLimitHandleEnums.TO_SUSPEND.getProcessComment());
            }
        }
        workBusinessTypeInfoEntity.setNode(userTask.getId());
        workBusinessTypeInfoEntity.setUserId(task.getAssignee() == null ? null : Long.valueOf(task.getAssignee()));
        this.save(workBusinessTypeInfoEntity);
    }

}
