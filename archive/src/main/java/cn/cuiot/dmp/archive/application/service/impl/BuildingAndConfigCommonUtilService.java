package cn.cuiot.dmp.archive.application.service.impl;

import cn.cuiot.dmp.archive.infrastructure.entity.BuildingArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.BuildingArchivesMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
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

    /**
     * 使用楼盘id列表查询出，对应的名称关系
     * @param loupanIds
     * @return
     */
    public Map<Long, String> getLoupanIdNameMap(Set<Long> loupanIds){
        List<BuildingArchivesEntity> list = buildingArchivesMapper.selectBatchIds(loupanIds);
        return list.stream().collect(Collectors.toMap(BuildingArchivesEntity::getId, BuildingArchivesEntity::getName));
    }

    /**
     * 使用楼盘名称查询出，对应的id关系
     * @param names
     * @return
     */
    public Map<String, Long> getLoupanNameIdMap(Set<String> names){
        LambdaQueryWrapper<BuildingArchivesEntity> wp = new LambdaQueryWrapper<>();
        wp.in(BuildingArchivesEntity::getName, names);
        List<BuildingArchivesEntity> list = buildingArchivesMapper.selectList(wp);
        return list.stream().collect(Collectors.toMap(BuildingArchivesEntity::getName, BuildingArchivesEntity::getId));
    }

    /**
     * 使用配置id列表查询出，对应的名称关系
     * @param configIds
     * @return
     */
    public Map<Long, String> getConfigIdNameMap(Set<Long> configIds){
        return null;
    }

    /**
     * 使用配置名称查询出，对应的id关系
     * @param names
     * @return
     */
    public Map<String, Long> getConfigNameIdMap(Set<String> names){
        return null;
    }

}
