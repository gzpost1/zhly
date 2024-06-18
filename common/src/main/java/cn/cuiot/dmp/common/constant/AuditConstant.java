package cn.cuiot.dmp.common.constant;

/**
 * @Description TODO
 * @Date 2024-06-14 11:32
 * @Author by Mujun~
 */
public class AuditConstant {
    public static final String AUDIT_MSG_TEMP = "审核了意向合同的%s" + System.lineSeparator() + "审核结果为:%s" + System.lineSeparator() + "审核备注:%s";

    //对应审核配置表name
    public static final String AUDIT_CONFIG_INTENTION_NEW = "合同新建后提交";
    public static final String AUDIT_CONFIG_INTENTION_SIGN = "合同签约";
    public static final String AUDIT_CONFIG_INTENTION_CANCEL = "合同退定";
    public static final String AUDIT_CONFIG_INTENTION_USELESS = "合同作废";

    public static final String OPERATE_COMMIT = "提交";
    public static final String PASS = "通过";
    public static final String REFUSE = "不通过";
    public static final String OPERATE_SIGN_CONTRACT = "签约";
    public static final String OPERATE_CANCEL = "退定";
    public static final String OPERATE_USELESS = "作废";
}
