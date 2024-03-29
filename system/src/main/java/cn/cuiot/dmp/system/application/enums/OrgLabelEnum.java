package cn.cuiot.dmp.system.application.enums;

/**
 * @param
 * @Author xieSH
 * @Description org标签枚举
 * @Date 2022/1/20 10:15
 * @return
 **/
public enum OrgLabelEnum {
    /**
     * org标签枚举
     */
    FACTORY_PARK(9, "厂园区"),
    /**
     * 联通管理房（标签表不存在此标签，联通用户）
     */
    UNICOM_ADMIN(13, "联通管理方");


    private final Integer id;

    private final String name;

    OrgLabelEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}