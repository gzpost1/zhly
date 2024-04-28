package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.application.param.dto.BusinessTypeCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.BusinessTypeUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.BusinessTypeVO;
import cn.cuiot.dmp.system.domain.aggregate.BusinessType;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface BusinessTypeService {

    /**
     * 根据id获取业务类型详情
     */
    BusinessTypeVO queryForDetail(Long id);

    /**
     * 保存
     */
    int saveBusinessType(BusinessTypeCreateDTO businessTypeCreateDTO);

    /**
     * 更新
     */
    int updateBusinessType(BusinessTypeUpdateDTO businessTypeUpdateDTO);

}
