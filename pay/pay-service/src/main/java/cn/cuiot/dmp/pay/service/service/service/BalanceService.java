package cn.cuiot.dmp.pay.service.service.service;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.pay.service.service.entity.BalanceEntity;
import cn.cuiot.dmp.pay.service.service.mapper.BalanceMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员钱包表 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2023-11-29
 */
@Service
public class BalanceService extends ServiceImpl<BalanceMapper, BalanceEntity> {

    /**
     * 余额变更
     *
     * @param entity
     */
    public void changeBalance(BalanceEntity entity) {
        if (entity.getBalance() == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "余额变更数据库失败,变更余额不能为空");
        }
        int count = baseMapper.changeBalance(entity);
        if (count == 0) {
            //重试一次
            BalanceEntity old = baseMapper.selectById(entity.getHouseId());
            entity.setVersion(old.getVersion());
            count = baseMapper.changeBalance(entity);
            if (count == 0) {
                throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "余额变更数据库失败");
            }
        }
    }

    /**
     * 创建余额账户
     *
     * @param houseId
     */
    public BalanceEntity createBalance(Long houseId) {
        BalanceEntity balanceEntity = new BalanceEntity();
        balanceEntity.setHouseId(houseId);
        balanceEntity.setBalance(0);
        balanceEntity.setVersion(1);
        this.saveOrUpdate(balanceEntity);
        return balanceEntity;
    }


}
