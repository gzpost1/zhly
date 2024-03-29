package cn.cuiot.dmp.domain.types;

import cn.cuiot.dmp.exception.InvalidLocalTimeRange;
import java.time.Duration;
import java.time.LocalTime;
import lombok.Data;
import lombok.NonNull;

/**
 * @Description 时、分、秒的时间范围
 * @Author 犬豪
 * @Date 2023/9/20 11:28
 * @Version V1.0
 */
@Data
public class LocalTimeRange {
    public LocalTimeRange(@NonNull String rangeTime) {
        String[] split = rangeTime.split("-");
        this.startTime = LocalTime.parse(split[0]);
        this.endTime = LocalTime.parse(split[1]);
        if (!startTime.isBefore(endTime)) {
            //不允许时间一样的
            throw new InvalidLocalTimeRange(startTime, endTime);
        }
    }

    public LocalTimeRange(@NonNull LocalTime startTime, @NonNull LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new InvalidLocalTimeRange(startTime, endTime);
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * 开始时间
     */
    private LocalTime startTime;
    /**
     * 结束时间
     */
    private LocalTime endTime;

    /**
     * 判断当前时间是否包含给定的时间(双闭区间)
     *
     * @param localTimeRange
     * @return
     */
    public boolean contain(@NonNull LocalTimeRange localTimeRange) {
        return !localTimeRange.getStartTime().isBefore(this.startTime) && !localTimeRange.getEndTime().isAfter(this.endTime);
    }

    public boolean contain(@NonNull LocalTime localTime) {
        return !(localTime.isBefore(this.startTime) || localTime.isAfter(this.endTime));
    }

    public String toStrFormat() {
        return startTime.toString() + "-" + endTime.toString();
    }

    /**
     * 判断时间是否有交集(双闭区间)
     */
    public boolean hasIntersection(@NonNull LocalTimeRange localTimeRange) {
        return !localTimeRange.getEndTime().isBefore(this.startTime) && !localTimeRange.getStartTime().isAfter(this.endTime);
    }

    /**
    * 获取开始时间到结束时间的分钟数
    */
    public Double getDiff(){
        return Duration.between(startTime, endTime).getSeconds()/60/60.0;
    }

}
