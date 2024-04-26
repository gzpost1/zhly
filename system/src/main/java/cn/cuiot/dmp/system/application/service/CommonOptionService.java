package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.application.param.vo.CommonOptionVO;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface CommonOptionService {

    /**
     * 根据id获取业务类型详情
     */
    CommonOptionVO queryForDetail(Long id);

}
