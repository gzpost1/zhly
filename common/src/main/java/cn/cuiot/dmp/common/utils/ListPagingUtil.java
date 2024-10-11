package cn.cuiot.dmp.common.utils;

import java.util.List;

/**
 * 内存分页工具
 * @author: wuyongchong
 * @date: 2024/10/11 10:24
 */
public class ListPagingUtil {

    public static <T> List<T> pageBySubList(List<T> list, Integer page, Integer limit) {
        //获取pageable的参数的页码，从1开始
        int pageNo = page;
        //获取pageable的页显示大小
        int pageSize = limit;

        int fromIndex = pageSize * (pageNo - 1);
        int toIndex = pageSize * pageNo;

        if (toIndex > list.size()) {
            toIndex = list.size();
        }
        if (fromIndex > toIndex) {
            fromIndex = toIndex;
        }
        //获取list的分页集合
        List result = list.subList(fromIndex, toIndex);
        return result;
    }

}
