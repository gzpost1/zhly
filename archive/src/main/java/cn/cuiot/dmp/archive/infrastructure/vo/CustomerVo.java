package cn.cuiot.dmp.archive.infrastructure.vo;

import cn.cuiot.dmp.archive.infrastructure.entity.CustomerEntity;
import java.util.List;
import lombok.Data;

/**
 * 客户信息
 * @author: wuyongchong
 * @date: 2024/6/12 11:22
 */
@Data
public class CustomerVo extends CustomerEntity {

    /**
     * 房屋列表
     */
    private List<CustomerHouseVo> houseList;

    /**
     * 成员列表
     */
    private List<CustomerMemberVo> memberList;

    /**
     * 车辆列表
     */
    private List<CustomerVehicleVo> vehicleList;
}
