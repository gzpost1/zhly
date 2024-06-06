package cn.cuiot.dmp.system.application.service;

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
 * @date 2024/6/5
 */
@Slf4j
@Service
public class VisitorRecordService extends ServiceImpl<VisitorRecordMapper, VisitorRecordEntity> {

    /**
     * 查询详情
     */
    public VisitorRecordDTO queryForDetail(Long id) {
        VisitorRecordEntity visitorRecordEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        VisitorRecordDTO visitorRecordDTO = new VisitorRecordDTO();
        BeanUtils.copyProperties(visitorRecordEntity, visitorRecordDTO);
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
    public boolean saveVisitorRecord(VisitorRecordCreateDTO createDTO) {
        VisitorRecordEntity visitorRecordEntity = new VisitorRecordEntity();
        BeanUtils.copyProperties(createDTO, visitorRecordEntity);
        return save(visitorRecordEntity);
    }

    /**
     * 更新
     */
    public boolean updateVisitorRecord(VisitorRecordUpdateDTO updateDTO) {
        VisitorRecordEntity visitorRecordEntity = Optional.ofNullable(getById(updateDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(updateDTO, visitorRecordEntity);
        return updateById(visitorRecordEntity);
    }

    /**
     * 删除
     */
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
        visitorRecordDTOPageResult.setList(visitorRecordDTOList);
        visitorRecordDTOPageResult.setCurrentPage((int) visitorRecordEntityIPage.getCurrent());
        visitorRecordDTOPageResult.setPageSize((int) visitorRecordEntityIPage.getSize());
        visitorRecordDTOPageResult.setTotal(visitorRecordEntityIPage.getTotal());
        return visitorRecordDTOPageResult;
    }

}
