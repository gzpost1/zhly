package cn.cuiot.dmp.baseconfig.flow.enums;

import java.util.Objects;

/**
 * @author pengjian
 * @create 2024/5/7 16:13
 */
public enum WorkOrderStatusEnums {
    completed( (byte) 1, "已完结"),
    progress( (byte) 2, "进行中"),
    terminated( (byte) 3, "已终止"),
    Suspended( (byte) 4, "已挂起"),

    revoke( (byte) 5, "已撤销")
    ;

    private Byte status;

    private String desc;

    WorkOrderStatusEnums(Byte status, String desc) {
    this.status = status;
    this.desc = desc;
    }


    public static String getWorkOrderStatus(Byte status) {
        for (WorkOrderStatusEnums WorkOrderStatusEnums : WorkOrderStatusEnums.values()) {
            if (Objects.equals(WorkOrderStatusEnums.getStatus(),status)) {
                return WorkOrderStatusEnums.getDesc();
            }
        }
        return null;
    }
    public Byte getStatus() {
        return status;
    }


    public String getDesc() {
        return desc;
    }

    public void setTypeName(Byte status) {
        this.status = status;
    }
}
