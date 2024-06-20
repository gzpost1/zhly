package cn.cuiot.dmp.baseconfig.flow.constants;

import java.util.Arrays;
import java.util.List;

/**
 * @author pengjian
 * @create 2024/4/23 19:39
 */
public interface WorkOrderConstants {
    Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    Integer SC_OK_200 = 200;

    String FORM_VAR="formData";
    String PROCESS_STATUS="processStatus";
    String START_USER_INFO="startUser";
    String INITIATOR_ID="initiatorId";
    String BUSINESS_STATUS_1="1"; //正在处理


    String USER_ROOT = "root";

    /**
     * 评价节点类型
     */
    String COMMENT_NODE_TYPE="COMMENT";

    /**
     * 节点前缀
     */
    String NODE_START="node";

    /**
     * 用户任务
     */
    String USER_TASK ="userTask";

    /**
     * 工单来源 计划生成
     */
    Byte WORK_SOURCE_PLAN = 0;

    /**
     * 工单来源 自查
     */
    Byte WORK_SOURCE_MAKE = 1;

    /**
     * 节点类型集合
     */
    List<String> nodes = Arrays.asList("ROOT","APPROVAL","TASK");

    /**
     * 审批节点类型
     */
    String approvalNodeType = "APPROVAL";

}
