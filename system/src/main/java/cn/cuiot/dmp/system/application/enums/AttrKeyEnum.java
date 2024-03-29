package cn.cuiot.dmp.system.application.enums;

/**
 * @author wen
 * @className AttrKeyEnum
 * @description 组织属性
 * @date 2020-09-07 11:43:17
 */
public enum AttrKeyEnum {

    /**
     * 小区行政区域
     */
    COMMUNITY_AREA_CODE("community_area_code", "小区行政区域"),
    /**
     * 楼层数
     */
    FLOOR_NUM("floor_num", "楼层数"),
    /**
     * 当前楼层数
     */
    CURRENT_FLOOR("current_floor", "当前楼层"),
    /**
     * 建筑类型
     */
    BUILDING_TYPE("building_type", "建筑类型"),
    /**
     * 楼栋朝向
     */
    ORIENTATION("orientation", "楼栋朝向"),

    /**
     * 详细地址
     */
    ADDRESS("address", "详细地址"),

    /**
     * 园区类型
     */
    PARK_TYPE("park_type", "园区类型"),

    /**
     * 其他区域类型名称
     */
    OTHER_PARK_TYPE_NAME("other_park_type_name", "其他园区类型名称"),

    /**
     * 区域类型
     */
    REGION_TYPE("region_type", "区域类型"),

    /**
     * 其他区域类型名称
     */
    OTHER_REGION_TYPE_NAME("other_region_type_name", "其他区域类型名称"),

    /**
    * 经度
    */
    LONGITUDE("longitude","经度"),

    /**
    * 纬度
    */
    LATITUDE("latitude","纬度")

    ;



    private String key;
    private String description;

    AttrKeyEnum(String key, String name) {
        this.key = key;
        this.description = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
