package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.system.infrastructure.entity.CommonOptionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface CommonOptionMapper extends BaseMapper<CommonOptionEntity> {

    /**
     * 根据id批量更新常用选项的分类或者状态
     *
     * @param typeId 分类ID
     * @param status 状态
     * @param idList 主键ID集合
     */
    int batchUpdateCommonOptionById(@Param("typeId") Long typeId, @Param("status") Byte status,
                                  @Param("idList") List<Long> idList);

    /**
     * 根据分类列表，默认把所选分类下所有的常用选项到"全部"分类下
     *
     * @param typeIdList 主键ID集合
     */
    int batchMoveCommonOptionDefault(@Param("typeIdList") List<String> typeIdList, @Param("rootTypeId") Long rootTypeId);

}