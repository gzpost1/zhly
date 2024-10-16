package cn.cuiot.dmp.pay.service.service.enums;

import lombok.Getter;

public enum PayBusinessTypeEnum {
    CHARGE((byte) 1, "账单缴费"),
    RECHARGE((byte) 2, "预缴"),
    DEPOSIT((byte) 3, "押金缴费"),
    ;

    @Getter
    private Byte code;
    @Getter
    private String message;



    PayBusinessTypeEnum(byte code, String message) {
        this.code = code;
        this.message = message;
    }



}
