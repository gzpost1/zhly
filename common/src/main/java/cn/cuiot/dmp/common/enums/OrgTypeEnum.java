package cn.cuiot.dmp.common.enums;

/**
 * @author zjb
 * @classname OrgTypeEnum
 * @description 租户类型枚举
 * @date 2021/12/1
 */
public enum OrgTypeEnum {

    //账户类型（5：联通账户、11：物业账户、12：通用账户、）

    UNICOM_ACCOUNT("联通账户", "5"),
    PROPERTY_ACCOUNT("物业账户", "11"),
    CURRENCY_ACCOUNT("通用账户", "12");

    /**
     * 组织类型
     */
    private String orgType;

    /**
     * code
     */
    private String code;

    OrgTypeEnum(String orgType, String code) {
        this.code = code;
        this.orgType = orgType;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
