package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.application.param.dto.SysLogQuery;
import cn.cuiot.dmp.system.infrastructure.entity.OperateLogEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * 系统日志
 * @author: wuyongchong
 * @date: 2024/6/18 10:21
 */
@Service
public class SysLogService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public IPage<OperateLogEntity> queryForPage(SysLogQuery param) {
        Query query = new Query();
        if(StringUtils.isNotBlank(param.getOperationSource())){
            query.addCriteria(Criteria.where("operationSource").is(param.getOperationSource()));
        }
        List<OperateLogEntity> list = mongoTemplate
                .find(query, OperateLogEntity.class);

        return null;
    }

}
