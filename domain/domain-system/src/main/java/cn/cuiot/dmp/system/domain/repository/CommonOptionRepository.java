package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.system.domain.aggregate.CommonOption;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface CommonOptionRepository {

    /**
     * 根据id获取详情
     */
    CommonOption queryForDetail(Long id);

}
