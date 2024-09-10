package cn.cuiot.dmp.externalapi.service.vendor.gw.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jcajce.provider.digest.SM3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description TODO
 * @Date 2024-02-21 14:43
 */
public class Sm3Utils {
    private static Log logger = LogFactory.getLog(SM3.class);
    private static char[] chars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final String ivHexStr = "7380166f 4914b2b9 172442d7 da8a0600 a96f30bc 163138aa e38dee4d b0fb0e4e";
    private static final BigInteger IV = new BigInteger("7380166f 4914b2b9 172442d7 da8a0600 a96f30bc 163138aa e38dee4d b0fb0e4e".replaceAll(" ", ""), 16);
    private static final Integer Tj15 = Integer.valueOf("79cc4519", 16);
    private static final Integer Tj63 = Integer.valueOf("7a879d8a", 16);
    private static final byte[] FirstPadding = new byte[]{-128};
    private static final byte[] ZeroPadding = new byte[]{0};
    private static final String[] hexDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


    private static int T(int j) {
        if (j >= 0 && j <= 15) {
            return Tj15;
        } else if (j >= 16 && j <= 63) {
            return Tj63;
        } else {
            throw new RuntimeException("data invalid");
        }
    }

    private static Integer FF(Integer x, Integer y, Integer z, int j) {
        if (j >= 0 && j <= 15) {
            return x ^ y ^ z;
        } else if (j >= 16 && j <= 63) {
            return x & y | x & z | y & z;
        } else {
            throw new RuntimeException("data invalid");
        }
    }

    private static Integer GG(Integer x, Integer y, Integer z, int j) {
        if (j >= 0 && j <= 15) {
            return x ^ y ^ z;
        } else if (j >= 16 && j <= 63) {
            return x & y | ~x & z;
        } else {
            throw new RuntimeException("data invalid");
        }
    }

    private static Integer P0(Integer x) {
        return x ^ Integer.rotateLeft(x, 9) ^ Integer.rotateLeft(x, 17);
    }

    private static Integer P1(Integer x) {
        return x ^ Integer.rotateLeft(x, 15) ^ Integer.rotateLeft(x, 23);
    }

    private static byte[] padding(byte[] source) throws IOException {
        if ((long)source.length >= 2305843009213693952L) {
            throw new RuntimeException("src data invalid.");
        } else {
            long l = (long)(source.length * 8);
            long k = 448L - (l + 1L) % 512L;
            if (k < 0L) {
                k += 512L;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("k = " + k);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(source);
            baos.write(FirstPadding);

            for(long i = k - 7L; i > 0L; i -= 8L) {
                baos.write(ZeroPadding);
            }

            baos.write(long2bytes(l));
            if (logger.isDebugEnabled()) {
                logger.debug("paded size = " + baos.size());
            }

            return baos.toByteArray();
        }
    }

    private static byte[] long2bytes(long l) {
        byte[] bytes = new byte[8];

        for(int i = 0; i < 8; ++i) {
            bytes[i] = (byte)((int)(l >>> (7 - i) * 8));
        }

        return bytes;
    }

    public static byte[] hash(byte[] source) throws IOException {
        byte[] m1 = padding(source);
        int n = m1.length / 64;
        if (logger.isDebugEnabled()) {
            logger.debug("n = " + n);
        }

        byte[] vi = IV.toByteArray();
        byte[] vi1 = null;

        for(int i = 0; i < n; ++i) {
            byte[] b = Arrays.copyOfRange(m1, i * 64, (i + 1) * 64);
            vi1 = CF(vi, b);
            vi = vi1;
        }

        return vi1;
    }

    private static byte[] CF(byte[] vi, byte[] bi) throws IOException {
        int a = toInteger(vi, 0);
        int b = toInteger(vi, 1);
        int c = toInteger(vi, 2);
        int d = toInteger(vi, 3);
        int e = toInteger(vi, 4);
        int f = toInteger(vi, 5);
        int g = toInteger(vi, 6);
        int h = toInteger(vi, 7);
        int[] w = new int[68];
        int[] w1 = new int[64];

        int ss1;
        for(ss1 = 0; ss1 < 16; ++ss1) {
            w[ss1] = toInteger(bi, ss1);
        }

        for(ss1 = 16; ss1 < 68; ++ss1) {
            w[ss1] = P1(w[ss1 - 16] ^ w[ss1 - 9] ^ Integer.rotateLeft(w[ss1 - 3], 15)) ^ Integer.rotateLeft(w[ss1 - 13], 7) ^ w[ss1 - 6];
        }

        for(ss1 = 0; ss1 < 64; ++ss1) {
            w1[ss1] = w[ss1] ^ w[ss1 + 4];
        }

        for(int j = 0; j < 64; ++j) {
            ss1 = Integer.rotateLeft(Integer.rotateLeft(a, 12) + e + Integer.rotateLeft(T(j), j), 7);
            int ss2 = ss1 ^ Integer.rotateLeft(a, 12);
            int tt1 = FF(a, b, c, j) + d + ss2 + w1[j];
            int tt2 = GG(e, f, g, j) + h + ss1 + w[j];
            d = c;
            c = Integer.rotateLeft(b, 9);
            b = a;
            a = tt1;
            h = g;
            g = Integer.rotateLeft(f, 19);
            f = e;
            e = P0(tt2);
        }

        byte[] v = toByteArray(a, b, c, d, e, f, g, h);

        for(int i = 0; i < v.length; ++i) {
            v[i] ^= vi[i];
        }

        return v;
    }

    private static int toInteger(byte[] source, int index) {
        StringBuilder valueStr = new StringBuilder("");

        for(int i = 0; i < 4; ++i) {
            valueStr.append(chars[(byte)((source[index * 4 + i] & 240) >> 4)]);
            valueStr.append(chars[(byte)(source[index * 4 + i] & 15)]);
        }

        return Long.valueOf(valueStr.toString(), 16).intValue();
    }

    private static byte[] toByteArray(int a, int b, int c, int d, int e, int f, int g, int h) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(32);
        baos.write(toByteArray(a));
        baos.write(toByteArray(b));
        baos.write(toByteArray(c));
        baos.write(toByteArray(d));
        baos.write(toByteArray(e));
        baos.write(toByteArray(f));
        baos.write(toByteArray(g));
        baos.write(toByteArray(h));
        return baos.toByteArray();
    }

