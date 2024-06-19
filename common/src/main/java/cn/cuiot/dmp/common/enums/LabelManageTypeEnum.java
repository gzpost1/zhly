package cn.cuiot.dmp.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 系统配置-初始化配置-标签管理
 *
 * @author caorui
 * @date 2024/6/19
 */
@Getter
@AllArgsConstructor
public enum LabelManageTypeEnum {

    INTENTION_CONTRACT((byte) 1, "意向合同"),
    LEASE_CONTRACT((byte) 2, "租赁合同");

    private final Byte code;
    private final String message;

    public static String getMessage(Byte code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (LabelManageTypeEnum labelManageTypeEnum : values()) {
            if (labelManageTypeEnum.getCode().equals(code)) {
                return labelManageTypeEnum.getMessage();
            }
        }
        return null;
    }

}
