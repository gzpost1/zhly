package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.lease.dto.price.PriceManageDetailCreateDTO;
import cn.cuiot.dmp.lease.entity.PriceManageDetailEntity;
import cn.cuiot.dmp.lease.mapper.PriceManageDetailMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/6/24
 */
@Slf4j
@Service
public class PriceManageDetailService extends ServiceImpl<PriceManageDetailMapper, PriceManageDetailEntity> {

    /**
     * 通过定价管理id查询定价管理明细
     */
    public List<PriceManageDetailEntity> queryByPriceId(Long priceId) {
        AssertUtil.notNull(priceId, "定价管理id不能为空");
        LambdaQueryWrapper<PriceManageDetailEntity> queryWrapper = new LambdaQueryWrapper<PriceManageDetailEntity>()
                .eq(PriceManageDetailEntity::getPriceId, priceId);
        List<PriceManageDetailEntity> priceManageDetailEntities = list(queryWrapper);
        if (CollectionUtils.isEmpty(priceManageDetailEntities)) {
            return new ArrayList<>();
        }
        return priceManageDetailEntities;
    }

    /**
     * 保存房屋定价明细
     */
    public void savePriceManageDetailList(Long priceId, List<PriceManageDetailCreateDTO> priceManageDetailCreateList) {
        AssertUtil.notNull(priceId, "定价管理编码不能为空");
        if (CollectionUtils.isEmpty(priceManageDetailCreateList)) {
            return;
        }
        List<PriceManageDetailEntity> priceManageDetailEntities = priceManageDetailCreateList.stream()
                .map(o -> {
                    PriceManageDetailEntity priceManageDetailEntity = new PriceManageDetailEntity();
                    BeanUtils.copyProperties(o, priceManageDetailEntity);
                    priceManageDetailEntity.setPriceId(priceId);
                    return priceManageDetailEntity;
                })
                .collect(Collectors.toList());
        saveBatch(priceManageDetailEntities);
    }

    /**
     * 根据定价管理id删除定价明细
     */
    public void deletePriceManageDetailList(Long priceId) {
        LambdaUpdateWrapper<PriceManageDetailEntity> updateWrapper = new LambdaUpdateWrapper<PriceManageDetailEntity>()
                .eq(PriceManageDetailEntity::getPriceId, priceId);
        remove(updateWrapper);
    }

    /**
     * 复制新增定价明细
     */
    public void copyPriceManageDetail(Long priceId, Long copyPriceId) {
        AssertUtil.notNull(priceId, "定价管理id不能为空");
        AssertUtil.notNull(copyPriceId, "复制定价管理id不能为空");
        LambdaQueryWrapper<PriceManageDetailEntity> queryWrapper = new LambdaQueryWrapper<PriceManageDetailEntity>()
                .eq(PriceManageDetailEntity::getPriceId, priceId);
        List<PriceManageDetailEntity> priceManageDetailEntities = list(queryWrapper);
        if (CollectionUtils.isEmpty(priceManageDetailEntities)) {
            return;
        }
        List<PriceManageDetailEntity> copyPriceManageDetailEntities = priceManageDetailEntities.stream()
                .peek(o -> {
                    o.setId(IdWorker.getId());
                    o.setPriceId(copyPriceId);
                })
                .collect(Collectors.toList());
        saveBatch(copyPriceManageDetailEntities);
    }

    /**
     * 根据房屋id查询对应的最新定价
     */
    public Map<Long, Integer> batchQueryHousePriceForMap(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new HashMap<>();
        }
        List<PriceManageDetailEntity> priceManageDetailEntities = baseMapper.batchQueryHousePrice(ids);
        if (CollectionUtils.isEmpty(priceManageDetailEntities)) {
            return new HashMap<>();
        }
        Map<Long, List<PriceManageDetailEntity>> priceManageDetailMap = priceManageDetailEntities.stream()
                .collect(Collectors.groupingBy(PriceManageDetailEntity::getHouseId));
        Map<Long, Integer> housePriceMap = new HashMap<>();
        priceManageDetailMap.forEach((k, v) -> {
            int price = v.get(0).getPrice();
            housePriceMap.put(k, price);
        });
        return housePriceMap;
    }

}
