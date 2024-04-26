package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.system.domain.aggregate.CommonOptionType;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface CommonOptionTypeRepository {

    /**
     * 根据id获取详情
     */
    CommonOptionType queryForDetail(Long id);

}
