package cn.cuiot.dmp.sms.service;

import cn.cuiot.dmp.base.application.service.impl.ApiSystemServiceImpl;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.OrganizationReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.OrganizationRespDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.OrgTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.sms.entity.SmsSendRecordEntity;
import cn.cuiot.dmp.sms.query.SmsPushDataQuery;
import cn.cuiot.dmp.sms.query.SmsSendRecordPageQuery;
import cn.cuiot.dmp.sms.query.SmsStatisticsQuery;
import cn.cuiot.dmp.sms.vo.SmsSendRecordVO;
import cn.cuiot.dmp.sms.vo.SmsStatisticsVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 短信发送记录
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
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
     * 第三方推送数据
     *
     * @Param pushDataQuery 参数
     */
    public void pushSendRecord(List<SmsPushDataQuery> queryList) {
        if (CollectionUtils.isNotEmpty(queryList)) {
            queryList.forEach(item ->{
                if (StringUtils.isNotBlank(item.getTaskId())) {
                    // 构建匹配操作
                    Query query = new Query();
                    query.addCriteria(Criteria.where(SmsSendRecordEntity.TASK_ID).is(item.getTaskId()));

                    // 构建更新操作
                    Update update = new Update();
                    update.set(SmsSendRecordEntity.REPORT_STATUS, item.getReportStatus());
                    update.set(SmsSendRecordEntity.REPORT_DESCRIPTION, item.getReportDescription());
                    update.set(SmsSendRecordEntity.REPORT_TIME, item.getReportTime());
                    mongoTemplate.updateFirst(query, update, SmsSendRecordEntity.class);
                }
            });
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
        // 获取当前账户类型
        Long orgTypeId = getOrgTypeId();
        if (!Objects.equals(orgTypeId, OrgTypeEnum.PLATFORM.getValue())) {
            return new Page<>();
        }

        // 构建查询条件
        Criteria criteria = buildCriteria(statisticsQuery);

        // 统计总记录数的聚合操作
        Aggregation aggregationTotal = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group(SmsSendRecordEntity.COMPANY_ID),
                Aggregation.group().count().as("total")
        );

        // 执行聚合查询获取总记录数
        AggregationResults<Map> totalRecords = mongoTemplate.aggregate(aggregationTotal, SmsSendRecordEntity.class, Map.class);
        long totalCount = totalRecords.getMappedResults().isEmpty() ? 0 : Long.parseLong(totalRecords.getMappedResults().get(0).get("total") + "");

        // 分页查询的聚合操作
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group(SmsStatisticsVO.COMPANY_ID).count().as(SmsStatisticsVO.NUMBER),
                Aggregation.project(SmsStatisticsVO.NUMBER).and("_id").as(SmsStatisticsVO.COMPANY_ID),
                Aggregation.sort(Sort.Direction.DESC, SmsStatisticsVO.NUMBER),
                Aggregation.skip((statisticsQuery.getPageNo() - 1) * statisticsQuery.getPageSize()),
                Aggregation.limit(statisticsQuery.getPageSize())
        );

        // 执行分页查询
        AggregationResults<SmsStatisticsVO> results = mongoTemplate.aggregate(aggregation, SmsSendRecordEntity.class, SmsStatisticsVO.class);
        List<SmsStatisticsVO> mappedResults = results.getMappedResults();

        // 设置企业名称
        if (CollectionUtils.isNotEmpty(mappedResults)) {
            List<Long> companyIds = mappedResults.stream()
                    .map(SmsStatisticsVO::getCompanyId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(companyIds)) {
                // 查询企业信息
                OrganizationReqDTO reqDTO = new OrganizationReqDTO();
                reqDTO.setIdList(companyIds);
                List<OrganizationRespDTO> dtoList = apiSystemService.queryOrganizationList(reqDTO);

                if (CollectionUtils.isNotEmpty(dtoList)) {
                    Map<Long, String> companyMap = dtoList.stream()
                            .collect(Collectors.toMap(OrganizationRespDTO::getId, OrganizationRespDTO::getCompanyName));

                    // 为结果设置企业名称
                    mappedResults.forEach(item ->
                            item.setCompanyName(companyMap.getOrDefault(item.getCompanyId(), null))
                    );
                }
            }
        }

        // 创建并返回 IPage 对象
        IPage<SmsStatisticsVO> iPage = new Page<>(statisticsQuery.getPageNo(), statisticsQuery.getPageSize());
        iPage.setRecords(mappedResults);
        iPage.setTotal(totalCount);

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
            criteria.and(SmsSendRecordEntity.DEPT_PATH).regex(Pattern.compile(departmentDto.getPath() + ".*$", Pattern.CASE_INSENSITIVE));
        }
        // 条件-时间查询
        if (Objects.nonNull(statisticsQuery.getBeginDate()) || Objects.nonNull(statisticsQuery.getEndDate())) {
            Criteria timeCriteria = Criteria.where(SmsSendRecordEntity.CREATE_TIME);
            if (Objects.nonNull(statisticsQuery.getBeginDate())) {
                timeCriteria.gte(statisticsQuery.getBeginDate());
            }
            if (Objects.nonNull(statisticsQuery.getEndDate())) {
                timeCriteria.lte(statisticsQuery.getEndDate());
            }
            criteria.andOperator(timeCriteria);
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
