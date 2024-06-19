package cn.cuiot.dmp.common.constant;

/**
 * 房屋认证状态
 * @author: wuyongchong
 * @date: 2024/6/19 10:10
 */
public class UserHouseAuditStatusConstants {

    /**
     * 待审核
     */
    public final static Byte WAIT= 0;

    /**
     * 审核通过
     */
    public final static Byte PASS= 1;

    /**
     * 审核驳回
     */
    public final static Byte REJECT= 2;

    /**
     * 认证失效
     */
    public final static Byte INVALID= 3;
}
