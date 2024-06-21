package cn.cuiot.dmp.lease.mapper.charge;

import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeManager;
import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TbChargeManagerMapper extends BaseMapper<TbChargeManager> {
    IPage<ChargeManagerPageDto> queryForPage(Page page, @Param("query") TbChargeManagerQuery query);

    ChargeHouseDetailDto queryForHouseDetail(Long id);

    int receivedAmount(@Param("received") TbChargeReceived received);

    List<HouseInfoDto> getHouseInfoByIds(@Param("ids") List<Long> ids);

    ChargeHouseDetailDto getOwnerInfo(Long houseId);

    List<CustomerUserInfo> getUserInfo(@Param("houseIds") List<Long> houseIds,@Param("userIds") List<Long> userIds);

    List<CustomerUserInfo> getUserInfoByIds(@Param("userIds") List<Long> userIds);

    IPage<CustomerUserInfo> queryHouseCustmerPage(Page page, @Param("query")HouseCustomerQuery query);

    int insertList(@Param("list")List<TbChargeManager> list);


}