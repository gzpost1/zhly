package cn.cuiot.dmp.baseconfig.flow.flowable.listener;

import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.ChildNode;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.Properties;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import cn.cuiot.dmp.baseconfig.flow.enums.AssigneeTypeEnums;
import cn.cuiot.dmp.baseconfig.flow.enums.WorkBusinessEnums;
import cn.cuiot.dmp.baseconfig.flow.service.WorkBusinessTypeInfoService;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.cuiot.dmp.baseconfig.flow.constants.CommonConstants.START_USER_INFO;
import static cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants.*;
import static cn.cuiot.dmp.baseconfig.flow.utils.BpmnModelUtils.getChildNode;


/**
 * @author LoveMyOrange
 * @Description: 任务启动时的监听器
 * @create 2022-10-15 13:35
 * @desc 本项目精髓代码实现-> 所有属性都在内存中取得,且该类最重要的一点就是  下面有一个if判断,防止人员重复解析--->
 */
@Component
public class CounterSignListener implements ExecutionListener {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private TaskService taskService;

    @Autowired
    private WorkBusinessTypeInfoService workBusinessTypeInfoService;

    @Override
    public void notify(DelegateExecution execution) {
        String currentActivityId = execution.getCurrentActivityId();
        Process mainProcess = repositoryService.getBpmnModel(execution.getProcessDefinitionId()).getMainProcess();
        UserTask userTask = (UserTask) mainProcess.getFlowElement(currentActivityId);

        String dingDing = mainProcess.getAttributeValue(FLOWABLE_NAME_SPACE, FLOWABLE_NAME_SPACE_NAME);
        JSONObject jsonObject = JSONObject.parseObject(dingDing, new TypeReference<JSONObject>() {
        });
        String processJson = jsonObject.getString(VIEW_PROCESS_JSON_NAME);
        ChildNode childNode = JSONObject.parseObject(processJson, new TypeReference<ChildNode>() {
        });
        List<String> assigneeList = new ArrayList<>();
        String variable = currentActivityId + ASSIGNEE_LIST_SUFFIX;
        List usersValue = (List) execution.getVariable(variable);
        if (usersValue == null) {
            ChildNode currentNode = getChildNode(childNode, currentActivityId);
            if (currentNode == null) {
                throw new BusinessException(ResultCode.PARAM_CANNOT_NULL, "查找审批人失败,请联系管理员重试");
            }
            Properties props = currentNode.getProps();
            String assignedType = props.getAssignedType();
            Map<String, Object> nobody = props.getNobody();

            if (Objects.nonNull(props.getTimeLimit()) &&
                    Objects.nonNull(props.getTimeLimit().getHandler()) &&
                    Objects.nonNull(props.getTimeLimit().getHandler().getType())
            ) {
                execution.setVariable(TIME_HANDLER_TYPE, props.getTimeLimit().getHandler().getType());
            }

            if (AssigneeTypeEnums.ASSIGN_USER.getTypeName().equals(assignedType)) {
                List<UserInfo> assignedUser = props.getAssignedUser();
                for (UserInfo userInfo : assignedUser) {
                    assigneeList.add(userInfo.getId());
                }
            } else if (AssigneeTypeEnums.SELF_SELECT.getTypeName().equals(assignedType)) {
                //发起人自己选择
                List<String> assigneeUsers = (List<String>) execution.getVariable(currentActivityId);
                if (assigneeUsers != null) {
                    assigneeList.addAll(assigneeUsers);
                }
            } else if (AssigneeTypeEnums.COMPLETE_SELECT.getTypeName().equals(assignedType)) {
                //完成人自己选择
                List<String> assigneeUsers = (List<String>) execution.getVariable(currentActivityId);
                if (assigneeUsers != null) {
                    assigneeList.addAll(assigneeUsers);
                }
            } else if (AssigneeTypeEnums.LEADER_TOP.getTypeName().equals(assignedType)) {
                /**
                 endCondition: "TOP", //结束条件 TOP 直到最上级主管、
                 level 指定不超过多少级主管
                 endLevel: 1, //指定级别主管审批后结束本节点
                 */
//                Map<String, Object> leaderTop = props.getLeaderTop();
//                String endCondition = MapUtil.getStr(leaderTop, "endCondition");
//                Integer endLevel = MapUtil.getInt(leaderTop, "endLevel");
//                Integer level = MapUtil.getInt(leaderTop, "level");
//                UserService userService = SpringContextHolder.getBean(UserService.class);
//                String startUserJson = execution.getVariable(START_USER_INFO, String.class);
//                UserInfo userInfo = JSONObject.parseObject(startUserJson, new TypeReference<UserInfo>() {
//                });
//                String id = userInfo.getId();
//                Users users = userService.getById(id);
//                //todo 因为此项目没有级联结构,自行递归获取1~10级某一级,不会有人这都不会吧,嘤嘤嘤
//                Integer admin = users.getAdmin();
//                if(admin!=null){
//                    assigneeList.add(admin+"");
//                }
            } else if (AssigneeTypeEnums.LEADER.getTypeName().equals(assignedType)) {
//                Map<String, Object> leaderTop = props.getLeader();
//                Integer level = MapUtil.getInt(leaderTop, "level");
//                UserService userService = SpringContextHolder.getBean(UserService.class);
//                String startUserJson = execution.getVariable(START_USER_INFO, String.class);
//                UserInfo userInfo = JSONObject.parseObject(startUserJson, new TypeReference<UserInfo>() {
//                });
//
//                String id = userInfo.getId();
//                Users users = userService.getById(id);
//                //todo 因为此项目没有级联结构,自行递归获取1~10级某一级,不会有人这都不会吧,嘤嘤嘤
//                Integer admin = users.getAdmin();
//                if(admin!=null){
//                    assigneeList.add(admin+"");
//                }
            } else if (AssigneeTypeEnums.ROLE.getTypeName().equals(assignedType)) {
                //指定角色
                //todo 等待接口
            } else if (AssigneeTypeEnums.DEPT.getTypeName().equals(assignedType)) {
                //指定部门
                //todo 等待接口
            } else if (AssigneeTypeEnums.SELF.getTypeName().equals(assignedType)) {
                String startUserJson = execution.getVariable(START_USER_INFO, String.class);
                UserInfo userInfo = JSONObject.parseObject(startUserJson, new TypeReference<UserInfo>() {
                });
                assigneeList.add(userInfo.getId());
            } else if (AssigneeTypeEnums.FORM_USER.getTypeName().equals(assignedType)) {
                String formUser = props.getFormUser();
                List<JSONObject> assigneeUsers = execution.getVariable(formUser, List.class);
                if (assigneeUsers != null) {
                    for (JSONObject assigneeUser : assigneeUsers) {
                        assigneeList.add(assigneeUser.getString(VIEW_ID_NAME));
                    }
                }

            }

            if (CollUtil.isEmpty(assigneeList)) {
                String handler = MapUtil.getStr(nobody, ASSIGNEE_NULL_ACTION_NAME);
                if (TO_PASS_ACTION.equals(handler)) {
                    assigneeList.add(DEFAULT_NULL_ASSIGNEE);
                    execution.setVariable(variable, assigneeList);
                } else if (TO_REFUSE_ACTION.equals(handler)) {
                    execution.setVariable(AUTO_REFUSE_STR, Boolean.TRUE);
                    assigneeList.add(DEFAULT_NULL_ASSIGNEE);
                    execution.setVariable(variable, assigneeList);
                } else if (TO_ADMIN_ACTION.equals(handler)) {
                    assigneeList.add(DEFAULT_ADMIN_ASSIGNEE);
                    execution.setVariable(variable, assigneeList);
                } else if (TO_USER_ACTION.equals(handler)) {
                    Object assignedUserObj = nobody.get(VIEW_ASSIGNEE_USER_NAME);
                    if (assignedUserObj != null) {
                        List<JSONObject> assignedUser = (List<JSONObject>) assignedUserObj;
                        if (assignedUser.size() > 0) {
                            for (JSONObject object : assignedUser) {
                                assigneeList.add(object.getString(VIEW_ID_NAME));
                            }
                            execution.setVariable(variable, assigneeList);
                        } else {
                            assigneeList.add(DEFAULT_NULL_ASSIGNEE);
                            execution.setVariable(variable, assigneeList);
                        }

                    }

                } else if (TO_SUSPEND.equals(handler)) {
                    //找不到审批人且是自动挂起
                    assigneeList.add(DEFAULT_NULL_ASSIGNEE);
                    execution.setVariable(variable, assigneeList);

                    List<Task> list = taskService.createTaskQuery()
                            .processDefinitionId(execution.getProcessDefinitionId())
                            .taskDefinitionKey(userTask.getId())
                            .list();

                    //挂起流程
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (Task task : list) {
                            taskService.addComment(task.getId(), execution.getProcessInstanceId(), BUSINESS_PENDING, "处理人为空，系统自动挂起");

                            //保存超时信息和挂起信息
                            workBusinessTypeInfoService.saveBusinessInfo(task, userTask, WorkBusinessEnums.SUSPEND, "处理人为空，系统自动挂起");
                        }
                    }
                } else {
                    throw new BusinessException(ResultCode.PARAM_CANNOT_NULL, "找不到审批人,请检查配置!!!");
                }
            } else {
                execution.setVariable(variable, assigneeList);
            }
        } else {
        }
    }
}
