package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.domain.aggregate.CustomConfig;
import cn.cuiot.dmp.system.domain.aggregate.CustomConfigPageQuery;

/**
 * @author caorui
 * @date 2024/5/17
 */
public interface CustomConfigRepository {

    /**
     * 根据id获取详情
     */
    CustomConfig queryForDetail(Long id);

    /**
     * 保存
     */
    int saveCustomConfig(CustomConfig customConfig);

    /**
     * 更新
     */
    int updateCustomConfig(CustomConfig customConfig);

    /**
     * 更新状态
     */
    int updateCustomConfigStatus(CustomConfig customConfig);

    /**
     * 删除
     */
    int deleteCustomConfig(Long id);

    /**
     * 根据档案分类查询自定义配置列表
     */
    PageResult<CustomConfig> queryCustomConfigByType(CustomConfigPageQuery pageQuery);

    /**
     * 初始化常用选项
     */
    void initCustomConfig(Long companyId, Long typeId);

}
