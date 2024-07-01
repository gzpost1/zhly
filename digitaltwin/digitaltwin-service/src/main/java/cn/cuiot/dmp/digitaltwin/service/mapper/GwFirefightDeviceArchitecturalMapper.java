package cn.cuiot.dmp.digitaltwin.service.mapper;

import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightDeviceArchitecturalEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 格物消防-建筑对象 mapper接口
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
public interface GwFirefightDeviceArchitecturalMapper extends BaseMapper<GwFirefightDeviceArchitecturalEntity> {
    /**
     * 根据主表id删除数据
     *
     * @Param parentId 主表id
     */
    void deleteByParentId(Long parentId);
}
