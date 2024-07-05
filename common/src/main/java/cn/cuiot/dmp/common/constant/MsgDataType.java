package cn.cuiot.dmp.common.constant;//	模板

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/12 17:44
 */
public class MsgDataType {

    /**
     * 公告
     */
    public final static String NOTICE = "notice";

    /**
     * 线索分配通知：分配线索后，向被分配人发送通知
     */
    public final static String CLUE_DISTRIBUTE = "clueDistribute";

    /**
     * 工单完成通知：工单完成，向发起人发送通知
     */
    public final static String WORK_INFO_COMPLETED = "workInfoCompleted";


    /**
     * 工单被终止通知：工单被终止，包括手动终止和超时终止，向发起人发送通知
     */
    public final static String WORK_INFO_CANCEL = "workInfoCancel";

    /**
     * 驳回
     */
    public final static String WORK_INFO_TURNDOWN = "workInfoTurndown";

    /**
     * 待审批
     */
    public final static String WORK_INFO_APPROVAL = "workInfoApproval";

    /**
     * 待处理
     */
    public final static String WORK_INFO_PROCESS = "workInfoProcess";

    /**
     * 待评价
     */
    public final static String WORK_INFO_EVALUATE = "workInfoEvaluate";

    /**
     * 退回发起
     */
    public final static String WORK_INFO_RETURN_INITIATE = "workInfoReturnInitiate";

    /**
     * 退回审批
     */
    public final static String WORK_INFO_RETURN_APPROVAL = "workInfoReturnApproval";

    /**
     * 退回处理
     */
    public final static String WORK_INFO_RETURN_PROCESS = "workInfoReturnProcess";

    /**
     * 抄送
     */
    public final static String WORK_INFO_COPY = "workInfoCopy";

    /**
     * 收费通知单
     */
    public final static String CHARGE_NOTICE = "chargeNotice";

    /**
     * 催款通知单
     */
    public final static String COLLECTION_NOTICE = "collectionNotice";

}
