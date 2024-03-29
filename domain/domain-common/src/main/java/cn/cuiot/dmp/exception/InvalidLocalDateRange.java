package cn.cuiot.dmp.exception;

import java.time.LocalDate;
import lombok.ToString;

/**
 * @Author 犬豪
 * @Date 2023/9/20 11:30
 * @Version V1.0
 */
@ToString
public class InvalidLocalDateRange extends DomainException {
    public InvalidLocalDateRange(LocalDate startDate, LocalDate endDate) {
        super(String.format("开始日期：%s ，不能晚于结束日期：%s", startDate, endDate));
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * 开始时间
     */
    private final LocalDate startDate;
    /**
     * 结束时间
     */
    private final LocalDate endDate;
}
