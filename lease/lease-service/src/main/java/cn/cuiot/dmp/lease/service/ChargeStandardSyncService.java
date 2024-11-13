package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyRelationDTO;
import cn.cuiot.dmp.base.application.service.DataSyncService;
import cn.cuiot.dmp.lease.entity.charge.TbChargeStandard;
import cn.cuiot.dmp.lease.mapper.charge.TbChargeStandardMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 同步企业审收费标准
 *
 * @Author: zc
 * @Date: 2024-11-06
 */
@Slf4j
@Component
public class ChargeStandardSyncService extends DataSyncService<TbChargeStandard> {

    @Resource
    private TbChargeStandardMapper chargeStandardMapper;

    @Override
    public List<TbChargeStandard> fetchData(Long sourceCompanyId) {
        List<TbChargeStandard> list = chargeStandardMapper.selectList(
                new LambdaQueryWrapper<TbChargeStandard>()
                        .eq(TbChargeStandard::getCompanyId, sourceCompanyId));
        if (CollectionUtils.isEmpty(list)) {
            log.error("企业初始化【收费标准】异常，模板企业不存在数据");
        }
        return list;
    }

    @Override
    public List<SyncCompanyRelationDTO<TbChargeStandard>> preprocessData(List<TbChargeStandard> data, Long targetCompanyId) {
        return data.stream().map(item -> {
            Long oldId = item.getId();
            TbChargeStandard entity = new TbChargeStandard();
            entity.setId(IdWorker.getId());
            entity.setChargeProjectId(item.getChargeProjectId());
            entity.setCompanyId(targetCompanyId);
            entity.setChargeStandard(item.getChargeStandard());
            entity.setRemark(item.getRemark());
            entity.setStatus(item.getStatus());
            return new SyncCompanyRelationDTO<>(entity, oldId);
        }).collect(Collectors.toList());
    }

    @Override
    public void saveData(List<SyncCompanyRelationDTO<TbChargeStandard>> data, Long targetCompanyId) {
        data.forEach(item -> chargeStandardMapper.insert(item.getEntity()));
    }

    @Override
    public void syncAssociatedData(List<SyncCompanyRelationDTO<TbChargeStandard>> targetData, SyncCompanyDTO dto) {

    }
}
