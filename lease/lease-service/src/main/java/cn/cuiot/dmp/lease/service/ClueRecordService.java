package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomConfigDetailReqDTO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.lease.dto.clue.ClueRecordDTO;
import cn.cuiot.dmp.lease.dto.clue.ClueRecordPageQueryDTO;
import cn.cuiot.dmp.lease.dto.clue.ClueRecordUpdateDTO;
import cn.cuiot.dmp.lease.entity.ClueEntity;
import cn.cuiot.dmp.lease.entity.ClueRecordEntity;
import cn.cuiot.dmp.lease.mapper.ClueMapper;
import cn.cuiot.dmp.lease.mapper.ClueRecordMapper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/6/1
 */
@Slf4j
@Service
public class ClueRecordService extends ServiceImpl<ClueRecordMapper, ClueRecordEntity> {

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    @Autowired
    private ClueMapper clueMapper;

    /**
     * 查询详情
     */
    public ClueRecordDTO queryForDetail(Long id) {
        ClueRecordEntity clueRecordEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        ClueRecordDTO clueRecordDTO = new ClueRecordDTO();
        BeanUtils.copyProperties(clueRecordEntity, clueRecordDTO);
        clueRecordDTO.setFormData(JSON.parseObject(clueRecordEntity.getFormData()));
        fillSystemOptionName(Lists.newArrayList(clueRecordDTO));
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
        List<ClueRecordDTO> clueRecordDTOList = clueRecordEntityList.stream()
                .map(o -> {
                    ClueRecordDTO clueRecordDTO = new ClueRecordDTO();
                    BeanUtils.copyProperties(o, clueRecordDTO);
                    clueRecordDTO.setFormData(JSON.parseObject(o.getFormData()));
                    return clueRecordDTO;
                })
                .collect(Collectors.toList());
        fillUserName(clueRecordDTOList);
        fillSystemOptionName(clueRecordDTOList);
        return clueRecordDTOList;
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
    @Transactional(rollbackFor = Exception.class)
    public boolean updateClueRecord(ClueRecordUpdateDTO updateDTO) {
        ClueRecordEntity clueRecordEntity = Optional.ofNullable(getById(updateDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(updateDTO, clueRecordEntity);
        clueRecordEntity.setFormData(String.valueOf(updateDTO.getFormData()));
        // 如果是最新的线索记录，则需要同步修改线索的对应字段
        ClueEntity clueEntity = Optional.ofNullable(clueMapper.selectById(clueRecordEntity.getClueId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        if (clueRecordEntity.getId().equals(clueEntity.getCurrentFollowRecordId())) {
            clueEntity.setCurrentFollowStatusId(clueRecordEntity.getFollowStatusId());
            clueMapper.updateById(clueEntity);
        }
        return updateById(clueRecordEntity);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteClueRecord(Long id) {
        // 如果删除的是最新的线索记录，则需要同步修改线索的对应字段
        ClueRecordEntity clueRecordEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        ClueEntity clueEntity = Optional.ofNullable(clueMapper.selectById(clueRecordEntity.getClueId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        if (clueRecordEntity.getId().equals(clueEntity.getCurrentFollowRecordId())) {
            ClueRecordPageQueryDTO queryDTO = new ClueRecordPageQueryDTO();
            queryDTO.setClueId(clueEntity.getId());
            List<ClueRecordDTO> clueRecordDTOList = queryForList(queryDTO);
            // 删除后仍存在线索记录，则更新线索状态为最新的一条
            if (CollectionUtils.isNotEmpty(clueRecordDTOList) && clueRecordDTOList.size() > 1) {
                ClueRecordDTO clueRecordDTO = clueRecordDTOList.get(1);
                clueEntity.setCurrentFollowRecordId(clueRecordDTO.getId());
                clueEntity.setCurrentFollowerId(clueRecordDTO.getFollowerId());
                clueEntity.setCurrentFollowTime(clueRecordDTO.getFollowTime());
                clueEntity.setCurrentFollowStatusId(clueRecordDTO.getFollowStatusId());
                clueMapper.updateById(clueEntity);
            } else {
                // 不存在则清空线索状态
                LambdaUpdateWrapper<ClueEntity> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(ClueEntity::getId, clueEntity.getId());
                updateWrapper.set(ClueEntity::getCurrentFollowRecordId, null);
                updateWrapper.set(ClueEntity::getCurrentFollowTime, null);
                updateWrapper.set(ClueEntity::getCurrentFollowStatusId, null);
                clueMapper.update(clueEntity, updateWrapper);
            }
        }
        return removeById(id);
    }

    /**
     * 根据线索ID删除
     */
    public boolean deleteClueRecordByClueId(Long clueId) {
        LambdaUpdateWrapper<ClueRecordEntity> updateWrapper = new LambdaUpdateWrapper<ClueRecordEntity>()
                .eq(ClueRecordEntity::getClueId, clueId);
        return remove(updateWrapper);
    }

    private PageResult<ClueRecordDTO> clueRecordEntity2ClueRecordDTOs(IPage<ClueRecordEntity> clueRecordEntityIPage) {
        PageResult<ClueRecordDTO> clueRecordDTOPageResult = new PageResult<>();
        List<ClueRecordDTO> clueRecordDTOList = clueRecordEntityIPage.getRecords().stream()
                .map(o -> {
                    ClueRecordDTO clueRecordDTO = new ClueRecordDTO();
                    BeanUtils.copyProperties(o, clueRecordDTO);
                    clueRecordDTO.setFormData(JSON.parseObject(o.getFormData()));
                    return clueRecordDTO;
                })
                .collect(Collectors.toList());
        fillUserName(clueRecordDTOList);
        fillSystemOptionName(clueRecordDTOList);
        clueRecordDTOPageResult.setList(clueRecordDTOList);
        clueRecordDTOPageResult.setCurrentPage((int) clueRecordEntityIPage.getCurrent());
        clueRecordDTOPageResult.setPageSize((int) clueRecordEntityIPage.getSize());
        clueRecordDTOPageResult.setTotal(clueRecordEntityIPage.getTotal());
        return clueRecordDTOPageResult;
    }

    /**
     * 使用用户id列表查询出，对应的用户名称
     *
     * @param clueRecordDTOList 线索记录列表
     */
    private void fillUserName(List<ClueRecordDTO> clueRecordDTOList) {
        if (CollectionUtils.isEmpty(clueRecordDTOList)) {
            return;
        }
        Set<Long> userIdList = new HashSet<>();
        clueRecordDTOList.forEach(o -> {
            if (StringUtils.isNotBlank(o.getCreatedBy())) {
                userIdList.add(Long.valueOf(o.getCreatedBy()));
            }
            if (StringUtils.isNotBlank(o.getUpdatedBy())) {
                userIdList.add(Long.valueOf(o.getUpdatedBy()));
            }
            if (Objects.nonNull(o.getFollowerId())) {
                userIdList.add(o.getFollowerId());
            }
        });
        BaseUserReqDto reqDto = new BaseUserReqDto();
        reqDto.setUserIdList(new ArrayList<>(userIdList));
        List<BaseUserDto> baseUserDtoList = systemApiFeignService.lookUpUserList(reqDto).getData();
        Map<Long, String> userMap = baseUserDtoList.stream().collect(Collectors.toMap(BaseUserDto::getId, BaseUserDto::getName));
        clueRecordDTOList.forEach(o -> {
            if (Objects.nonNull(o.getCreatedBy()) && userMap.containsKey(Long.valueOf(o.getCreatedBy()))) {
                o.setCreatedName(userMap.get(Long.valueOf(o.getCreatedBy())));
            }
            if (Objects.nonNull(o.getUpdatedBy()) && userMap.containsKey(Long.valueOf(o.getUpdatedBy()))) {
                o.setUpdatedName(userMap.get(Long.valueOf(o.getUpdatedBy())));
            }
            if (Objects.nonNull(o.getFollowerId()) && userMap.containsKey(o.getFollowerId())) {
                o.setFollowerName(userMap.get(o.getFollowerId()));
            }
        });
    }

    /**
     * 使用配置id列表查询出，对应的名称关系
     *
     * @param clueRecordDTOList 线索记录列表
     */
    private void fillSystemOptionName(List<ClueRecordDTO> clueRecordDTOList) {
        if (CollectionUtils.isEmpty(clueRecordDTOList)) {
            return;
        }
        Set<Long> configIdList = new HashSet<>();
        clueRecordDTOList.forEach(o -> {
            if (Objects.nonNull(o.getFollowStatusId())) {
                configIdList.add(o.getFollowStatusId());
            }
        });
        CustomConfigDetailReqDTO customConfigDetailReqDTO = new CustomConfigDetailReqDTO();
        customConfigDetailReqDTO.setCustomConfigDetailIdList(new ArrayList<>(configIdList));
        Map<Long, String> systemOptionMap = systemApiFeignService.batchQueryCustomConfigDetailsForMap(customConfigDetailReqDTO)
                .getData();
        clueRecordDTOList.forEach(o -> {
            if (Objects.nonNull(o.getFollowStatusId()) && systemOptionMap.containsKey(o.getFollowStatusId())) {
                o.setFollowStatusIdName(systemOptionMap.get(o.getFollowStatusId()));
            }
        });
    }

}
