package cn.cuiot.dmp.common.constant;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 初始化配置-表单管理-系统表单常量
 *
 * @author caorui
 * @date 2024/6/6
 */
public class SystemFormConfigConstant {

    /**
     * 表单配置相关
     */
    public static final List<String> CLUE_FORM_CONFIG = Lists.newArrayList("线索表单", "跟进表单");

    /**
     * 表单配置分类相关
     */
    public static final List<String> FORM_CONFIG_TYPE_LIST = Lists.newArrayList("线索相关");
    public static final String ROOT_NAME = "系统表单分类";
    public static final Long DEFAULT_PARENT_ID = -2L;
    public static final Byte ROOT_LEVEL_TYPE = (byte) 0;
    public static final Byte FIRST_LEVEL_TYPE = (byte) 1;
    public static final Long DEFAULT_USER_ID = 1L;

}
