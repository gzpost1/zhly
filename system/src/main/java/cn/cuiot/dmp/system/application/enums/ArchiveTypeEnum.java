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

    BUILDING_ARCHIVE(1, "楼盘档案"),
    HOUSE_ARCHIVE(2, "房屋档案"),
    ROOM_ARCHIVE(3, "空间档案"),
    PARK_ARCHIVE(4, "车位档案"),
    DEVICE_ARCHIVE(5, "设备档案"),
    CODE_ARCHIVE(6, "二维码/条形码档案");

    private final Integer code;
    private final String message;

    public static String getMessage(Integer code) {
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
