package cn.cuiot.dmp.system.application.service;

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

}
