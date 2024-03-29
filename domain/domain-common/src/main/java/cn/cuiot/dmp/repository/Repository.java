package cn.cuiot.dmp.repository;

import cn.cuiot.dmp.domain.entity.AbstractDomainEntity;
import cn.cuiot.dmp.domain.entity.Aggregate;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.domain.types.OperateInfo;
import cn.cuiot.dmp.domain.types.enums.OperateByTypeEnum;
import cn.cuiot.dmp.domain.types.id.Identifier;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.NonNull;

/**
 * @Description Repository 基础接口类
 * @Author yth
 * @Date 2023/8/28 09:16
 * @Version V1.0
 */
public interface Repository<T extends Aggregate<I>, I extends Identifier> {

    String COLUMN_ID = "id";
    String COLUMN_DELETED_FLAG = "deleted_flag";
    String COLUMN_DELETED_ON = "deleted_on";
    String COLUMN_DELETED_BY = "deleted_by";
    String COLUMN_DELETE_BY_TYPE = "delete_by_type";

    LocalDateTime deleteTime = LocalDateTime.parse("1970-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    /**
     * 通过ID寻找Aggregate
     */
    T find(@NonNull I id);

    /**
     * 批量ID查询
     */
    List<T> findList(@NonNull List<I> idList);

    /**
     * 将一个Aggregate从Repository移除
     */
    boolean remove(@NonNull T aggregate);

    default boolean remove(@NonNull I id) {
        T t = find(id);
        return remove(t);
    }

    /**
     * 批量ID删除
     *
     * @param aggregate
     * @return
     */
    default int removeList(@NonNull List<I> aggregate) {
        //默认不支持，AbstraceRepositoryImpl已经实现了这个方法，可以继承它
        return 0;
    }


    /**
     * 保存一个Aggregate
     */
    boolean save(@NonNull T aggregate);

    default LocalDateTime getDefaultOperateOn() {
        OperateInfo currentLoginOperateInfo = LoginInfoHolder.getCurrentLoginOperateInfo();
        return currentLoginOperateInfo != null ? currentLoginOperateInfo.getOperateOn() : LocalDateTime.now();
    }

    default String getDefaultOperateBy() {
        OperateInfo currentLoginOperateInfo = LoginInfoHolder.getCurrentLoginOperateInfo();
        return currentLoginOperateInfo != null ? currentLoginOperateInfo.getOperateBy() : OperateByTypeEnum.SYSTEM.name().toLowerCase();
    }

    default OperateByTypeEnum getDefaultOperateByType() {
        OperateInfo currentLoginOperateInfo = LoginInfoHolder.getCurrentLoginOperateInfo();
        return currentLoginOperateInfo != null ? currentLoginOperateInfo.getOperateByType() : OperateByTypeEnum.SYSTEM;
    }

    default Integer getDefaultOperateByTypeValue() {
        OperateInfo currentLoginOperateInfo = LoginInfoHolder.getCurrentLoginOperateInfo();
        if (currentLoginOperateInfo != null && currentLoginOperateInfo.getOperateByType() != null) {
            return currentLoginOperateInfo.getOperateByType().getValue();
        }
        return OperateByTypeEnum.SYSTEM.getValue();
    }

    default void fillPropertyValueBeforeUpdate(AbstractDomainEntity domainEntity) {
        if (domainEntity.getUpdatedOn() == null) {
            domainEntity.setUpdatedOn(getDefaultOperateOn());
        }

        if (domainEntity.getUpdatedBy() == null) {
            domainEntity.setUpdatedBy(getDefaultOperateBy());
        }

        if (domainEntity.getUpdatedByType() == null) {
            domainEntity.setUpdatedByType(getDefaultOperateByType());
        }
    }


    default void fillPropertyValueBeforeInsert(AbstractDomainEntity domainEntity) {
        //处理id<=0的异常情况
        domainEntity.setId(null);
        if (domainEntity.getCreatedOn() == null) {
            domainEntity.setCreatedOn(getDefaultOperateOn());
        }
        if (domainEntity.getCreatedBy() == null) {
            domainEntity.setCreatedBy(getDefaultOperateBy());
        }
        if (domainEntity.getCreatedByType() == null) {
            domainEntity.setCreatedByType(getDefaultOperateByType());
        }
    }

    default void fillPropertyValueBeforeDelete(AbstractDomainEntity domainEntity) {
        if (domainEntity.getDeletedOn() == null || domainEntity.getDeletedOn().equals(deleteTime)) {
            domainEntity.setDeletedOn(getDefaultOperateOn());
        }
        if (domainEntity.getDeletedBy() == null) {
            domainEntity.setDeletedBy(getDefaultOperateBy());
        }
        if (domainEntity.getDeleteByType() == null) {
            domainEntity.setDeleteByType(getDefaultOperateByType());
        }
    }

}
