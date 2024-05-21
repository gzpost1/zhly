package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.domain.aggregate.CommonOptionType;
import cn.cuiot.dmp.system.domain.aggregate.CommonOption;
import cn.cuiot.dmp.system.domain.aggregate.CommonOptionPageQuery;

import java.util.List;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface CommonOptionTypeRepository {

    /**
     * 根据id获取详情
     */
    CommonOptionType queryForDetail(Long id);

    /**
     * 获取常用选项类型列表
     */
    List<CommonOptionType> queryForList(List<Long> idList);

    /**
     * 根据企业id获取常用选项类型
     */
    List<CommonOptionType> queryByCompany(Long companyId);

    /**
     * 保存
     */
    int saveCommonOptionType(CommonOptionType commonOptionType);

    /**
     * 更新
     */
    int updateCommonOptionType(CommonOptionType commonOptionType);

    /**
     * 删除
     */
    int deleteCommonOptionType(List<String> idList);

    /**
     * 根据常用选项分类查询常用选项列表
     */
    PageResult<CommonOption> queryCommonOptionByType(CommonOptionPageQuery pageQuery);

    /**
     * 根据企业id获取根节点类型id
     */
    Long getRootTypeId(Long companyId);

}
