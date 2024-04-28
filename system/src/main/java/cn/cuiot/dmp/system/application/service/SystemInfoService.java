package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.application.param.dto.SystemInfoCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.SystemInfoQueryDTO;
import cn.cuiot.dmp.system.application.param.vo.SystemInfoVO;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface SystemInfoService {

    /**
     * 根据id获取业务类型详情
     */
    SystemInfoVO queryForDetail(Long id);

    /**
     * 根据来源id和来源类型获取详情
     */
    SystemInfoVO queryBySource(SystemInfoQueryDTO systemInfoQueryDTO);

    /**
     * 保存
     */
    int saveOrUpdateSystemInfo(SystemInfoCreateDTO systemInfoCreateDTO);

}
