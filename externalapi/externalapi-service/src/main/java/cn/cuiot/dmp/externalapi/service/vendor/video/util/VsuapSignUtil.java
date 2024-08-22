package cn.cuiot.dmp.externalapi.service.vendor.video.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Comparator;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 云智眼-签名工具类
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
public class VsuapSignUtil {

    public static String signByHmacSHA256(String secretKey, Map<String, Object> paramMap) {
        try {
            //将参数已key的方式进行排序，并拼接为字符串
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(Charsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            //将参数已key的方式进行排序，并拼接为字符串
            StringBuilder sb = new StringBuilder();
            StringJoiner joiner = new StringJoiner("&");
            paramMap.entrySet()
                    .stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .forEach(entry -> joiner.add(String.join("=", entry.getKey(), JSONObject.toJSONString(entry.getValue()))));
            String encryptStr = joiner.toString();

            //进行加密
            byte[] bytes = mac.doFinal(encryptStr.getBytes(Charsets.UTF_8));
            //清空
            sb.setLength(0);
            String stmp;
            //字节转换为16进制字符串
            for (int n = 0; bytes != null && n < bytes.length; n++) {
                stmp = Integer.toHexString(bytes[n] & 0XFF);
                if (stmp.length() == 1) {
                    sb.append('0');
                }
                sb.append(stmp);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("signByHmacSHA256 error", e);
        }
    }

}
