package cn.cuiot.dmp.pay.service.service.service;

import cn.cuiot.dmp.common.utils.AssertUtil;
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
import org.springframework.stereotype.Service;

import java.util.Objects;


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
    private AbstractStrategyChoose choose;


    /**
     * 初始化参数
     *
     * @param channelSetting
     */
    public void initPaySetting(SysPayChannelSetting channelSetting) {
        if (StringUtils.isEmpty(channelSetting.getPayMchId())) {
            log.info("支付号为空，不进行初始化------");
        }
        IPayBaseInterface payBaseInterface =
                (IPayBaseInterface) choose.choose(PayChannelEnum.getPayMark(channelSetting.getPayChannel(),
                        channelSetting.getMchType()));
        payBaseInterface.initSetting(channelSetting);
    }


    /**
     * 后台管理调用-返回系统设置的支付参数
     *
     * @return
     */
    public SysPayChannelSettingDetailVo queryAdminForDetail() {
        //暂时只有微信普通商户，这里先直接取微信普通商户
        LambdaQueryWrapper<SysPayChannelSetting> queryWrapper = new LambdaQueryWrapper<SysPayChannelSetting>()
                .eq(SysPayChannelSetting::getPayChannel, PayChannelEnum.WECHAT_NORMAL.getPayChannel())
                .eq(SysPayChannelSetting::getMchType, PayChannelEnum.WECHAT_NORMAL.getMchType());
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
                .eq(SysPayChannelSetting::getMchType, settingDto.getMchType());
        SysPayChannelSetting channelSetting = this.getOne(queryWrapper);
        AssertUtil.isFalse(Objects.isNull(channelSetting),"基础支付配置不存在，请通知技术人员进行初始化");
        channelSetting.update(settingDto);
        //更新数据库
        this.updateById(channelSetting);
        //注册支付渠道，如果注册失败，说明配置有误
        initPaySetting(channelSetting);
    }
}


