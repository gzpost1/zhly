package cn.cuiot.dmp.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 系统配置-初始化配置-审核配置
 *
 * @author caorui
 * @date 2024/6/11
 */
@Getter
@AllArgsConstructor
public enum AuditConfigTypeEnum {

    NOTICE_MANAGE((byte) 1, "公告管理"),
    CONTENT_MANAGE((byte) 2, "图文管理"),
    PRICE_MANAGE((byte) 3, "定价管理"),
    LEASE_CONTRACT((byte) 4, "租赁合同"),
    INTENTION_CONTRACT((byte) 5, "意向合同");

    private final Byte code;
    private final String message;

    public static String getMessage(Byte code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (AuditConfigTypeEnum auditConfigTypeEnum : values()) {
            if (auditConfigTypeEnum.getCode().equals(code)) {
                return auditConfigTypeEnum.getMessage();
            }
        }
        return null;
    }

}
