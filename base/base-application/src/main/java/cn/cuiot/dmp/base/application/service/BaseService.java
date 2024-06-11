package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.common.constant.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * @author Mujun
 * @since 2020-09-27
 */
public interface BaseService<T> extends IService {

      List<T> list(T params);

      PageResult<T> page(PageQuery param) ;
}
