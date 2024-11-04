package cn.cuiot.dmp.externalapi.service.mapper.gw.waterleachalarm;

import cn.cuiot.dmp.externalapi.service.entity.gw.waterleachalarm.GwWaterLeachAlarmFaultRecordEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.waterleachalarm.GwWaterLeachAlarmFaultRecordQuery;
import cn.cuiot.dmp.externalapi.service.vo.gw.waterleachalarm.GwWaterLeachAlarmFaultRecordVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 格物水浸报警故障记录 mapper接口
 *
 * @Author: zc
 * @Date: 2024-10-24
 */
public interface GwWaterLeachAlarmFaultRecordMapper extends BaseMapper<GwWaterLeachAlarmFaultRecordEntity> {

    /**
     * 故障记录表
     *
     * @return IPage
     * @Param query 参数
     */
    IPage<GwWaterLeachAlarmFaultRecordVO> queryRecordForPage(Page<?> page, @Param("params") GwWaterLeachAlarmFaultRecordQuery query);
}
