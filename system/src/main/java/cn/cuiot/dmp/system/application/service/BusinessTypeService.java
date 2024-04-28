package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.application.param.vo.BusinessTypeVO;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface BusinessTypeService {

    /**
     * 根据id获取业务类型详情
     */
    BusinessTypeVO queryForDetail(Long id);

}
