package cn.cuiot.dmp.pay.service.service.service;

import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.pay.service.service.dto.PayChannelSettingDto;
import cn.cuiot.dmp.pay.service.service.entity.SysPayChannelSetting;
import cn.cuiot.dmp.pay.service.service.enums.PayChannelEnum;
import cn.cuiot.dmp.pay.service.service.mapper.SysPayChannelSettingMapper;
import cn.cuiot.dmp.pay.service.service.vo.SysPayChannelSettingDetailVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static cn.cuiot.dmp.pay.service.service.enums.PayConstant.NORMAL_WECHAT_PAY_KEY;


/**
 * 支付渠道配置
 *
 * @author huq
 * @ClassName SysPayChannelSettingService
 * @Date 2024/1/17 14:45
 **/
@Slf4j
@Service
public class SysPayChannelSettingService extends ServiceImpl<SysPayChannelSettingMapper, SysPayChannelSetting> {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CertificatesVerifierCacheService verifierCacheService;
    /**
     * 初始化参数
     *
     * @param channelSetting
     */
    public void initPaySetting(SysPayChannelSetting channelSetting) {
        if (StringUtils.isEmpty(channelSetting.getPayMchId())) {
            log.info("支付号为空，不进行初始化------");
        }
        verifierCacheService.refresh(channelSetting);
    }
    /**
     * 后台管理调用-返回系统设置的支付参数
     *
     * @return
     */
    public SysPayChannelSettingDetailVo queryAdminForDetail(Long orgId) {
        //暂时只有微信普通商户，这里先直接取微信普通商户
        LambdaQueryWrapper<SysPayChannelSetting> queryWrapper = new LambdaQueryWrapper<SysPayChannelSetting>()
                .eq(SysPayChannelSetting::getPayChannel, PayChannelEnum.WECHAT_NORMAL.getPayChannel())
                .eq(SysPayChannelSetting::getMchType, PayChannelEnum.WECHAT_NORMAL.getMchType())
                .eq(SysPayChannelSetting::getOrgId, orgId);
        SysPayChannelSetting channelSetting = this.getOne(queryWrapper);
        return SysPayChannelSettingDetailVo.toBuilder(Objects.isNull(channelSetting) ? null : channelSetting, PayChannelEnum.WECHAT_NORMAL.getPayChannel());
    }



    /**
     * 更新支付渠道参数
     *
     * @param settingDto
     */
    public void updatePaySetting(PayChannelSettingDto settingDto) {
        settingDto.valid();
        //获得该记录
        LambdaQueryWrapper<SysPayChannelSetting> queryWrapper = new LambdaQueryWrapper<SysPayChannelSetting>()
                .eq(SysPayChannelSetting::getPayChannel, settingDto.getPayChannel())
                .eq(SysPayChannelSetting::getMchType, settingDto.getMchType())
                .eq(SysPayChannelSetting::getOrgId, LoginInfoHolder.getCurrentOrgId());
        SysPayChannelSetting channelSetting = Optional.ofNullable(this.getOne(queryWrapper)).orElse(new SysPayChannelSetting());
        channelSetting.update(settingDto);
        //更新数据库
        this.saveOrUpdate(channelSetting);
        initPaySetting(channelSetting);
        redisTemplate.delete(NORMAL_WECHAT_PAY_KEY + "::" + channelSetting.getOrgId());
    }

    /**
     * 根据企业id获取支付渠道信息-只用于微信支付-普通商户模式
     *
     * @param orgId 企业id
     * @return 商户支付渠道信息
     */
    public SysPayChannelSetting getPaySettingByOrgId(Long orgId) {
        String orgPayKey = NORMAL_WECHAT_PAY_KEY + "::" + orgId;
        // 从缓存中获取商户渠道信息
        Object object = redisTemplate.opsForValue().get(orgPayKey);
        if (object == null) {
            LambdaQueryWrapper<SysPayChannelSetting> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysPayChannelSetting::getOrgId,orgId);
            SysPayChannelSetting payChannelSetting = this.getOne(queryWrapper);
            AssertUtil.isFalse(Objects.isNull(payChannelSetting),"企业支付渠道信息不存在");
            redisTemplate.opsForValue().set(orgPayKey, JsonUtil.writeValueAsString(payChannelSetting), 24, TimeUnit.HOURS);
            return payChannelSetting;
        } else {
            return JsonUtil.readValue(object.toString(), SysPayChannelSetting.class);
        }
    }
}


