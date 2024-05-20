package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.domain.aggregate.CommonOption;
import cn.cuiot.dmp.system.domain.aggregate.CommonOptionPageQuery;

import java.util.List;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface CommonOptionRepository {

    /**
     * 根据id获取详情
     */
    CommonOption queryForDetail(Long id);

    /**
     * 根据条件获取详情
     */
    CommonOption queryForDetailByName(CommonOption commonOption);

    /**
     * 保存
     */
    int saveCommonOption(CommonOption commonOption);

    /**
     * 更新
     */
    int updateCommonOption(CommonOption commonOption);

    /**
     * 更新状态
     */
    int updateCommonOptionStatus(CommonOption commonOption);

    /**
     * 删除预校验
     */
    void checkDeleteStatus(Long id);

    /**
     * 删除
     */
    int deleteCommonOption(Long id);

    /**
     * 批量查询
     */
    List<CommonOption> batchQueryCommonOption(Byte status, List<Long> idList);

    /**
     * 批量移动
     */
    int batchMoveCommonOption(Long typeId, List<Long> idList);

    /**
     * 默认批量移动分类列表下所有的表单配置到"全部"分类下
     */
    int batchMoveCommonOptionDefault(List<String> typeIdList);

    /**
     * 批量更新状态
     */
    int batchUpdateCommonOptionStatus(Byte status, List<Long> idList);

    /**
     * 批量删除
     */
    int batchDeleteCommonOption(List<Long> idList);

    /**
     * 根据常用选项分类查询常用选项列表
     */
    PageResult<CommonOption> queryCommonOptionByType(CommonOptionPageQuery pageQuery);

    /**
     * 初始化常用选项
     */
    void initCommonOption(Long companyId, Long typeId);

}
