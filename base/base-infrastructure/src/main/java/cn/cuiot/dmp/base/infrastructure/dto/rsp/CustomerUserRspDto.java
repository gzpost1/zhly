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
    private Long customerId;
    private String customerName;
    private Long userId;
    private String userName;
}
