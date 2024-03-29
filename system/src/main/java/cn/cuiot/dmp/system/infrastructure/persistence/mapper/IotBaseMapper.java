package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Author 犬豪
 * @Date 2023/10/19 15:48
 * @Version V1.0
 */
public interface IotBaseMapper<T> extends BaseMapper<T> {
    int deleteByIdWithAllField(T entity);

}
