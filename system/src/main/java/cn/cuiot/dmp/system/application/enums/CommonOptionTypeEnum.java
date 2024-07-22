package cn.cuiot.dmp.system.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 系统配置-初始化配置-自定义选项类别
 *
 * @author caorui
 * @date 2024/7/1
 */
@Getter
@AllArgsConstructor
public enum CommonOptionTypeEnum {

    CUSTOM_OPTION((byte) 0, "自定义选项"),
    TRADE_METHOD((byte) 1, "交易方式"),
    CHARGE_METHOD((byte) 2, "收费方式");

    private final Byte code;
    private final String message;

    public static String getMessage(Byte code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (CommonOptionTypeEnum commonOptionTypeEnum : values()) {
            if (commonOptionTypeEnum.getCode().equals(code)) {
                return commonOptionTypeEnum.getMessage();
            }
        }
        return null;
    }

}
