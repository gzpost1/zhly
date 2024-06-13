package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.system.domain.aggregate.SystemOptionType;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/20
 */
public interface SystemOptionTypeRepository {

    /**
     * 根据条件获取档案类型列表
     */
    List<SystemOptionType> queryForList(SystemOptionType systemOptionType);

    /**
     * 根据id获取档案类型详情
     */
    SystemOptionType queryForDetail(Long id);

}
