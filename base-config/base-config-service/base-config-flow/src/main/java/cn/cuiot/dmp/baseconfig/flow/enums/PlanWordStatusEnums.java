package cn.cuiot.dmp.baseconfig.flow.enums;

import java.util.Objects;

/**
 * @author pengjian
 * @create 2024/10/9 20:47
 */
public enum PlanWordStatusEnums {
    /**
     * 禁用
     */
    DISABLE(0, "禁用"),
    /**
     * 启用
     */
    ENABLE(1, "启用");

    private Integer code;
    private String name;

    PlanWordStatusEnums(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 查询计划工单状态
     * @param code
     * @return
     */
    public static String getWorkOrderSource(Integer code) {
        for (PlanWordStatusEnums WorkOrderStatusEnums : PlanWordStatusEnums.values()) {
            if (Objects.equals(WorkOrderStatusEnums.getCode(),code)) {
                return WorkOrderStatusEnums.getName();
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
