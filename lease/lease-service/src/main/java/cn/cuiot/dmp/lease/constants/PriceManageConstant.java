package cn.cuiot.dmp.lease.constants;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author caorui
 * @date 2024/6/27
 */
public class PriceManageConstant {

    /**
     * 定价管理操作记录
     */
    public static final String OPERATE_CREATE = "新增";
    public static final String OPERATE_SUBMIT = "提交";
    public static final String OPERATE_SUBMIT_AUDIT = "提交审核";
    public static final String OPERATE_INVALID = "作废";

    /**
     * 定价管理
     */
    public static final List<Byte> Price_Manage_Status = Lists.newArrayList((byte) 0, (byte) 1, (byte) 2,
            (byte) 3, (byte) 4, (byte) 5, (byte) 6);

}
