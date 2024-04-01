package cn.cuiot.dmp.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 日期时间转换工具
 * @author: wuyongchong
 * @date: 2024/4/1 19:49
 */
public final class DateTimeUtil {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String[][] WEEK_ARRAY = {{"MONDAY", "一"}, {"TUESDAY", "二"}, {"WEDNESDAY", "三"},
            {"THURSDAY", "四"}, {"FRIDAY", "五"}, {"SATURDAY", "六"}, {"SUNDAY", "日"}};

    public DateTimeUtil() {
    }

    public static String dateToString(Date date) {
        return DateFormatUtils.format(date, DEFAULT_DATETIME_FORMAT);
    }

    public static String dateToString(Date date, String format) {
        return DateFormatUtils.format(date, format);
    }

    public static Date stringToDate(String dateStr) {
        return stringToDate(dateStr, DEFAULT_DATETIME_FORMAT);
    }

    public static Date stringToDate(String dateStr, String format) {
        if (!StringUtils.isEmpty(dateStr) && !StringUtils.isEmpty(format)) {
            try {
                return new SimpleDateFormat(format).parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static LocalDate getMonthStartDay(LocalDate date) {
        return Objects.isNull(date) ? null : date.withDayOfMonth(1);
    }

    public static LocalDate getCurMonthStartDay() {
        return getMonthStartDay(LocalDate.now());
    }

    public static LocalDate getDayBeforeDate(LocalDate date, int n) {
        return Objects.isNull(date) ? null : date.minusDays((long) n);
    }

    public static LocalDate getLatestSevenDay(LocalDate date) {
        return getDayBeforeDate(date, 6);
    }

    public static LocalDate getLatestSevenDayOfToday() {
        return getLatestSevenDay(LocalDate.now());
    }

    public static LocalDate stringToLocalDate(String dateStr, String format) {
        return !StringUtils.isEmpty(dateStr) && !StringUtils.isEmpty(format) ? LocalDate
                .parse(dateStr, DateTimeFormatter.ofPattern(format)) : null;
    }

    public static LocalDate stringToLocalDate(String dateStr) {
        return stringToLocalDate(dateStr, DEFAULT_DATE_FORMAT);
    }

    public static LocalDateTime stringToLocalDateTime(String dateStr, String format) {
        return !StringUtils.isEmpty(dateStr) && !StringUtils.isEmpty(format) ? LocalDateTime
                .parse(dateStr, DateTimeFormatter.ofPattern(format)) : null;
    }

    public static LocalDateTime stringToLocalDateTime(String dateStr) {
        return stringToLocalDateTime(dateStr, DEFAULT_DATETIME_FORMAT);
    }

    public static Long localDateToLong(LocalDate localDate) {
        return localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static Date localDateToDate(LocalDate localDate) {
        return new Date(localDateToLong(localDate));
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return new Date(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    public static LocalDate dateToLocalDate(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime
                .ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    public static String localDateTimeToString(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String localDateToString(LocalDate localDate, String pattern) {
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String strLocalDateToString(String date, String pattern, String fomatPattern) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
        return localDateToString(localDate, fomatPattern);
    }

    public static LocalDateTime localDateToLocalDateTime(LocalDate localDate) {
        long timestamp = localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
        LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8))
                .toLocalDateTime();
        return localDateTime;
    }

    public static String localDateToStrLocalDateTime(LocalDate localDate, String pattern) {
        long timestamp = localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
        LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8))
                .toLocalDateTime();
        return localDateTimeToString(localDateTime, pattern);
    }

    public static LocalDate secondsLongToLocalDate(Long seconds) {
        return dateToLocalDate(new Date(seconds * 1000));
    }

    /**
     * 获取两个日期间隔的所有日期
     */
    public static List<LocalDate> getBetweenDate(LocalDate beginDate, LocalDate endDate) {
        List<LocalDate> list = new LinkedList<>();

        long distance = ChronoUnit.DAYS.between(beginDate, endDate);
        System.out.println(distance);
        if (distance < 1) {
            list.add(beginDate);
        }
        Stream.iterate(beginDate, d -> {
            return d.plusDays(1);
        }).limit(distance + 1).forEach(f -> {
            list.add(f);
        });
        return list;
    }

    /**
     * 获取每天的开始时间 00:00:00:00
     */
    public static Date getStartTime(Date date) {
        Calendar dateStart = Calendar.getInstance();
        dateStart.setTime(date);
        dateStart.set(Calendar.HOUR_OF_DAY, 0);
        dateStart.set(Calendar.MINUTE, 0);
        dateStart.set(Calendar.SECOND, 0);
        return dateStart.getTime();
    }

    /**
     * 获取每天的开始时间 23:59:59:999
     */
    public static Date getEndTime(Date date) {
        Calendar dateEnd = Calendar.getInstance();
        dateEnd.setTime(date);
        dateEnd.set(Calendar.HOUR_OF_DAY, 23);
        dateEnd.set(Calendar.MINUTE, 59);
        dateEnd.set(Calendar.SECOND, 59);
        return dateEnd.getTime();
    }

    /**
     * 获取两个日期间隔的所有日期-包含end
     *
     * @param start
     * @param end
     * @return
     */
    public static List<LocalDate> getDateList(LocalDate start, LocalDate end) {
        List<LocalDate> result = new ArrayList<LocalDate>();
        if (start == null || end == null || start.isAfter(end)) {
            return result;
        }
        while (end.compareTo(start) >= 0) {
            result.add(start);
            start = start.plusDays(1);
        }
        return result;
    }

    /**
     * 按日期获取对应周几
     *
     * @param reservationDate
     * @return
     */
    public static String getDayOfTheWeek(LocalDate reservationDate) {
        String k = String.valueOf(reservationDate.getDayOfWeek());
        //获取行数
        for (int i = 0; i < WEEK_ARRAY.length; i++) {
            if (k.equals(WEEK_ARRAY[i][0])) {
                k = WEEK_ARRAY[i][1];
                break;
            }
        }
        return "周" + k;
    }


    public static Date getToday() {
        return localDateToDate(LocalDate.now());
    }

    public static Date yearAddNum(Date time, Integer num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(1, num);
        Date newTime = calendar.getTime();
        return newTime;
    }

    public static Date monthAddNum(Date time, Integer num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(2, num);
        Date newTime = calendar.getTime();
        return newTime;
    }

    public static Date dayAddNum(Date time, Integer num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.DATE, num);
        Date newTime = calendar.getTime();
        return newTime;
    }

    public static Date getMonthStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, 1);
        return calendar.getTime();
    }

    public static Date getMonthEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, calendar.getActualMaximum(5));
        return calendar.getTime();
    }

    public static Date getBeginWeekDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int dayofweek = cal.get(7);
        if (dayofweek == 1) {
            dayofweek += 7;
        }

        cal.add(5, 2 - dayofweek);
        return cal.getTime();
    }

    public static Date getEndWeekDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int dayofweek = cal.get(7);
        if (dayofweek == 1) {
            dayofweek += 7;
        }

        cal.add(5, 8 - dayofweek);
        return cal.getTime();
    }

    public static Date stampToDate(String s) {
        long lt = new Long(s);
        Date date = new Date(lt * 1000L);
        return date;
    }
}