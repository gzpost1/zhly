package cn.cuiot.dmp.base.infrastructure.dto.rsp;

import java.io.Serializable;
import lombok.Data;

/**
 * 客户与用户
 * @author: wuyongchong
 * @date: 2024/6/19 10:37
 */
@Data
public class CustomerUserRspDto implements Serializable {

    /**
     * 客户ID
     */
    private Long customerId;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 联系人
     */
    private String contactName;
    /**
     * 联系人手机号
     */
    private String contactPhone;
    /**
     * 关联用户ID
     */
    private Long userId;
    /**
     * 关联用户名称
     */
    private String userName;
}
