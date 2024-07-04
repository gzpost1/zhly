package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.lease.dto.charge.ChargeHangupQueryDto;
import cn.cuiot.dmp.lease.dto.charge.PaidInManageMentQuery;
import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import cn.cuiot.dmp.lease.mapper.charge.TbChargeReceivedMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TbChargeReceivedService extends ServiceImpl<TbChargeReceivedMapper, TbChargeReceived> {

    public void insertList(List<TbChargeReceived> receiveds) {
        baseMapper.insertList(receiveds);
    }

    /**
     * 分页查询
     * @param query
     * @return
     */
    public IPage<TbChargeReceived> queryForPage(ChargeHangupQueryDto query) {
        LambdaQueryWrapper<TbChargeReceived> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TbChargeReceived::getChargeId, query.getChargeId());
        wrapper.orderByDesc(TbChargeReceived::getCreateTime);
        return this.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
    }

    /**
     * 实收管理分页查询
     * @param query
     * @return
     */
    public IPage<TbChargeReceived> queryForPaidinPage(PaidInManageMentQuery query) {
        LambdaQueryWrapper<TbChargeReceived> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TbChargeReceived::getHouseId, query.getHouseId());
        if(StringUtils.isNotBlank(query.getReceivableCode())){
            wrapper.like(TbChargeReceived::getChargeId, query.getReceivableCode());
        }
        if(StringUtils.isNotBlank(query.getReceivedCode())){
            wrapper.like(TbChargeReceived::getId, query.getReceivedCode());
        }

        if(Objects.nonNull(query.getCustomerUserId())){
            wrapper.eq(TbChargeReceived::getCustomerUserId, query.getCustomerUserId());
        }
        if(Objects.nonNull(query.getChargeItemId())){
            wrapper.eq(TbChargeReceived::getChargeItemId, query.getChargeItemId());
        }
        if(Objects.nonNull(query.getOwnershipPeriodBegin())){
            wrapper.ge(TbChargeReceived::getOwnershipPeriodBegin, query.getOwnershipPeriodBegin());
        }
        if(Objects.nonNull(query.getOwnershipPeriodEnd())){
            wrapper.le(TbChargeReceived::getOwnershipPeriodEnd, query.getOwnershipPeriodEnd());
        }
        wrapper.orderByDesc(TbChargeReceived::getCreateTime);
        return this.page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper);
    }
}
