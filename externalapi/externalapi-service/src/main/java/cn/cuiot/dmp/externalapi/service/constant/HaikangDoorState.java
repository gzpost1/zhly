package cn.cuiot.dmp.externalapi.service.constant;

import lombok.Getter;

/**
 * 门禁状态
 *
 * @author: wuyongchong
 * @date: 2024/10/10 17:40
 */
public enum HaikangDoorState {

    INIT((byte) 0, "初始状态"),
    OPEN((byte) 1, "开门状态"),
    CLOSE((byte) 2, "关门状态"),
    OFFLINE((byte) 3, "离线状态");

    @Getter
    private Byte status;

    @Getter
    private String message;

    HaikangDoorState(Byte status, String message) {
        this.status = status;
        this.message = message;
    }

    public static String parseMessage(Byte st) {
        if (st == null) {
            return null;
        }
        return parseItem(st).message;
    }

    public static HaikangDoorState parseItem(Byte st) {
        for (HaikangDoorState item : HaikangDoorState.values()) {
            if (item.status.equals(st)) {
                return item;
            }
        }
        return null;
    }

}
