package cn.cuiot.dmp.baseconfig.flow.service;

import lombok.AllArgsConstructor;
import org.flowable.engine.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pengjian
 * @create 2024/6/13 16:07
 */
@AllArgsConstructor
public class AddMultiInstanceUserTaskService {


    protected ManagementService managementService;

    /**
     * 会签加签
     * @param taskId     当前操作taskId
     * @param assignee   待加签用户
     */
    public void addMultiInstanceUserTask(String taskId, String assignee) {
        //实例化自定义跳转Command类
        AddMultiInstanceUserTaskCmd addMultiInstanceUserTaskCmd = new AddMultiInstanceUserTaskCmd(taskId, assignee);
        //执行加签操作
        this.managementService.executeCommand(addMultiInstanceUserTaskCmd);
    }
}
