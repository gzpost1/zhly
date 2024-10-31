package cn.cuiot.dmp.externalapi.service.mapper.gw.gasalarm;

import cn.cuiot.dmp.externalapi.service.entity.gw.gasalarm.GwGasAlarmFaultRecordEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.gasalarm.GwGasAlarmFaultRecordQuery;
import cn.cuiot.dmp.externalapi.service.vo.gw.gasalarm.GwGasAlarmFaultRecordVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 格物燃气报警故障记录 mapper接口
 *
 * @Author: zc
 * @Date: 2024-10-24
 */
public interface GwGasAlarmFaultRecordMapper extends BaseMapper<GwGasAlarmFaultRecordEntity> {

    /**
     * 故障记录表
     *
     * @return IPage
     * @Param query 参数
     */
    IPage<GwGasAlarmFaultRecordVO> queryRecordForPage(Page page, @Param("params") GwGasAlarmFaultRecordQuery query);
}
