package cn.cuiot.dmp.sms.service;

import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.sms.contant.SmsRedisKeyConstant;
import cn.cuiot.dmp.sms.entity.SmsConfigEntity;
import cn.cuiot.dmp.sms.mapper.SmsConfigMapper;
import cn.cuiot.dmp.sms.query.SmsConfigDto;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 短信配置 业务层
 *
 * @Author: zc
 * @Date: 2024-09-24
 */
@Service
public class SmsConfigService extends ServiceImpl<SmsConfigMapper, SmsConfigEntity> {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 保存配置
     *
     * @Param dto 参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(SmsConfigDto dto) {
        remove(new LambdaQueryWrapper<>());

        SmsConfigEntity config = new SmsConfigEntity();
        BeanUtils.copyProperties(dto, config);
        save(config);

        redisUtil.set(SmsRedisKeyConstant.CONFIG, JsonUtil.writeValueAsString(config));
    }

    /**
     * 从缓存获取配置信息
     *
     * @return SmsConfigEntity
     */
    public SmsConfigEntity queryRedisData() {
        String jsonStr = redisUtil.get(SmsRedisKeyConstant.CONFIG);
        if (StringUtils.isNotBlank(jsonStr)) {
            return JsonUtil.readValue(jsonStr, SmsConfigEntity.class);
        }
        SmsConfigEntity smsConfig = getOne(new LambdaQueryWrapper<SmsConfigEntity>().last(" LIMIT 1 "));
        if (Objects.nonNull(smsConfig)) {
            redisUtil.set(SmsRedisKeyConstant.CONFIG, JsonUtil.writeValueAsString(smsConfig));
            return smsConfig;
        }
        return null;
    }
}
