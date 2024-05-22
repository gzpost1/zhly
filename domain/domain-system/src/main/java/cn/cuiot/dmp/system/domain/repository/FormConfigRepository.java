package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.domain.aggregate.FormConfig;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigPageQuery;

import java.util.List;

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

    /**
     * 批量查询
     */
    List<FormConfig> batchQueryFormConfig(Byte status, List<Long> idList);

    /**
     * 批量移动
     */
    int batchMoveFormConfig(Long typeId, List<Long> idList);

    /**
     * 默认批量移动分类列表下所有的表单配置到"全部"分类下
     */
    int batchMoveFormConfigDefault(List<String> typeIdList, Long rootTypeId);

    /**
     * 批量更新状态
     */
    int batchUpdateFormConfigStatus(Byte status, List<Long> idList);

    /**
     * 批量删除
     */
    int batchDeleteFormConfig(List<Long> idList);

    /**
     * 根据表单分类查询表单配置列表
     */
    PageResult<FormConfig> queryFormConfigByType(FormConfigPageQuery pageQuery);

    /**
     * 根据表单配置检查常用选项是否引用，true为引用，false为未引用
     */
    boolean useCommonOptionByFormConfig(Long commonOptionId);

}