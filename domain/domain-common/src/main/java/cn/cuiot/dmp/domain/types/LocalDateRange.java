package cn.cuiot.dmp.domain.types;

import cn.cuiot.dmp.exception.InvalidLocalDateRange;
import java.time.LocalDate;
import lombok.Data;
import lombok.NonNull;

/**
 * @Description 日期时间范围，年月日
 * @Author 犬豪
 * @Date 2023/9/20 14:13
 * @Version V1.0
 */
@Data
public class LocalDateRange {
    public LocalDateRange(@NonNull LocalDate startDate, @NonNull LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidLocalDateRange(startDate, endDate);
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * 开始日期时间
     */
    private LocalDate startDate;
    /**
     * 结束日期时间
     */
    private LocalDate endDate;

    /**
     * 判断当前日期时间是否包含给定的时间
     */
    public boolean contain(@NonNull LocalDateRange localDateRange) {
        return !localDateRange.getStartDate().isBefore(this.startDate) && !localDateRange.getEndDate().isAfter(this.endDate);
    }

    /**
     * 获取开始时间的星期数
     */
    public int getStartDayOfWeek() {
        return startDate.getDayOfWeek().getValue();
    }

    public int getEndDayOfWeek() {
        return endDate.getDayOfWeek().getValue();
    }
}
