package cn.cuiot.dmp.baseconfig.flow.constants;

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

}
