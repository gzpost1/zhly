package cn.cuiot.dmp.archive.infrastructure.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * 房屋关联客户信息
 * @author: wuyongchong
 * @date: 2024/7/15 10:15
 */
@Data
public class HouseCustomerVo implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户名称
     */
    private String customerName;


    /**
     * 身份 1-业主 2-租户
     */
    private String identityType;


}
