package cn.cuiot.dmp.base.application.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 分页助手
 *
 * @date 2024/8/22 14:06
 * @author gxp
 */
public class PageUtils {
    /**
     * 集合进行自主分页
     *
     * @param pageNo
     * @param pageSize
     * @param list
     * @param <E>
     * @return
     */
    public static <E> IPage<E> page (int pageNo, int pageSize, List<E> list) {
        List<E> newList = Lists.newArrayList();

        int begin = pageNo * pageSize - pageSize;
        int end = pageNo * pageSize;
        int pages = 0;

        if (end > list.size()) {
            end = list.size();
        }
        for (int i = begin; i < end; i++) {
            newList.add(list.get(i));
        }

        // 计算页面大小
        if (!CollectionUtils.isEmpty(list)) {
            BigDecimal bcs = new BigDecimal(list.size());
            BigDecimal cs = new BigDecimal(pageSize);
            BigDecimal divideResult = cs.divide(bcs, 0, BigDecimal.ROUND_UP);
            pages = divideResult.intValue();
        }

        // 设置page值
        Page<E> page  = new Page<>();
        page.setPages(pages);
        page.setCurrent(pageNo);
        page.setTotal(list.size());
        page.setSize(pageSize);
        page.setRecords(newList);
        return page;
    }


    /**
     * 集合进行自主分页
     *
     * @param pageNo
     * @param pageSize
     * @param list
     * @param <E>
     * @return
     */
    public static <E> IPage<E> page (Long pageNo, Long pageSize, List<E> list) {
        return page(pageNo.intValue(), pageSize.intValue(), list);
    }
}
