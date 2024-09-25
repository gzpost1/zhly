package cn.cuiot.dmp.common.constant;

import org.apache.commons.lang3.StringUtils;

public enum BaseConfigMessage {
    WORK_INFO_COMPLETED(MsgDataType.WORK_INFO_COMPLETED, "你的工单已完成，工单名称：%s，点击查看详情", SmsStdTemplate.MANAGE_WORK_INFO_COMPLETE),
    WORK_INFO_CANCEL(MsgDataType.WORK_INFO_CANCEL, "你的工单被终止，工单名称：%s，点击查看详情", SmsStdTemplate.MANAGE_WORK_INFO_TERMINATE),
    WORK_INFO_TURNDOWN(MsgDataType.WORK_INFO_TURNDOWN, "你的工单被驳回，工单名称：%s，点击查看详情", SmsStdTemplate.MANAGE_WORK_INFO_REJECT),
    WORK_INFO_APPROVAL(MsgDataType.WORK_INFO_APPROVAL, "你有一条工单待审批，工单名称：%s，点击查看详情", SmsStdTemplate.MANAGE_WORK_INFO_AWAIT_AUDIT),
    WORK_INFO_PROCESS(MsgDataType.WORK_INFO_PROCESS, "你有一条工单待处理，工单名称：%s，点击查看详情", SmsStdTemplate.MANAGE_WORK_INFO_AWAIT_HANDLE),
    WORK_INFO_EVALUATE(MsgDataType.WORK_INFO_EVALUATE, "你有一条工单待评价，工单名称：%s，点击查看详情", SmsStdTemplate.MANAGE_WORK_INFO_AWAIT_EVALUATED),
    WORK_INFO_RETURN_INITIATE(MsgDataType.WORK_INFO_RETURN_INITIATE, "你的工单被退回，工单名称：%s，点击查看详情", SmsStdTemplate.MANAGE_WORK_INFO_RETURN),
    WORK_INFO_RETURN_APPROVAL(MsgDataType.WORK_INFO_RETURN_APPROVAL, "你审批的工单被退回，工单名称：%s，点击查看详情", SmsStdTemplate.MANAGE_WORK_INFO_AUDIT_RETURN),
    WORK_INFO_RETURN_PROCESS(MsgDataType.WORK_INFO_RETURN_PROCESS, "你处理的工单被退回，工单名称：%s，点击查看详情", SmsStdTemplate.MANAGE_WORK_INFO_HANDLE_RETURN),
    WORK_INFO_COPY(MsgDataType.WORK_INFO_COPY, "你的工单已完成，工单名称：%s，点击查看详情", SmsStdTemplate.MANAGE_WORK_INFO_COMPLETE_COPY),
    ;
    private String messageType;
    private String message;

    private Integer smsTemplateId;

    BaseConfigMessage(String messageType, String message, Integer smsTemplateId) {
        this.messageType = messageType;
        this.message = message;
        this.smsTemplateId = smsTemplateId;
    }

    public static String getMessageByType(String type, String flowConfigName) {
        for (BaseConfigMessage value : BaseConfigMessage.values()) {
            if (StringUtils.equals(type, value.messageType)) {
                return String.format(value.message, flowConfigName);
            }
        }
        return "";
    }

    public static Integer getSmsTemplateIdByType(String type) {
        for (BaseConfigMessage value : BaseConfigMessage.values()) {
            if (StringUtils.equals(type, value.messageType)) {
                return value.smsTemplateId;
            }
        }
        return null;
    }
}
