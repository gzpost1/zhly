package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.system.application.param.dto.SysLogQuery;
import cn.cuiot.dmp.system.infrastructure.entity.OperateLogEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * 系统日志
 *
 * @author: wuyongchong
 * @date: 2024/6/18 10:21
 */
@Service
public class SysLogService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public IPage<OperateLogEntity> queryForPage(SysLogQuery param) {
        Query query = new Query();
        if (StringUtils.isNotBlank(param.getOperationSource())) {
            query.addCriteria(Criteria.where("operationSource").is(param.getOperationSource()));
        }
        if (Objects.nonNull(param.getOrgId())) {
            query.addCriteria(Criteria.where("orgId").is(param.getOrgId().toString()));
        }
        if (StringUtils.isNotBlank(param.getServiceTypeName())) {
            //模糊匹配
            Pattern pattern = Pattern
                    .compile("^.*" + param.getServiceTypeName() + ".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("serviceTypeName").regex(pattern));
        }
        //时间查询
        if (Objects.nonNull(param.getOptStartTime())||Objects.nonNull(param.getOptEndTime())) {
            Criteria criteria = Criteria.where("requestTime");
            if(Objects.nonNull(param.getOptStartTime())){
                criteria.gte(DateTimeUtil.dateToString(param.getOptStartTime()));
            }
            if(Objects.nonNull(param.getOptEndTime())){
                criteria.lte(DateTimeUtil.dateToString(param.getOptEndTime()));
            }
            query.addCriteria(criteria);
        }

        //query.addCriteria(Criteria.where("serviceTypeName").not());
        query.addCriteria(Criteria.where("serviceTypeName").ne(null));
        query.addCriteria(Criteria.where("serviceTypeName").ne("null"));

        //计算总数
        long total = mongoTemplate.count(query, OperateLogEntity.class);

        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, "requestTime");
        query.with(sort);
        // 分页 (当前页-1)*每页大小，每页大小
        long pageNum = param.getPageNo();
        int pageSize = param.getPageSize().intValue();
        query.skip((pageNum - 1) * pageSize).limit(pageSize);

        List<OperateLogEntity> dataList = mongoTemplate
                .find(query, OperateLogEntity.class);

        Page<OperateLogEntity> page = new Page<>(pageNum, pageSize);
        page.setTotal(total);
        page.setRecords(dataList);

        return page;
    }

}
