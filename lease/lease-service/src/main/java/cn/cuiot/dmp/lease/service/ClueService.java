package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.infrastructure.dto.req.CustomConfigDetailReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.FormConfigReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.FormConfigRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.SystemFormConfigConstant;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.lease.dto.clue.*;
import cn.cuiot.dmp.lease.entity.ClueEntity;
import cn.cuiot.dmp.lease.entity.ClueRecordEntity;
import cn.cuiot.dmp.lease.enums.ClueStatusEnum;
import cn.cuiot.dmp.lease.mapper.ClueMapper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
public class ClueService extends ServiceImpl<ClueMapper, ClueEntity> {

    @Autowired
    private ClueRecordService clueRecordService;

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    /**
     * 查询详情
     */
    public ClueDTO queryForDetail(Long id) {
        ClueEntity clueEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        ClueDTO clueDTO = new ClueDTO();
        BeanUtils.copyProperties(clueEntity, clueDTO);
        clueDTO.setFormData(JSON.parseObject(clueEntity.getFormData()));
        fillSystemOptionName(Lists.newArrayList(clueDTO));
        return clueDTO;
    }

    /**
     * 查询列表
     */
    public List<ClueDTO> queryForList(CluePageQueryDTO queryDTO) {
        LambdaQueryWrapper<ClueEntity> queryWrapper = new LambdaQueryWrapper<ClueEntity>()
                .like(StringUtils.isNotBlank(queryDTO.getName()), ClueEntity::getName, queryDTO.getName())
                .eq(Objects.nonNull(queryDTO.getDepartmentId()), ClueEntity::getDepartmentId, queryDTO.getDepartmentId())
                .eq(Objects.nonNull(queryDTO.getBuildingId()), ClueEntity::getBuildingId, queryDTO.getBuildingId())
                .eq(Objects.nonNull(queryDTO.getSourceId()), ClueEntity::getSourceId, queryDTO.getSourceId())
                .eq(Objects.nonNull(queryDTO.getStatus()), ClueEntity::getStatus, queryDTO.getStatus())
                .eq(Objects.nonNull(queryDTO.getCurrentUserId()), ClueEntity::getCreatedBy, queryDTO.getCurrentUserId())
                .eq(Objects.nonNull(queryDTO.getCurrentFollowerId()), ClueEntity::getCurrentFollowerId, queryDTO.getCurrentFollowerId())
                .ge(Objects.nonNull(queryDTO.getBeginTime()), ClueEntity::getCreatedOn, queryDTO.getBeginTime())
                .le(Objects.nonNull(queryDTO.getEndTime()), ClueEntity::getCreatedOn, queryDTO.getEndTime())
                .orderByDesc(ClueEntity::getCreatedOn);
        List<ClueEntity> clueEntityList = list(queryWrapper);
        if (CollectionUtils.isEmpty(clueEntityList)) {
            return new ArrayList<>();
        }
        List<ClueDTO> clueDTOList = clueEntityList.stream()
                .map(o -> {
                    ClueDTO clueDTO = new ClueDTO();
                    BeanUtils.copyProperties(o, clueDTO);
                    clueDTO.setFormData(JSON.parseObject(o.getFormData()));
                    return clueDTO;
                })
                .collect(Collectors.toList());
        fillSystemOptionName(clueDTOList);
        return clueDTOList;
    }

