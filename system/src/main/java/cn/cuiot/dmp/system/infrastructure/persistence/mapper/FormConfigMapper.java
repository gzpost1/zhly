package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.system.infrastructure.entity.FormConfigEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface FormConfigMapper extends BaseMapper<FormConfigEntity> {

    /**
     * 批量保存表单配置
     *
     * @param formConfigEntityList 表单配置列表
     */
    int batchSaveFormConfig(@Param("list") List<FormConfigEntity> formConfigEntityList);

    /**
     * 根据id批量更新表单配置的分类或者状态
     *
     * @param typeId 分类ID
     * @param status 状态
     * @param idList 主键ID集合
     */
    int batchUpdateFormConfigById(@Param("typeId") Long typeId, @Param("status") Byte status,
                                  @Param("idList") List<Long> idList);

    /**
     * 根据分类列表，默认把所选分类下所有的表单配置到"全部"分类下
     *
     * @param typeIdList 主键ID集合
     */
    int batchMoveFormConfigDefault(@Param("typeIdList") List<String> typeIdList, @Param("rootTypeId") Long rootTypeId);

}
