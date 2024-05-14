package cn.cuiot.dmp.baseconfig.flow.constants;

import io.lettuce.core.StrAlgoArgs;

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
    String DEFAULT_ADMIN_ASSIGNEE="381496";
    String AUTO_REFUSE_STR="autoRefuse";
    String FLOWABLE_NAME_SPACE_NAME="yunjintech";
    String FLOWABLE_NAME_SPACE="http://flowable.org/bpmn";
    String VIEW_PROCESS_JSON_NAME="processJson";
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
     * 评论
     */
    String COMMENTS_COMMENT="comments";

    /**
     * 督办
     */
    String BUSINESS_SUPERVISION="supervision";

    /**
     * 启动流程
     */
    String START_PROCESS = "startProcess";


    /**
     * 转办
     */
    Byte BUSINESS_TRANSFER=5;

    /**
     * 拒绝 invokeTarget
     */
    String BUSINESS_REFUSE = "businessRefuse";

    String JOB_INVOKETARGET="createPlanWork";

    /**
     *回退
     */
    Byte BUSINESS_BACK = 8;

    /**
     * 挂起
     */
    String BUSINESS_PENDING = "businessPending";

    /**
     * 评论
     */
    Byte BUSINESS_TYPE_COMMENT = 2;

    /**
     * 督办
     */
    Byte BUSINESS_TYPE_SUPER = 3;

    /**
     * 拒绝refuse
     */
    Byte BUSINESS_TYPE_REFUSR =7;

    /**
     * 超时
     */
    Byte BUSINESS_TYPE_TIME_OUT = 1;

    /**
     * 挂起
     */
    Byte BUSINESS_BYPE_PENDING=1;

    /**
     * 定时任务状态
     */
    Byte JOB_STATUS=1;

    /**
     * 终止
     */
    Byte BUSINESS_BYTE_CLOSE =4;

    String ATTRIBUTE_NAME_SPACE="http://flowable.org/bpmn";

    /**
     * 超时处理类型
     */
    String TIME_HANDLER_TYPE = "timeHandler";

    String ATTRIBUTE_NAME="DingDing";
    /**
     * 返回结果
     */
    Byte RESULT_1 =1;

    Byte RESULT_0 = 0;
    /**
     * 获取待已审批列表
     */
    Integer QUERY_TYPE_APPROVAL = 1;
}
