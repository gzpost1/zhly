package cn.cuiot.dmp.common.utils;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 通用对象转换
 *
 * @author wuyongchong
 * @date 2019/8/19
 */
public class BeanMapper {

    private static Mapper dozer = DozerBeanMapperBuilder.buildDefault();

    public BeanMapper() {
    }

    public static <T> T map(Object source, Class<T> destinationClass) {
        return dozer.map(source, destinationClass);
    }

    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        List<T> destinationList = Lists.newArrayList();
        Iterator var3 = sourceList.iterator();

        while (var3.hasNext()) {
            Object sourceObject = var3.next();
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }

        return destinationList;
    }

    public static void copy(Object source, Object destinationObject) {
        dozer.map(source, destinationObject);
    }

    /**
     * copy类 ,可防止LocalDateTime类型复制出错
     * @param source
     * @param destinationClass
     * @param <T>
     * @return
     */
    public static <T> T copyBean(Object source, Class<T> destinationClass) {
        T t = null;
        try {
            t = destinationClass.newInstance();
            BeanUtils.copyProperties(source, t);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }
}