package cn.cuiot.dmp.base.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.entity.AbstractAggregate;
import cn.cuiot.dmp.domain.types.id.LongId;
import cn.cuiot.dmp.repository.Repository;
import cn.cuiot.dmp.base.infrastructure.domain.converter.Converter;
import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import cn.cuiot.dmp.base.infrastructure.persistence.mapper.IotBaseMapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @Author 犬豪
 * @Date 2023/10/19 17:38
 * @Version V1.0
 */
@Slf4j
public abstract class AbstraceRepositoryImpl<E extends BaseEntity, T extends AbstractAggregate<I>, I extends LongId, M extends IotBaseMapper<E>> implements Repository<T, I> {

    protected abstract M getEntityMapper();

    protected abstract Converter<T, E> getConverter();

    protected Class<E> entityClass;
    protected Class<I> idClass;

    protected AbstraceRepositoryImpl() {
        Type superclass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = null;
        if (superclass instanceof ParameterizedType) {
            parameterizedType = (ParameterizedType) superclass;
            Type[] typeArray = parameterizedType.getActualTypeArguments();
            if (typeArray != null && typeArray.length >= 4) {
                entityClass = (Class<E>) typeArray[0];
                idClass = (Class<I>) typeArray[2];
            }
        }
    }

    @Override
    public T find(@NonNull I id) {
        if (id.getValue() == null) {
            return null;
        }
        E entity = getEntityMapper().selectById(id.getValue());

        if (entity == null) {
            return null;
        }
        return getConverter().toDomain(entity);
    }

    @Override
    public List<T> findList(@NonNull List<I> idList) {
        List<Serializable> IdList = idList.stream().map(I::getValue).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<E> entityList = getEntityMapper().selectBatchIds(IdList);

        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        return getConverter().toDomainList(entityList);
    }

    @Override
    public boolean remove(@NonNull T aggregate) {
        if (aggregate.getId() == null) {
            return false;
        }

        //检查删除权限
        removeBefore(aggregate);

        //删除前属性填充
        fillPropertyValueBeforeDelete(aggregate);

        // 底层是逻辑删除
        return 1 == getEntityMapper().deleteByIdWithAllField(getConverter().toEntity(aggregate));
    }


    @Override
    public int removeList(@NonNull List<I> aggregate) {
        List<Long> idList = aggregate.stream().map(I::getValue).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)) {
            return 0;
        }
        return batchDeleteByIdList(idList);
    }

    private int batchDeleteByIdList(List<Long> idList) {
        UpdateWrapper<E> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in(COLUMN_ID, idList);
        updateWrapper.set(COLUMN_DELETED_FLAG, 1);
        updateWrapper.set(COLUMN_DELETED_ON, getDefaultOperateOn());
        updateWrapper.set(COLUMN_DELETED_BY, getDefaultOperateBy());
        updateWrapper.set(COLUMN_DELETE_BY_TYPE, getDefaultOperateByTypeValue());
        try {
            return getEntityMapper().update(entityClass.newInstance(), updateWrapper);
        } catch (Exception e) {
            log.error("batchDeleteByIdList error",e);
            throw new BusinessException(ResultCode.INNER_ERROR, "batchDeleteByIdList error");
        }
    }

    /**
     * 删除前扩展点
     *
     * @param aggregate
     */
    protected void removeBefore(T aggregate) {

    }

    @Override
    public boolean save(@NonNull T aggregate) {
        int count = 0;

        if (aggregate.getId() != null && aggregate.getId().getValue() != null) {
            // 更新前检查一些属性，没有设置默认值
            fillPropertyValueBeforeUpdate(aggregate);
            E entity = getConverter().toEntity(aggregate);
            count = getEntityMapper().updateById(entity);

        } else {
            // 新增
            fillPropertyValueBeforeInsert(aggregate);
            E entity = getConverter().toEntity(aggregate);
            count = getEntityMapper().insert(entity);

            try {
                //回填ID
                Constructor<I> constructor = idClass.getConstructor(Long.class);
                aggregate.setId(constructor.newInstance(entity.getId()));
            } catch (Exception e) {
                throw new BusinessException(ResultCode.INNER_ERROR, "idClass need a constructor of type Long");
            }
        }
        return count == 1;
    }
}
