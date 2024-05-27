package cn.cuiot.dmp.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * java流工具类
 *
 * @author caorui
 * @date 2024/5/27
 */
public class StreamUtil {

    /**
     * 根据对象属性去重：
     * putIfAbsent方法添加键值对，如果map集合中没有该key对应的值，则直接添加，并返回null，如果已经存在对应的值，则依旧为原来的值。
     * 如果返回null表示添加数据成功(不重复)，不重复(null==null :TRUE)
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
