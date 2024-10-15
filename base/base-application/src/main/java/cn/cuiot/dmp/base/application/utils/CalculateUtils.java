package cn.cuiot.dmp.base.application.utils;

import cn.hutool.core.util.NumberUtil;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author xiaotao
 * @description 统计计算工具类
 * @data 2024/10/09 11:10
 */
public class CalculateUtils {

    public static <T,R extends Number> BigDecimal calculateSum(List<T> list, SFunction<T, R> func){
        if(CollectionUtils.isEmpty(list)){
            return new BigDecimal(0);
        }
        List<R> collectList = list.stream().map(func::apply).collect(Collectors.toList());
        BigDecimal res = new BigDecimal(0);
        for (R r : collectList) {
            res = NumberUtil.add(res,r);
        }
        return res;
    }

}
