package cn.cuiot.dmp.pay.service.service.mapper;

import cn.cuiot.dmp.pay.service.service.dto.OrderSettlementStatics;
import cn.cuiot.dmp.pay.service.service.entity.TbOrderSettlement;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbOrderSettlementMapper extends BaseMapper<TbOrderSettlement> {
    int insertList(@Param("list") List<TbOrderSettlement> list);


    OrderSettlementStatics queryForStatics(@Param("ew") LambdaQueryWrapper<TbOrderSettlement> queryWrapper);
}