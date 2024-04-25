package cn.cuiot.dmp.base.infrastructure.config;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

/**
 * 自动填充处理类
 *
 * @author badao
 * @version 1.0
 * @see
 **/
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private final static String CREATETIME_FIELD = "createTime";
    private final static String CREATEUSER_FIELD = "createUser";
    private final static String UPDATETIME_FIELD = "updateTime";
    private final static String UPDATEUSER_FIELD = "createUser";
    private final static String DELETED_FIELD = "deleted";

    private final static String ET = "et";

    /**
     * 在执行mybatisPlus的insert()时，为我们自动给某些字段填充值，这样的话，我们就不需要手动给insert()里的实体类赋值了
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //其中方法参数中第一个是前面自动填充所对应的字段，第二个是要自动填充的值。第三个是指定实体类的对象

        this.setFieldValByName("createdOn", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateOn", LocalDateTime.now(), metaObject);
        this.setFieldValByName("deletedFlag", 0, metaObject);

        if (metaObject.hasSetter(CREATETIME_FIELD)) {
            Object createTime = getFieldValByName(CREATETIME_FIELD, metaObject);
            if (createTime == null) {
                Class<?> fieldType = getFieldType(metaObject, CREATETIME_FIELD);
                if (fieldType.isAssignableFrom(LocalDateTime.class)) {
                    setFieldValByName(CREATETIME_FIELD, LocalDateTime.now(), metaObject);
                } else if (fieldType.isAssignableFrom(LocalDate.class)) {
                    setFieldValByName(CREATETIME_FIELD, LocalDate.now(), metaObject);
                } else {
                    setFieldValByName(CREATETIME_FIELD, new Date(), metaObject);
                }
            }
        }
        if (metaObject.hasSetter(CREATEUSER_FIELD)) {
            Object createUser = getFieldValByName(CREATEUSER_FIELD, metaObject);
            if (createUser == null) {
                setFieldValByName(CREATEUSER_FIELD, LoginInfoHolder.getCurrentUserId(), metaObject);
            }
        }

        if (metaObject.hasSetter(DELETED_FIELD)) {
            Object deleted = getFieldValByName(DELETED_FIELD, metaObject);
            if (deleted == null) {
                setFieldValByName(DELETED_FIELD, EntityConstants.NOT_DELETED, metaObject);
            }
        }
        if (metaObject.hasSetter(UPDATETIME_FIELD)) {
            Object updateTime = getFieldValByName(UPDATETIME_FIELD, metaObject);
            if (updateTime == null) {
                update(metaObject);
            }
        }
        if (metaObject.hasSetter(UPDATEUSER_FIELD)) {
            Object updateUser = getFieldValByName(UPDATEUSER_FIELD, metaObject);
            if (updateUser == null) {
                setFieldValByName(UPDATEUSER_FIELD, LoginInfoHolder.getCurrentUserId(), metaObject);
            }

        }
    }

    /**
     * //在执行mybatisPlus的update()时，为我们自动给某些字段填充值，这样的话，我们就不需要手动给update()里的实体类赋值了
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateOn", LocalDateTime.now(), metaObject);
        if (metaObject.hasSetter(UPDATETIME_FIELD)) {
            update(metaObject);
        }
        if (metaObject.hasSetter(UPDATEUSER_FIELD)) {
            //Object updateUser = getFieldValByName(UPDATEUSER_FIELD, metaObject);
            setFieldValByName(UPDATEUSER_FIELD, LoginInfoHolder.getCurrentUserId(), metaObject);
        }

    }

    private void update(MetaObject metaObject) {
        Class<?> fieldType = getFieldType(metaObject, UPDATETIME_FIELD);
        if (fieldType.isAssignableFrom(LocalDateTime.class)) {
            setFieldValByName(UPDATETIME_FIELD, LocalDateTime.now(), metaObject);
        } else if (fieldType.isAssignableFrom(LocalDate.class)) {
            setFieldValByName(UPDATETIME_FIELD, LocalDate.now(), metaObject);
        } else {
            setFieldValByName(UPDATETIME_FIELD, new Date(), metaObject);
        }
    }

    private Class<?> getFieldType(MetaObject metaObject, String name) {
        if (metaObject.hasSetter(name) && metaObject.hasGetter(name)) {
            return metaObject.getSetterType(name);
        } else if (metaObject.hasGetter(ET)) {
            Object et = metaObject.getValue(ET);
            if (et != null) {
                MetaObject etMeta = SystemMetaObject.forObject(et);
                if (etMeta.hasSetter(name)) {
                    return etMeta.getSetterType(name);
                }
            }
        }
        return Object.class;
    }
}