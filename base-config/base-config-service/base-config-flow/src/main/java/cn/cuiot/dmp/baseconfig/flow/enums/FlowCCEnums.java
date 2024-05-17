package cn.cuiot.dmp.baseconfig.flow.enums;

/**
 * @Description 抄送配置
 * @Date 2024/4/28 16:53
 * @Created by libo
 */
public enum FlowCCEnums {
    //抄送类型 0指定部门 1指定人员 2指定角色
    DEPARTMENT((byte) 0, "指定部门"),
    PERSON((byte) 1, "指定人员"),
    ROLE((byte) 2, "指定角色"),
    ;
    private Byte code;
    private String desc;

    FlowCCEnums(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Byte getCode() {
        return code;
    }
}
