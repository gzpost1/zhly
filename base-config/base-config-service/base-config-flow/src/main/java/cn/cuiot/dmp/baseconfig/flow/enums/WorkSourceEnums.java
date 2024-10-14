package cn.cuiot.dmp.baseconfig.flow.enums;

import java.util.Objects;

/**
 * @author pengjian
 * @create 2024/6/12 9:28
 */
public enum WorkSourceEnums {

    WORK_SOURCE_PLAN((byte)0,"计划生成"),

    WORK_SOURCE_MAKE((byte)1,"自查报事"),

    CUSTOMER_BILL_LADING((byte)2,"客户提单"),

    PROXY_CUSTOMER_RECORD((byte)3,"代录客单")
    ;

    private Byte code;

    private String desc;

    WorkSourceEnums(Byte code,String desc){
        this.code=code;
        this.desc=desc;
    }

    /**
     * 根据编码查询工单来源
     * @param code
     * @return
     */
    public static String getWorkOrderSource(Byte code) {
        for (WorkSourceEnums WorkOrderStatusEnums : WorkSourceEnums.values()) {
            if (Objects.equals(WorkOrderStatusEnums.getCode(),code)) {
                return WorkOrderStatusEnums.getDesc();
            }
        }
        return null;
    }
    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
