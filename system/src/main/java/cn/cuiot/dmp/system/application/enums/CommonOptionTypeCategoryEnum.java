package cn.cuiot.dmp.system.application.enums;

import java.util.Objects;

/**
 * 常用选项分类枚举
 *
 * @Author: zc
 * @Date: 2024-07-11
 */
public enum CommonOptionTypeCategoryEnum {
    /**
     * 自定义
     */
    SYSTEM((byte) 0, "自定义"),
    /**
     * 交易方式
     */
    TENANT((byte) 1, "交易方式"),
    /**
     * 收费方式
     */
    COMMUNITY((byte) 2, "收费方式"),
    ;

    private Byte code;

    private String name;

    CommonOptionTypeCategoryEnum(Byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static CommonOptionTypeCategoryEnum getCategoryNameByCode(Byte code) {
        for (CommonOptionTypeCategoryEnum value : CommonOptionTypeCategoryEnum.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }
}
