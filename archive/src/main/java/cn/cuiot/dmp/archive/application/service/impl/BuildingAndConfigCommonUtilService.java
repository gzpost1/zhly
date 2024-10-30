package cn.cuiot.dmp.archive.application.service.impl;

import cn.cuiot.dmp.archive.infrastructure.entity.BuildingArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.BuildingArchivesMapper;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomConfigDetailReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomConfigReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CustomConfigDetailRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CustomConfigRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liujianyu
 * @description 楼盘和配置相关公共查询
 * @since 2024-05-22 14:31
 */
@Slf4j
@Service
public class BuildingAndConfigCommonUtilService {

    @Autowired
    private BuildingArchivesMapper buildingArchivesMapper;

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    /**
     * 使用楼盘id列表查询出，对应的名称关系
     *
     * @param loupanIds
     * @return
     */
    public Map<Long, String> getLoupanIdNameMap(Set<Long> loupanIds) {
        List<BuildingArchivesEntity> list = buildingArchivesMapper.selectBatchIds(loupanIds);
        return list.stream().collect(Collectors.toMap(BuildingArchivesEntity::getId, BuildingArchivesEntity::getName));
    }

    /**
     * 使用楼盘名称查询出，对应的id关系
     *
     * @param names
     * @return
     */
    public Map<String, Long> getLoupanNameIdMap(Set<String> names) {
        LambdaQueryWrapper<BuildingArchivesEntity> wp = new LambdaQueryWrapper<>();
        wp.in(BuildingArchivesEntity::getName, names);
        List<BuildingArchivesEntity> list = buildingArchivesMapper.selectList(wp);
        return list.stream().collect(Collectors.toMap(BuildingArchivesEntity::getName, BuildingArchivesEntity::getId));
    }

    /**
     * 使用配置id列表查询出，对应的名称关系
     *
     * @param configIds
     * @return
     */
    public Map<Long, String> getConfigIdNameMap(Set<Long> configIds) {
        if (CollectionUtils.isEmpty(configIds)) {
            return new HashMap<>();
        }
        CustomConfigDetailReqDTO customConfigDetailReqDTO = new CustomConfigDetailReqDTO();
        customConfigDetailReqDTO.setCustomConfigDetailIdList(new ArrayList<>(configIds));
        IdmResDTO<List<CustomConfigDetailRspDTO>> res = systemApiFeignService.batchQueryCustomConfigDetails(customConfigDetailReqDTO);
        return res.getData().stream().collect(Collectors.toMap(CustomConfigDetailRspDTO::getId, CustomConfigDetailRspDTO::getName));
    }

    /**
     * 使用配置名称查询出，对应的id关系
     *
     * @return
     */
    public Map<String, Map<String, Long>> getConfigNameIdMap(Long companyId, Byte type) {
        CustomConfigReqDTO customConfigReqDTO = new CustomConfigReqDTO();
        customConfigReqDTO.setCompanyId(companyId);
        customConfigReqDTO.setSystemOptionType(type);
        log.info("查询自定义配置的公司id:{}和type:{}", companyId, type);
        IdmResDTO<List<CustomConfigRspDTO>> res = systemApiFeignService.batchQueryCustomConfigs(customConfigReqDTO);
        log.info("查询自定义配置的返回结果{}", JSONObject.toJSONString(res));
        Map<String, Map<String, Long>> map = new HashMap<>();
        try {
            map = res.getData().stream()
                    .collect(Collectors.toMap(
                            CustomConfigRspDTO::getName,
                            dto -> {
                                if (CollectionUtils.isEmpty(dto.getCustomConfigDetailList())) {
                                    return new HashMap<>();
                                } else {
                                    return dto.getCustomConfigDetailList().stream()
                                            .collect(Collectors.toMap(
                                                    CustomConfigDetailRspDTO::getName,
                                                    CustomConfigDetailRspDTO::getId
                                            ));
                                }
                            }
                    ));
        } catch (Exception e) {
            log.error("获取自定义配置失败", e);
        }
        return map;
    }

}
