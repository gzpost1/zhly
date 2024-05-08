package cn.cuiot.dmp.baseconfig.flow.enums;

/**
 * @author pengjian
 * @create 2024/5/7 16:13
 */
public enum WorkOrderStatusEnums {
    completed( (byte) 1, "已完结"),
    progress( (byte) 2, "进行中"),
    terminated( (byte) 3, "已终止"),
    Suspended( (byte) 4, "已挂起"),
    ;

    private Byte status;

    private String desc;

    WorkOrderStatusEnums(Byte status, String desc) {
    this.status = status;
    this.desc = desc;
    }

    public Byte getStatus() {
        return status;
    }

    public void setTypeName(Byte status) {
        this.status = status;
    }
}
