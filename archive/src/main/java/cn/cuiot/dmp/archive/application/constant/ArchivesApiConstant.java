package cn.cuiot.dmp.archive.application.constant;

/**
 * @author caorui
 * @date 2024/5/23
 */
public class ArchivesApiConstant {

    /**
     * 楼盘档案
     */
    public static final byte BUILDING_ARCHIVE = (byte) 1;
    /**
     * 房屋档案
     */
    public static final byte HOUSE_ARCHIVE = (byte) 2;
    /**
     * 空间档案
     */
    public static final byte ROOM_ARCHIVE = (byte) 3;
    /**
     * 设备档案
     */
    public static final byte DEVICE_ARCHIVE = (byte) 4;
    /**
     * 车位档案
     */
    public static final byte PARK_ARCHIVE = (byte) 5;

    public static String getArchivesName(Byte type) {
        String name = null;
        switch (type) {
            case BUILDING_ARCHIVE:
                name = "buildingArchivesService";
                break;
            case HOUSE_ARCHIVE:
                name = "housesArchivesService";
                break;
            case ROOM_ARCHIVE:
                name = "roomArchivesService";
                break;
            case DEVICE_ARCHIVE:
                name = "deviceArchivesService";
                break;
            case PARK_ARCHIVE:
                name = "parkingArchivesService";
                break;
        }
        return name;
    }

    /**
     * 导入相关校验
     */
    public static final Integer ARCHIVES_IMPORT_MAX_NUM = 5000;

}
