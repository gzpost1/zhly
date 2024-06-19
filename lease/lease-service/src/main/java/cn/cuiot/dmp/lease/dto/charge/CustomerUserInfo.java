package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

/**
 * @Description 用户信息
 * @Date 2024/6/19 14:55
 * @Created by libo
 */
@Data
public class CustomerUserInfo {
    /**
     * 客户id
     */
    private Long customerUserId;

    /**
     * 客户名称
     */
    private String customerUserName;

    /**
     * 客户手机号
     */
    private String customerUserPhone;

    /**
     * 客户身份
     */
    private String customerUserRoleName;

    /**
     * 业主身份
     */
    private Byte identityType;

    /**
     * 房屋ID
     */
    private Long houseId;
}
