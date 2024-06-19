package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.lease.dto.charge.ChargeHouseDetailDto;
import cn.cuiot.dmp.lease.dto.charge.CustomerUserInfo;
import cn.cuiot.dmp.lease.dto.charge.HouseInfoDto;
import cn.cuiot.dmp.lease.mapper.charge.TbChargeManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Description 收费管理中的房屋和用户相关服务
 * @Date 2024/6/19 14:56
 * @Created by libo
 */
@Service
public class ChargeHouseAndUserService {
    @Autowired
    private TbChargeManagerMapper chargeManagerMapper;

    public List<HouseInfoDto> getHouseInfoByIds(ArrayList<Long> ids) {
        return chargeManagerMapper.getHouseInfoByIds(ids);
    }

    public ChargeHouseDetailDto getOwnerInfo(Long houseId) {
        return chargeManagerMapper.getOwnerInfo(houseId);
    }

    public List<CustomerUserInfo> getUserInfo(List<Long> houseIds,List<Long> userIds) {
        return chargeManagerMapper.getUserInfo(houseIds,userIds);
    }
}