    /**
     * 查询分页列表
     */
    public PageResult<ClueDTO> queryForPage(CluePageQueryDTO queryDTO) {
        LambdaQueryWrapper<ClueEntity> queryWrapper = new LambdaQueryWrapper<ClueEntity>()
                .like(StringUtils.isNotBlank(queryDTO.getName()), ClueEntity::getName, queryDTO.getName())
                .eq(Objects.nonNull(queryDTO.getDepartmentId()), ClueEntity::getDepartmentId, queryDTO.getDepartmentId())
                .eq(Objects.nonNull(queryDTO.getBuildingId()), ClueEntity::getBuildingId, queryDTO.getBuildingId())
                .eq(Objects.nonNull(queryDTO.getSourceId()), ClueEntity::getSourceId, queryDTO.getSourceId())
                .eq(Objects.nonNull(queryDTO.getStatus()), ClueEntity::getStatus, queryDTO.getStatus())
                .eq(Objects.nonNull(queryDTO.getCurrentUserId()), ClueEntity::getCreatedBy, queryDTO.getCurrentUserId())
                .eq(Objects.nonNull(queryDTO.getCurrentFollowerId()), ClueEntity::getCurrentFollowerId, queryDTO.getCurrentFollowerId())
                .ge(Objects.nonNull(queryDTO.getBeginTime()), ClueEntity::getCreatedOn, queryDTO.getBeginTime())
                .le(Objects.nonNull(queryDTO.getEndTime()), ClueEntity::getCreatedOn, queryDTO.getEndTime())
                .orderByDesc(ClueEntity::getCreatedOn);
        IPage<ClueEntity> clueEntityIPage = page(new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize()), queryWrapper);
        if (CollectionUtils.isEmpty(clueEntityIPage.getRecords())) {
            return new PageResult<>();
        }
        return clueEntity2ClueDTOs(clueEntityIPage);
    }

    /**
     * 保存
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveClue(ClueCreateDTO createDTO) {
        ClueEntity clueEntity = new ClueEntity();
        BeanUtils.copyProperties(createDTO, clueEntity);
        clueEntity.setFormData(String.valueOf(createDTO.getFormData()));
        // 保存线索表单快照
        FormConfigReqDTO formConfigReqDTO = new FormConfigReqDTO();
        formConfigReqDTO.setCompanyId(createDTO.getCompanyId());
        formConfigReqDTO.setName(SystemFormConfigConstant.CLUE_FORM_CONFIG.get(0));
        FormConfigRspDTO formConfigRspDTO = systemApiFeignService.lookUpFormConfigByName(formConfigReqDTO).getData();
        clueEntity.setFormConfigDetail(formConfigRspDTO.getFormConfigDetail());
        return save(clueEntity);
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateClue(ClueUpdateDTO updateDTO) {
        ClueEntity clueEntity = Optional.ofNullable(getById(updateDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(updateDTO, clueEntity);
        clueEntity.setFormData(String.valueOf(updateDTO.getFormData()));
        return updateById(clueEntity);
    }

    /**
     * 分配
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean distributeClue(ClueDistributeDTO distributeDTO) {
        ClueEntity clueEntity = Optional.ofNullable(getById(distributeDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        clueEntity.setCurrentFollowerId(distributeDTO.getCurrentFollowerId());
        clueEntity.setStatus(ClueStatusEnum.FOLLOW_STATUS.getCode());
        return updateById(clueEntity);
    }

    /**
     * 跟进
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean followClue(ClueFollowDTO followDTO) {
        ClueRecordEntity clueRecord = new ClueRecordEntity();
        BeanUtils.copyProperties(followDTO, clueRecord);
        clueRecord.setFormData(String.valueOf(followDTO.getFormData()));
        clueRecord.setFollowTime(new Date());
        return clueRecordService.save(clueRecord);
    }

    /**
     * 完成
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean finishClue(ClueFinishDTO finishDTO) {
        ClueEntity clueEntity = Optional.ofNullable(getById(finishDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(finishDTO, clueEntity);
        clueEntity.setFinishTime(new Date());
        clueEntity.setStatus(ClueStatusEnum.FINISH_STATUS.getCode());
        return updateById(clueEntity);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteClue(Long id) {
        // 删除关联的线索记录
        clueRecordService.deleteClueRecordByClueId(id);
        return removeById(id);
    }

    /**
     * 批量分配
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDistributeClue(ClueBatchUpdateDTO batchUpdateDTO) {
        AssertUtil.notNull(batchUpdateDTO.getCurrentFollowerId(), "当前跟进人ID不能为空");
        LambdaQueryWrapper<ClueEntity> queryWrapper = new LambdaQueryWrapper<ClueEntity>()
                .in(CollectionUtils.isNotEmpty(batchUpdateDTO.getIdList()), ClueEntity::getId, batchUpdateDTO.getIdList());
        List<ClueEntity> clueEntityList = list(queryWrapper);
        if (CollectionUtils.isEmpty(clueEntityList)) {
            return true;
        }
        clueEntityList.forEach(o -> {
            o.setStatus(ClueStatusEnum.FOLLOW_STATUS.getCode());
            o.setCurrentFollowerId(batchUpdateDTO.getCurrentFollowerId());
        });
        return saveBatch(clueEntityList);
    }

    /**
     * 批量完成
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchFinishClue(ClueBatchUpdateDTO batchUpdateDTO) {
        AssertUtil.notNull(batchUpdateDTO.getResultId(), "线索结果不能为空");
        LambdaQueryWrapper<ClueEntity> queryWrapper = new LambdaQueryWrapper<ClueEntity>()
                .in(CollectionUtils.isNotEmpty(batchUpdateDTO.getIdList()), ClueEntity::getId, batchUpdateDTO.getIdList());
        List<ClueEntity> clueEntityList = list(queryWrapper);
        if (CollectionUtils.isEmpty(clueEntityList)) {
            return true;
        }
        clueEntityList.forEach(o -> {
            o.setResultId(batchUpdateDTO.getResultId());
            o.setFinishTime(new Date());
            o.setStatus(ClueStatusEnum.FINISH_STATUS.getCode());
            if (StringUtils.isNotBlank(batchUpdateDTO.getRemark())) {
                o.setRemark(batchUpdateDTO.getRemark());
            }
        });
        return saveBatch(clueEntityList);
    }

    /**
     * 批量删除
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteClue(List<Long> idList) {
        return removeByIds(idList);
    }

    private PageResult<ClueDTO> clueEntity2ClueDTOs(IPage<ClueEntity> clueEntityIPage) {
        PageResult<ClueDTO> clueDTOPageResult = new PageResult<>();
        List<ClueDTO> clueDTOList = clueEntityIPage.getRecords().stream()
                .map(o -> {
                    ClueDTO clueDTO = new ClueDTO();
                    BeanUtils.copyProperties(o, clueDTO);
                    clueDTO.setFormData(JSON.parseObject(o.getFormData()));
                    return clueDTO;
                })
                .collect(Collectors.toList());
        fillSystemOptionName(clueDTOList);
        clueDTOPageResult.setList(clueDTOList);
        clueDTOPageResult.setCurrentPage((int) clueEntityIPage.getCurrent());
        clueDTOPageResult.setPageSize((int) clueEntityIPage.getSize());
        clueDTOPageResult.setTotal(clueEntityIPage.getTotal());
        return clueDTOPageResult;
    }

    /**
     * 使用配置id列表查询出，对应的名称关系
     *
     * @param clueDTOList 线索列表
     */
    private void fillSystemOptionName(List<ClueDTO> clueDTOList) {
        if (CollectionUtils.isEmpty(clueDTOList)) {
            return;
        }
        Set<Long> configIdList = new HashSet<>();
        clueDTOList.forEach(o -> {
            if (Objects.nonNull(o.getSourceId())) {
                configIdList.add(o.getSourceId());
            }
            if (Objects.nonNull(o.getResultId())) {
                configIdList.add(o.getResultId());
            }
        });
        CustomConfigDetailReqDTO customConfigDetailReqDTO = new CustomConfigDetailReqDTO();
        customConfigDetailReqDTO.setCustomConfigDetailIdList(new ArrayList<>(configIdList));
        Map<Long, String> systemOptionMap = systemApiFeignService.batchQueryCustomConfigDetailsForMap(customConfigDetailReqDTO)
                .getData();
        clueDTOList.forEach(o -> {
            if (Objects.nonNull(o.getSourceId()) && systemOptionMap.containsKey(o.getSourceId())) {
                o.setSourceIdName(systemOptionMap.get(o.getSourceId()));
            }
            if (Objects.nonNull(o.getResultId()) && systemOptionMap.containsKey(o.getResultId())) {
                o.setResultIdName(systemOptionMap.get(o.getResultId()));
            }
        });
    }

}
