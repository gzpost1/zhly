package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomConfigDetailReqDTO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.constants.PriceManageConstant;
import cn.cuiot.dmp.lease.dto.price.*;
import cn.cuiot.dmp.lease.entity.PriceManageEntity;
import cn.cuiot.dmp.lease.enums.PriceManageStatusEnum;
import cn.cuiot.dmp.lease.mapper.PriceManageMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
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
 * @date 2024/6/24
 */
@Slf4j
@Service
public class PriceManageService extends ServiceImpl<PriceManageMapper, PriceManageEntity> {

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    @Autowired
    private PriceManageDetailService priceManageDetailService;

    @Autowired
    private PriceManageRecordService priceManageRecordService;

    /**
     * 查询详情
     */
    public PriceManageDTO queryForDetail(Long id) {
        PriceManageEntity priceManageEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        PriceManageDTO priceManageDTO = new PriceManageDTO();
        BeanUtils.copyProperties(priceManageEntity, priceManageDTO);
        fillUserName(Lists.newArrayList(priceManageDTO));
        fillSystemOptionName(Lists.newArrayList(priceManageDTO));
        return priceManageDTO;
    }

    /**
     * 查询列表
     */
    public List<PriceManageDTO> queryForList(PriceManagePageQueryDTO queryDTO) {
        LambdaQueryWrapper<PriceManageEntity> queryWrapper = new LambdaQueryWrapper<PriceManageEntity>()
                .like(StringUtils.isNotBlank(queryDTO.getName()), PriceManageEntity::getName, queryDTO.getName())
                .eq(Objects.nonNull(queryDTO.getId()), PriceManageEntity::getId, queryDTO.getId())
                .in(Objects.nonNull(queryDTO.getHouseId()), PriceManageEntity::getHouseIds, queryDTO.getHouseId())
                .eq(Objects.nonNull(queryDTO.getCategoryId()), PriceManageEntity::getCategoryId, queryDTO.getCategoryId())
                .eq(Objects.nonNull(queryDTO.getTypeId()), PriceManageEntity::getTypeId, queryDTO.getTypeId())
                .eq(Objects.nonNull(queryDTO.getStatus()), PriceManageEntity::getStatus, queryDTO.getStatus())
                .ge(Objects.nonNull(queryDTO.getPriceBeginTime()), PriceManageEntity::getPriceDate, queryDTO.getPriceBeginTime())
                .le(Objects.nonNull(queryDTO.getPriceEndTime()), PriceManageEntity::getPriceDate, queryDTO.getPriceEndTime())
                .ge(Objects.nonNull(queryDTO.getExecuteBeginTime()), PriceManageEntity::getExecuteDate, queryDTO.getExecuteBeginTime())
                .le(Objects.nonNull(queryDTO.getExecuteEndTime()), PriceManageEntity::getExecuteDate, queryDTO.getExecuteEndTime());
        queryWrapper.last("ORDER BY IFNULL(updated_on, created_on) DESC");
        List<PriceManageEntity> priceManageEntityList = list(queryWrapper);
        if (CollectionUtils.isEmpty(priceManageEntityList)) {
            return new ArrayList<>();
        }
        return priceManageEntityList.stream()
                .map(o -> {
                    PriceManageDTO priceManageDTO = new PriceManageDTO();
                    BeanUtils.copyProperties(o, priceManageDTO);
                    return priceManageDTO;
                })
                .collect(Collectors.toList());
    }

