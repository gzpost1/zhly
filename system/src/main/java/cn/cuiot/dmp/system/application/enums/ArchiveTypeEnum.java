package cn.cuiot.dmp.system.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 系统配置-自定义配置-档案类型
 *
 * @author caorui
 * @date 2024/5/16
 */
@Getter
@AllArgsConstructor
public enum ArchiveTypeEnum {

    BUILDING_ARCHIVE((byte) 1, "楼盘档案"),
    HOUSE_ARCHIVE((byte) 2, "房屋档案"),
    ROOM_ARCHIVE((byte) 3, "空间档案"),
    DEVICE_ARCHIVE((byte) 4, "设备档案"),
    PARK_ARCHIVE((byte) 5, "车位档案"),
    CODE_ARCHIVE((byte) 6, "二维码档案");

    private final Byte code;
    private final String message;

    public static String getMessage(Byte code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (ArchiveTypeEnum archiveTypeEnum : values()) {
            if (archiveTypeEnum.getCode().equals(code)) {
                return archiveTypeEnum.getMessage();
            }
        }
        return null;
    }

}
