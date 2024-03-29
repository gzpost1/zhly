package cn.cuiot.dmp.system.infrastructure.domain.converter;

import cn.cuiot.dmp.domain.entity.AbstractDomainEntity;
import cn.cuiot.dmp.domain.types.id.LongId;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.BaseEntity;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Description 模板方法
 * @Author 犬豪
 * @Date 2024/1/11 10:03
 * @Version V1.0
 */
public abstract class AbstractConverter<I extends LongId, D extends AbstractDomainEntity<I>, E extends BaseEntity> implements Converter<D, E> {
    protected Class<I> idClass;
    protected Class<D> domainClass;
    protected Class<E> entityClass;

    protected AbstractConverter() {
        Type superclass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = null;
        if (superclass instanceof ParameterizedType) {
            parameterizedType = (ParameterizedType) superclass;
            Type[] typeArray = parameterizedType.getActualTypeArguments();
            if (typeArray != null && typeArray.length >= 3) {
                idClass = (Class<I>) typeArray[0];
                domainClass = (Class<D>) typeArray[1];
                entityClass = (Class<E>) typeArray[2];
            }
        }
    }

    @Override
    public D toDomain(E entity) {
        if (entity == null) {
            return null;
        }
        D domain = null;
        try {
            domain = domainClass.getConstructor().newInstance();
            domain.setId(entity.getId() != null ? idClass.getConstructor(Long.class).newInstance(entity.getId()) : null);
            setDomainInfo(domain, entity);
            setDomainBaseInfo(domain, entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return domain;
    }

    protected abstract void setDomainInfo(D domain, E entity);

    @Override
    public E toEntity(D domain) {
        if (domain == null) {
            return null;
        }
        E entity = null;
        try {
            entity = entityClass.getConstructor().newInstance();
            entity.setId(domain.getId() != null ? domain.getId().getValue() : null);
            setEntityInfo(entity, domain);
            setEntityBaseInfo(entity, domain);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    protected abstract void setEntityInfo(E entity, D domain);
}
