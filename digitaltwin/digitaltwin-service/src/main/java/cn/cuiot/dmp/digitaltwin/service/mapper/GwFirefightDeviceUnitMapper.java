package cn.cuiot.dmp.digitaltwin.service.mapper;

import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightDeviceUnitEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 格物消防-单位信息 mapper接口
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
public interface GwFirefightDeviceUnitMapper extends BaseMapper<GwFirefightDeviceUnitEntity> {

    /**
     * 根据主表id删除数据
     *
     * @Param parentId 主表id
     */
    void deleteByParentId(Long parentId);
}
