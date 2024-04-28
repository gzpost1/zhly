package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.system.application.param.dto.SystemInfoQueryDTO;
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

    /**
     * 根据来源id和来源类型获取详情
     */
    SystemInfo queryBySource(SystemInfoQueryDTO systemInfoQueryDTO);

    /**
     * 保存
     */
    int saveSystemInfo(SystemInfo systemInfo);

    /**
     * 更新
     */
    int updateSystemInfo(SystemInfo systemInfo);

}
