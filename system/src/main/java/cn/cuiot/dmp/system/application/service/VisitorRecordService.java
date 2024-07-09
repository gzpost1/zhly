package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.system.application.param.dto.VisitorRecordCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.VisitorRecordDTO;
import cn.cuiot.dmp.system.application.param.dto.VisitorRecordPageQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.VisitorRecordUpdateDTO;
import cn.cuiot.dmp.system.infrastructure.entity.VisitorRecordEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.VisitorRecordMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/6/5
 */
@Slf4j
@Service
public class VisitorRecordService extends ServiceImpl<VisitorRecordMapper, VisitorRecordEntity> {

    @Autowired
    private ArchiveFeignService archiveFeignService;

    /**
     * 查询详情
     */
    public VisitorRecordDTO queryForDetail(Long id) {
        VisitorRecordEntity visitorRecordEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        VisitorRecordDTO visitorRecordDTO = new VisitorRecordDTO();
        BeanUtils.copyProperties(visitorRecordEntity, visitorRecordDTO);
        fillBuildingName(Lists.newArrayList(visitorRecordDTO));
        return visitorRecordDTO;
    }

    /**
     * 查询列表
     */
    public List<VisitorRecordDTO> queryForList(VisitorRecordPageQueryDTO queryDTO) {
        LambdaQueryWrapper<VisitorRecordEntity> queryWrapper = new LambdaQueryWrapper<VisitorRecordEntity>()
                .eq(Objects.nonNull(queryDTO.getCreatorId()), VisitorRecordEntity::getCreatedBy, queryDTO.getCreatorId());
        List<VisitorRecordEntity> visitorRecordEntityList = list(queryWrapper);
        if (CollectionUtils.isEmpty(visitorRecordEntityList)) {
            return new ArrayList<>();
        }
        return visitorRecordEntityList.stream()
                .map(o -> {
                    VisitorRecordDTO visitorRecordDTO = new VisitorRecordDTO();
                    BeanUtils.copyProperties(o, visitorRecordDTO);
                    return visitorRecordDTO;
                })
                .collect(Collectors.toList());
    }

    /**
     * 查询分页列表
     */
    public PageResult<VisitorRecordDTO> queryForPage(VisitorRecordPageQueryDTO queryDTO) {
        LambdaQueryWrapper<VisitorRecordEntity> queryWrapper = new LambdaQueryWrapper<VisitorRecordEntity>()
                .eq(Objects.nonNull(queryDTO.getCreatorId()), VisitorRecordEntity::getCreatedBy, queryDTO.getCreatorId());
        IPage<VisitorRecordEntity> visitorRecordEntityIPage = page(new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize()), queryWrapper);
        if (CollectionUtils.isEmpty(visitorRecordEntityIPage.getRecords())) {
            return new PageResult<>();
        }
        return visitorRecordEntity2VisitorRecordDTOs(visitorRecordEntityIPage);
    }

    /**
     * 保存
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveVisitorRecord(VisitorRecordCreateDTO createDTO) {
        VisitorRecordEntity visitorRecordEntity = new VisitorRecordEntity();
        BeanUtils.copyProperties(createDTO, visitorRecordEntity);
        return save(visitorRecordEntity);
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateVisitorRecord(VisitorRecordUpdateDTO updateDTO) {
        VisitorRecordEntity visitorRecordEntity = Optional.ofNullable(getById(updateDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(updateDTO, visitorRecordEntity);
        return updateById(visitorRecordEntity);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteVisitorRecord(Long id) {
        return removeById(id);
    }

    private PageResult<VisitorRecordDTO> visitorRecordEntity2VisitorRecordDTOs(IPage<VisitorRecordEntity> visitorRecordEntityIPage) {
        PageResult<VisitorRecordDTO> visitorRecordDTOPageResult = new PageResult<>();
        List<VisitorRecordDTO> visitorRecordDTOList = visitorRecordEntityIPage.getRecords().stream()
                .map(o -> {
                    VisitorRecordDTO visitorRecordDTO = new VisitorRecordDTO();
                    BeanUtils.copyProperties(o, visitorRecordDTO);
                    return visitorRecordDTO;
                })
                .collect(Collectors.toList());
        fillBuildingName(visitorRecordDTOList);
        visitorRecordDTOPageResult.setList(visitorRecordDTOList);
        visitorRecordDTOPageResult.setCurrentPage((int) visitorRecordEntityIPage.getCurrent());
        visitorRecordDTOPageResult.setPageSize((int) visitorRecordEntityIPage.getSize());
        visitorRecordDTOPageResult.setTotal(visitorRecordEntityIPage.getTotal());
        return visitorRecordDTOPageResult;
    }

    /**
     * 使用楼盘id列表查询出，对应的楼盘名称
     *
     * @param visitorRecordDTOList 访客记录列表
     */
    private void fillBuildingName(List<VisitorRecordDTO> visitorRecordDTOList) {
        if (CollectionUtils.isEmpty(visitorRecordDTOList)) {
            return;
        }
        Set<Long> buildingIdList = new HashSet<>();
        visitorRecordDTOList.forEach(o -> {
            if (Objects.nonNull(o.getBuildingId())) {
                buildingIdList.add(o.getBuildingId());
            }
        });
        BuildingArchiveReq buildingArchiveReq = new BuildingArchiveReq();
        buildingArchiveReq.setIdList(new ArrayList<>(buildingIdList));
        List<BuildingArchive> buildingArchiveList = archiveFeignService.buildingArchiveQueryForList(buildingArchiveReq).getData();
        if (CollectionUtils.isEmpty(buildingArchiveList)) {
            return;
        }
        Map<Long, List<BuildingArchive>> buildingMap = buildingArchiveList.stream()
                .collect(Collectors.groupingBy(BuildingArchive::getId));
        visitorRecordDTOList.forEach(o -> {
            if (Objects.nonNull(o.getBuildingId()) && buildingMap.containsKey(o.getBuildingId())) {
                o.setBuildingName(buildingMap.get(o.getBuildingId()).get(0).getName());
            }
        });
    }

}
