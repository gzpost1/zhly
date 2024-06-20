package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.infrastructure.domain.converter.OrganizatioConverter;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.OrganizationEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.OrganizationEntityMapper;
import cn.cuiot.dmp.system.domain.entity.Organization;
import cn.cuiot.dmp.system.domain.types.enums.OrderByTypeEnum;
import cn.cuiot.dmp.system.domain.query.OrganizationCommonQuery;
import cn.cuiot.dmp.system.domain.query.OrganizationMapperQuery;
import cn.cuiot.dmp.system.domain.repository.OrganizationRepository;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Author 犬豪
 * @Date 2023/8/28 14:30
 * @Version V1.0
 */
@Repository
public class OrganizationRepositoryImpl implements OrganizationRepository {

    @Autowired
    private OrganizationEntityMapper organizationEntityMapper;

    private static final String COLUMN_NAME_DELETED_ON = "deleted_on";
    private static final String COLUMN_NAME_ORG_NAME = "org_name";
    private static final String COLUMN_NAME_PARENT_ID = "parent_id";
    private static final String COLUMN_NAME_PRIMARY_KEY = "id";
    private static final String COLUMN_NAME_ORG_KEY = "org_key";
    private static final String COLUMN_NAME_COMPANY_NAME = "company_name";
    private static final String COLUMN_NAME_ORG_TYPE_ID = "org_type_id";
    private static final String COLUMN_NAME_SOCIAL_CREDIT_CODE = "social_credit_code";
    private static final String COLUMN_NAME_STATUS = "status";
    private static final String COLUMN_NAME_CREATED_ON = "created_on";


    @Override
    public Organization find(@NonNull OrganizationId organizationId) {
        OrganizationEntity organizationEntity = organizationEntityMapper.selectById(organizationId.getValue());
        return OrganizatioConverter.INSTANCE.toDomain(organizationEntity);
    }

    @Override
    public List<Organization> findList(@NonNull List<OrganizationId> organizationIdList) {
        List<OrganizationEntity> organizationEntities = organizationEntityMapper.selectBatchIds(organizationIdList.stream().map(OrganizationId::getValue).collect(Collectors.toList()));
        return OrganizatioConverter.INSTANCE.toDomainList(organizationEntities);
    }

    @Override
    public boolean remove(@NonNull Organization aggregate) {
        if (aggregate.getId() == null || aggregate.getId().getValue() < 0) {
            return false;
        }
        // 底层是逻辑删除
        LambdaUpdateWrapper<OrganizationEntity> updateWrapper = new UpdateWrapper<OrganizationEntity>().lambda();
        updateWrapper.eq(OrganizationEntity::getId, aggregate.getId().getValue())
                .set(OrganizationEntity::getDeletedFlag, 1)
                .set(OrganizationEntity::getDeletedOn, LocalDateTime.now());
        return 1 == organizationEntityMapper.update(new OrganizationEntity(), updateWrapper);
    }

    @Override
    public int removeList(@NonNull List<OrganizationId> aggregate) {
        return organizationEntityMapper.deleteBatchIds(aggregate.stream().map(OrganizationId::getValue).collect(Collectors.toList()));
    }

    @Override
    public boolean save(@NonNull Organization aggregate) {
        int count = 0;
        // 更新
        if (aggregate.getId() != null && aggregate.getId().getValue() != null) {
            // 更新前检查一些属性，没有设置默认值
            fillUpdatePropertyValue(aggregate);
            OrganizationEntity entity = OrganizatioConverter.INSTANCE.toEntity(aggregate);
            count = organizationEntityMapper.updateById(entity);
        } else {
            // 新增
            fillInsertPropertyValue(aggregate);
            OrganizationEntity entity = OrganizatioConverter.INSTANCE.toEntity(aggregate);
            count = organizationEntityMapper.insert(entity);
            aggregate.setId(new OrganizationId(entity.getId()));
        }
        return count == 1;
    }

    @Override
    public boolean updateByParams(@NonNull Organization aggregate, @NonNull OrganizationCommonQuery organizationCommonQuery) {
        OrganizationEntity entity = OrganizatioConverter.INSTANCE.toEntity(aggregate);
        QueryWrapper<OrganizationEntity> queryWrapper = buildUpdateByParamsQueryWrapper(organizationCommonQuery);
        int count = 0;
        count = organizationEntityMapper.update(entity, queryWrapper);
        return count == 1;
    }

    @Override
    public int updateInitFlag(Long companyId, Byte initFlag) {
        OrganizationEntity organizationEntity = Optional.ofNullable(organizationEntityMapper.selectById(companyId))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        AssertUtil.isTrue(EntityConstants.DISABLED.equals(organizationEntity.getInitFlag()), "企业已初始化，请勿重复操作");
        organizationEntity.setInitFlag(initFlag);
        return organizationEntityMapper.updateById(organizationEntity);
    }

    private void fillUpdatePropertyValue(Organization aggregate) {
        if (aggregate.getUpdatedOn() == null) {
            aggregate.setUpdatedOn(LocalDateTime.now());
        }
    }

    private void fillInsertPropertyValue(Organization aggregate) {
        if (aggregate.getCreatedOn() == null) {
            aggregate.setCreatedOn(LocalDateTime.now());
        }
        if (aggregate.getDeletedOn() == null) {
            aggregate.setDeletedOn(deleteTime);
        }
    }

