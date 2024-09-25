package cn.cuiot.dmp.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.shaded.com.google.common.base.Joiner;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author pengjian
 * @create 2024/9/3 10:52
 */
public class KtSignUtils {


    /**
     *
     * @param requestBody 参数对象
     * @param appSecret 秘钥
     * @return
     */
    public static String paramsSign(JSONObject requestBody, String appSecret) {
        TreeMap<String, String> params = new TreeMap<>();
        //过滤掉key，appId字段，空属性及Map或List等复杂对象
        requestBody.entrySet().stream().filter(
                        p -> !"key".equals(p.getKey())
                                && !"appId".equals(p.getKey())
                                && p.getValue() != null
                                && !(p.getValue() instanceof Map)
                                && !(p.getValue() instanceof Iterable))
                .forEach(p -> {
                    if (!p.getValue().equals("")) {
                        params.put(p.getKey(), p.getValue().toString());
                    }
                });
        //拼接appSecret
        String temp = Joiner.on("&").withKeyValueSeparator("=").join(params).concat("&").concat(appSecret);
        return md5(temp).toUpperCase();
    }

    /**
     * 对文本执行 md5 摘要加密, 此算法与 mysql,JavaScript生成的md5摘要进行过一致性对比.
     *
     * @param plainText
     * @return 返回值中的字母为小写
     */
    private static String md5(String plainText) {
        if (null == plainText) {
            plainText = "";
        }
        String mD5Str = null;
        try {
            // JDK 支持以下6种消息摘要算法，不区分大小写
            // md5,sha(sha-1),md2,sha-256,sha-384,sha-512
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] b = md.digest();
            int i;
            StringBuilder builder = new StringBuilder(32);
            for (byte value : b) {
                i = value;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    builder.append("0");
                }
                builder.append(Integer.toHexString(i));
            }
            mD5Str = builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return mD5Str;
    }

    public static void main(String[] args) {
        String str ="{\"amount\":100,\"orderNo\":\"闽C12345\",\"payTime\":\"2020-03-06 10:57:22\",\"freeDetail\":\"[{\\\"code\\\":\\\"\\\",\\\"money\\\":100,\\\"time\\\":0,\\\"type\\\":0}]\",\"paySource\":\"EED96C219E83450A\",\"outOrderNo\":\"T20200306124536001\",\"parkId\":\"1000001\",\"payableAmount\":200,\"reqId\":\"748584ae47104b0ab239732767ddc679\",\"payType\":1006,\"payMethod\":6,\"appId\":\"EED96C219E83450A\",\"freeTime\":0,\"paymentExt\":\"{\\\"deviceNo\\\":\\\"123456\\\"}\",\"freeMoney\":100,\"ts\":1583464576264}\n";

        JSONObject jsonObject = JSONObject.parseObject(str);

        String s = paramsSign(jsonObject, "85d15350778b11e9bbaa506b4b2f6421");
        System.out.println(s);
    }
}

