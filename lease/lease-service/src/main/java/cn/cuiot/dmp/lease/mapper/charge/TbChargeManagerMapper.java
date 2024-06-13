package cn.cuiot.dmp.lease.mapper.charge;

import cn.cuiot.dmp.lease.dto.charge.ChargeHouseDetailDto;
import cn.cuiot.dmp.lease.dto.charge.ChargeManagerPageDto;
import cn.cuiot.dmp.lease.dto.charge.TbChargeManagerQuery;
import cn.cuiot.dmp.lease.entity.charge.TbChargeManager;
import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface TbChargeManagerMapper extends BaseMapper<TbChargeManager> {
    IPage<ChargeManagerPageDto> queryForPage(Page page, @Param("query") TbChargeManagerQuery query);

    ChargeHouseDetailDto queryForHouseDetail(Long id);

    int receivedAmount(@Param("received") TbChargeReceived received);
}