package cn.cuiot.dmp.pay.task;

import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.pay.service.service.entity.PayOrderEntity;
import cn.cuiot.dmp.pay.service.service.enums.OrderStatusEnum;
import cn.cuiot.dmp.pay.service.service.service.OrderPayAtHandler;
import cn.cuiot.dmp.pay.service.service.service.PayOrderService;
import cn.cuiot.dmp.pay.service.service.vo.OrderQuery;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 每天定时查待支付的订单状态
 *
 * @author huq
 */
@Component
@Slf4j
public class OrderPayStatusTrigger implements InitializingBean {
    /**
     * 每天2点
     */
    private final static String CRON_OF_REFUND_STATUS = "0 0 2 * * ? ";
    private final static String REDIS_LOCK_KEY = "PAY::LOCK::KEY";
    private final static long MAX_TIME = 60;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private PayOrderService payOrderService;
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private OrderPayAtHandler orderPayAtHandler;

    /**
     * 每天定时查待支付的订单状态
     */
    @XxlJob("payStatusJobHandler")
    public ReturnT payStatusTriggerHandler(String param) {
        log.info("---xxljob---待支付定时任务触发---");
        RLock lock = null;
        boolean res = false;
        try {
            lock = redissonClient.getLock("OrderPayStatusTrigger:payStatusTriggerHandler");
            res = lock.tryLock(10, -1, TimeUnit.SECONDS);
            if (!res) {
                log.error("OrderPayStatusTrigger-payStatusTriggerHandler,获取锁失败");
                return ReturnT.SUCCESS;
            }
            if (redisTemplate.opsForValue().get(REDIS_LOCK_KEY) != null) {
                return ReturnT.SUCCESS;
            }

            List<PayOrderEntity> orderEntities =
                    payOrderService.queryForList(OrderQuery.builder()
                            .status(OrderStatusEnum.TO_BE_PAY.getStatus())
                            .payEndDateTime(DateTimeUtil.localDateTimeToDate(LocalDateTime.now().plusHours(-2))).build());
            orderEntities.forEach(orderEntity -> {
                try {
                    orderPayAtHandler.orderPayStatusTriggerHandler(orderEntity);
                } catch (Exception ex) {
                    log.error("待支付订单对账失败，父订单号：{},异常：", orderEntity.getOrderId(), ex);
                }
            });
            redisTemplate.opsForValue().set(REDIS_LOCK_KEY, "true", MAX_TIME, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("=========每天定时查清分中的订单状态=OrderPayStatusTrigger==payStatusTriggerHandler报错，原因:{}",
                    e.getMessage());
        } finally {
            if (lock != null && res) {
                try {
                    lock.unlock();
                } catch (Exception e) {
                    log.error("=========每天定时查清分中的订单状态=OrderPayStatusTrigger==payStatusTriggerHandler执行释放lock报错，原因:{}",
                            e.getMessage());
                }
            }
        }
        return ReturnT.SUCCESS;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
