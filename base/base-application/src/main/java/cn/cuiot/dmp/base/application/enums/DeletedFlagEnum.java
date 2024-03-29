package cn.cuiot.dmp.base.application.enums;

/**
 * @Description
 * @Author cds
 * @Date 2020/9/4
 */
public enum DeletedFlagEnum {
    /**
     * 状态枚举类
     */
    NOT_DELETED(0, "正常"),
    DELETED(1, "已删除");

    private Integer code;
    private String name;

    DeletedFlagEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
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
