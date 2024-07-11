package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.AuditConfigTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomConfigDetailReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.AuditConfigTypeRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.constant.AuditConfigConstant;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.constants.PriceManageConstant;
import cn.cuiot.dmp.lease.dto.price.*;
import cn.cuiot.dmp.lease.entity.PriceManageDetailEntity;
import cn.cuiot.dmp.lease.entity.PriceManageEntity;
import cn.cuiot.dmp.lease.entity.PriceManageRecordEntity;
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
        List<PriceManageDetailEntity> priceManageDetailEntities = priceManageDetailService.queryByPriceId(priceManageEntity.getId());
        priceManageDTO.setPriceManageDetailEntities(priceManageDetailEntities);
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
                .eq(StringUtils.isNotBlank(queryDTO.getId()), PriceManageEntity::getId, queryDTO.getId())
                .like(Objects.nonNull(queryDTO.getHouseId()), PriceManageEntity::getHouseIds, queryDTO.getHouseId())
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
                .eq(StringUtils.isNotBlank(queryDTO.getId()), PriceManageEntity::getId, queryDTO.getId())
                .like(Objects.nonNull(queryDTO.getHouseId()), PriceManageEntity::getHouseIds, queryDTO.getHouseId())
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
    public Long savePriceManage(PriceManageCreateDTO createDTO) {
        Long userId = LoginInfoHolder.getCurrentUserId();
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        AssertUtil.notNull(companyId, "企业id不能为空");
        createDTO.setCompanyId(companyId);
        PriceManageEntity priceManageEntity = new PriceManageEntity();
        BeanUtils.copyProperties(createDTO, priceManageEntity);
        List<String> houseIds = createDTO.getPriceManageDetailEntities().stream()
                .map(o -> o.getHouseId().toString())
                .collect(Collectors.toList());
        priceManageEntity.setId(IdWorker.getId());
        priceManageEntity.setHouseIds(houseIds);
        priceManageEntity.setStatus(PriceManageStatusEnum.DRAFT_STATUS.getCode());
        // 保存定价明细
        priceManageDetailService.savePriceManageDetailList(priceManageEntity.getId(), createDTO.getPriceManageDetailEntities());
        // 保存定价操作记录
        PriceManageRecordEntity priceManageRecordEntity = new PriceManageRecordEntity(priceManageEntity.getId(),
                PriceManageConstant.OPERATE_CREATE, userId, new Date(), null, null, null);
        priceManageRecordService.savePriceManageRecord(priceManageRecordEntity);
        save(priceManageEntity);
        return priceManageEntity.getId();
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePriceManage(PriceManageUpdateDTO updateDTO) {
        PriceManageEntity priceManageEntity = Optional.ofNullable(getById(updateDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(updateDTO, priceManageEntity);
        List<String> houseIds = updateDTO.getPriceManageDetailEntities().stream()
                .map(o -> o.getHouseId().toString())
                .collect(Collectors.toList());
        priceManageEntity.setHouseIds(houseIds);
        // 先删除定价明细，再新增定价明细
        priceManageDetailService.deletePriceManageDetailList(priceManageEntity.getId());
        priceManageDetailService.savePriceManageDetailList(priceManageEntity.getId(), updateDTO.getPriceManageDetailEntities());
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
        copyPriceManageEntity.setStatus(PriceManageStatusEnum.DRAFT_STATUS.getCode());
        // 复制新增定价明细
        priceManageDetailService.copyPriceManageDetail(id, copyPriceManageEntity.getId());
        return save(copyPriceManageEntity);
    }

    /**
     * 提交
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean submitPriceManage(Long id) {
        Long userId = LoginInfoHolder.getCurrentUserId();
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        PriceManageEntity priceManageEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        // 如果定价管理-定价单提交未开启审核管理，则直接转到审核通过状态
        AuditConfigTypeReqDTO reqDTO = new AuditConfigTypeReqDTO(companyId, AuditConfigTypeEnum.PRICE_MANAGE.getCode(),
                AuditConfigConstant.PRICE_MANAGE_INIT.get(0));
        List<AuditConfigTypeRspDTO> auditConfigTypeRspDTOS = systemApiFeignService.lookUpAuditConfig(reqDTO).getData();
        AssertUtil.notEmpty(auditConfigTypeRspDTOS, "审核管理未初始化");
        byte auditStatus = auditConfigTypeRspDTOS.get(0).getAuditConfigList().get(0).getStatus();
        if (EntityConstants.ENABLED.equals(auditStatus)) {
            priceManageEntity.setStatus(PriceManageStatusEnum.AUDIT_STATUS.getCode());
        } else {
            // 如果当前日期大于等于执行日期，则变更状态为已执行
            Date now = new Date();
            if (now.compareTo(priceManageEntity.getExecuteDate()) >= 0) {
                priceManageEntity.setStatus(PriceManageStatusEnum.EXECUTED_STATUS.getCode());
            } else {
                priceManageEntity.setStatus(PriceManageStatusEnum.PASS_STATUS.getCode());
            }
        }
        // 保存定价操作记录
        PriceManageRecordEntity priceManageRecordEntity = new PriceManageRecordEntity(priceManageEntity.getId(),
                PriceManageConstant.OPERATE_SUBMIT, userId, new Date(), null, null, null);
        priceManageRecordService.savePriceManageRecord(priceManageRecordEntity);
        return updateById(priceManageEntity);
    }

    /**
     * 审核
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean auditPriceManage(PriceManageAuditDTO auditDTO) {
        Long userId = LoginInfoHolder.getCurrentUserId();
        AssertUtil.notNull(auditDTO.getStatus(), "审核状态不能为空");
        PriceManageEntity priceManageEntity = Optional.ofNullable(getById(auditDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        priceManageEntity.setStatus(auditDTO.getStatus());
        if (StringUtils.isNotBlank(auditDTO.getRemark())) {
            priceManageEntity.setAuditRemark(auditDTO.getRemark());
        }
        // 保存定价操作记录
        String auditResult;
        if (PriceManageStatusEnum.PASS_STATUS.getCode().equals(auditDTO.getStatus())) {
            auditResult = "通过";
        } else {
            auditResult = "不通过";
        }
        PriceManageRecordEntity priceManageRecordEntity = new PriceManageRecordEntity(priceManageEntity.getId(),
                PriceManageConstant.OPERATE_SUBMIT_AUDIT, userId, new Date(), auditResult, auditDTO.getRemark(), null);
        priceManageRecordService.savePriceManageRecord(priceManageRecordEntity);
        return updateById(priceManageEntity);
    }

    /**
     * 作废
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean invalidPriceManage(PriceManageAuditDTO auditDTO) {
        Long userId = LoginInfoHolder.getCurrentUserId();
        PriceManageEntity priceManageEntity = Optional.ofNullable(getById(auditDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        priceManageEntity.setStatus(PriceManageStatusEnum.INVALID_STATUS.getCode());
        if (StringUtils.isNotBlank(auditDTO.getRemark())) {
            priceManageEntity.setInvalidRemark(auditDTO.getRemark());
        }
        // 保存定价操作记录
        PriceManageRecordEntity priceManageRecordEntity = new PriceManageRecordEntity(priceManageEntity.getId(),
                PriceManageConstant.OPERATE_INVALID, userId, new Date(), null, null, auditDTO.getRemark());
        priceManageRecordService.savePriceManageRecord(priceManageRecordEntity);
        return updateById(priceManageEntity);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePriceManage(Long id) {
        return removeById(id);
    }

    /**
     * 通过定价管理id查询定价管理记录
     */
    public PageResult<PriceManageRecordEntity> queryRecordByPriceId(PriceManageRecordPageQueryDTO pageQueryDTO) {
        return priceManageRecordService.queryByPriceId(pageQueryDTO);
    }

    /**
     * 通过定价管理状态查询统计数量
     */
    public List<PriceManageCountDTO> queryCountByStatus() {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        AssertUtil.notNull(companyId, "企业id不能为空");
        List<PriceManageEntity> priceManageEntityList = list();
        if (CollectionUtils.isEmpty(priceManageEntityList)) {
            return PriceManageConstant.Price_Manage_Status.stream()
                    .map(o -> {
                        PriceManageCountDTO priceManageCountDTO = new PriceManageCountDTO();
                        priceManageCountDTO.setStatus(o);
                        priceManageCountDTO.setCount(0);
                        return priceManageCountDTO;
                    })
                    .collect(Collectors.toList());
        }
        return PriceManageConstant.Price_Manage_Status.stream()
                .map(o -> {
                    PriceManageCountDTO priceManageCountDTO = new PriceManageCountDTO();
                    priceManageCountDTO.setStatus(o);
                    if (PriceManageStatusEnum.ALL_STATUS.getCode().equals(o)) {
                        priceManageCountDTO.setCount(priceManageEntityList.size());
                    } else {
                        long count = priceManageEntityList.stream()
                                .filter(item -> o.equals(item.getStatus()))
                                .count();
                        priceManageCountDTO.setCount((int) count);
                    }
                    return priceManageCountDTO;
                })
                .collect(Collectors.toList());
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
