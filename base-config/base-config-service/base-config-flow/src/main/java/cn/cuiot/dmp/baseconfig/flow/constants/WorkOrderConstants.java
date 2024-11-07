package cn.cuiot.dmp.baseconfig.flow.constants;

import java.util.Arrays;
import java.util.List;

/**
 * @author pengjian
 * @create 2024/4/23 19:39
 */
public interface WorkOrderConstants {
    String FORM_VAR="formData";
    String PROCESS_STATUS="processStatus";
    String START_USER_INFO="startUser";
    String INITIATOR_ID="initiatorId";
    String BUSINESS_STATUS_1="1"; //正在处理
    /**
     * 开始系欸但
     */
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
     * 工单来源 计划生成
     */
    Byte WORK_SOURCE_PLAN = 0;

    /**
     * 节点类型集合
     */
    List<String> nodes = Arrays.asList("ROOT","APPROVAL","TASK");

    /**
     * 审批节点类型
     */
    String approvalNodeType = "APPROVAL";

    /**
     * 任务节点类型
     */
    String taskNode="TASK";

    /**
     * 导出页面
     */
     Integer pageSize = 10000;
    /**
     * 导出数据
     */
    Long PAGE_SIZE =2000L;
    /**
     * 未生效
     */
     String NOT_EFFECTIVE="未开始";

    String EFFECTIVE ="进行中";

    String END = "已结束";

    /**
     * 撤回
     */
    Byte  REVOKE = 11;
}
