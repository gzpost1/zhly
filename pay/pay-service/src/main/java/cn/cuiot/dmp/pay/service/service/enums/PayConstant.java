package cn.cuiot.dmp.pay.service.service.enums;

/**
 * @author huq
 * @ClassName SysConstant
 * @Date 2022/5/7 10:21
 **/
public class PayConstant {



    /**
     * 普通商户模式key
     */
    public static final String NORMAL_WECHAT_PAY_KEY = "NORMAL::WECHAT::PAY::KEY";


    /**
     * 账单缴费
     */
    public static final Byte CHARGE = (byte)1;

    /**
     * 预缴（余额充值）
     */
    public static final Byte BALANCE = (byte)2;

}
