package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.application.param.vo.CommonOptionTypeVO;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface CommonOptionTypeService {

    /**
     * 根据id获取业务类型详情
     */
    CommonOptionTypeVO queryForDetail(Long id);

}
