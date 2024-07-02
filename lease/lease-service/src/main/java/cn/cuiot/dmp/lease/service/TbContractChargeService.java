package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.lease.entity.TbContractChargeEntity;
import cn.cuiot.dmp.lease.mapper.TbContractChargeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

/**
 * 费用条款 服务实现类
 *
 * @author Mujun
 * @since 2024-06-24
 */
@Service
public class TbContractChargeService extends BaseMybatisServiceImpl<TbContractChargeMapper, TbContractChargeEntity> {

    public void removeByContractId(Long id){
        LambdaQueryWrapper<TbContractChargeEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbContractChargeEntity::getContractId,id);
        remove(queryWrapper);
    }
}
