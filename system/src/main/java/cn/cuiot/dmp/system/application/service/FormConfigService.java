package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.system.application.param.dto.BatchFormConfigDTO;
import cn.cuiot.dmp.system.application.param.dto.FormConfigCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.FormConfigUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigVO;

import java.util.List;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface FormConfigService {

    /**
     * 根据id获取业务类型详情
     */
    FormConfigVO queryForDetail(Long id);

    /**
     * 保存
     */
    int saveFormConfig(FormConfigCreateDTO createDTO);

    /**
     * 更新
     */
    int updateFormConfig(FormConfigUpdateDTO updateDTO);

    /**
     * 更新状态
     */
    int updateFormConfigStatus(UpdateStatusParam updateStatusParam);

    /**
     * 删除
     */
    int deleteFormConfig(Long id);

    /**
     * 批量移动
     */
    int batchMoveFormConfig(BatchFormConfigDTO batchFormConfigDTO);

    /**
     * 批量更新状态
     */
    int batchUpdateFormConfigStatus(BatchFormConfigDTO batchFormConfigDTO);

    /**
     * 批量删除
     */
    int batchDeleteFormConfig(List<Long> idList);

}
