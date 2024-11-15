package cn.cuiot.dmp.system.application.service.initialize;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.base.application.service.DataSyncService;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyRelationDTO;
import cn.cuiot.dmp.system.infrastructure.entity.AuditConfigEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.AuditConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 同步企业审核配置表
 *
 * @Author: zc
 * @Date: 2024-11-06
 */
@Slf4j
@Component
public class AuditConfigSyncService extends DataSyncService<AuditConfigEntity> {

    @Resource
    private AuditConfigMapper auditConfigMapper;

    @Override
    public List<AuditConfigEntity> fetchData(Long sourceCompanyId) {
        List<AuditConfigEntity> list = auditConfigMapper.selectList(
                new LambdaQueryWrapper<AuditConfigEntity>()
                        .eq(AuditConfigEntity::getCompanyId, sourceCompanyId));
        if (CollectionUtils.isEmpty(list)) {
            log.error("企业初始化【审核配置表】异常，模板企业不存在数据");
        }
        return list;
    }

    @Override
    public List<SyncCompanyRelationDTO<AuditConfigEntity>> preprocessData(List<AuditConfigEntity> data, Long targetCompanyId) {
        return data.stream().map(item -> {
            AuditConfigEntity entity = new AuditConfigEntity();
            entity.setId(IdWorker.getId());
            entity.setName(item.getName());
            entity.setCompanyId(targetCompanyId);
            entity.setAuditConfigType(item.getAuditConfigType());
            entity.setStatus(item.getStatus());
            entity.setDeletedFlag(item.getDeletedFlag());

            return new SyncCompanyRelationDTO<>(entity, item.getId());
        }).collect(Collectors.toList());
    }

    @Override
    public void saveData(List<SyncCompanyRelationDTO<AuditConfigEntity>> data, Long targetCompanyId) {
        data.forEach(item -> auditConfigMapper.insert(item.getEntity()));
    }

    @Override
    public void syncAssociatedData(List<SyncCompanyRelationDTO<AuditConfigEntity>> targetData, SyncCompanyDTO dto) {

    }
}