    /**
     * 查询分页列表
     */
    public PageResult<PriceManageDTO> queryForPage(PriceManagePageQueryDTO queryDTO) {
        LambdaQueryWrapper<PriceManageEntity> queryWrapper = new LambdaQueryWrapper<PriceManageEntity>()
                .like(StringUtils.isNotBlank(queryDTO.getName()), PriceManageEntity::getName, queryDTO.getName())
                .eq(Objects.nonNull(queryDTO.getId()), PriceManageEntity::getId, queryDTO.getId())
                .in(Objects.nonNull(queryDTO.getHouseId()), PriceManageEntity::getHouseIds, queryDTO.getHouseId())
                .eq(Objects.nonNull(queryDTO.getCategoryId()), PriceManageEntity::getCategoryId, queryDTO.getCategoryId())
                .eq(Objects.nonNull(queryDTO.getTypeId()), PriceManageEntity::getTypeId, queryDTO.getTypeId())
                .eq(Objects.nonNull(queryDTO.getStatus()), PriceManageEntity::getStatus, queryDTO.getStatus())
                .ge(Objects.nonNull(queryDTO.getPriceBeginTime()), PriceManageEntity::getPriceDate, queryDTO.getPriceBeginTime())
                .le(Objects.nonNull(queryDTO.getPriceEndTime()), PriceManageEntity::getPriceDate, queryDTO.getPriceEndTime())
                .ge(Objects.nonNull(queryDTO.getExecuteBeginTime()), PriceManageEntity::getExecuteDate, queryDTO.getExecuteBeginTime())
                .le(Objects.nonNull(queryDTO.getExecuteEndTime()), PriceManageEntity::getExecuteDate, queryDTO.getExecuteEndTime());
        queryWrapper.last("ORDER BY IFNULL(updated_on, created_on) DESC");
        IPage<PriceManageEntity> priceManageEntityIPage = page(new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize()), queryWrapper);
        if (CollectionUtils.isEmpty(priceManageEntityIPage.getRecords())) {
            return new PageResult<>();
        }
        return priceManageEntity2PriceManageDTOs(priceManageEntityIPage);
    }

    /**
     * 保存草稿
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean savePriceManage(PriceManageCreateDTO createDTO) {
        Long userId = LoginInfoHolder.getCurrentUserId();
        String userName = LoginInfoHolder.getCurrentUsername();
        AssertUtil.notNull(createDTO.getCompanyId(), "企业id不能为空");
        PriceManageEntity priceManageEntity = new PriceManageEntity();
        BeanUtils.copyProperties(createDTO, priceManageEntity);
        List<String> houseIds = createDTO.getPriceManageDetailCreateList().stream()
                .map(o -> o.getHouseId().toString())
                .collect(Collectors.toList());
        priceManageEntity.setId(IdWorker.getId());
        priceManageEntity.setHouseIds(houseIds);
        priceManageEntity.setStatus(PriceManageStatusEnum.DRAFT_STATUS.getCode());
        // 保存定价明细
        priceManageDetailService.savePriceManageDetailList(priceManageEntity.getId(), createDTO.getPriceManageDetailCreateList());
        // 保存定价操作记录
        priceManageRecordService.savePriceManageRecord(userName, userId);
        return save(priceManageEntity);
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePriceManage(PriceManageUpdateDTO updateDTO) {
        PriceManageEntity priceManageEntity = Optional.ofNullable(getById(updateDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(updateDTO, priceManageEntity);
        List<String> houseIds = updateDTO.getPriceManageDetailCreateList().stream()
                .map(o -> o.getHouseId().toString())
                .collect(Collectors.toList());
        priceManageEntity.setHouseIds(houseIds);
        // 先删除定价明细，再新增定价明细
        priceManageDetailService.deletePriceManageDetailList(priceManageEntity.getId());
        priceManageDetailService.savePriceManageDetailList(priceManageEntity.getId(), updateDTO.getPriceManageDetailCreateList());
        return updateById(priceManageEntity);
    }

    /**
     * 复制新增
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean copyPriceManage(Long id) {
        PriceManageEntity priceManageEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        PriceManageEntity copyPriceManageEntity = new PriceManageEntity();
        BeanUtils.copyProperties(priceManageEntity, copyPriceManageEntity);
        copyPriceManageEntity.setId(IdWorker.getId());
        copyPriceManageEntity.setName(priceManageEntity.getName() + "(1)");
        return save(copyPriceManageEntity);
    }

    /**
     * 提交
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean submitPriceManage(Long id) {
        PriceManageEntity priceManageEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        AssertUtil.isTrue(PriceManageStatusEnum.DRAFT_STATUS.getCode().equals(priceManageEntity.getStatus()),
                "只能提交草稿数据");
        priceManageEntity.setStatus(PriceManageStatusEnum.AUDIT_STATUS.getCode());
        return updateById(priceManageEntity);
    }

    /**
     * 审核
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean auditPriceManage(PriceManageAuditDTO auditDTO) {
        AssertUtil.notNull(auditDTO.getStatus(), "审核状态不能为空");
        PriceManageEntity priceManageEntity = Optional.ofNullable(getById(auditDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        priceManageEntity.setStatus(auditDTO.getStatus());
        if (StringUtils.isNotBlank(auditDTO.getRemark())) {
            priceManageEntity.setAuditRemark(auditDTO.getRemark());
        }
        return updateById(priceManageEntity);
    }

    /**
     * 作废
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean invalidPriceManage(PriceManageAuditDTO auditDTO) {
        PriceManageEntity priceManageEntity = Optional.ofNullable(getById(auditDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        priceManageEntity.setStatus(PriceManageStatusEnum.INVALID_STATUS.getCode());
        if (StringUtils.isNotBlank(auditDTO.getRemark())) {
            priceManageEntity.setInvalidRemark(auditDTO.getRemark());
        }
        return updateById(priceManageEntity);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePriceManage(Long id) {
        return removeById(id);
    }

    private PageResult<PriceManageDTO> priceManageEntity2PriceManageDTOs(IPage<PriceManageEntity> priceManageEntityIPage) {
        PageResult<PriceManageDTO> priceManageDTOPageResult = new PageResult<>();
        List<PriceManageDTO> priceManageDTOList = priceManageEntityIPage.getRecords().stream()
                .map(o -> {
                    PriceManageDTO priceManageDTO = new PriceManageDTO();
                    BeanUtils.copyProperties(o, priceManageDTO);
                    return priceManageDTO;
                })
                .collect(Collectors.toList());
        fillUserName(priceManageDTOList);
        fillSystemOptionName(priceManageDTOList);
        priceManageDTOPageResult.setList(priceManageDTOList);
        priceManageDTOPageResult.setCurrentPage((int) priceManageEntityIPage.getCurrent());
        priceManageDTOPageResult.setPageSize((int) priceManageEntityIPage.getSize());
        priceManageDTOPageResult.setTotal(priceManageEntityIPage.getTotal());
        return priceManageDTOPageResult;
    }

    /**
     * 使用用户id列表查询出，对应的用户名称
     *
     * @param priceManageDTOList 定价管理列表
     */
    private void fillUserName(List<PriceManageDTO> priceManageDTOList) {
        if (CollectionUtils.isEmpty(priceManageDTOList)) {
            return;
        }
        Set<Long> userIdList = new HashSet<>();
        priceManageDTOList.forEach(o -> {
            if (StringUtils.isNotBlank(o.getCreatedBy())) {
                userIdList.add(Long.valueOf(o.getCreatedBy()));
            }
            if (StringUtils.isNotBlank(o.getUpdatedBy())) {
                userIdList.add(Long.valueOf(o.getUpdatedBy()));
            }
            if (Objects.nonNull(o.getPriceUserId())) {
                userIdList.add(o.getPriceUserId());
            }
        });
        BaseUserReqDto reqDto = new BaseUserReqDto();
        reqDto.setUserIdList(new ArrayList<>(userIdList));
        List<BaseUserDto> baseUserDtoList = systemApiFeignService.lookUpUserList(reqDto).getData();
        Map<Long, String> userMap = baseUserDtoList.stream().collect(Collectors.toMap(BaseUserDto::getId, BaseUserDto::getName));
        priceManageDTOList.forEach(o -> {
            if (StringUtils.isNotBlank(o.getCreatedBy()) && userMap.containsKey(Long.valueOf(o.getCreatedBy()))) {
                o.setCreatedName(userMap.get(Long.valueOf(o.getCreatedBy())));
            }
            if (StringUtils.isNotBlank(o.getUpdatedBy()) && userMap.containsKey(Long.valueOf(o.getUpdatedBy()))) {
                o.setUpdatedName(userMap.get(Long.valueOf(o.getUpdatedBy())));
            }
            if (Objects.nonNull(o.getPriceUserId()) && userMap.containsKey(o.getPriceUserId())) {
                o.setPriceUserName(userMap.get(o.getPriceUserId()));
            }
        });
    }

    /**
     * 使用配置id列表查询出，对应的名称关系
     *
     * @param priceManageDTOList 定价管理列表
     */
    private void fillSystemOptionName(List<PriceManageDTO> priceManageDTOList) {
        if (CollectionUtils.isEmpty(priceManageDTOList)) {
            return;
        }
        Set<Long> configIdList = new HashSet<>();
        priceManageDTOList.forEach(o -> {
            if (Objects.nonNull(o.getCategoryId())) {
                configIdList.add(o.getCategoryId());
            }
            if (Objects.nonNull(o.getTypeId())) {
                configIdList.add(o.getTypeId());
            }
        });
        CustomConfigDetailReqDTO customConfigDetailReqDTO = new CustomConfigDetailReqDTO();
        customConfigDetailReqDTO.setCustomConfigDetailIdList(new ArrayList<>(configIdList));
        Map<Long, String> systemOptionMap = systemApiFeignService.batchQueryCustomConfigDetailsForMap(customConfigDetailReqDTO)
                .getData();
        priceManageDTOList.forEach(o -> {
            if (Objects.nonNull(o.getCategoryId()) && systemOptionMap.containsKey(o.getCategoryId())) {
                o.setCategoryName(systemOptionMap.get(o.getCategoryId()));
            }
            if (Objects.nonNull(o.getTypeId()) && systemOptionMap.containsKey(o.getTypeId())) {
                o.setTypeName(systemOptionMap.get(o.getTypeId()));
            }
        });
    }

}
