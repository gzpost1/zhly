package cn.cuiot.dmp.base.application.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author Mujun~
 * @Date 2020-09-25 11:29
 */
public interface BaseMybatisMapper<T> extends BaseMapper<T> {
    List<T> list(Page<T> page, @Param("params") T params);
}
