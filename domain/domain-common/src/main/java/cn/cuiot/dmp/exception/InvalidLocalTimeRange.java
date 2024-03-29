package cn.cuiot.dmp.exception;

import java.time.LocalTime;
import lombok.ToString;

/**
 * @Author 犬豪
 * @Date 2023/9/20 11:30
 * @Version V1.0
 */
@ToString
public class InvalidLocalTimeRange extends DomainException {
    public InvalidLocalTimeRange(LocalTime startTime, LocalTime endTime) {
        super(String.format("开始时间：%s ，不能晚于结束时间：%s", startTime, endTime));
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * 开始时间
     */
    private final LocalTime startTime;
    /**
     * 结束时间
     */
    private final LocalTime endTime;
}
