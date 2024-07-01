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
import cn.cuiot.dmp.lease.dto.contractTemplate.ContractTemplateCreateDTO;
import cn.cuiot.dmp.lease.dto.contractTemplate.ContractTemplateDTO;
import cn.cuiot.dmp.lease.dto.contractTemplate.ContractTemplatePageQueryDTO;
import cn.cuiot.dmp.lease.dto.contractTemplate.ContractTemplateUpdateDTO;
import cn.cuiot.dmp.lease.entity.ContractTemplateEntity;
import cn.cuiot.dmp.lease.mapper.ContractTemplateMapper;
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
public class ContractTemplateService extends ServiceImpl<ContractTemplateMapper, ContractTemplateEntity> {

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    /**
     * 查询详情
     */
    public ContractTemplateDTO queryForDetail(Long id) {
        ContractTemplateEntity contractTemplateEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        ContractTemplateDTO contractTemplateDTO = new ContractTemplateDTO();
        BeanUtils.copyProperties(contractTemplateEntity, contractTemplateDTO);
        fillUserName(Lists.newArrayList(contractTemplateDTO));
        fillSystemOptionName(Lists.newArrayList(contractTemplateDTO));
        return contractTemplateDTO;
    }

    /**
     * 查询列表
     */
    public List<ContractTemplateDTO> queryForList(ContractTemplatePageQueryDTO queryDTO) {
        LambdaQueryWrapper<ContractTemplateEntity> queryWrapper = new LambdaQueryWrapper<ContractTemplateEntity>()
                .like(StringUtils.isNotBlank(queryDTO.getName()), ContractTemplateEntity::getName, queryDTO.getName())
                .eq(Objects.nonNull(queryDTO.getId()), ContractTemplateEntity::getId, queryDTO.getId())
                .eq(Objects.nonNull(queryDTO.getNatureId()), ContractTemplateEntity::getNatureId, queryDTO.getNatureId())
                .eq(Objects.nonNull(queryDTO.getTypeId()), ContractTemplateEntity::getTypeId, queryDTO.getTypeId())
                .eq(Objects.nonNull(queryDTO.getStatus()), ContractTemplateEntity::getStatus, queryDTO.getStatus())
                .ge(Objects.nonNull(queryDTO.getBeginTime()), ContractTemplateEntity::getCreatedOn, queryDTO.getBeginTime())
                .le(Objects.nonNull(queryDTO.getEndTime()), ContractTemplateEntity::getCreatedOn, queryDTO.getEndTime());
        queryWrapper.orderByDesc(ContractTemplateEntity::getCreatedOn);
        List<ContractTemplateEntity> contractTemplateEntityList = list(queryWrapper);
        if (CollectionUtils.isEmpty(contractTemplateEntityList)) {
            return new ArrayList<>();
        }
        return contractTemplateEntityList.stream()
                .map(o -> {
                    ContractTemplateDTO contractTemplateDTO = new ContractTemplateDTO();
                    BeanUtils.copyProperties(o, contractTemplateDTO);
                    return contractTemplateDTO;
                })
                .collect(Collectors.toList());
    }

