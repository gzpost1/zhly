package cn.cuiot.dmp.externalapi.service.mapper.hik;

import cn.cuiot.dmp.externalapi.service.entity.hik.HikAcsDoorEventsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 海康门禁点事件 mapper接口
 *
 * @Author: zc
 * @Date: 2024-10-11
 */
public interface HikAcsDoorEventsMapper extends BaseMapper<HikAcsDoorEventsEntity> {

    /**
     * 批量覆盖保存事件
     *
     * @Param list 参数
     */
    void batchReplaceInsert(@Param("list") List<HikAcsDoorEventsEntity> list);
}
