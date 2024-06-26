package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.lease.dto.price.PriceManageDetailCreateDTO;
import cn.cuiot.dmp.lease.entity.PriceManageDetailEntity;
import cn.cuiot.dmp.lease.mapper.PriceManageDetailMapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/6/24
 */
@Slf4j
@Service
public class PriceManageDetailService extends ServiceImpl<PriceManageDetailMapper, PriceManageDetailEntity> {

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

}
