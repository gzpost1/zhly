package cn.cuiot.dmp.baseconfig.flow.constants;

import io.lettuce.core.StrAlgoArgs;

import java.util.List;

/**
 * @author LoveMyOrange
 * @create 2022-10-10 17:40
 */
public interface WorkFlowConstants {
    String PROCESS_PREFIX="Flowable";
    String START_EVENT_ID="startEventNode";
    String END_EVENT_ID="endEventNode";
    String EXPRESSION_CLASS="exUtils.";
    String DEFAULT_NULL_ASSIGNEE="100000000000";
    String DEFAULT_NULL_SUSPEND="1000000000001111";
    String DEFAULT_ADMIN_ASSIGNEE="381496";
    String AUTO_REFUSE_STR="autoRefuse";
    String FLOWABLE_NAME_SPACE_NAME="yunjintech";
    String FLOWABLE_NAME_SPACE="http://flowable.org/bpmn";
    String VIEW_PROCESS_JSON_NAME="processJson";
    /**
     * 任务信息
     */
    String TASK_CONFIG="taskConfig";

    String VIEW_FLOW_CONFIG = "flowconfig";
    String VIEW_ASSIGNEE_USER_NAME="assignedUser";
    String VIEW_ID_NAME="id";
    String ASSIGNEE_LIST_SUFFIX="assigneeList";
    String ASSIGNEE_NULL_ACTION_NAME="handler";
    String TO_PASS_ACTION="TO_PASS";
    String TO_REFUSE_ACTION="TO_REFUSE";

    String TO_ADMIN_ACTION="TO_ADMIN";
    String TO_USER_ACTION="TO_USER";
    String TO_SUSPEND="TO_SUSPEND";
    /**
     * 审批通过
     */
    String OPINION_COMMENT="opinion";
    String OPTION_COMMENT="option";
    String SIGN_COMMENT="sign";
    /**
     * 挂起
     */
    Byte BUSINESS_BYPE_PENDING=0;
    /**
     * 超时处理类型
     */
    String TIME_HANDLER_TYPE = "timeHandler";
    /**
     * 最后一天
     */
    String LAST_DAT="32";
    /**
     * 返回结果
     */
    Byte RESULT_1 =1;

    Byte RESULT_0 = 0;

    Byte PARAM_2 = 2;

}
