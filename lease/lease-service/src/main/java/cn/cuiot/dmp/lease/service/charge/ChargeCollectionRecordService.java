package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.lease.dto.charge.ChargeCollectionManageRecordQuery;
import cn.cuiot.dmp.lease.dto.charge.ChargeCollectionRecordStatisticsDto;
import cn.cuiot.dmp.lease.dto.charge.ChargeCollectionRecordStatisticsQuery;
import cn.cuiot.dmp.lease.entity.charge.ChargeCollectionRecordEntity;
import cn.cuiot.dmp.lease.vo.ChargeCollectionRecordVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 收费管理-催款记录 业务层
 *
 * @author zc
 */
@Service
public class ChargeCollectionRecordService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询统计
     *
     * @return list
     * @Param chargeIds 缴费记录ids
     */
    public List<ChargeCollectionRecordStatisticsDto> getStatistics(ChargeCollectionRecordStatisticsQuery query) {
        // 匹配操作根据标准筛选记录
        MatchOperation matchOperation = Aggregation.match(
                Criteria.where(ChargeCollectionRecordEntity.MONGODB_COMPANY_ID).is(query.getCompanyId())
                        .and(ChargeCollectionRecordEntity.MONGODB_CUSTOMER_USER_ID).in(query.getCustomerUserIds()));

        // 组操作聚合数据
        GroupOperation groupOperation = Aggregation.group(ChargeCollectionRecordEntity.MONGODB_CUSTOMER_USER_ID)
                .first(ChargeCollectionRecordEntity.MONGODB_CUSTOMER_USER_ID).as("customerUserId")
                .max(ChargeCollectionRecordEntity.MONGODB_DATE).as("lastNoticeTime")
                .count().as("totalNoticeNum");

        // 聚合
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation);

        // 执行聚合
        AggregationResults<ChargeCollectionRecordStatisticsDto> results = mongoTemplate.aggregate(aggregation,
                "tb_charge_collection_record", ChargeCollectionRecordStatisticsDto.class);
        return results.getMappedResults();
    }

    public void batchSave(List<ChargeCollectionRecordEntity> list) {
        mongoTemplate.insertAll(list);
    }

    /**
     * 记录
     */
    public IPage<ChargeCollectionRecordVo> record(ChargeCollectionManageRecordQuery params) {
        // 构建匹配操作
        Query query = new Query();
        if (Objects.nonNull(params.getCompanyId())) {
            query.addCriteria(Criteria.where(ChargeCollectionRecordEntity.MONGODB_COMPANY_ID).is(params.getCompanyId()));
        }
        if (Objects.nonNull(params.getCustomerUserId())) {
            query.addCriteria(Criteria.where(ChargeCollectionRecordEntity.MONGODB_CUSTOMER_USER_ID).is(params.getCustomerUserId()));
        }
        if (Objects.nonNull(params.getBeginTime())) {
            query.addCriteria(Criteria.where(ChargeCollectionRecordEntity.MONGODB_DATE).gte(params.getBeginTime()));
        }
        if (Objects.nonNull(params.getEndTime())) {
            query.addCriteria(Criteria.where(ChargeCollectionRecordEntity.MONGODB_DATE).lte(params.getEndTime()));
        }
        if (StringUtils.isNotBlank(params.getId())) {
            query.addCriteria(Criteria.where(ChargeCollectionRecordEntity.MONGODB_ID).is(params.getId()));
        }

        //计算总数
        long total = mongoTemplate.count(query, ChargeCollectionRecordEntity.class);

        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, ChargeCollectionRecordEntity.MONGODB_DATE);
        query.with(sort);
        // 分页 (当前页-1)*每页大小，每页大小
        long pageNum = params.getPageNo();
        int pageSize = params.getPageSize().intValue();
        query.skip((pageNum - 1) * pageSize).limit(pageSize);

        List<ChargeCollectionRecordEntity> dataList = mongoTemplate.find(query, ChargeCollectionRecordEntity.class);
        List<ChargeCollectionRecordVo> voList = BeanMapper.mapList(dataList, ChargeCollectionRecordVo.class);

        Page<ChargeCollectionRecordVo> page = new Page<>(pageNum, pageSize);
        page.setTotal(total);
        page.setRecords(voList);

        return page;
    }
}