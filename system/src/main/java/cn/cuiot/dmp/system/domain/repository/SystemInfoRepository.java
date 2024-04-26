package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.system.domain.aggregate.SystemInfo;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface SystemInfoRepository {

    /**
     * 根据id获取详情
     */
    SystemInfo queryForDetail(Long id);

}
