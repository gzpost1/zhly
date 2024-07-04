package cn.cuiot.dmp.lease.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author caorui
 * @date 2024/6/27
 */
@Getter
@AllArgsConstructor
public enum PriceManageStatusEnum {

    ALL_STATUS((byte) 0, "全部"),
    DRAFT_STATUS((byte) 1, "草稿"),
    AUDIT_STATUS((byte) 2, "审核中"),
    PASS_STATUS((byte) 3, "审核通过"),
    NOT_PASS_STATUS((byte) 4, "审核不通过"),
    EXECUTED_STATUS((byte) 5, "已执行"),
    INVALID_STATUS((byte) 6, "已作废");

    private final Byte code;
    private final String message;

    public static String getMessage(Byte code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (PriceManageStatusEnum priceManageStatusEnum : values()) {
            if (priceManageStatusEnum.getCode().equals(code)) {
                return priceManageStatusEnum.getMessage();
            }
        }
        return null;
    }

}
