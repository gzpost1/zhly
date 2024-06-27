package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.AuditConfigRspDTO;
import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import cn.cuiot.dmp.lease.mapper.TbContractLeaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 租赁合同 服务实现类
 *
 * @author Mujun
 * @since 2024-06-19
 */
@Service
public class TbContractLeaseService extends BaseMybatisServiceImpl<TbContractLeaseMapper, TbContractLeaseEntity> {
    @Autowired
    TbContractIntentionService contractIntentionService;
    @Autowired
    TbContractBindInfoService bindInfoService;
    @Autowired
    BaseContractService baseContractService;

    @Override
    public boolean save(Object o) {
        TbContractLeaseEntity entity = (TbContractLeaseEntity) o;
        if (Objects.isNull(entity.getContractNo())) {
            Long code = SnowflakeIdWorkerUtil.nextId();
            entity.setContractNo(String.valueOf(code));
        }
        super.save(entity);
        bindInfoService.createContractBind(entity);
        return true;
    }

    @Override
    public boolean updateById(Object o) {
        TbContractLeaseEntity entity = (TbContractLeaseEntity) o;
        bindInfoService.createContractBind(entity);
        return super.updateById(entity);
    }

    /**
     * 删除合同同时删除绑定信息
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        bindInfoService.removeByContractId(Long.valueOf(String.valueOf(id)));
        return super.removeById(id);
    }

    @Override
    public List<TbContractLeaseEntity> list(TbContractLeaseEntity params) {
        String houseName = params.getHouseName();
        if (StringUtils.isNotEmpty(houseName)) {
            List<Long> queryIds = bindInfoService.queryContractIdsByHouseName(houseName);
            params.setQueryIds(queryIds);
        }
        List<TbContractLeaseEntity> list = super.list(params);
        list.forEach(c -> {
            baseContractService.fullInfo(c);
        });
        return list;
    }

    /**
     * 取消续租合同关联
     */
    public void cancelReletBind(Long reletContractId) {
        LambdaQueryWrapper<TbContractLeaseEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbContractLeaseEntity::getReletContractId,reletContractId);
        queryWrapper.last("limit 1");
        TbContractLeaseEntity contractLeaseEntity = (TbContractLeaseEntity) getOne(queryWrapper);
        contractLeaseEntity.setReletContractId(null);
        saveOrUpdate(contractLeaseEntity);
    }
}
