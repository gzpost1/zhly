package cn.cuiot.dmp.domain;

import cn.cuiot.dmp.domain.types.LocalTimeRange;
import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author 犬豪
 * @Date 2023/9/20 11:33
 * @Version V1.0
 */
public class LocalTimeRangeTest {
    @Test
    public void testConstruct() {
        try {
            LocalTime startTime = LocalTime.parse("10:00:00");
            LocalTime endTime = LocalTime.parse("9:00:00");
            LocalTimeRange localTimeRange = new LocalTimeRange(startTime, endTime);
            Assert.assertTrue(false);
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        try {
            LocalTimeRange localTimeRange = new LocalTimeRange("10:00:00-10:00:00");
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.assertFalse(false);
        }
    }

    @Test
    public void testContain() {
        LocalTime startTime = LocalTime.parse("10:00:00");
        LocalTime endTime = LocalTime.parse("12:00:00");
        LocalTimeRange localTimeRange1 = new LocalTimeRange(startTime, endTime);

        startTime = LocalTime.parse("10:00:00");
        endTime = LocalTime.parse("12:00:00");
        LocalTimeRange localTimeRange2 = new LocalTimeRange(startTime, endTime);

        startTime = LocalTime.parse("09:30:00");
        endTime = LocalTime.parse("10:00:00");
        LocalTimeRange localTimeRange3 = new LocalTimeRange(startTime, endTime);

        startTime = LocalTime.parse("10:30:00");
        endTime = LocalTime.parse("11:00:00");
        LocalTimeRange localTimeRange4 = new LocalTimeRange(startTime, endTime);


        Assert.assertTrue(localTimeRange1.contain(localTimeRange2));
        Assert.assertFalse(localTimeRange1.contain(localTimeRange3));
        Assert.assertTrue(localTimeRange1.contain(localTimeRange4));

    }
}
