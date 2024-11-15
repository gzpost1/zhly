package cn.cuiot.dmp.system.application.service.initialize;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.base.application.service.DataSyncService;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyRelationDTO;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.system.infrastructure.entity.CustomConfigDetailEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CustomConfigDetailMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyCacheConstant.COMPANY_INITIALIZE;
import static cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyCacheConstant.CUSTOM_CONFIG_DETAIL;

/**
 * 同步企业常用选项（系统选项）
 *
 * @Author: zc
 * @Date: 2024-11-06
 */
@Slf4j
@Component
public class CustomConfigDetailSyncService extends DataSyncService<CustomConfigDetailEntity> {

    @Resource
    private CustomConfigDetailMapper customConfigDetailMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<CustomConfigDetailEntity> fetchData(Long sourceCompanyId) {
        List<CustomConfigDetailEntity> list = customConfigDetailMapper.selectList(
                new LambdaQueryWrapper<CustomConfigDetailEntity>()
                        .eq(CustomConfigDetailEntity::getCompanyId, sourceCompanyId));
        if (CollectionUtils.isEmpty(list)) {
            log.error("企业初始化【常用选项-系统选项】异常，模板企业不存在数据");
        }
        return list;
    }

    @Override
    public List<SyncCompanyRelationDTO<CustomConfigDetailEntity>> preprocessData(List<CustomConfigDetailEntity> data, Long targetCompanyId) {
        List<SyncCompanyRelationDTO<CustomConfigDetailEntity>> collect = data.stream().map(item -> {
            Long oldId = item.getId();
            CustomConfigDetailEntity entity = new CustomConfigDetailEntity();
            entity.setId(IdWorker.getId());
            entity.setName(item.getName());
            entity.setCompanyId(targetCompanyId);
            entity.setCustomConfigId(item.getCustomConfigId());
            entity.setSort(item.getSort());
            entity.setStatus(item.getStatus());
            entity.setDeletedFlag(item.getDeletedFlag());
            return new SyncCompanyRelationDTO<>(entity, oldId);
        }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(collect)) {
            redisUtil.set(COMPANY_INITIALIZE + targetCompanyId + ":" + CUSTOM_CONFIG_DETAIL, JsonUtil.writeValueAsString(collect), Const.ONE_DAY_SECOND);
        }

        return collect;
    }

    @Override
    public void saveData(List<SyncCompanyRelationDTO<CustomConfigDetailEntity>> data, Long targetCompanyId) {
        List<CustomConfigDetailEntity> collect = data.stream().map(SyncCompanyRelationDTO::getEntity).collect(Collectors.toList());
        customConfigDetailMapper.batchSaveCustomConfigDetails(collect);
    }

    @Override
    public void syncAssociatedData(List<SyncCompanyRelationDTO<CustomConfigDetailEntity>> targetData, SyncCompanyDTO dto) {

    }
}
