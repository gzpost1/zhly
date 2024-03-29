package cn.cuiot.dmp.system.application.enums;

/**
 * @author hk
 * @classname HasChildrenEnum
 * @description 是否存在下级组织枚举类
 * @date 2023/04/07
 */
public enum HasChildrenEnum {

    /**
     * 状态枚举类
     */
    DEPT_HAS_CHILDREN("1", "存在下级组织"),
    DEPT_NO_CHILDREN("0", "不存在下级组织");

    private String code;
    private String name;

    HasChildrenEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
