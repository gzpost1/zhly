package cn.cuiot.dmp.baseconfig.flow.enums;

/**
 * @Description 工作状态
 * @Date 2024/5/7 9:23
 * @Created by libo
 */
public enum WorkInfoEnums {
    /**
     * 1-已完结
     */
    FINISH((byte)1, "已完结"),
    /**
     * 2-进行中
     */
    PROCESSING((byte)2, "进行中"),
    /**
     * 3-已终止
     */
    TERMINATED((byte)3, "已终止"),
    /**
     * 4-已挂起
     */
    SUSPEND((byte)4, "已挂起");

    private byte code;
    private String desc;

    WorkInfoEnums(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
