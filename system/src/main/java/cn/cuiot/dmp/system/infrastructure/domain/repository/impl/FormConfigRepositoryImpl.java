package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.query.PageResult;
import cn.cuiot.dmp.system.application.constant.FormConfigConstant;
import cn.cuiot.dmp.system.domain.aggregate.FormConfig;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigPageQuery;
import cn.cuiot.dmp.system.domain.repository.FormConfigRepository;
import cn.cuiot.dmp.system.infrastructure.entity.FormConfigDetailEntity;
import cn.cuiot.dmp.system.infrastructure.entity.FormConfigEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.FormConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
        return formConfig;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveFormConfig(FormConfig formConfig) {
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
        Query query = getQuery(formConfig.getId());
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
        Query query = getQuery(id);
        mongoTemplate.remove(query, FormConfigConstant.FORM_CONFIG_COLLECTION);
        return formConfigMapper.deleteById(id);
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

    private Query getQuery(Long id) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and(FormConfigConstant.FORM_CONFIG_COLLECTION_PK).is(id);
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

}
