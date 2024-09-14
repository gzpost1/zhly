package cn.cuiot.dmp.base.infrastructure.utils;

/**
 * @author pengjian
 * @create 2024/7/18 18:37
 */
public class MD5Util {

    public static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
