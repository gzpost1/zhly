package cn.cuiot.dmp.base.infrastructure.utils;


import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Objects;


public class MathTool {
    /**
     * 计算取整后的金额
     *
     * @param price      价格 分
     * @param percentage 百分比
     * @return 计算后的金额，分
     */
    public static int percentCalculate(Integer price, BigDecimal percentage) {
        return percentCalculate(BigDecimal.valueOf(price), percentage);
    }

    /**
     * 计算取整后的金额
     *
     * @param price      价格 分
     * @param percentage 百分比
     * @return 计算后的金额，分
     */
    public static int percentCalculate(BigDecimal price, BigDecimal percentage) {
        BigDecimal point = percentage.divide(BigDecimal.valueOf(100), 5, BigDecimal.ROUND_HALF_UP);
        return point.multiply(price).intValue();
    }

    /**
     * 四舍五入
     *
     * @param price      价格 分
     * @param percentage 百分比
     * @return 计算后的金额，分
     */
    public static int rounding(Integer price, BigDecimal percentage) {
        BigDecimal realWeChatCharge = percentage.divide(BigDecimal.valueOf(100));
        return realWeChatCharge.multiply(BigDecimal.valueOf(price)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    public static int yuanToCent(BigDecimal price) {
        if (Objects.isNull(price)) {
            return 0;
        } else {
            return price.multiply(BigDecimal.valueOf(100L)).intValue();
        }
    }

    public static double centToYuan(Integer price) {
        if (Objects.isNull(price)) {
            return 0;
        } else {
            return BigDecimal.valueOf(price).divide(BigDecimal.valueOf(100)).doubleValue();
        }
    }


    public static <T> boolean checkCollectionEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static int checkIndexInArrays(byte[] arrays, Byte key) {
        if (key == null) {
            return -1;
        }
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i] == key) {
                return i;
            }
        }
        return -1;
    }

    public static int checkIndexInArrays(long[] arrays, Long key) {
        if (key == null) {
            return -1;
        }
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i] == key) {
                return i;
            }
        }
        return -1;
    }

    public static int checkIndexInArrays(String[] arrays, String key) {
        if (key == null) {
            return -1;
        }
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }

    public static String formatTime(String str) {
        if (str == null || str.length() <= 0) {
            return str;
        }
        if (str.matches("^\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return str.substring(0, str.lastIndexOf(":"));
        }
        return str;
    }

    public static String getMsg(String str, String defaultMsg) {
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(str)) {
            return str;
        }
        return defaultMsg;
    }

    public static String getTruncateStr(String str, int index) {
        if (str == null || str.length() <= index) {
            return str;
        }
        return str.substring(0, index);
    }

    /**
     * 获取系统临时路径
     */
    public static String getTempDir() {
        String temp = System.getProperty("java.io.tmpdir");
        if (temp.endsWith(File.separator)) {
            return temp;
        } else {
            return temp + File.separator;
        }
    }

    public static Double divideRate(Integer a, Integer b) {
        if (Objects.isNull(a) || a == 0) {
            return BigDecimal.ZERO.doubleValue();
        }
        if (Objects.isNull(b) || b == 0) {
            return BigDecimal.ZERO.doubleValue();
        }
        double v = (double) a / b;
        return new BigDecimal(v).setScale(2, RoundingMode.UP).doubleValue();
    }

    public static Double divideRatePercent(Integer a, Integer b) {
        if (Objects.isNull(a) || a == 0) {
            return BigDecimal.ZERO.doubleValue();
        }
        if (Objects.isNull(b) || b == 0) {
            return BigDecimal.ZERO.doubleValue();
        }
        double v = (double) a / b * 100;
        return new BigDecimal(v).setScale(2, RoundingMode.UP).doubleValue();
    }
}