    private static byte[] toByteArray(int i) {
        byte[] byteArray = new byte[]{(byte)(i >>> 24), (byte)((i & 16777215) >>> 16), (byte)((i & '\uffff') >>> 8), (byte)(i & 255)};
        return byteArray;
    }

    private static void printIntArray(int[] intArray, int lineSize) {
        for(int i = 0; i < intArray.length; ++i) {
            byte[] byteArray = toByteArray(intArray[i]);

            for(int j = 0; j < byteArray.length; ++j) {
                System.out.print(chars[(byteArray[j] & 255) >> 4]);
                System.out.print(chars[byteArray[j] & 15]);
            }

            System.out.print(" ");
            if (i % lineSize == lineSize - 1) {
                System.out.println(" ");
            }
        }

    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();

        for(int i = 0; i < b.length; ++i) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (b < 0) {
            n = b + 256;
        }

        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String SM3Encode(String source, String charsetname) throws IOException {
        String resultString = null;
        if (charsetname != null && !"".equals(charsetname)) {
            resultString = byteArrayToHexString(hash(source.getBytes(charsetname)));
        } else {
            resultString = byteArrayToHexString(hash(source.getBytes()));
        }

        return resultString;
    }

    public static Map<String, Object> makeToken(String appId, String appSecrect) throws IOException {
        Map<String, Object> result = new HashMap();
        String token = "";
        String timestamp = "2017-09-14 13:54:45 687";
        String transId = "20170914135445687536273";
        timestamp = getFormatedDateString(8.0F);
        transId = timestamp.replace("-", "").replace(" ", "").replace(":", "");
        transId = transId + (new Random()).nextInt(999999);
        StringBuilder sb = new StringBuilder();
        sb.append("app_id");
        sb.append(appId);
        sb.append("timestamp");
        sb.append(timestamp);
        sb.append("trans_id");
        sb.append(transId);
        sb.append(appSecrect);
        token = SM3Encode(sb.toString(), "UTF-8");
        result.put("token", token);
        result.put("timestamp", timestamp);
        result.put("trans_id", transId);
        return result;
    }

    public static String getFormatedDateString(float timeZoneOffset) {
        if (timeZoneOffset > 13.0F || timeZoneOffset < -12.0F) {
            timeZoneOffset = 0.0F;
        }

        int newTime = (int)(timeZoneOffset * 60.0F * 60.0F * 1000.0F);
        String[] ids = TimeZone.getAvailableIDs(newTime);
        Object timeZone;
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        sdf.setTimeZone((TimeZone)timeZone);
        return sdf.format(new Date());
    }

   /* public static void main(String[] args) throws IOException {
        byte[] source = new byte[]{97, 98, 99, 100, 97, 98, 99, 100, 97, 98, 99, 100, 97, 98, 99, 100, 97, 98, 99, 100, 97, 98, 99, 100, 97, 98, 99, 100, 97, 98, 99, 100, 97, 98, 99, 100, 97, 98, 99, 100, 97, 98, 99, 100, 97, 98, 99, 100, 97, 98, 99, 100, 97, 98, 99, 100, 97, 98, 99, 100, 97, 98, 99, 100};
        byte[] sm3HashValue = hash(source);
        System.out.println(SM3Encode(sm3HashValue.toString(), "utf-8"));
        String token = SM3Encode("app_id5E6339211Ftimestamp2017-09-14 13:54:45 687trans_id20170914135445687536273PG65cP4C4977I0z3durISe1c24547409", "utf-8");
        System.out.println(token);
        System.out.println("d1e36c3e8e1abdd704c74591ee3da7a124f072e11ee041968f3940cdc1499438".equals(token));
        System.out.println("20170914135445687536273".length());
        System.out.println("20170914155804273365621".length());
    }*/
}
