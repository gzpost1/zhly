package cn.cuiot.dmp.pay.service.service.service;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.pay.service.service.dto.BalanceChangeRecordQuery;
import cn.cuiot.dmp.pay.service.service.dto.BalanceChargeRecordQuery;
import cn.cuiot.dmp.pay.service.service.entity.BalanceChangeRecord;
import cn.cuiot.dmp.pay.service.service.mapper.BalanceChangeRecordMapper;
import cn.cuiot.dmp.pay.service.service.utils.PageHelper;
import cn.cuiot.dmp.pay.service.service.vo.BalanceChargeRecordVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.nonNull;

/**
 * <p>
 * 余额明细变动表 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2023-11-29
 */
@Service
public class MbBalanceChangeRecordService extends ServiceImpl<BalanceChangeRecordMapper, BalanceChangeRecord>{

    /**
     * 按订单及变更状态查明细
     *
     * @param orderId
     * @param changeType
     * @return
     */
    public BalanceChangeRecord selectOneByOrderId(Long orderId, Byte changeType) {
        if (orderId == null || changeType == null) {
            throw new BusinessException(ResultCode.OBJECT_NOT_EXIST, "订单号及变更类型都必传");
        }
        List<BalanceChangeRecord> list = queryForList(BalanceChangeRecordQuery.builder()
                .orderId(orderId).changeType(changeType)
                .build());
        return CollectionUtils.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    public IPage<BalanceChangeRecord> queryForPage(BalanceChangeRecordQuery query) {
        LambdaQueryWrapper<BalanceChangeRecord> queryWrapper = initQuery(query);
        if (query.getDescs() == null || query.getDescs().length <= 0) {
            query.setDescs(new String[]{"create_time"});
        }
        return baseMapper.selectPage(PageHelper.queryToPage(query), queryWrapper);
    }

    /**
     * 不分页查询
     *
     * @param query
     * @return
     */
    public List<BalanceChangeRecord> queryForList(BalanceChangeRecordQuery query) {
        LambdaQueryWrapper<BalanceChangeRecord> queryWrapper = initQuery(query);
        queryWrapper.orderByDesc(BalanceChangeRecord::getCreateTime);
        return this.baseMapper.selectList(queryWrapper);
    }
    private LambdaQueryWrapper<BalanceChangeRecord> initQuery(BalanceChangeRecordQuery query) {
        LambdaQueryWrapper<BalanceChangeRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(nonNull(query.getHouseId()), BalanceChangeRecord::getHouseId, query.getHouseId());
        queryWrapper.eq(nonNull(query.getOrderId()), BalanceChangeRecord::getOrderId, query.getOrderId());
        queryWrapper.eq(nonNull(query.getChangeType()), BalanceChangeRecord::getChangeType, query.getChangeType());
        queryWrapper.eq(StringUtils.isNotEmpty(query.getOutRefundNo()), BalanceChangeRecord::getOutRefundNo,
                query.getOutRefundNo());
        if (StringUtils.isNotEmpty(query.getOrderName())) {
            queryWrapper.like(BalanceChangeRecord::getOrderName, query.getOrderName().trim());
        }
        queryWrapper.ge(nonNull(query.getBeginDate()), BalanceChangeRecord::getCreateTime, query.getBeginDate());
        queryWrapper.le(nonNull(query.getEndDate()), BalanceChangeRecord::getCreateTime, query.getEndDate().plusDays(1));
        return queryWrapper;
    }

    public IPage<BalanceChargeRecordVO> queryChargeForPage(BalanceChargeRecordQuery query) {
        return baseMapper.queryChargeForPage(new Page(query.getPageNo(), query.getPageSize()), query);
    }
}
