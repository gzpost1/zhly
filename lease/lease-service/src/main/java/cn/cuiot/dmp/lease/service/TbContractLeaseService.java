package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.AuditConfigRspDTO;
import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import cn.cuiot.dmp.lease.mapper.TbContractLeaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Long code = SnowflakeIdWorkerUtil.nextId();
        TbContractLeaseEntity entity = (TbContractLeaseEntity) o;
        entity.setContractNo(String.valueOf(code));
        bindInfoService.createContractBind(entity);
        return super.save(entity);
    }

}
