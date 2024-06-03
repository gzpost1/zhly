package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.lease.dto.clue.ClueRecordDTO;
import cn.cuiot.dmp.lease.dto.clue.ClueRecordPageQueryDTO;
import cn.cuiot.dmp.lease.dto.clue.ClueRecordUpdateDTO;
import cn.cuiot.dmp.lease.entity.ClueRecordEntity;
import cn.cuiot.dmp.lease.mapper.ClueRecordMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
public class ClueRecordService extends ServiceImpl<ClueRecordMapper, ClueRecordEntity> {

    /**
     * 查询详情
     */
    public ClueRecordDTO queryForDetail(Long id) {
        ClueRecordEntity clueRecordEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        ClueRecordDTO clueRecordDTO = new ClueRecordDTO();
        BeanUtils.copyProperties(clueRecordEntity, clueRecordDTO);
        return clueRecordDTO;
    }

    /**
     * 查询列表
     */
    public List<ClueRecordDTO> queryForList(ClueRecordPageQueryDTO queryDTO) {
        LambdaQueryWrapper<ClueRecordEntity> queryWrapper = new LambdaQueryWrapper<ClueRecordEntity>()
                .eq(Objects.nonNull(queryDTO.getClueId()), ClueRecordEntity::getClueId, queryDTO.getClueId())
                .orderByDesc(ClueRecordEntity::getCreatedOn);
        List<ClueRecordEntity> clueRecordEntityList = list(queryWrapper);
        if (CollectionUtils.isEmpty(clueRecordEntityList)) {
            return new ArrayList<>();
        }
        return clueRecordEntityList.stream()
                .map(o -> {
                    ClueRecordDTO clueRecordDTO = new ClueRecordDTO();
                    BeanUtils.copyProperties(o, clueRecordDTO);
                    return clueRecordDTO;
                })
                .collect(Collectors.toList());
    }

    /**
     * 查询分页列表
     */
    public PageResult<ClueRecordDTO> queryForPage(ClueRecordPageQueryDTO queryDTO) {
        LambdaQueryWrapper<ClueRecordEntity> queryWrapper = new LambdaQueryWrapper<ClueRecordEntity>()
                .eq(Objects.nonNull(queryDTO.getClueId()), ClueRecordEntity::getClueId, queryDTO.getClueId())
                .orderByDesc(ClueRecordEntity::getCreatedOn);
        IPage<ClueRecordEntity> clueRecordEntityIPage = page(new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize()),
                queryWrapper);
        if (CollectionUtils.isEmpty(clueRecordEntityIPage.getRecords())) {
            return new PageResult<>();
        }
        return clueRecordEntity2ClueRecordDTOs(clueRecordEntityIPage);
    }

    /**
     * 更新
     */
    public boolean updateClue(ClueRecordUpdateDTO updateDTO) {
        ClueRecordEntity clueRecordEntity = Optional.ofNullable(getById(updateDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(updateDTO, clueRecordEntity);
        return updateById(clueRecordEntity);
    }

    /**
     * 删除
     */
    public boolean deleteClue(Long id) {
        return removeById(id);
    }

    private PageResult<ClueRecordDTO> clueRecordEntity2ClueRecordDTOs(IPage<ClueRecordEntity> clueRecordEntityIPage) {
        PageResult<ClueRecordDTO> clueRecordDTOPageResult = new PageResult<>();
        List<ClueRecordDTO> clueRecordDTOList = clueRecordEntityIPage.getRecords().stream()
                .map(o -> {
                    ClueRecordDTO clueRecordDTO = new ClueRecordDTO();
                    BeanUtils.copyProperties(o, clueRecordDTO);
                    return clueRecordDTO;
                })
                .collect(Collectors.toList());
        clueRecordDTOPageResult.setList(clueRecordDTOList);
        clueRecordDTOPageResult.setCurrentPage((int) clueRecordEntityIPage.getCurrent());
        clueRecordDTOPageResult.setPageSize((int) clueRecordEntityIPage.getSize());
        clueRecordDTOPageResult.setTotal(clueRecordEntityIPage.getTotal());
        return clueRecordDTOPageResult;
    }

}
