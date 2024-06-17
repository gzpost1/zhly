package cn.cuiot.dmp.lease.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author caorui
 * @date 2024/6/17
 */
@Getter
@AllArgsConstructor
public enum ClueFollowDayEnum {

    ZERO_THREE_DAY((byte) 1, "3天以内"),
    THREE_SEVEN_DAY((byte) 2, "3-7天"),
    SEVEN_FIFTEEN_DAY((byte) 3, "7-15天"),
    FIFTEEN_THIRTY_DAY((byte) 4, "15天-30天"),
    THIRTY_MORE_DAY((byte) 5, "30天以上");

    private final Byte code;
    private final String message;

    public static String getMessage(Byte code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (ClueFollowDayEnum clueFollowDayEnum : values()) {
            if (clueFollowDayEnum.getCode().equals(code)) {
                return clueFollowDayEnum.getMessage();
            }
        }
        return null;
    }

}
