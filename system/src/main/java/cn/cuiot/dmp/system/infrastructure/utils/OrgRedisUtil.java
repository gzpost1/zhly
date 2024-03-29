package cn.cuiot.dmp.system.infrastructure.utils;

import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.system.application.service.DbExecution;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: wuyongchong
 * @date: 2024/3/29 17:59
 */
@Component
public class OrgRedisUtil {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedissonClient redissonClient;

    public void doubleDeleteForDbOperation(DbExecution dbExecution, String orgId) {
        // 未加锁的删除会导致redis数据错误，所以去掉了这里的删除，必须要加锁后删除，取消双删操作。
        dbExecution.execute();
        RLock disLock = redissonClient.getLock("LOCK_"
                + CacheConst.ROBOT_ORGANIZATION_REDIS_KEY + "_" + orgId);
        boolean isLock;
        try {
            //尝试获取分布式锁
            isLock = disLock.tryLock(1000, 1000, TimeUnit.MILLISECONDS);
            if (isLock) {
                redisUtil.del(CacheConst.ROBOT_ORGANIZATION_REDIS_KEY + "_" + orgId);
                System.out.println("doubleDeleteForDbOperation second!");
            }
        } catch (Exception e) {
        } finally {
            // 最后解锁
            disLock.forceUnlock();
        }
    }

}
