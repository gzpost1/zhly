package cn.cuiot.dmp.base.application.service.impl;

import cn.cuiot.dmp.base.application.service.BaseService;
import cn.cuiot.dmp.base.application.service.impl.BaseMybatisMapper;
import cn.cuiot.dmp.base.application.utils.NameUtils;
import cn.cuiot.dmp.common.bean.OrderItemParam;
import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.utils.BeanMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description
 * @Author Mujun~
 * @Date 2020-09-25 11:21
 */
public abstract class BaseMybatisServiceImpl<M extends BaseMybatisMapper<T>,T> extends ServiceImpl  implements BaseService<T> {
    @Autowired
    protected M baseMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResult<T> page(PageQuery param) {
        Page<T> page = new Page<T>(param.getPageNo(), param.getPageSize());
        List<OrderItem> orderItems = new LinkedList<>();
        if(param.getAscs()!=null && param.getAscs().length>0){
            Arrays.asList(param.getAscs()).forEach(bean->{
                orderItems.add(OrderItem.asc(NameUtils.humpToLine(bean)));
            });
        }
        if(param.getDescs()!=null&& param.getDescs().length>0){
            Arrays.asList(param.getDescs()).forEach(bean->{
                orderItems.add(OrderItem.desc(NameUtils.humpToLine(bean)));
            });
        }
        List<OrderItemParam> orderItemParams = param.getOrderByItems();
        if (orderItemParams !=null&& orderItemParams.size()>0) {
            ArrayList<OrderItem> itemArrayList = Lists.newArrayList();
            orderItemParams.forEach(o->{
                OrderItem orderItem = BeanMapper.copyBean(o, OrderItem.class);
                itemArrayList.add(orderItem);
            });
            orderItems.addAll(itemArrayList);
        }
        page.setOrders(orderItems);
        T entity = BeanMapper.copyBean(param, getTClass());
        List<T> queryList = baseMapper.list(page, entity);
        page.setRecords(queryList);
        PageResult pageResult = BeanMapper.copyBean(page, PageResult.class);
        pageResult.setList(queryList);
        return pageResult;

    }

    @Override
    @Transactional(readOnly = true)
    public List<T> list(T params) {
        return baseMapper.list(null,params);
    }


    public Class<T> getTClass() {
        return (Class<T>)
                ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Override
    public T getById(Serializable id) {
        return (T) super.getById(id);
    }
}
