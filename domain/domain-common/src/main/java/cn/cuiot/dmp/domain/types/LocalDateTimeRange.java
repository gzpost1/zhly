package cn.cuiot.dmp.domain.types;

import cn.cuiot.dmp.exception.InvalidLocalDateTimeRange;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NonNull;

/**
 * @Description 日期时间范围，年月日时分秒
 * @Author 犬豪
 * @Date 2023/9/20 14:13
 * @Version V1.0
 */
@Data
public class LocalDateTimeRange {
    public LocalDateTimeRange(@NonNull LocalDateTime startDateTime, @NonNull LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new InvalidLocalDateTimeRange(startDateTime, endDateTime);
        }
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * 开始日期时间
     */
    private LocalDateTime startDateTime;
    /**
     * 结束日期时间
     */
    private LocalDateTime endDateTime;

    /**
     * 判断当前日期时间是否包含给定的时间
     */
    public boolean contain(@NonNull LocalDateTimeRange localDateTimeRange) {
        return !localDateTimeRange.getStartDateTime().isBefore(this.startDateTime) && !localDateTimeRange.getEndDateTime().isAfter(this.endDateTime);
    }

    /**
     * 获取开始时间的星期数
     */
    public int getStartDayOfWeek() {
        return startDateTime.getDayOfWeek().getValue();
    }

    public int getEndDayOfWeek() {
        return endDateTime.getDayOfWeek().getValue();
    }

    /**
     * 获取时分秒的范围
     *
     * @return
     */
    public LocalTimeRange getLocalTimeRange() {
        return new LocalTimeRange(startDateTime.toLocalTime(), endDateTime.toLocalTime());
    }
}
