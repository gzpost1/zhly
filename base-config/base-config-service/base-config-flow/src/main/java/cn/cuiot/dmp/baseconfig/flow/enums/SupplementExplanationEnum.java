package cn.cuiot.dmp.baseconfig.flow.enums;

import java.util.Objects;

/**
 * @author pengjian
 * @create 2024/6/12 15:51
 */
public enum SupplementExplanationEnum {

    REJECT((byte)7,"您的工单被驳回，驳回理由："),

    TERMINATION((byte)4,"您的工单被终止，终止原因："),

    FALL_BACK((byte)8,"您的工单被退回，退回原因：%s，您可以重新修改内容后再次提交工单"),

    TIME_OUT((byte)1,"您的工单被系统自动终止，如有疑问，请联系物业管家")
    ;
    private Byte code;

    private String message;

    SupplementExplanationEnum(Byte code, String message){
        this.code=code;
        this.message=message;
    }

    public static SupplementExplanationEnum getSupplementExplanation(Byte code){
        for (SupplementExplanationEnum Explanation: SupplementExplanationEnum.values()) {
            if (Objects.equals(Explanation.getCode(),code)){
                return Explanation;
            }
        }
        return null;
    }

    public byte getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
