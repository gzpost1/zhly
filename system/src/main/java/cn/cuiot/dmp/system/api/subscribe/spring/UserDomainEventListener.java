package cn.cuiot.dmp.system.api.subscribe.spring;

import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.system.domain.entity.User;
import cn.cuiot.dmp.system.domain.event.ResetPasswordSuccessEvent;
import cn.cuiot.dmp.system.domain.event.UserUpdateEvent;
import cn.cuiot.dmp.system.domain.service.VerifyDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Author 犬豪
 * @Date 2023/8/31 11:53
 * @Version V1.0
 */
@Component
public class UserDomainEventListener {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private VerifyDomainService verifyDomainService;

    @EventListener
    public void userUpdateEvent(UserUpdateEvent userUpdateEvent) {
        User user = userUpdateEvent.getUser();
        // 用户信息变更删除用户缓存
        redisUtil.del(CacheConst.USER_CACHE_KEY_PREFIX + user.getId().getStrValue());
    }

    @EventListener
    public void resetPasswordSuccessEvent(ResetPasswordSuccessEvent resetPasswordSuccessEvent) {
        // 密码重置成功后，删除对应的验证Code
        verifyDomainService.deleteVerificationCode(resetPasswordSuccessEvent.getUserVerificationCode());
    }
}
