package cn.cuiot.dmp.sms.service;

import cn.cuiot.dmp.base.application.service.impl.ApiSystemServiceImpl;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.OrganizationReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.OrganizationRespDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.OrgTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.sms.entity.SmsSendRecordEntity;
import cn.cuiot.dmp.sms.query.SmsPushDataQuery;
import cn.cuiot.dmp.sms.query.SmsSendRecordPageQuery;
import cn.cuiot.dmp.sms.query.SmsStatisticsQuery;
import cn.cuiot.dmp.sms.vo.SmsSendRecordVO;
import cn.cuiot.dmp.sms.vo.SmsStatisticsVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 短信发送记录
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@Slf4j
@Service
public class SmsSendRecordService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ApiSystemServiceImpl apiSystemService;

    /**
     * 新增
     */
    public void create(SmsSendRecordEntity entity) {
        mongoTemplate.insert(entity);
    }

    /**
     * 推送数据
     *
     * @Param entity 参数
     */
    public void pushData(SmsPushDataQuery pushDataQuery) {
        log.info("获取第三方推送的短信发送记录.............{}", JsonUtil.writeValueAsString(pushDataQuery));

        if (StringUtils.isNotBlank(pushDataQuery.getTaskId())) {
            // 构建匹配操作
            Query query = new Query();
            query.addCriteria(Criteria.where(SmsSendRecordEntity.TASK_ID).regex(pushDataQuery.getTaskId()));

            // 构建更新操作
            Update update = new Update();
            update.set(SmsSendRecordEntity.REPORT_STATUS, pushDataQuery.getReportStatus());
            update.set(SmsSendRecordEntity.REPORT_DESCRIPTION, pushDataQuery.getReportDescription());
            if (StringUtils.isNotBlank(pushDataQuery.getReportTime())) {
                update.set(SmsSendRecordEntity.REPORT_TIME, DateTimeUtil.stringToDate(pushDataQuery.getReportTime()));
            }
            mongoTemplate.updateFirst(query, update, SmsSendRecordEntity.class);
        }
    }

    /**
     * 短信统计已使用数
     *
     * @return Long 使用量
     * @Param statisticsQuery 参数
     */
    public Long statisticsUsed(SmsStatisticsQuery statisticsQuery) {
        //构建查询条件
        Criteria criteria = buildCriteria(statisticsQuery);
        return mongoTemplate.count(new Query(criteria), SmsSendRecordEntity.class);
    }

    /**
     * 短信统计
     *
     * @return IPage
     * @Param query 参数
     */
    public IPage<SmsStatisticsVO> statisticsPage(SmsStatisticsQuery statisticsQuery) {
        //获取当前账户类型
        Long orgTypeId = getOrgTypeId();
        if (!Objects.equals(orgTypeId, OrgTypeEnum.PLATFORM.getValue())) {
            return new Page<>();
        }

        //构建查询条件
        Criteria criteria = buildCriteria(statisticsQuery);

        // 聚合操作
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group(SmsSendRecordEntity.COMPANY_ID).count().as("count"),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "count")),
                Aggregation.skip(statisticsQuery.getPageNo()),
                Aggregation.limit(statisticsQuery.getPageSize())
        );

        // 执行聚合查询
        AggregationResults<SmsStatisticsVO> results = mongoTemplate.aggregate(aggregation, SmsSendRecordEntity.class, SmsStatisticsVO.class);

        // 总记录数
        long totalRecords = mongoTemplate.count(new Query(criteria), SmsSendRecordEntity.class);
        // 记录
        List<SmsStatisticsVO> mappedResults = results.getMappedResults();

        //设置企业名称
        if (CollectionUtils.isNotEmpty(mappedResults)) {
            List<Long> companyIds = mappedResults.stream().map(SmsStatisticsVO::getCompanyId)
                    .filter(Objects::nonNull)
                    .distinct().collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(companyIds)) {
                OrganizationReqDTO reqDTO = new OrganizationReqDTO();
                reqDTO.setIdList(companyIds);
                List<OrganizationRespDTO> dtoList = apiSystemService.queryOrganizationList(reqDTO);

                if (CollectionUtils.isNotEmpty(dtoList)) {
                    Map<Long, String> map = dtoList.stream().collect(Collectors.toMap(OrganizationRespDTO::getId, OrganizationRespDTO::getCompanyName));
                    mappedResults.forEach(item ->{
                        item.setCompanyName(map.getOrDefault(item.getCompanyId(), null));
                    });
                }
            }
        }

        // 创建 IPage 对象
        IPage<SmsStatisticsVO> iPage = new Page<>(statisticsQuery.getPageNo(), statisticsQuery.getPageSize());
        iPage.setRecords(mappedResults);
        iPage.setTotal(totalRecords);

        return iPage;
    }

    /**
     * 构建查询条件
     *
     * @return Criteria
     * @Param statisticsQuery 参数
     */
    private Criteria buildCriteria(SmsStatisticsQuery statisticsQuery) {
        // 构建匹配操作
        Criteria criteria = new Criteria();

        // 条件-企业id
        if (Objects.nonNull(statisticsQuery.getCompanyId())) {
            criteria.and(SmsSendRecordEntity.COMPANY_ID).is(statisticsQuery.getCompanyId());
        }
        // 条件-部门id
        if (Objects.nonNull(statisticsQuery.getDeptId())) {
            DepartmentDto departmentDto = apiSystemService.lookUpDepartmentInfo(statisticsQuery.getDeptId(), null, null);
            criteria.and(SmsSendRecordEntity.COMPANY_ID).regex(departmentDto.getPath() + ".*");
        }
        // 条件-时间查询
        if (Objects.nonNull(statisticsQuery.getBeginDate()) || Objects.nonNull(statisticsQuery.getEndDate())) {
            if (Objects.nonNull(statisticsQuery.getBeginDate())) {
                criteria.and(SmsSendRecordEntity.CREATE_TIME).gte(statisticsQuery.getBeginDate());
            }
            if (Objects.nonNull(statisticsQuery.getEndDate())) {
                criteria.and(SmsSendRecordEntity.CREATE_TIME).lte(statisticsQuery.getEndDate());
            }
        }
        return criteria;
    }

    /**
     * 发送记录
     *
     * @return IPage
     * @Param pageQuery 参数
     */
    public IPage<SmsSendRecordVO> sendRecord(SmsSendRecordPageQuery pageQuery) {

        // 获取企业id
        Long companyId = getCompanyId();

        // 构建匹配操作
        Query query = new Query();

        long total;
        long pageNum = pageQuery.getPageNo();
        int pageSize = pageQuery.getPageSize().intValue();
        List<SmsSendRecordVO> voList;

        // 条件-企业id
        query.addCriteria(Criteria.where(SmsSendRecordEntity.COMPANY_ID).is(companyId));
        // 条件-手机号
        if (StringUtils.isNotBlank(pageQuery.getPhone())) {
            query.addCriteria(Criteria.where(SmsSendRecordEntity.PHONE).regex(".*" + pageQuery.getPhone() + ".*"));
        }
        // 条件-发送状态
        if (Objects.nonNull(pageQuery.getReportStatus())) {
            query.addCriteria(Criteria.where(SmsSendRecordEntity.REPORT_STATUS).is(pageQuery.getReportStatus()));
        }
        // 短信类型
        if (Objects.nonNull(pageQuery.getSmsType())) {
            query.addCriteria(Criteria.where(SmsSendRecordEntity.SMS_TYPE).is(pageQuery.getSmsType()));
        }
        // 条件-时间查询
        if (Objects.nonNull(pageQuery.getBeginDate()) || Objects.nonNull(pageQuery.getEndDate())) {
            Criteria criteria = Criteria.where(SmsSendRecordEntity.CREATE_TIME);
            if (Objects.nonNull(pageQuery.getBeginDate())) {
                criteria.gte(pageQuery.getBeginDate());
            }
            if (Objects.nonNull(pageQuery.getEndDate())) {
                criteria.lte(pageQuery.getEndDate());
            }
            query.addCriteria(criteria);
        }

        //计算总数
        total = mongoTemplate.count(query, SmsSendRecordEntity.class);

        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, SmsSendRecordEntity.CREATE_TIME);
        query.with(sort);
        query.skip((pageNum - 1) * pageSize).limit(pageSize);

        List<SmsSendRecordEntity> dataList = mongoTemplate.find(query, SmsSendRecordEntity.class);
        voList = BeanMapper.mapList(dataList, SmsSendRecordVO.class);

        Page<SmsSendRecordVO> page = new Page<>(pageNum, pageSize);
        page.setTotal(total);
        page.setRecords(voList);

        return page;
    }

    /**
     * 获取企业id
     */
    private Long getCompanyId() {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        if (Objects.isNull(companyId)) {
            throw new BusinessException(ResultCode.ERROR, "所属企业不存在");
        }
        return companyId;
    }

    /**
     * 获取账户类型
     */
    private Long getOrgTypeId() {
        Integer currentOrgTypeId = LoginInfoHolder.getCurrentOrgTypeId();
        if (Objects.isNull(currentOrgTypeId)) {
            throw new BusinessException(ResultCode.ERROR, "所属企业不存在");
        }
        return (long) currentOrgTypeId;
    }
}
