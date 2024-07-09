package cn.cuiot.dmp.common.constant;

import org.apache.commons.lang3.StringUtils;

public enum BaseConfigMessage {
    WORK_INFO_COMPLETED(MsgDataType.WORK_INFO_COMPLETED,"你的工单已完成，工单名称：%s，点击查看详情"),
    WORK_INFO_CANCEL(MsgDataType.WORK_INFO_CANCEL,"你的工单被终止，工单名称：%s，点击查看详情"),
    WORK_INFO_TURNDOWN(MsgDataType.WORK_INFO_TURNDOWN,"你的工单被驳回，工单名称：%s，点击查看详情"),
    WORK_INFO_APPROVAL(MsgDataType.WORK_INFO_APPROVAL,"你有一条工单待审批，工单名称：%s，点击查看详情"),
    WORK_INFO_PROCESS(MsgDataType.WORK_INFO_PROCESS,"你有一条工单待处理，工单名称：%s，点击查看详情"),
    WORK_INFO_EVALUATE(MsgDataType.WORK_INFO_EVALUATE,"你有一条工单待评价，工单名称：%s，点击查看详情"),
    WORK_INFO_RETURN_INITIATE(MsgDataType.WORK_INFO_RETURN_INITIATE,"你的工单被退回，工单名称：%s，点击查看详情"),
    WORK_INFO_RETURN_APPROVAL(MsgDataType.WORK_INFO_RETURN_APPROVAL,"你审批的工单被退回，工单名称：%s，点击查看详情"),
    WORK_INFO_RETURN_PROCESS(MsgDataType.WORK_INFO_RETURN_PROCESS,"你处理的工单被退回，工单名称：%s，点击查看详情"),
    WORK_INFO_COPY(MsgDataType.WORK_INFO_COPY,"你的工单已完成，工单名称：%s，点击查看详情"),
    ;
    private String messageType;
    private String message;

    BaseConfigMessage(String messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public static String getMessageByType(String type,String flowConfigName){
        for (BaseConfigMessage value : BaseConfigMessage.values()) {
            if(StringUtils.equals(type,value.messageType)){
                return String.format(value.message, flowConfigName);
            }
        }
        return "";
    }
}
