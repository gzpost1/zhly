package cn.cuiot.dmp.exception;

import java.time.LocalDateTime;
import lombok.ToString;

/**
 * @Author 犬豪
 * @Date 2023/9/20 11:30
 * @Version V1.0
 */
@ToString
public class InvalidLocalDateTimeRange extends DomainException {
    public InvalidLocalDateTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(String.format("开始日期时间：%s ，不能晚于结束日期时间：%s", startDateTime, endDateTime));
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * 开始时间
     */
    private final LocalDateTime startDateTime;
    /**
     * 结束时间
     */
    private final LocalDateTime endDateTime;
}
