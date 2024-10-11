package cn.cuiot.dmp.pay.service.service.enums;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

public enum OrderStatusEnum {
    TO_BE_PAY((byte) 1, "待支付"),
    CANCEL((byte) 2, "已取消"),
    PAYING((byte) 3, "支付中"),
    PAY_FAILED((byte) 4, "支付失败"),
    PAID((byte) 5, "已支付"),


    UNKNOWN((byte) 99, "未知状态"),
    NOT_STATUS((byte) -1, "-"),
    ;

    @Getter
    private Byte status;
    @Getter
    private String message;


    /**
     * 可以进行再次支付的订单状态
     */
    public static final List<Byte> CAN_PAY_STATUS = Lists.newArrayList(TO_BE_PAY.getStatus(), PAYING.getStatus(),
            PAID.getStatus());

    /**
     * 状态-完结状态
     */
    public static final List<Byte> PARENT_FINISHI_STATUS = Lists.newArrayList(CANCEL.getStatus(),
            PAY_FAILED.getStatus(), PAID.getStatus());

    /**
     * 父单状态-不可取消状态
     */
    public static final List<Byte> NOT_CANCEL_STATUS = Lists.newArrayList(PAYING.getStatus(), PAID.getStatus(),
            UNKNOWN.getStatus());


    OrderStatusEnum(byte status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * 获取状态名称
     * @param status
     * @return
     */
    public static String getName(Byte status) {
        //为空，返回"-"
        if (Objects.isNull(status)) {
            return NOT_STATUS.getMessage();
        }
        for (OrderStatusEnum statusEnum : OrderStatusEnum.values()) {
            if (status.equals(statusEnum.getStatus())) {
                return statusEnum.getMessage();
            }
        }
        return UNKNOWN.getMessage();
    }


}
