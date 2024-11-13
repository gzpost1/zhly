package cn.cuiot.dmp.content.service;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyRelationDTO;
import cn.cuiot.dmp.base.application.service.DataSyncService;
import cn.cuiot.dmp.content.dal.entity.ContentModule;
import cn.cuiot.dmp.content.dal.mapper.ContentModuleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 同步企业审小程序配置
 *
 * @Author: zc
 * @Date: 2024-11-06
 */
@Slf4j
@Component
public class ContentModuleSyncService extends DataSyncService<ContentModule> {

    @Resource
    private ContentModuleMapper contentModuleMapper;

    @Override
    public List<ContentModule> fetchData(Long sourceCompanyId) {
        List<ContentModule> list = contentModuleMapper.selectList(
                new LambdaQueryWrapper<ContentModule>()
                        .eq(ContentModule::getCompanyId, sourceCompanyId));
        if (CollectionUtils.isEmpty(list)) {
            log.error("企业初始化【小程序配置】异常，模板企业不存在数据");
        }
        return list;
    }

    @Override
    public List<SyncCompanyRelationDTO<ContentModule>> preprocessData(List<ContentModule> data, Long targetCompanyId) {
        return data.stream().map(item -> {
            Long oldId = item.getId();
            ContentModule entity = new ContentModule();
            BeanUtils.copyProperties(item, entity);
            entity.setId(IdWorker.getId());
            entity.setCompanyId(targetCompanyId);
            return new SyncCompanyRelationDTO<>(entity, oldId);
        }).collect(Collectors.toList());
    }

    @Override
    public void saveData(List<SyncCompanyRelationDTO<ContentModule>> data, Long targetCompanyId) {
        data.forEach(item -> contentModuleMapper.insert(item.getEntity()));
    }

    @Override
    public void syncAssociatedData(List<SyncCompanyRelationDTO<ContentModule>> targetData, SyncCompanyDTO dto) {

    }

    @Override
    public void cleanSyncData(SyncCompanyDTO dto) {
        contentModuleMapper.delete(
                new LambdaQueryWrapper<ContentModule>()
                .eq(ContentModule::getCompanyId, dto.getTargetCompanyId()));
    }

}
