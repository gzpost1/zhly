package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.application.param.vo.FormConfigVO;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface FormConfigService {

    /**
     * 根据id获取业务类型详情
     */
    FormConfigVO queryForDetail(Long id);

}
