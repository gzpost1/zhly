package cn.cuiot.dmp.sms.service;

import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.sms.contant.SmsRedisKeyConstant;
import cn.cuiot.dmp.sms.entity.SmsSignEntity;
import cn.cuiot.dmp.sms.mapper.SmsSignMapper;
import cn.cuiot.dmp.sms.query.SmsSendSignSetQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cuiot.dmp.sms.entity.SmsSignRelationEntity;
import cn.cuiot.dmp.sms.mapper.SmsSignRelationMapper;

import java.util.List;
import java.util.Objects;

/**
 * 短信-发送签名设置 业务层
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
@Service
public class SmsSignRelationService extends ServiceImpl<SmsSignRelationMapper, SmsSignRelationEntity> {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SmsSignMapper smsSignMapper;

    /**
     * 根据id查询汇总
     *
     * @return Long
     * @Param companyId 企业id
     */
    public Long countBySignId(Long signId) {
        return count(new LambdaQueryWrapper<SmsSignRelationEntity>()
                .eq(SmsSignRelationEntity::getSignId, signId));
    }

    /**
     * 发送签名设置
     *
     * @Param query 参数
     */
    public void sendSignSet(SmsSendSignSetQuery query) {
        Long companyId = getCompanyId();
        List<SmsSignRelationEntity> list = list(new LambdaQueryWrapper<SmsSignRelationEntity>()
                .eq(SmsSignRelationEntity::getCompanyId, companyId));

        SmsSignRelationEntity signRelation = CollectionUtils.isNotEmpty(list) ? list.get(0) : new SmsSignRelationEntity();
        BeanUtils.copyProperties(query, signRelation);
        signRelation.setCompanyId(companyId);
        saveOrUpdate(signRelation);

        // 修改模版内容redis缓存
        SmsSignEntity sign = smsSignMapper.selectById(query.getSignId());
        if (Objects.nonNull(sign)) {
            redisUtil.set(SmsRedisKeyConstant.SIGN_COMPANY + companyId, JsonUtil.writeValueAsString(sign));
        }
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