    /**
     * 查询分页列表
     */
    public PageResult<ContractTemplateDTO> queryForPage(ContractTemplatePageQueryDTO queryDTO) {
        LambdaQueryWrapper<ContractTemplateEntity> queryWrapper = new LambdaQueryWrapper<ContractTemplateEntity>()
                .like(StringUtils.isNotBlank(queryDTO.getName()), ContractTemplateEntity::getName, queryDTO.getName())
                .eq(Objects.nonNull(queryDTO.getId()), ContractTemplateEntity::getId, queryDTO.getId())
                .eq(Objects.nonNull(queryDTO.getNatureId()), ContractTemplateEntity::getNatureId, queryDTO.getNatureId())
                .eq(Objects.nonNull(queryDTO.getTypeId()), ContractTemplateEntity::getTypeId, queryDTO.getTypeId())
                .eq(Objects.nonNull(queryDTO.getStatus()), ContractTemplateEntity::getStatus, queryDTO.getStatus())
                .ge(Objects.nonNull(queryDTO.getBeginTime()), ContractTemplateEntity::getCreatedOn, queryDTO.getBeginTime())
                .le(Objects.nonNull(queryDTO.getEndTime()), ContractTemplateEntity::getCreatedOn, queryDTO.getEndTime());
        queryWrapper.orderByDesc(ContractTemplateEntity::getCreatedOn);
        IPage<ContractTemplateEntity> contractTemplateEntityIPage = page(new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize()), queryWrapper);
        if (CollectionUtils.isEmpty(contractTemplateEntityIPage.getRecords())) {
            return new PageResult<>();
        }
        return contractTemplateEntity2ContractTemplateDTOs(contractTemplateEntityIPage);
    }

    /**
     * 保存
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveContractTemplate(ContractTemplateCreateDTO createDTO) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        AssertUtil.notNull(companyId, "企业id不能为空");
        createDTO.setCompanyId(companyId);
        ContractTemplateEntity contractTemplateEntity = new ContractTemplateEntity();
        BeanUtils.copyProperties(createDTO, contractTemplateEntity);
        return save(contractTemplateEntity);
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateContractTemplate(ContractTemplateUpdateDTO updateDTO) {
        ContractTemplateEntity contractTemplateEntity = Optional.ofNullable(getById(updateDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(updateDTO, contractTemplateEntity);
        return updateById(contractTemplateEntity);
    }

    /**
     * 复制新增
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean copyContractTemplate(Long id) {
        ContractTemplateEntity contractTemplateEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        ContractTemplateEntity copyContractTemplateEntity = new ContractTemplateEntity();
        BeanUtils.copyProperties(contractTemplateEntity, copyContractTemplateEntity);
        copyContractTemplateEntity.setId(IdWorker.getId());
        copyContractTemplateEntity.setName(copyContractTemplateEntity.getName() + "(1)");
        return save(copyContractTemplateEntity);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteContractTemplate(Long id) {
        return removeById(id);
    }

    private PageResult<ContractTemplateDTO> contractTemplateEntity2ContractTemplateDTOs(
            IPage<ContractTemplateEntity> contractTemplateEntityIPage) {
        PageResult<ContractTemplateDTO> contractTemplateDTOPageResult = new PageResult<>();
        List<ContractTemplateDTO> contractTemplateDTOList = contractTemplateEntityIPage.getRecords().stream()
                .map(o -> {
                    ContractTemplateDTO contractTemplateDTO = new ContractTemplateDTO();
                    BeanUtils.copyProperties(o, contractTemplateDTO);
                    return contractTemplateDTO;
                })
                .collect(Collectors.toList());
        fillUserName(contractTemplateDTOList);
        fillSystemOptionName(contractTemplateDTOList);
        contractTemplateDTOPageResult.setList(contractTemplateDTOList);
        contractTemplateDTOPageResult.setCurrentPage((int) contractTemplateEntityIPage.getCurrent());
        contractTemplateDTOPageResult.setPageSize((int) contractTemplateEntityIPage.getSize());
        contractTemplateDTOPageResult.setTotal(contractTemplateEntityIPage.getTotal());
        return contractTemplateDTOPageResult;
    }

    /**
     * 使用用户id列表查询出，对应的用户名称
     *
     * @param contractTemplateDTOList 合同模板列表
     */
    private void fillUserName(List<ContractTemplateDTO> contractTemplateDTOList) {
        if (CollectionUtils.isEmpty(contractTemplateDTOList)) {
            return;
        }
        Set<Long> userIdList = new HashSet<>();
        contractTemplateDTOList.forEach(o -> {
            if (StringUtils.isNotBlank(o.getCreatedBy())) {
                userIdList.add(Long.valueOf(o.getCreatedBy()));
            }
            if (StringUtils.isNotBlank(o.getUpdatedBy())) {
                userIdList.add(Long.valueOf(o.getUpdatedBy()));
            }
        });
        BaseUserReqDto reqDto = new BaseUserReqDto();
        reqDto.setUserIdList(new ArrayList<>(userIdList));
        List<BaseUserDto> baseUserDtoList = systemApiFeignService.lookUpUserList(reqDto).getData();
        Map<Long, String> userMap = baseUserDtoList.stream().collect(Collectors.toMap(BaseUserDto::getId, BaseUserDto::getName));
        contractTemplateDTOList.forEach(o -> {
            if (StringUtils.isNotBlank(o.getCreatedBy()) && userMap.containsKey(Long.valueOf(o.getCreatedBy()))) {
                o.setCreatedName(userMap.get(Long.valueOf(o.getCreatedBy())));
            }
            if (StringUtils.isNotBlank(o.getUpdatedBy()) && userMap.containsKey(Long.valueOf(o.getUpdatedBy()))) {
                o.setUpdatedName(userMap.get(Long.valueOf(o.getUpdatedBy())));
            }
        });
    }

    /**
     * 使用配置id列表查询出，对应的名称关系
     *
     * @param contractTemplateDTOList 合同模板列表
     */
    private void fillSystemOptionName(List<ContractTemplateDTO> contractTemplateDTOList) {
        if (CollectionUtils.isEmpty(contractTemplateDTOList)) {
            return;
        }
        Set<Long> configIdList = new HashSet<>();
        contractTemplateDTOList.forEach(o -> {
            if (Objects.nonNull(o.getNatureId())) {
                configIdList.add(o.getNatureId());
            }
            if (Objects.nonNull(o.getTypeId())) {
                configIdList.add(o.getTypeId());
            }
        });
        CustomConfigDetailReqDTO customConfigDetailReqDTO = new CustomConfigDetailReqDTO();
        customConfigDetailReqDTO.setCustomConfigDetailIdList(new ArrayList<>(configIdList));
        Map<Long, String> systemOptionMap = systemApiFeignService.batchQueryCustomConfigDetailsForMap(customConfigDetailReqDTO)
                .getData();
        contractTemplateDTOList.forEach(o -> {
            if (Objects.nonNull(o.getNatureId()) && systemOptionMap.containsKey(o.getNatureId())) {
                o.setNatureName(systemOptionMap.get(o.getNatureId()));
            }
            if (Objects.nonNull(o.getTypeId()) && systemOptionMap.containsKey(o.getTypeId())) {
                o.setTypeName(systemOptionMap.get(o.getTypeId()));
            }
        });
    }

}
