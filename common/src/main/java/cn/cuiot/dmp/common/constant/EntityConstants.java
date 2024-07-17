package cn.cuiot.dmp.common.constant;

import org.jpedal.parser.shape.S;

/**
 * 常用变量
 * @author: wuyongchong
 * @date: 2024/4/1 20:21
 */
public final class EntityConstants {
    /**
     * 未删除
     */
    public static final Byte NOT_DELETED = Byte.parseByte("0");
    /**
     * 已删除
     */
    public static final Byte DELETED = Byte.parseByte("1");

    /**
     * 启用
     */
    public static final Byte ENABLED = Byte.parseByte("1");
    /**
     * 禁用
     */
    public static final Byte DISABLED = Byte.parseByte("0");

    /**
     * 否
     */
    public static final Byte NO = 0;
    /**
     * 是
     */
    public static final Byte YES = 1;

    /**
     * 正常
     */
    public static final Byte NORMAL = 1;

    /**
     * 已过期
     */
    public static final Byte EXPIRE = 2;

    /**
     * 未生效
     */
    public static final Byte NOT_EFFECTIVE = 0;

    /**
     * null 字符串
     */
    public static final String NULL_STR="null";

    /**
     * 空 字符串
     */
    public static final String BLANK_STR="";
}
