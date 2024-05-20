package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.system.domain.aggregate.CustomConfigDetail;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/17
 */
public interface CustomConfigDetailRepository {

    /**
     * 根据自定义配置ID批量查询自定义配置详情
     */
    List<CustomConfigDetail> batchQueryCustomConfigDetails(Long customConfigId);

    /**
     * 批量保存或更新
     */
    void batchSaveOrUpdateCustomConfigDetails(Long customConfigId, List<CustomConfigDetail> customConfigDetails);

    /**
     * 根据自定义配置ID批量删除自定义配置详情
     */
    void batchDeleteCustomConfigDetails(List<Long> customConfigIdList);

}