    @Override
    public List<Organization> commonQuery(@NonNull OrganizationCommonQuery organizationCommonQuery) {
        QueryWrapper<OrganizationEntity> queryWrapper = buildQueryWrapper(organizationCommonQuery);
        List<OrganizationEntity> organizationEntities = organizationEntityMapper.selectList(queryWrapper);
        return OrganizatioConverter.INSTANCE.toDomainList(organizationEntities);
    }

    @Override
    public Organization commonQueryOne(@NonNull OrganizationCommonQuery organizationCommonQuery) {
        List<Organization> resultList = commonQuery(organizationCommonQuery);
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }
        return resultList.get(0);
    }

    @Override
    public long commonCountQuery(@NonNull OrganizationCommonQuery organizationCommonQuery) {
        QueryWrapper<OrganizationEntity> queryWrapper = buildQueryWrapper(organizationCommonQuery);
        return organizationEntityMapper.selectCount(queryWrapper);
    }

    @Override
    public PageResult<Organization> pageQuery(OrganizationMapperQuery organizationMapperQuery) {
        PageHelper.startPage(Integer.parseInt(organizationMapperQuery.getPageNo()),
                Integer.parseInt(organizationMapperQuery.getPageSize()));

        List<OrganizationEntity> organizationEntities = organizationEntityMapper.selectListByLogicDel(organizationMapperQuery);
        PageInfo<OrganizationEntity> pageInfo = new PageInfo<>(organizationEntities);

        PageResult<Organization> pageResult = new PageResult<>();
        pageResult.setList(OrganizatioConverter.INSTANCE.toDomainList(pageInfo.getList()));
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setCurrentPage(pageInfo.getPageNum());

        return pageResult;
    }

    private QueryWrapper<OrganizationEntity> buildQueryWrapper(OrganizationCommonQuery organizationCommonQuery) {
        QueryWrapper<OrganizationEntity> queryWrapper = new QueryWrapper<>();

        // ID
        if (CollectionUtils.isNotEmpty(organizationCommonQuery.getIdList())) {
            queryWrapper.in(COLUMN_NAME_PRIMARY_KEY, organizationCommonQuery.getIdList().stream().map(OrganizationId::getValue).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        }

        if (organizationCommonQuery.getDeleteStartTime() != null && organizationCommonQuery.getDeleteEndTime() != null) {
            queryWrapper.between(COLUMN_NAME_DELETED_ON, organizationCommonQuery.getDeleteStartTime(), organizationCommonQuery.getDeleteEndTime());
        }

        if (organizationCommonQuery.getOrgName() != null) {
            queryWrapper.like(COLUMN_NAME_ORG_NAME, organizationCommonQuery.getOrgName());
        }

        if (organizationCommonQuery.getParentId() != null) {
            queryWrapper.eq(COLUMN_NAME_PARENT_ID, organizationCommonQuery.getParentId());
        }

        if (organizationCommonQuery.getOrderByType() != null) {
            if (ObjectUtil.equals(organizationCommonQuery.getOrderByType().getValue(), OrderByTypeEnum.ASC.getValue())) {
                queryWrapper.orderByAsc(COLUMN_NAME_PRIMARY_KEY);
            } else {
                queryWrapper.orderByDesc(COLUMN_NAME_PRIMARY_KEY);
            }
        }

        if (organizationCommonQuery.getOrgKey() != null) {
            queryWrapper.eq(COLUMN_NAME_ORG_KEY, organizationCommonQuery.getOrgKey());
        }

        if (organizationCommonQuery.getCompanyName() != null) {
            queryWrapper.eq(COLUMN_NAME_COMPANY_NAME, organizationCommonQuery.getCompanyName());
        }

        if (organizationCommonQuery.getOrgTypeId() != null) {
            queryWrapper.eq(COLUMN_NAME_ORG_TYPE_ID, organizationCommonQuery.getOrgTypeId().getValue());
        }

        if (organizationCommonQuery.getSocialCreditCode() != null) {
            queryWrapper.eq(COLUMN_NAME_SOCIAL_CREDIT_CODE, organizationCommonQuery.getSocialCreditCode());
        }

        if (organizationCommonQuery.getStatus() != null) {
            queryWrapper.eq(COLUMN_NAME_STATUS, organizationCommonQuery.getStatus());
        }

        if (organizationCommonQuery.getCreatedEndTime() != null) {
            queryWrapper.le(COLUMN_NAME_CREATED_ON, organizationCommonQuery.getCreatedEndTime());
        }


        return queryWrapper;
    }

    private QueryWrapper<OrganizationEntity> buildUpdateByParamsQueryWrapper(OrganizationCommonQuery organizationCommonQuery) {
        QueryWrapper<OrganizationEntity> queryWrapper = new QueryWrapper<>();

        // ID
        if (CollectionUtils.isNotEmpty(organizationCommonQuery.getIdList())) {
            queryWrapper.in(COLUMN_NAME_PRIMARY_KEY, organizationCommonQuery.getIdList().stream().map(OrganizationId::getValue).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        }

        if (organizationCommonQuery.getOrgTypeId() != null) {
            queryWrapper.eq(COLUMN_NAME_ORG_TYPE_ID, organizationCommonQuery.getOrgTypeId().getValue());
        }
        queryWrapper.isNull(COLUMN_NAME_COMPANY_NAME);
        queryWrapper.isNull(COLUMN_NAME_SOCIAL_CREDIT_CODE);
        return queryWrapper;
    }
}
