package cn.cuiot.dmp.common.enums;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * 系统配置-自定义配置-档案类型
 *
 * @author caorui
 * @date 2024/5/16
 */
@Getter
@AllArgsConstructor
public enum SystemOptionTypeEnum {

    BUILDING_ARCHIVE((byte) 1, "楼盘档案"),
    HOUSE_ARCHIVE((byte) 2, "房屋档案"),
    ROOM_ARCHIVE((byte) 3, "空间档案"),
    DEVICE_ARCHIVE((byte) 4, "设备档案"),
    PARK_ARCHIVE((byte) 5, "车位档案"),
    CODE_ARCHIVE((byte) 6, "二维码档案");

    private final Byte code;
    private final String message;

    /**
     * 档案类型
     */
    private static final List<Byte> archiveTypeList = Lists.newArrayList(
            BUILDING_ARCHIVE.getCode(), HOUSE_ARCHIVE.getCode(), ROOM_ARCHIVE.getCode(),
            DEVICE_ARCHIVE.getCode(), PARK_ARCHIVE.getCode(), CODE_ARCHIVE.getCode());

    /**
     * 是否为档案类型
     */
    public static boolean archiveTypeFlag(Byte status) {
        return archiveTypeList.contains(status);
    }

    public static String getMessage(Byte code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (SystemOptionTypeEnum systemOptionTypeEnum : values()) {
            if (systemOptionTypeEnum.getCode().equals(code)) {
                return systemOptionTypeEnum.getMessage();
            }
        }
        return null;
    }

}
