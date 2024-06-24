package cn.cuiot.dmp.lease.enums;


import com.google.common.collect.Lists;

import java.util.List;

/**
 * 执行频率 0(byte)每月 1(byte)每日 2(byte)指定日期
 */
public enum ChargePlainCronType {
    MONTHLY((byte) 0, "每月"),
    DAILY((byte) 1, "每日"),
    SPECIFIED_DATE((byte) 2, "指定日期");

    private byte code;
    private String desc;

    ChargePlainCronType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ChargePlainCronType getByCode(byte code) {
        for (ChargePlainCronType value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }

    public static List<Byte> dayCreate = Lists.newArrayList(MONTHLY.getCode(),DAILY.getCode());
}
