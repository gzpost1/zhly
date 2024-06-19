package cn.cuiot.dmp.common.enums;

/**
 * 客户身份类型
 *
 * @author: wuyongchong
 * @date: 2024/6/13 14:25
 */
public enum CustomerIdentityTypeEnum {

    OWNER("1", "业主"),
    TENANT("2", "租户");

    private String code;
    private String name;

    CustomerIdentityTypeEnum(String code, String name) {
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

    /**
     * 根据名称获取枚举
     */
    public static CustomerIdentityTypeEnum parseByName(String name) {
        CustomerIdentityTypeEnum[] values = CustomerIdentityTypeEnum.values();
        for (CustomerIdentityTypeEnum typeEnum : values) {
            if (typeEnum.getName().equals(name)) {
                return typeEnum;
            }
        }
        return null;
    }

    /**
     * 根据编码获取枚举
     */
    public static CustomerIdentityTypeEnum parseByCode(String code) {
        CustomerIdentityTypeEnum[] values = CustomerIdentityTypeEnum.values();
        for (CustomerIdentityTypeEnum typeEnum : values) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }
}
