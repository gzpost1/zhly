package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.query.PageResult;
import cn.cuiot.dmp.system.domain.aggregate.FormConfig;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigPageQuery;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigType;

import java.util.List;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface FormConfigTypeRepository {

    /**
     * 根据id获取详情
     */
    FormConfigType queryForDetail(Long id);

    /**
     * 根据企业id获取表单配置类型
     */
    List<FormConfigType> queryByCompany(Long companyId);

    /**
     * 保存
     */
    int saveFormConfigType(FormConfigType formConfigType);

    /**
     * 更新
     */
    int updateFormConfigType(FormConfigType formConfigType);

    /**
     * 删除
     */
    int deleteFormConfigType(List<String> idList);

    /**
     * 根据表单分类查询表单配置列表
     */
    PageResult<FormConfig> queryFormConfigByType(FormConfigPageQuery pageQuery);

}
