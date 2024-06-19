package cn.cuiot.dmp.content.service.impl;//	模板

import cn.cuiot.dmp.content.constant.ContentConstants;
import cn.cuiot.dmp.content.dal.entity.ContentAudit;
import cn.cuiot.dmp.content.dal.mapper.ContentAuditMapper;
import cn.cuiot.dmp.content.param.dto.AuditResultDto;
import cn.cuiot.dmp.content.param.req.AuditReqVo;
import cn.cuiot.dmp.content.service.AuditResultDealService;
import cn.cuiot.dmp.content.service.ContentAuditService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/31 15:24
 */
@Service
public class ContentAuditServiceImpl extends ServiceImpl<ContentAuditMapper, ContentAudit> implements ContentAuditService {

    @Autowired
    private Map<String, AuditResultDealService> auditResultDealServiceHashMap;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean audit(AuditReqVo auditReqVo) {
        String dataService = ContentConstants.DataType.getDataService(auditReqVo.getDataType());
        if (dataService != null) {
            Boolean result = auditResultDealServiceHashMap.get(dataService).dealAuditResult(new AuditResultDto().setId(auditReqVo.getDataId()).setAuditStatus(auditReqVo.getAuditStatus()));
            if (result) {
                ContentAudit contentAudit = new ContentAudit();
                contentAudit.setDataId(auditReqVo.getDataId());
                contentAudit.setAuditStatus(auditReqVo.getAuditStatus());
                contentAudit.setAuditOpinion(auditReqVo.getAuditOpinion());
                contentAudit.setAuditUser(LoginInfoHolder.getCurrentUserId());
                contentAudit.setAuditUserName(LoginInfoHolder.getCurrentName());
                contentAudit.setAuditTime(new Date());
                this.baseMapper.insert(contentAudit);
            }
        }
        return null;
    }

    @Override
    public ContentAudit getLastAuditResult(Long id) {
        LambdaQueryWrapper<ContentAudit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContentAudit::getDataId, id);
        queryWrapper.orderByDesc(ContentAudit::getAuditTime);
        queryWrapper.last("limit 1");
        return this.baseMapper.selectOne(queryWrapper);
    }
}
