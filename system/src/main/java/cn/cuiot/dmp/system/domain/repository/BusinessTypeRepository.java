package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.system.domain.aggregate.BusinessType;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface BusinessTypeRepository {

    /**
     * 根据id获取业务类型详情
     */
    BusinessType queryForDetail(Long id);

}
