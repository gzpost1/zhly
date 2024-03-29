package cn.cuiot.dmp.base.infrastructure.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.time.LocalDateTime;
import org.apache.ibatis.reflection.MetaObject;
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

        /**
         * 在执行mybatisPlus的insert()时，为我们自动给某些字段填充值，这样的话，我们就不需要手动给insert()里的实体类赋值了
         * @param metaObject
         */
        @Override
        public void insertFill(MetaObject metaObject) {
                //其中方法参数中第一个是前面自动填充所对应的字段，第二个是要自动填充的值。第三个是指定实体类的对象

                this.setFieldValByName("createdOn", LocalDateTime.now(), metaObject);
                this.setFieldValByName("updateOn", LocalDateTime.now(), metaObject);
                this.setFieldValByName("deletedFlag", 0, metaObject);
        }

        /**
         * //在执行mybatisPlus的update()时，为我们自动给某些字段填充值，这样的话，我们就不需要手动给update()里的实体类赋值了
         * @param metaObject
         */
        @Override
        public void updateFill(MetaObject metaObject) {
                this.setFieldValByName("updateOn", LocalDateTime.now(), metaObject);
        }
}