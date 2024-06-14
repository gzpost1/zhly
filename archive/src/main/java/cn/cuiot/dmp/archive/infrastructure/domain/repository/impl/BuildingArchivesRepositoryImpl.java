package cn.cuiot.dmp.archive.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.archive.domain.aggregate.BuildingArchives;
import cn.cuiot.dmp.archive.domain.aggregate.BuildingArchivesPageQuery;
import cn.cuiot.dmp.archive.domain.repository.BuildingArchivesRepository;
import cn.cuiot.dmp.archive.infrastructure.entity.BuildingArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.BuildingArchivesMapper;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Slf4j
@Service
public class BuildingArchivesRepositoryImpl implements BuildingArchivesRepository {

    @Autowired
    private BuildingArchivesMapper buildingArchivesMapper;

    @Override
    public BuildingArchives queryForDetail(Long id) {
        BuildingArchivesEntity buildingArchivesEntity = Optional.ofNullable(buildingArchivesMapper.selectById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BuildingArchives buildingArchives = new BuildingArchives();
        BeanUtils.copyProperties(buildingArchivesEntity, buildingArchives);
        return buildingArchives;
    }

    @Override
    public List<BuildingArchives> queryForList(BuildingArchivesPageQuery pageQuery) {
        LambdaQueryWrapper<BuildingArchivesEntity> queryWrapper = new LambdaQueryWrapper<BuildingArchivesEntity>()
                .eq(Objects.nonNull(pageQuery.getId()), BuildingArchivesEntity::getId, pageQuery.getId())
                .eq(Objects.nonNull(pageQuery.getCompanyId()), BuildingArchivesEntity::getCompanyId, pageQuery.getCompanyId())
                .eq(Objects.nonNull(pageQuery.getDepartmentId()), BuildingArchivesEntity::getDepartmentId, pageQuery.getDepartmentId())
                .like(StringUtils.isNotBlank(pageQuery.getName()), BuildingArchivesEntity::getName, pageQuery.getName())
                .in(CollectionUtils.isNotEmpty(pageQuery.getDepartmentIdList()), BuildingArchivesEntity::getDepartmentId, pageQuery.getDepartmentIdList())
                .in(CollectionUtils.isNotEmpty(pageQuery.getIdList()), BuildingArchivesEntity::getId, pageQuery.getIdList())
                .eq(StringUtils.isNotBlank(pageQuery.getAreaCode()), BuildingArchivesEntity::getAreaCode, pageQuery.getAreaCode());
        List<BuildingArchivesEntity> buildingArchivesEntityList = buildingArchivesMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(buildingArchivesEntityList)) {
            return new ArrayList<>();
        }
        return buildingArchivesEntityList.stream()
                .map(o -> {
                    BuildingArchives buildingArchives = new BuildingArchives();
                    BeanUtils.copyProperties(o, buildingArchives);
                    return buildingArchives;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<BuildingArchives> queryForPage(BuildingArchivesPageQuery pageQuery) {
        LambdaQueryWrapper<BuildingArchivesEntity> queryWrapper = new LambdaQueryWrapper<BuildingArchivesEntity>()
                .eq(Objects.nonNull(pageQuery.getId()), BuildingArchivesEntity::getId, pageQuery.getId())
                .eq(Objects.nonNull(pageQuery.getCompanyId()), BuildingArchivesEntity::getCompanyId, pageQuery.getCompanyId())
                .eq(Objects.nonNull(pageQuery.getDepartmentId()), BuildingArchivesEntity::getDepartmentId, pageQuery.getDepartmentId())
                .like(StringUtils.isNotBlank(pageQuery.getName()), BuildingArchivesEntity::getName, pageQuery.getName())
                .in(CollectionUtils.isNotEmpty(pageQuery.getDepartmentIdList()), BuildingArchivesEntity::getDepartmentId, pageQuery.getDepartmentIdList())
                .in(CollectionUtils.isNotEmpty(pageQuery.getIdList()), BuildingArchivesEntity::getId, pageQuery.getIdList())
                .eq(StringUtils.isNotBlank(pageQuery.getAreaCode()), BuildingArchivesEntity::getAreaCode, pageQuery.getAreaCode());
        if (StringUtils.isNotBlank(pageQuery.getKeyword())) {
            queryWrapper.and(eqw -> eqw.eq(BuildingArchivesEntity::getId, pageQuery.getKeyword())
                    .or().like(BuildingArchivesEntity::getName, pageQuery.getKeyword()));
        }
        queryWrapper.orderByDesc(BuildingArchivesEntity::getCreateTime);
        IPage<BuildingArchivesEntity> buildingArchivesEntityPage = buildingArchivesMapper.selectPage(
                new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), queryWrapper);
        if (CollectionUtils.isEmpty(buildingArchivesEntityPage.getRecords())) {
            return new PageResult<>();
        }
        return buildingArchivesEntity2BuildingArchives(buildingArchivesEntityPage);
    }

    @Override
    public int saveBuildingArchives(BuildingArchives buildingArchives) {
        BuildingArchivesEntity buildingArchivesEntity = new BuildingArchivesEntity();
        BeanUtils.copyProperties(buildingArchives, buildingArchivesEntity);
        return buildingArchivesMapper.insert(buildingArchivesEntity);
    }

    @Override
    public int updateBuildingArchives(BuildingArchives buildingArchives) {
        BuildingArchivesEntity buildingArchivesEntity = Optional.ofNullable(buildingArchivesMapper.selectById(buildingArchives.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(buildingArchives, buildingArchivesEntity);
        return buildingArchivesMapper.updateById(buildingArchivesEntity);
    }

    @Override
    public int deleteBuildingArchives(Long id) {
        return buildingArchivesMapper.deleteById(id);
    }

    @Override
    public int batchSaveBuildingArchives(List<BuildingArchives> buildingArchivesList, Long userId) {
        AssertUtil.notNull(userId, "用户id不能为空");
        List<BuildingArchivesEntity> buildingArchivesEntityList = buildingArchivesList.stream()
                .map(o -> {
                    BuildingArchivesEntity buildingArchivesEntity = new BuildingArchivesEntity();
                    BeanUtils.copyProperties(o, buildingArchivesEntity);
                    buildingArchivesEntity.setId(IdWorker.getId());
                    buildingArchivesEntity.setCreateUser(userId);
                    return buildingArchivesEntity;
                })
                .collect(Collectors.toList());
        return buildingArchivesMapper.batchSaveBuildingArchives(buildingArchivesEntityList);
    }

    @Override
    public int batchUpdateBuildingArchives(Long departmentId, List<Long> idList) {
        return buildingArchivesMapper.batchUpdateBuildingArchives(departmentId, idList);
    }

    @Override
    public int batchDeleteBuildingArchives(List<Long> idList) {
        return buildingArchivesMapper.deleteBatchIds(idList);
    }

    private PageResult<BuildingArchives> buildingArchivesEntity2BuildingArchives(IPage<BuildingArchivesEntity> buildingArchivesEntityPage) {
        PageResult<BuildingArchives> buildingArchivesPageResult = new PageResult<>();
        List<BuildingArchives> buildingArchivesList = buildingArchivesEntityPage.getRecords().stream()
                .map(o -> {
                    BuildingArchives buildingArchives = new BuildingArchives();
                    BeanUtils.copyProperties(o, buildingArchives);
                    return buildingArchives;
                })
                .collect(Collectors.toList());
        buildingArchivesPageResult.setList(buildingArchivesList);
        buildingArchivesPageResult.setCurrentPage((int) buildingArchivesEntityPage.getCurrent());
        buildingArchivesPageResult.setPageSize((int) buildingArchivesEntityPage.getSize());
        buildingArchivesPageResult.setTotal(buildingArchivesEntityPage.getTotal());
        return buildingArchivesPageResult;
    }

}
