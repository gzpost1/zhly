package cn.cuiot.dmp.baseconfig.flow.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description 超时处理枚举
 * @Date 2024/4/25 10:39
 * @Created by libo
 */
public enum TimeLimitHandleEnums {
    //不处理
    DO_NOTHING("DO_NOTHING", "不处理",0,""),
    //自动终止
    TO_END("TO_END", "自动终止",1,"审批超时，系统自动终止"),
    //自动挂起
    TO_SUSPEND("TO_SUSPEND", "自动挂起",2,"审批超时，系统自动挂起"),
    //自动通过
    TO_APPROVE("TO_APPROVE", "自动通过",3,"审批超时，系统自动通过"),
    ;

    private String code;
    private String desc;
    private Integer value;

    private String processComment;

    TimeLimitHandleEnums(String code, String desc, Integer value, String processComment) {
        this.code = code;
        this.desc = desc;
        this.value = value;
        this.processComment = processComment;
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getProcessComment() {
        return processComment;
    }

    /**
     * 根据value获取code
     */
    public static String getCodeByValue(Integer value) {
        for (TimeLimitHandleEnums e : TimeLimitHandleEnums.values()) {
            if (e.getValue().equals(value)) {
                return e.getCode();
            }
        }
        return StringUtils.EMPTY;
    }
}
