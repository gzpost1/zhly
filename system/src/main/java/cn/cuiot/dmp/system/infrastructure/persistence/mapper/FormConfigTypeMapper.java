package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.system.infrastructure.entity.FormConfigTypeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface FormConfigTypeMapper extends BaseMapper<FormConfigTypeEntity> {

    /**
     * 批量保存表单配置分类
     *
     * @param formConfigTypeEntityList 表单配置分类列表
     */
    int batchSaveFormConfigType(@Param("list") List<FormConfigTypeEntity> formConfigTypeEntityList);

}
