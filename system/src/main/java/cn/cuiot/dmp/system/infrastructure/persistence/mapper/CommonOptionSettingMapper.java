package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.system.infrastructure.entity.CommonOptionSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/14
 */
public interface CommonOptionSettingMapper extends BaseMapper<CommonOptionSettingEntity> {

    /**
     * 批量保存常用选项设置
     *
     * @param commonOptionSettingEntityList 常用选项设置列表
     */
    int batchSaveCommonOptionSettings(@Param("list") List<CommonOptionSettingEntity> commonOptionSettingEntityList);

    /**
     * 批量更新常用选项设置
     *
     * @param commonOptionSettingEntityList 常用选项设置列表
     */
    int batchUpdateCommonOptionSettings(@Param("list") List<CommonOptionSettingEntity> commonOptionSettingEntityList);

}
