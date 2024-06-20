package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description 房屋用户查询
 * @Date 2024/6/19 16:55
 * @Created by libo
 */
@Data
public class HouseCustomerQuery extends PageQuery {
    /**
     * 房屋主键
     */
    @NotNull(message = "房屋主键不能为空")
    private Long houseId;

    /**
     * 客户名称
     */
    private String customerUserName;
}
