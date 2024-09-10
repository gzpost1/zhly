package cn.cuiot.dmp.externalapi.service.mapper;

import cn.cuiot.dmp.externalapi.service.entity.PersonGroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 人员分组关联表 mapper 接口
 *
 * @Author: zc
 * @Date: 2024-09-05
 */
public interface PersonGroupRelationMapper extends BaseMapper<PersonGroupRelationEntity> {

    /**
     * 是否存在关联数据
     */
    Boolean isExistRelation(@Param("businessType") Byte businessType, @Param("personGroupId") Long personGroupId);
}