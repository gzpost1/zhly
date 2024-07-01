package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.lease.dto.charge.ChargeCollectionManageRecordQuery;
import cn.cuiot.dmp.lease.dto.charge.ChargeCollectionRecordStatisticsDto;
import cn.cuiot.dmp.lease.dto.charge.ChargeCollectionRecordStatisticsQuery;
import cn.cuiot.dmp.lease.entity.charge.ChargeCollectionRecordEntity;
import cn.cuiot.dmp.lease.vo.ChargeCollectionRecordVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.lease.mapper.charge.ChargeCollectionRecordMapper;

import java.util.List;

/**
 * 收费管理-催款记录 业务层
 *
 * @author zc
 */
@Service
public class ChargeCollectionRecordService extends ServiceImpl<ChargeCollectionRecordMapper, ChargeCollectionRecordEntity> {

    /**
     * 查询统计
     *
     * @return list
     * @Param chargeIds 缴费记录ids
     */
    public List<ChargeCollectionRecordStatisticsDto> getStatistics(ChargeCollectionRecordStatisticsQuery query) {
        return baseMapper.getStatistics(query);
    }

    /**
     * 记录
     */
    public IPage<ChargeCollectionRecordVo> record(ChargeCollectionManageRecordQuery query) {
        return baseMapper.record(new Page<>(query.getPageNo(), query.getPageSize()), query);
    }
}