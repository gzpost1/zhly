package cn.cuiot.dmp.baseconfig.flow.enums;

/**
 * @Description 节点保存的内容
 * @Date 2024/4/28 15:41
 * @Created by libo
 */
public enum FlowAccessEnums {
    //0表单 1任务
    FORM((byte) 0, "表单"),
    TASK((byte) 1, "任务");
    private Byte code;
    private String desc;

    FlowAccessEnums(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Byte getCode() {
        return code;
    }
}
