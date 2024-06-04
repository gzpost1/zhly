package cn.cuiot.dmp.base.infrastructure.stream.messaging;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息包装类
 *
 * @author: wuyongchong
 * @date: 2024/6/4 15:28
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SimpleMsg<T> implements Serializable {

    /**
     * 操作标签
     */
    private String operateTag;

    /**
     * 延迟时间级别
     * <pre>
     * Apache RocketMQ 一共支持18个等级的延迟投递，具体时间如下：
     * 投递等级（delay level）	延迟时间	投递等级（delay level）	延迟时间
     * 1	1s	10	6min
     * 2	5s	11	7min
     * 3	10s	12	8min
     * 4	30s	13	9min
     * 5	1min	14	10min
     * 6	2min	15	20min
     * 7	3min	16	30min
     * 8	4min	17	1h
     * 9	5min	18	2h
     * </pre>
     */
    private Integer delayTimeLevel;

    /**
     * 数据
     */
    private T data;
}
