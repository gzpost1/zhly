package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.*;
import cn.cuiot.dmp.base.application.service.DataSyncService;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.lease.entity.charge.TbChargeStandard;
import cn.cuiot.dmp.lease.mapper.charge.TbChargeStandardMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyCacheConstant.*;

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
    @Autowired
    private RedisUtil redisUtil;

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
        List<SyncCompanyRelationDTO<TbChargeStandard>> collect = data.stream().map(item -> {
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

        if (CollectionUtils.isNotEmpty(collect)) {
            handleChargeProjectId(collect, targetCompanyId);
        }

        return collect;
    }

    @Override
    public void saveData(List<SyncCompanyRelationDTO<TbChargeStandard>> data, Long targetCompanyId) {
        data.forEach(item -> chargeStandardMapper.insert(item.getEntity()));
    }

    @Override
    public void syncAssociatedData(List<SyncCompanyRelationDTO<TbChargeStandard>> targetData, SyncCompanyDTO dto) {

    }

    /**
     * 设置收费项目Id
     */
    private void handleChargeProjectId(List<SyncCompanyRelationDTO<TbChargeStandard>> list, Long targetCompanyId) {
        list.forEach(item -> {
            Long chargeProjectId;
            TbChargeStandard entity = item.getEntity();
            if (Objects.nonNull(entity) && Objects.nonNull(chargeProjectId = entity.getChargeProjectId())) {

                Map<Long, CommonOptionTypeSyncDTO> commonOptionTypeMap = getCommonOptionTypeMap(targetCompanyId);
                Map<Long, CommonOptionSyncDTO> commonOptionMap = getCommonOptionMap(targetCompanyId);
                Map<Long, CommonOptionSettingSyncDTO> commonOptionSettingMap = getCommonOptionSettingMap(targetCompanyId);

                Long newChargeProjectId = null;
                if (commonOptionTypeMap.containsKey(chargeProjectId)) {
                    newChargeProjectId = Optional.ofNullable(commonOptionTypeMap.get(chargeProjectId))
                            .map(CommonOptionTypeSyncDTO::getId)
                            .orElse(null);
                } else if (commonOptionMap.containsKey(chargeProjectId)) {
                    newChargeProjectId = Optional.ofNullable(commonOptionMap.get(chargeProjectId))
                            .map(CommonOptionSyncDTO::getId)
                            .orElse(null);
                } else if (commonOptionSettingMap.containsKey(chargeProjectId)) {
                    newChargeProjectId = Optional.ofNullable(commonOptionSettingMap.get(chargeProjectId))
                            .map(CommonOptionSettingSyncDTO::getId)
                            .orElse(null);
                }
                entity.setChargeProjectId(newChargeProjectId);
            }
        });
    }

    /**
     * 获取选项类型信息缓存数据
     *
     * @return Map
     * @Param targetCompanyId 企业id
     */
    private Map<Long, CommonOptionTypeSyncDTO> getCommonOptionTypeMap(Long targetCompanyId) {
        String redisJson = redisUtil.get(COMPANY_INITIALIZE + targetCompanyId + ":" + COMMON_OPTION_TYPE);

        if (StringUtils.isNotBlank(redisJson)) {
            List<SyncCompanyRelationDTO<CommonOptionTypeSyncDTO>> beans = JsonUtil.readValue(redisJson,
                    new TypeReference<List<SyncCompanyRelationDTO<CommonOptionTypeSyncDTO>>>() {
                    });
            if (CollectionUtils.isNotEmpty(beans)) {
                return beans.stream()
                        .filter(e -> Objects.nonNull(e.getEntity()) && Objects.equals(e.getEntity().getCategory(), (byte) 2))
                        .collect(Collectors.toMap(SyncCompanyRelationDTO::getOldId, SyncCompanyRelationDTO::getEntity));
            }
        }
        return Maps.newHashMap();
    }

    /**
     * 获取选项类型信息缓存数据
     *
     * @return Map
     * @Param targetCompanyId 企业id
     */
    private Map<Long, CommonOptionSyncDTO> getCommonOptionMap(Long targetCompanyId) {
        String redisJson = redisUtil.get(COMPANY_INITIALIZE + targetCompanyId + ":" + COMMON_OPTION);

        if (StringUtils.isNotBlank(redisJson)) {
            List<SyncCompanyRelationDTO<CommonOptionSyncDTO>> beans = JsonUtil.readValue(redisJson,
                    new TypeReference<List<SyncCompanyRelationDTO<CommonOptionSyncDTO>>>() {
                    });
            if (CollectionUtils.isNotEmpty(beans)) {
                return beans.stream()
                        .filter(e -> Objects.nonNull(e.getEntity()) && Objects.equals(e.getEntity().getTypeCategory(), (byte) 2))
                        .collect(Collectors.toMap(SyncCompanyRelationDTO::getOldId, SyncCompanyRelationDTO::getEntity));
            }
        }
        return Maps.newHashMap();
    }

    /**
     * 获取选项值信息缓存数据
     *
     * @return Map
     * @Param targetCompanyId 企业id
     */
    private Map<Long, CommonOptionSettingSyncDTO> getCommonOptionSettingMap(Long targetCompanyId) {
        String redisJson = redisUtil.get(COMPANY_INITIALIZE + targetCompanyId + ":" + COMMON_OPTION_SETTING);

        if (StringUtils.isNotBlank(redisJson)) {
            List<SyncCompanyRelationDTO<CommonOptionSettingSyncDTO>> beans = JsonUtil.readValue(redisJson,
                    new TypeReference<List<SyncCompanyRelationDTO<CommonOptionSettingSyncDTO>>>() {
                    });
            if (CollectionUtils.isNotEmpty(beans)) {
                return beans.stream().collect(Collectors.toMap(SyncCompanyRelationDTO::getOldId, SyncCompanyRelationDTO::getEntity));
            }
        }
        return Maps.newHashMap();
    }
}
