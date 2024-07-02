package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.lease.entity.TbContractIntentionMoneyEntity;
import cn.cuiot.dmp.lease.mapper.TbContractIntentionMoneyMapper;
import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 意向金 服务实现类
 *
 * @author Mujun
 * @since 2024-06-12
 */
@Service
public class TbContractIntentionMoneyService extends BaseMybatisServiceImpl<TbContractIntentionMoneyMapper, TbContractIntentionMoneyEntity> {
    public List<TbContractIntentionMoneyEntity> queryByIds(List<Long> ids) {
        LambdaQueryWrapper<TbContractIntentionMoneyEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(ids), TbContractIntentionMoneyEntity::getId, ids);
        List<TbContractIntentionMoneyEntity> moneyList = list(queryWrapper);
        return moneyList;
    }

    /**
     * 清除不存在关联的意向金(因为产品没设计意向金的管理界面)
     */
    public void clearMoney() {
        baseMapper.clearMoney();
    }

    public void removeByContractId(Long id){
        LambdaQueryWrapper<TbContractIntentionMoneyEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbContractIntentionMoneyEntity::getContractId,id);
        remove(queryWrapper);
    }
}
