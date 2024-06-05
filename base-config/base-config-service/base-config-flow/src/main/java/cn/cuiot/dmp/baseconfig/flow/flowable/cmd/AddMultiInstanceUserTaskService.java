/**
 * Copyright (c) 2023,人民邮电出版社,《深入Activiti流程引擎：核心原理与高阶实战》
 * All rights reserved.
 * 本案例代码节选自于贺波、胡海琴和刘晓鹏三位老师的著作《深入Activiti流程引擎：核心原理与高阶实战》。
 * 若需获取本书的完整案例及代码，请访问人民邮电出版社该书首页进行下载，网址为：https://www.epubit.com/bookDetails?id=UBd189db7e65bd。
 */

package cn.cuiot.dmp.baseconfig.flow.flowable.cmd;

import lombok.AllArgsConstructor;
import org.flowable.engine.ManagementService;

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
