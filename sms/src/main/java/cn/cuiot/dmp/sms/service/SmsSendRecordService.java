package cn.cuiot.dmp.sms.service;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.sms.entity.SmsSendRecordEntity;
import cn.cuiot.dmp.sms.query.SmsPushDataQuery;
import cn.cuiot.dmp.sms.query.SmsSendRecordPageQuery;
import cn.cuiot.dmp.sms.vo.SmsSendRecordVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
}
