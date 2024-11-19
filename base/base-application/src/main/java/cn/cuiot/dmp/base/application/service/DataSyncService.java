package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyRelationDTO;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.util.List;

/**
 * （企业初始化）数据同步业务
 *
 * @Author: zc
 * @Date: 2024-11-06
 */
public abstract class DataSyncService<T> {

    /**
     * 同步数据的核心方法
     *
     * @param dto 参数
     */
    public final void syncData(SyncCompanyDTO dto) {
        List<T> sourceData = fetchData(dto.getSourceCompanyId());
        if (CollectionUtils.isNotEmpty(sourceData)) {
            List<SyncCompanyRelationDTO<T>> targetData = preprocessData(sourceData, dto.getTargetCompanyId());
            saveData(targetData, dto.getTargetCompanyId());
            // 同步关联数据
            syncAssociatedData(targetData, dto);
        }
    }

    /**
     * 获取源企业数据
     *
     * @return List
     * @Param companyId 企业id
     */
    public abstract List<T> fetchData(Long sourceCompanyId);

    /**
     * 预处理数据，进行ID或其他字段替换
     *
     * @return List
     * @Param data 参数
     * @Param targetCompanyId 目标企业id
     */
    public abstract List<SyncCompanyRelationDTO<T>> preprocessData(List<T> data, Long targetCompanyId);

    /**
     * 保存数据到目标企业
     */
    public abstract void saveData(List<SyncCompanyRelationDTO<T>> data, Long targetCompanyId);

    /**
     * 同步与主数据关联的其他数据（例如角色的菜单授权）
     */
    public abstract void syncAssociatedData(List<SyncCompanyRelationDTO<T>> targetData, SyncCompanyDTO dto);

    /**
     * 情况同步数据（初始化非system服务报错时，执行此方法情况数据）
     */
    public void cleanSyncData(SyncCompanyDTO dto){};
}
