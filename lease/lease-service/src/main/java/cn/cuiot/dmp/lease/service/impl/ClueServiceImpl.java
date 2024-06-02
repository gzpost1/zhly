package cn.cuiot.dmp.lease.service.impl;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.lease.dto.clue.*;
import cn.cuiot.dmp.lease.entity.ClueEntity;
import cn.cuiot.dmp.lease.mapper.ClueMapper;
import cn.cuiot.dmp.lease.service.ClueService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/6/1
 */
@Slf4j
@Service
public class ClueServiceImpl extends ServiceImpl<ClueMapper, ClueEntity> implements ClueService {

    /**
     * 查询详情
     */
    @Override
    public ClueDTO queryForDetail(Long id) {
        ClueEntity clueEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        ClueDTO clueDTO = new ClueDTO();
        BeanUtils.copyProperties(clueEntity, clueDTO);
        return clueDTO;
    }

    @Override
    public List<ClueDTO> queryForList(CluePageQueryDTO queryDTO) {
        LambdaQueryWrapper<ClueEntity> queryWrapper = new LambdaQueryWrapper<ClueEntity>()
                .like((StringUtils.isNotBlank(queryDTO.getName())), ClueEntity::getName, queryDTO.getName())
                .eq((Objects.nonNull(queryDTO.getDepartmentId())), ClueEntity::getDepartmentId, queryDTO.getDepartmentId())
                .eq((Objects.nonNull(queryDTO.getBuildingId())), ClueEntity::getBuildingId, queryDTO.getBuildingId())
                .eq(Objects.nonNull(queryDTO.getSourceId()), ClueEntity::getSourceId, queryDTO.getSourceId())
                .ge(Objects.nonNull(queryDTO.getBeginTime()), ClueEntity::getCreatedOn, queryDTO.getBeginTime())
                .le(Objects.nonNull(queryDTO.getEndTime()), ClueEntity::getCreatedOn, queryDTO.getEndTime())
                .orderByDesc(ClueEntity::getCreatedOn);
        List<ClueEntity> clueEntityList = list(queryWrapper);
        if (CollectionUtils.isEmpty(clueEntityList)) {
            return new ArrayList<>();
        }
        return clueEntityList.stream()
                .map(o -> {
                    ClueDTO clueDTO = new ClueDTO();
                    BeanUtils.copyProperties(o, clueDTO);
                    return clueDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<ClueDTO> queryForPage(CluePageQueryDTO queryDTO) {
        LambdaQueryWrapper<ClueEntity> queryWrapper = new LambdaQueryWrapper<ClueEntity>()
                .like((StringUtils.isNotBlank(queryDTO.getName())), ClueEntity::getName, queryDTO.getName())
                .eq((Objects.nonNull(queryDTO.getDepartmentId())), ClueEntity::getDepartmentId, queryDTO.getDepartmentId())
                .eq((Objects.nonNull(queryDTO.getBuildingId())), ClueEntity::getBuildingId, queryDTO.getBuildingId())
                .eq(Objects.nonNull(queryDTO.getSourceId()), ClueEntity::getSourceId, queryDTO.getSourceId())
                .ge(Objects.nonNull(queryDTO.getBeginTime()), ClueEntity::getCreatedOn, queryDTO.getBeginTime())
                .le(Objects.nonNull(queryDTO.getEndTime()), ClueEntity::getCreatedOn, queryDTO.getEndTime())
                .orderByDesc(ClueEntity::getCreatedOn);
        IPage<ClueEntity> clueEntityIPage = page(new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize()), queryWrapper);
        if (CollectionUtils.isEmpty(clueEntityIPage.getRecords())) {
            return new PageResult<>();
        }
        return clueEntity2ClueDTOs(clueEntityIPage);
    }

    @Override
    public int saveClue(ClueCreateDTO createDTO) {
        return 0;
    }

    @Override
    public int updateClue(ClueUpdateDTO updateDTO) {
        return 0;
    }

    @Override
    public int distributeClue(Long currentFollowerId) {
        return 0;
    }

    @Override
    public int followClue(ClueFollowDTO followDTO) {
        return 0;
    }

    @Override
    public int finishClue(ClueFinishDTO finishDTO) {
        return 0;
    }

    @Override
    public int deleteClue(Long id) {
        return 0;
    }

    @Override
    public int batchDistributeClue(ClueBatchUpdateDTO batchUpdateDTO) {
        return 0;
    }

    @Override
    public int batchFinishClue(ClueBatchUpdateDTO batchUpdateDTO) {
        return 0;
    }

    @Override
    public int batchDeleteClue(List<Long> idList) {
        return 0;
    }

    private PageResult<ClueDTO> clueEntity2ClueDTOs(IPage<ClueEntity> clueEntityIPage) {
        PageResult<ClueDTO> clueDTOPageResult = new PageResult<>();
        List<ClueDTO> clueDTOList = clueEntityIPage.getRecords().stream()
                .map(o -> {
                    ClueDTO clueDTO = new ClueDTO();
                    BeanUtils.copyProperties(o, clueDTO);
                    return clueDTO;
                })
                .collect(Collectors.toList());
        clueDTOPageResult.setList(clueDTOList);
        clueDTOPageResult.setCurrentPage((int) clueEntityIPage.getCurrent());
        clueDTOPageResult.setPageSize((int) clueEntityIPage.getSize());
        clueDTOPageResult.setTotal(clueEntityIPage.getTotal());
        return clueDTOPageResult;
    }

}
