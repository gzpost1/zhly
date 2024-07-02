package cn.cuiot.dmp.lease.mapper.charge;

import cn.cuiot.dmp.lease.dto.charge.ChargeCollectionManageRecordQuery;
import cn.cuiot.dmp.lease.dto.charge.ChargeCollectionRecordStatisticsDto;
import cn.cuiot.dmp.lease.dto.charge.ChargeCollectionRecordStatisticsQuery;
import cn.cuiot.dmp.lease.entity.charge.ChargeCollectionRecordEntity;
import cn.cuiot.dmp.lease.vo.ChargeCollectionRecordVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收费管理-催款记录 mapper 接口
 *
 * @author zc
 */
public interface ChargeCollectionRecordMapper extends BaseMapper<ChargeCollectionRecordEntity> {

    List<ChargeCollectionRecordStatisticsDto> getStatistics(@Param("params") ChargeCollectionRecordStatisticsQuery query);

    IPage<ChargeCollectionRecordVo> record(Page page, @Param("params") ChargeCollectionManageRecordQuery query);
}