package cn.cuiot.dmp.baseconfig.flow.enums;

/**
 * @Description 发起人类型
 * @Date 2024/4/23 19:58
 * @Created by libo
 */
public enum AssignedUserType {

    ASSIGN_USER("user", (byte) 0, "指定用户"),
    ASSIGN_DEPT("dept", (byte) 1, "指定部分"),
    ASSIGN_ROLE("role", (byte) 2, "指定角色"),
    ;

    AssignedUserType(String flowType, Byte flowTypeCode, String desc) {
        this.flowType = flowType;
        this.flowTypeCode = flowTypeCode;
        this.desc = desc;
    }

    /**
     * flowable中的节点名称
     */
    private String flowType;
    /**
     * 数据库存储类型
     */
    private Byte flowTypeCode;

    /**
     * 描述
     */
    private String desc;

    /**
     * 根据flowType获取对应的AssignedUserType
     */
    public static AssignedUserType getAssignedUserType(String flowType) {
        for (AssignedUserType assignedUserType : AssignedUserType.values()) {
            if (assignedUserType.getFlowType().equals(flowType)) {
                return assignedUserType;
            }
        }
        return null;
    }

    /**
     * 根据flowType获取对应的flowTypeCode
     * @return
     */
    public static Byte getFlowTypeCode(String flowType) {
        AssignedUserType assignedUserType = getAssignedUserType(flowType);
        if (assignedUserType != null) {
            return assignedUserType.getFlowTypeCode();
        }
        return null;
    }
    public String getFlowType() {
        return flowType;
    }

    public Byte getFlowTypeCode() {
        return flowTypeCode;
    }

    public String getDesc() {
        return desc;
    }
}
