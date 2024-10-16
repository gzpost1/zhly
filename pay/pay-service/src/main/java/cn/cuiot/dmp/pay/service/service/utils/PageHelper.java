package cn.cuiot.dmp.pay.service.service.utils;

import cn.cuiot.dmp.common.bean.OrderItemParam;
import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.common.utils.BeanMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;

public class PageHelper {
    public static Page queryToPage(PageQuery pageQuery) {
        Page page = new Page();
        if (pageQuery.getPageNo() != null) {
            page.setCurrent(pageQuery.getPageNo());
        }
        if (pageQuery.getPageSize() != null) {
            page.setSize(pageQuery.getPageSize());
        }
        page.setOrders(new ArrayList<>());
        if (pageQuery.getOrderByItems() != null && pageQuery.getOrderByItems().size() > 0) {
            page.getOrders().addAll(BeanMapper.mapList(pageQuery.getOrderByItems(), OrderItem.class));
        }
        if (pageQuery.getDescs() != null) {
            for (String desc : pageQuery.getDescs()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setColumn(desc);
                orderItem.setAsc(false);
                page.getOrders().add(orderItem);
            }
        }
        if (pageQuery.getAscs() != null) {
            for (String asc : pageQuery.getAscs()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setColumn(asc);
                orderItem.setAsc(true);
                page.getOrders().add(orderItem);
            }
        }

        return page;
    }

    public static Page queryToPage(boolean conditionOrder, PageQuery pageQuery) {
        Page page = new Page();
        if (conditionOrder) {
            page = queryToPage(pageQuery);
        }
        if (pageQuery.getPageNo() != null) {
            page.setCurrent(pageQuery.getPageNo());
        }
        if (pageQuery.getPageSize() != null) {
            page.setSize(pageQuery.getPageSize());
        }
        return page;
    }

    public static <T> QueryWrapper<T> queryToWrapper(PageQuery pageQuery, QueryWrapper<T> queryWrapper) {
        if (pageQuery.getDescs() != null) {
            for (String desc : pageQuery.getDescs()) {
                queryWrapper.orderByDesc(desc);
            }
        }
        if (pageQuery.getAscs() != null) {
            for (String asc : pageQuery.getAscs()) {
                queryWrapper.orderByAsc(asc);
            }
        }
        if (pageQuery.getOrderByItems() != null && pageQuery.getOrderByItems().size() > 0) {
            for (OrderItemParam orderItem : pageQuery.getOrderByItems()) {
                if (orderItem.isAsc()) {
                    queryWrapper.orderByAsc(orderItem.getColumn());
                } else {
                    queryWrapper.orderByDesc(orderItem.getColumn());
                }
            }
        }

        return queryWrapper;
    }

}
