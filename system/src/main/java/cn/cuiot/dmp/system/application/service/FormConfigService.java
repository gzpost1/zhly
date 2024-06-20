package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.FormConfigReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.FormConfigRspDTO;
import cn.cuiot.dmp.system.application.param.dto.*;
import cn.cuiot.dmp.system.application.param.vo.FormConfigVO;

import java.util.List;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface FormConfigService {

    /**
     * 根据id获取表单详情
     */
    FormConfigVO queryForDetail(Long id);

    /**
     * 根据名称获取表单详情
     */
    FormConfigVO queryForDetailByName(FormConfigDTO formConfigDTO);

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
     * 批量查询
     */
    List<FormConfigRspDTO> batchQueryFormConfig(FormConfigReqDTO formConfigReqDTO);

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

    /**
     * 从缓存获取表单配置内容
     */
    String getFormConfigFromCache(FormConfigCacheDTO cacheDTO);

    /**
     * 写入表单配置内容到缓存
     */
    void setFormConfig2Cache(FormConfigCacheDTO cacheDTO);

    /**
     * 从缓存删除表单配置内容
     */
    void deleteFormConfigFromCache(FormConfigCacheDTO cacheDTO);

}
