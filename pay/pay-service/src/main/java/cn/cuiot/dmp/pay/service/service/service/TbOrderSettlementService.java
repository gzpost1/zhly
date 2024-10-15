package cn.cuiot.dmp.pay.service.service.service;

import cn.cuiot.dmp.pay.service.service.dto.OrderSettlementStatics;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.pay.service.service.entity.TbOrderSettlement;
import cn.cuiot.dmp.pay.service.service.mapper.TbOrderSettlementMapper;
@Service
public class TbOrderSettlementService extends ServiceImpl<TbOrderSettlementMapper, TbOrderSettlement> {

    public void insertList(List<TbOrderSettlement> orderSettlements) {
        baseMapper.insertList(orderSettlements);
    }

    public OrderSettlementStatics queryForStatics(LambdaQueryWrapper<TbOrderSettlement> queryWrapper) {
        return baseMapper.queryForStatics(queryWrapper);
    }
}
