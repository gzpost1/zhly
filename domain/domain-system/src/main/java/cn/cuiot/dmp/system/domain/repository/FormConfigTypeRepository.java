package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.system.domain.aggregate.FormConfigType;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface FormConfigTypeRepository {

    /**
     * 根据id获取详情
     */
    FormConfigType queryForDetail(Long id);

}
