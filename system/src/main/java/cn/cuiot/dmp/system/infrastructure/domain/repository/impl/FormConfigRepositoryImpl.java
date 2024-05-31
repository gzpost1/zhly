package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.system.application.constant.FormConfigConstant;
import cn.cuiot.dmp.system.domain.aggregate.FormConfig;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigPageQuery;
import cn.cuiot.dmp.system.domain.repository.FormConfigRepository;
import cn.cuiot.dmp.system.infrastructure.entity.FormConfigDetailEntity;
import cn.cuiot.dmp.system.infrastructure.entity.FormConfigEntity;
import cn.cuiot.dmp.system.infrastructure.entity.bo.FormConfigDetailBO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FormConfigDetailQueryDTO;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.FormConfigMapper;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.UserEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.UserEntityMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Slf4j
@Repository
public class FormConfigRepositoryImpl implements FormConfigRepository {

    @Autowired
    private FormConfigMapper formConfigMapper;

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public FormConfig queryForDetail(Long id) {
        FormConfigEntity formConfigEntity = Optional.ofNullable(formConfigMapper.selectById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        FormConfig formConfig = new FormConfig();
        BeanUtils.copyProperties(formConfigEntity, formConfig);
        // 查询表单详情
        FormConfigDetailEntity formConfigDetailEntity = mongoTemplate.findById(formConfig.getId(),
                FormConfigDetailEntity.class, FormConfigConstant.FORM_CONFIG_COLLECTION);
        if (Objects.nonNull(formConfigDetailEntity)) {
            formConfig.setFormConfigDetail(formConfigDetailEntity.getFormConfigDetail());
        }
        return formConfig;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveFormConfig(FormConfig formConfig) {
        // 保存校验
        checkSave(formConfig);
        formConfig.setId(IdWorker.getId());
        FormConfigEntity formConfigEntity = new FormConfigEntity();
        BeanUtils.copyProperties(formConfig, formConfigEntity);
        // 保存表单配置内容
        FormConfigDetailEntity formConfigDetailEntity = new FormConfigDetailEntity();
        BeanUtils.copyProperties(formConfig, formConfigDetailEntity);
        mongoTemplate.save(formConfigDetailEntity, FormConfigConstant.FORM_CONFIG_COLLECTION);
        return formConfigMapper.insert(formConfigEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateFormConfig(FormConfig formConfig) {
        FormConfigEntity formConfigEntity = Optional.ofNullable(formConfigMapper.selectById(formConfig.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        // 保存校验
        checkSave(formConfig);
        BeanUtils.copyProperties(formConfig, formConfigEntity);
        // 先删除后保存表单配置内容
        FormConfigDetailQueryDTO formConfigDetailQueryDTO = new FormConfigDetailQueryDTO();
        formConfigDetailQueryDTO.setId(formConfig.getId());
        Query query = getQuery(formConfigDetailQueryDTO);
        mongoTemplate.remove(query, FormConfigConstant.FORM_CONFIG_COLLECTION);
        FormConfigDetailEntity formConfigDetailEntity = new FormConfigDetailEntity();
        BeanUtils.copyProperties(formConfig, formConfigDetailEntity);
        mongoTemplate.save(formConfigDetailEntity, FormConfigConstant.FORM_CONFIG_COLLECTION);
        return formConfigMapper.updateById(formConfigEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateFormConfigStatus(FormConfig formConfig) {
        FormConfigEntity formConfigEntity = Optional.ofNullable(formConfigMapper.selectById(formConfig.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        formConfigEntity.setStatus(formConfig.getStatus());
        return formConfigMapper.updateById(formConfigEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteFormConfig(Long id) {
        // 先删除表单配置详情，后删除表单
        FormConfigDetailQueryDTO formConfigDetailQueryDTO = new FormConfigDetailQueryDTO();
        formConfigDetailQueryDTO.setId(id);
        Query query = getQuery(formConfigDetailQueryDTO);
        mongoTemplate.remove(query, FormConfigConstant.FORM_CONFIG_COLLECTION);
        return formConfigMapper.deleteById(id);
    }

    @Override
    public List<FormConfig> batchQueryFormConfig(Byte status, List<Long> idList) {
        AssertUtil.notEmpty(idList, "表单配置ID列表不能为空");
        // 获取表单配置详情列表
        FormConfigDetailQueryDTO formConfigDetailQueryDTO = new FormConfigDetailQueryDTO();
        formConfigDetailQueryDTO.setIdList(idList);
        Query query = getQuery(formConfigDetailQueryDTO);
        List<FormConfigDetailEntity> formConfigDetailEntityList = mongoTemplate.find(query,
                FormConfigDetailEntity.class, FormConfigConstant.FORM_CONFIG_COLLECTION);
        // 获取表单配置
        LambdaQueryWrapper<FormConfigEntity> queryWrapper = new LambdaQueryWrapper<FormConfigEntity>()
                .in(FormConfigEntity::getId, idList)
                .eq(Objects.nonNull(status), FormConfigEntity::getStatus, status);
        List<FormConfigEntity> formConfigEntityList = formConfigMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(formConfigEntityList)) {
            return new ArrayList<>();
        }
        return formConfigEntity2FormConfig(formConfigEntityList, formConfigDetailEntityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchMoveFormConfig(Long typeId, List<Long> idList) {
        return formConfigMapper.batchUpdateFormConfigById(typeId, null, idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchMoveFormConfigDefault(List<String> typeIdList, Long rootTypeId) {

        return formConfigMapper.batchMoveFormConfigDefault(typeIdList, rootTypeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateFormConfigStatus(Byte status, List<Long> idList) {
        return formConfigMapper.batchUpdateFormConfigById(null, status, idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteFormConfig(List<Long> idList) {
        // 先删除表单配置详情，后删除表单
        FormConfigDetailQueryDTO formConfigDetailQueryDTO = new FormConfigDetailQueryDTO();
        formConfigDetailQueryDTO.setIdList(idList);
        Query query = getQuery(formConfigDetailQueryDTO);
        mongoTemplate.remove(query, FormConfigConstant.FORM_CONFIG_COLLECTION);
        return formConfigMapper.deleteBatchIds(idList);
    }

    @Override
    public PageResult<FormConfig> queryFormConfigByType(FormConfigPageQuery pageQuery) {
        LambdaQueryWrapper<FormConfigEntity> queryWrapper = new LambdaQueryWrapper<FormConfigEntity>()
                .eq(Objects.nonNull(pageQuery.getCompanyId()), FormConfigEntity::getCompanyId, pageQuery.getCompanyId())
                .eq(Objects.nonNull(pageQuery.getTypeId()), FormConfigEntity::getTypeId, pageQuery.getTypeId())
                .like(StringUtils.isNotBlank(pageQuery.getName()), FormConfigEntity::getName, pageQuery.getName())
                .eq(Objects.nonNull(pageQuery.getStatus()), FormConfigEntity::getStatus, pageQuery.getStatus());
        IPage<FormConfigEntity> formConfigEntityPage = formConfigMapper.selectPage(
                new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), queryWrapper);
        if (Objects.isNull(formConfigEntityPage) || CollectionUtils.isEmpty(formConfigEntityPage.getRecords())) {
            return new PageResult<>();
        }
        return formConfigEntity2FormConfig(formConfigEntityPage);
    }

    @Override
    public boolean useCommonOptionByFormConfig(Long commonOptionId) {
        LambdaQueryWrapper<FormConfigEntity> queryWrapper = new LambdaQueryWrapper<>();
        List<FormConfigEntity> formConfigEntityList = formConfigMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(formConfigEntityList)) {
            return false;
        }
        List<Long> formConfigIdList = formConfigEntityList.stream()
                .map(FormConfigEntity::getId)
                .collect(Collectors.toList());
        // 获取表单配置详情列表
        FormConfigDetailQueryDTO formConfigDetailQueryDTO = new FormConfigDetailQueryDTO();
        formConfigDetailQueryDTO.setIdList(formConfigIdList);
        Query query = getQuery(formConfigDetailQueryDTO);
        List<FormConfigDetailEntity> formConfigDetailEntityList = mongoTemplate.find(query,
                FormConfigDetailEntity.class, FormConfigConstant.FORM_CONFIG_COLLECTION);
        if (CollectionUtils.isEmpty(formConfigDetailEntityList)) {
            return false;
        }
        List<FormConfigDetailBO> formConfigDetailBOList = new ArrayList<>();
        formConfigDetailEntityList.forEach(o -> {
                    List<FormConfigDetailBO> formConfigDetailBOs = JsonUtil.readValue(o.getFormConfigDetail(),
                            new TypeReference<List<FormConfigDetailBO>>() {
                            });
                    if (CollectionUtils.isNotEmpty(formConfigDetailBOs)) {
                        formConfigDetailBOList.addAll(formConfigDetailBOs);
                    }
                }
        );
        if (CollectionUtils.isEmpty(formConfigDetailBOList)) {
            return false;
        }
        for (FormConfigDetailBO formConfigDetailBO : formConfigDetailBOList) {
            if (Objects.isNull(formConfigDetailBO.getProps())) {
                continue;
            }
            if (commonOptionId.toString().equals(formConfigDetailBO.getProps().getTypeId())) {
                return true;
            }
        }
        return false;
    }

    private Query getQuery(FormConfigDetailQueryDTO formConfigDetailQueryDTO) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        if (Objects.nonNull(formConfigDetailQueryDTO.getId())) {
            criteria.and(FormConfigConstant.FORM_CONFIG_COLLECTION_PK).is(formConfigDetailQueryDTO.getId());
        }
        if (CollectionUtils.isNotEmpty(formConfigDetailQueryDTO.getIdList())) {
            criteria.and(FormConfigConstant.FORM_CONFIG_COLLECTION_PK).in(formConfigDetailQueryDTO.getIdList());
        }
        query.addCriteria(criteria);
        return query;
    }

    private PageResult<FormConfig> formConfigEntity2FormConfig(IPage<FormConfigEntity> formConfigEntityPage) {
        PageResult<FormConfig> formConfigPageResult = new PageResult<>();
        List<FormConfig> formConfigList = formConfigEntityPage.getRecords().stream()
                .map(o -> {
                    FormConfig formConfig = new FormConfig();
                    BeanUtils.copyProperties(o, formConfig);
                    if (StringUtils.isNotBlank(o.getCreatedBy())) {
                        String createdName = Optional.ofNullable(userEntityMapper.selectById(o.getCreatedBy()))
                                .orElse(new UserEntity()).getName();
                        formConfig.setCreatedName(createdName);
                    }
                    return formConfig;
                })
                .collect(Collectors.toList());
        formConfigPageResult.setList(formConfigList);
        formConfigPageResult.setCurrentPage((int) formConfigEntityPage.getCurrent());
        formConfigPageResult.setPageSize((int) formConfigEntityPage.getSize());
        formConfigPageResult.setTotal(formConfigEntityPage.getTotal());
        return formConfigPageResult;
    }

    private List<FormConfig> formConfigEntity2FormConfig(List<FormConfigEntity> formConfigEntityList,
                                                         List<FormConfigDetailEntity> formConfigDetailEntityList) {
        List<FormConfig> formConfigList = new ArrayList<>();
        for (FormConfigEntity formConfigEntity : formConfigEntityList) {
            FormConfig formConfig = new FormConfig();
            BeanUtils.copyProperties(formConfigEntity, formConfig);
            for (FormConfigDetailEntity formConfigDetailEntity : formConfigDetailEntityList) {
                if (formConfigDetailEntity.getId().equals(formConfigEntity.getId())) {
                    formConfig.setFormConfigDetail(formConfigDetailEntity.getFormConfigDetail());
                    break;
                }
            }
            formConfigList.add(formConfig);
        }
        return formConfigList;
    }

    private void checkSave(FormConfig formConfig) {
        AssertUtil.notNull(formConfig.getCompanyId(), "企业ID不能为空");
        AssertUtil.notBlank(formConfig.getName(), "表单名称不能为空");
        LambdaQueryWrapper<FormConfigEntity> queryWrapper = new LambdaQueryWrapper<FormConfigEntity>()
                .eq(FormConfigEntity::getCompanyId, formConfig.getCompanyId())
                // 更新的话排除自身
                .ne(Objects.nonNull(formConfig.getId()), FormConfigEntity::getId, formConfig.getId());
        List<FormConfigEntity> formConfigEntityList = formConfigMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(formConfigEntityList)) {
            return;
        }
        List<String> formConfigNameList = formConfigEntityList.stream()
                .map(FormConfigEntity::getName)
                .collect(Collectors.toList());
        AssertUtil.isFalse(formConfigNameList.contains(formConfig.getName()), "表单名称已存在");
    }

}
