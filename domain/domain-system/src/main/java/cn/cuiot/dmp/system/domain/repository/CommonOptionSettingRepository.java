package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.system.domain.aggregate.CommonOptionSetting;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/14
 */
public interface CommonOptionSettingRepository {

    /**
     * 根据常用选项批量查询常用选项分类
     */
    List<CommonOptionSetting> batchQueryCommonOptionSettings(Long commonOptionId);

    /**
     * 批量保存或更新
     */
    void batchSaveOrUpdateCommonOptionSettings(Long commonOptionId, List<CommonOptionSetting> commonOptionSettings);

    /**
     * 根据常用选项批量删除常用选项分类
     */
    void batchDeleteCommonOptionSettings(List<Long> commonOptionIdList);

    /**
     * 根据常用选项批量查询常用选项分类
     */
    List<CommonOptionSetting> batchQueryCommonOptionSettingsByIds(List<Long> ids);
}
