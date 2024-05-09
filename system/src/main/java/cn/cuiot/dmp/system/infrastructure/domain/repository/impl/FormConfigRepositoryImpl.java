package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.query.PageResult;
import cn.cuiot.dmp.system.application.constant.FormConfigConstant;
import cn.cuiot.dmp.system.domain.aggregate.FormConfig;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigPageQuery;
import cn.cuiot.dmp.system.domain.repository.FormConfigRepository;
import cn.cuiot.dmp.system.infrastructure.entity.FormConfigDetailEntity;
import cn.cuiot.dmp.system.infrastructure.entity.FormConfigEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FormConfigDetailQueryDTO;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.FormConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    private MongoTemplate mongoTemplate;

    @Override
    public FormConfig queryForDetail(Long id) {
        FormConfigEntity formConfigEntity = formConfigMapper.selectById(id);
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
    public int updateFormConfig(FormConfig formConfig) {
        FormConfigEntity formConfigEntity = Optional.ofNullable(formConfigMapper.selectById(formConfig.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
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
    public int updateFormConfigStatus(FormConfig formConfig) {
        FormConfigEntity formConfigEntity = Optional.ofNullable(formConfigMapper.selectById(formConfig.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        formConfigEntity.setStatus(formConfig.getStatus());
        return formConfigMapper.updateById(formConfigEntity);
    }

    @Override
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
    public int batchMoveFormConfig(Long typeId, List<Long> idList) {
        return formConfigMapper.batchUpdateFormConfigById(typeId, null, idList);
    }

    @Override
    public int batchUpdateFormConfigStatus(Byte status, List<Long> idList) {
        return formConfigMapper.batchUpdateFormConfigById(null, status, idList);
    }

    @Override
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
                .eq(Objects.nonNull(pageQuery.getTypeId()), FormConfigEntity::getTypeId, pageQuery.getTypeId())
                .eq(StringUtils.isNotBlank(pageQuery.getName()), FormConfigEntity::getName, pageQuery.getName())
                .eq(Objects.nonNull(pageQuery.getStatus()), FormConfigEntity::getStatus, pageQuery.getStatus());
        IPage<FormConfigEntity> formConfigEntityPage = formConfigMapper.selectPage(
                new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), queryWrapper);
        if (Objects.isNull(formConfigEntityPage) || CollectionUtils.isEmpty(formConfigEntityPage.getRecords())) {
            return new PageResult<>();
        }
        return formConfigEntity2FormConfig(formConfigEntityPage);
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

}
