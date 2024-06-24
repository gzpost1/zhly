package cn.cuiot.dmp.digitaltwin.service.mapper;

import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightDeviceNotifierEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 格物消防-设备信息（联系人） mapper接口
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
public interface GwFirefightDeviceNotifierMapper extends BaseMapper<GwFirefightDeviceNotifierEntity> {

    void batchInsert(@Param("list") List<GwFirefightDeviceNotifierEntity> list);

    /**
     * 根据主表id删除数据
     *
     * @Param parentId 主表id
     */
    void deleteByParentId(Long parentId);
}
