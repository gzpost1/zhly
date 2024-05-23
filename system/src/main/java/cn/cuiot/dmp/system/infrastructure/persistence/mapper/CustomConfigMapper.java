package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.system.infrastructure.entity.CustomConfigEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/17
 */
public interface CustomConfigMapper extends BaseMapper<CustomConfigEntity> {

    /**
     * 批量保存自定义配置
     *
     * @param customConfigEntityList 二自定义配置列表
     */
    int batchSaveCustomConfig(@Param("list") List<CustomConfigEntity> customConfigEntityList);

}
