package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.entity.BaseContractEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseRelateEntity;
import cn.cuiot.dmp.lease.mapper.TbContractLeaseRelateMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 租赁合同关联信息 服务实现类
 *
 * @author Mujun
 * @since 2024-07-03
 */
@Service
public class TbContractLeaseRelateService extends BaseMybatisServiceImpl<TbContractLeaseRelateMapper, TbContractLeaseRelateEntity> {
    /**
     * 记录意向合同关联信息
     *
     * @param entity
     * @param type
     */
    public void saveLeaseRelated(BaseContractEntity entity, Integer type, Long contractId) {
        TbContractLeaseRelateEntity leaseRelateEntity = new TbContractLeaseRelateEntity();
        leaseRelateEntity.setId(SnowflakeIdWorkerUtil.nextId());
        leaseRelateEntity.setContractId(contractId);
        leaseRelateEntity.setType(type);
        leaseRelateEntity.setDatetime(LocalDateTime.now());
        leaseRelateEntity.setOperator(LoginInfoHolder.getCurrentLoginInfo().getName());
        leaseRelateEntity.setOperatorId(LoginInfoHolder.getCurrentLoginInfo().getUserId());
        leaseRelateEntity.setName(entity.getName());
        leaseRelateEntity.setReason(entity.getContractNo());
        leaseRelateEntity.setExtId(Long.valueOf(entity.getContractNo()));
        leaseRelateEntity.setStatus(EntityConstants.DISABLED);
        save(leaseRelateEntity);
    }


    public void removeRelated(Long contractId,Integer type){
        LambdaQueryWrapper<TbContractLeaseRelateEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbContractLeaseRelateEntity::getContractId,contractId);
        queryWrapper.eq(TbContractLeaseRelateEntity::getType,type);
        remove(queryWrapper);
    }

    public void enableRelate(Long contractId,Integer type){
        LambdaQueryWrapper<TbContractLeaseRelateEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbContractLeaseRelateEntity::getContractId,contractId);
        queryWrapper.eq(TbContractLeaseRelateEntity::getType,type);
//        queryWrapper.eq(TbContractLeaseRelateEntity::getExtId,extContractNo);
        queryWrapper.last("limit 1");
        TbContractLeaseRelateEntity leaseRelateEntity = (TbContractLeaseRelateEntity) getOne(queryWrapper);
        leaseRelateEntity.setStatus(EntityConstants.ENABLED);
        updateById(leaseRelateEntity);

    }
}
