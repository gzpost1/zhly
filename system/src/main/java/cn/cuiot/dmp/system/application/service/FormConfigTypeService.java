package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeVO;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface FormConfigTypeService {

    /**
     * 根据id获取业务类型详情
     */
    FormConfigTypeVO queryForDetail(Long id);

}
