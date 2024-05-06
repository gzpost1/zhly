package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.system.domain.aggregate.FormConfig;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface FormConfigRepository {

    /**
     * 根据id获取详情
     */
    FormConfig queryForDetail(Long id);

    /**
     * 保存
     */
    int saveFormConfig(FormConfig formConfig);

    /**
     * 更新
     */
    int updateFormConfig(FormConfig formConfig);

    /**
     * 更新状态
     */
    int updateFormConfigStatus(FormConfig formConfig);

    /**
     * 删除
     */
    int deleteFormConfig(Long id);

}
