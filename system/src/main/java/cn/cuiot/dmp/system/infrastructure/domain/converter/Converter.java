package cn.cuiot.dmp.system.infrastructure.domain.converter;

import cn.cuiot.dmp.domain.entity.AbstractDomainEntity;
import cn.cuiot.dmp.domain.entity.DomainEntity;
import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.BaseEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @Description 领域模型和数据库模型之间的转换，实现使用单例模式
 * @Author 犬豪
 * @Date 2023/8/29 14:23
 * @Version V1.0
 */
public interface Converter<D extends DomainEntity, E> {

    D toDomain(E entity);

    E toEntity(D domain);

    default Map<E, D> toEntityMap(List<D> aggregateList) {
        if (CollectionUtils.isEmpty(aggregateList)) {
            return Collections.emptyMap();
        }
        Map<E, D> resut = new LinkedHashMap<>();
        for (D domain : aggregateList) {
            resut.put(toEntity(domain), domain);
        }
        return resut;
    }

    default List<E> toEntityList(List<D> aggregateList) {
        if (CollectionUtils.isEmpty(aggregateList)) {
            return Collections.emptyList();
        }
        return aggregateList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    default List<D> toDomainList(List<E> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        return entityList.stream().map(this::toDomain).collect(Collectors.toList());
    }


    default void setDomainBaseInfo(AbstractDomainEntity domainEntity, BaseEntity baseEntity) {
        domainEntity.setCreatedOn(baseEntity.getCreatedOn());
        domainEntity.setCreatedBy(baseEntity.getCreatedBy());
        domainEntity.setCreatedByType(OperateByTypeEnum.valueOf(baseEntity.getCreatedByType()));
        domainEntity.setUpdatedOn(baseEntity.getUpdatedOn());
        domainEntity.setUpdatedBy(baseEntity.getUpdatedBy());
        domainEntity.setUpdatedByType(OperateByTypeEnum.valueOf(baseEntity.getUpdatedByType()));
        domainEntity.setDeletedFlag(baseEntity.getDeletedFlag());
        domainEntity.setDeletedBy(baseEntity.getDeletedBy());
        domainEntity.setDeletedOn(baseEntity.getDeletedOn());
        domainEntity.setDeleteByType(OperateByTypeEnum.valueOf(baseEntity.getDeleteByType()));
    }

    default void setEntityBaseInfo(BaseEntity baseEntity, AbstractDomainEntity domainEntity) {
        baseEntity.setCreatedOn(domainEntity.getCreatedOn());
        baseEntity.setCreatedBy(domainEntity.getCreatedBy());
        baseEntity.setCreatedByType(domainEntity.getCreatedByType() != null ? domainEntity.getCreatedByType().getValue() : null);
        baseEntity.setUpdatedOn(domainEntity.getUpdatedOn());
        baseEntity.setUpdatedBy(domainEntity.getUpdatedBy());
        baseEntity.setUpdatedByType(domainEntity.getUpdatedByType() != null ? domainEntity.getUpdatedByType().getValue() : null);
        baseEntity.setDeletedFlag(domainEntity.getDeletedFlag());
        baseEntity.setDeletedBy(domainEntity.getDeletedBy());
        baseEntity.setDeletedOn(domainEntity.getDeletedOn());
        baseEntity.setDeleteByType(domainEntity.getDeleteByType() != null ? domainEntity.getDeleteByType().getValue() : null);
    }
}
