package cn.cuiot.dmp.base.application.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum ContractEnum {

    AUDIT_WAITING_COMMIT(0, "待提交"),
    AUDIT_WAITING(1, "审核中"),
    AUDIT_PASS(2, "审核通过"),
    AUDIT_REFUSE(3, "未通过"),
    //合同状态
    STATUS_DARFT(11, "草稿"),
    STATUS_COMMITING(12, "提交中"),
    STATUS_WAITING(13, "待执行"),
    STATUS_EXECUTING(14, "执行中"),
    STATUS_EXPIRED(15, "已过期"),
    STATUS_SIGNING(16, "签约中"),
    STATUS_SIGNED(17, "已签约"),
    STATUS_CANCELING(18, "退订中"),
    STATUS_CANCELLED(19, "已退订"),
    STATUS_USELESSING(20, "作废中"),
    STATUS_USELESS(21, "已作废"),
    STATUS_RELETING(22, "续租中"),
    STATUS_RELET(23, "已续租"),
    STATUS_CHANGING(24, "变更中"),
    STATUS_CHANGED(25, "已变更"),
    STATUS_BACKING_LEASE(26, "退租中"),
    STATUS_BACKED_LEASE(27, "已退租");

    private   Integer code;
    private   String desc;

    public static ContractEnum getEnumByCode(Integer code) {
        for (ContractEnum e : ContractEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
} 