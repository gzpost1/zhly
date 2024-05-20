package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.system.infrastructure.entity.CustomConfigDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/19
 */
public interface CustomConfigDetailMapper extends BaseMapper<CustomConfigDetailEntity> {

    /**
     * 批量保存自定义配置详情
     *
     * @param customConfigDetailEntityList 自定义配置详情列表
     */
    int batchSaveCustomConfigDetails(@Param("list") List<CustomConfigDetailEntity> customConfigDetailEntityList);

    /**
     * 批量更新自定义配置详情
     *
     * @param customConfigDetailEntityList 自定义配置详情列表
     */
    int batchUpdateCustomConfigDetails(@Param("list") List<CustomConfigDetailEntity> customConfigDetailEntityList);

}
