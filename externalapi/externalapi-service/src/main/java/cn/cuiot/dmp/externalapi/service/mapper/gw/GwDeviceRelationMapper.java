package cn.cuiot.dmp.externalapi.service.mapper.gw;

import cn.cuiot.dmp.externalapi.service.entity.gw.GwDeviceRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 格物设备数据关联表 mapper 接口
 *
 * @Author: zc
 * @Date: 2024-09-13
 */
public interface GwDeviceRelationMapper extends BaseMapper<GwDeviceRelationEntity> {

    /**
     * 查询设备关联数据
     *
     * @param entity 参数
     * @return GwDeviceRelationEntity
     */
    GwDeviceRelationEntity getGwDeviceRelation(@Param("params") GwDeviceRelationEntity entity);

}