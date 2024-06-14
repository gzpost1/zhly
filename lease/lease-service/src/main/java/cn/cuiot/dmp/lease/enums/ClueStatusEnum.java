package cn.cuiot.dmp.lease.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author caorui
 * @date 2024/6/14
 */
@Getter
@AllArgsConstructor
public enum ClueStatusEnum {

    DISTRIBUTE_STATUS((byte) 1, "待分配"),
    FOLLOW_STATUS((byte) 2, "跟进中"),
    FINISH_STATUS((byte) 3, "已完成");

    private final Byte code;
    private final String message;

    public static String getMessage(Byte code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (ClueStatusEnum clueStatusEnum : values()) {
            if (clueStatusEnum.getCode().equals(code)) {
                return clueStatusEnum.getMessage();
            }
        }
        return null;
    }

}
