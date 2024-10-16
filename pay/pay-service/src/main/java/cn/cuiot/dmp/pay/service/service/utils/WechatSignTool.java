package cn.cuiot.dmp.pay.service.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Random;

/**
 * @author huq
 * @ClassName WechatSignTool
 * @Date 2021/7/19 11:45
 **/
@Slf4j
public class WechatSignTool {

    private static final int TAG_LENGTH_BIT = 128;

    /**
     * 签名关键字
     */
    private static final String SIGN_KEY_VALUE = "sign";

    public enum SIGN_TYPE {
        HMAC_SHA256,
        MD5,
        SHA1
    }

    /**
     * 随机数生成
     */
    static public String getNoceStr(int length) {
        //1.  定义一个字符串（A-Z，a-z，0-9）即62个数字字母；
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz";
        //2.  由Random生成随机数
        Random random = new Random();

        StringBuffer sb = new StringBuffer();
        //3.  长度为几就循环几次
        for (int i = 0; i < length; ++i) {
            //从62个的数字或字母中选择
            int number = random.nextInt(str.length());
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

    /**
     * 功能描述: 微信V3版构建消息
     *
     * @param: [appId, timestamp, nonceStr, body]
     * @return: java.lang.String
     * @author: huq
     * @date: 2021/6/17 16:43
     **/
    public static String buildMessage(String appId, String timestamp, String nonceStr, String body) {
        return appId + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + body + "\n";
    }

    /**
     * 功能描述: 微信V3版签名
     *
     * @param: [message, privateKey]
     * @return: java.lang.String
     * @author: huq
     * @date: 2021/6/17 16:44
     **/
    public static String sign(byte[] message, PrivateKey privateKey) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            sign.update(message);

            return Base64.getEncoder().encodeToString(sign.sign());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持SHA256withRSA", e);
        } catch (SignatureException e) {
            throw new RuntimeException("签名计算失败", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("无效的私钥", e);
        }

    }





    /**
     * 微信支付-敏感信息加密
     */
    public static String rsaEncryptOAEP(String message, X509Certificate certificate)
            throws IllegalBlockSizeException, IOException {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());

            byte[] data = message.getBytes("utf-8");
            byte[] cipherdata = cipher.doFinal(data);
            return Base64.getEncoder().encodeToString(cipherdata);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("无效的证书", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalBlockSizeException("加密原串的长度不能超过214字节");
        }
    }

    /**
     * aes解密
     *
     * @param aesKey
     * @param associatedData
     * @param nonce
     * @param ciphertext
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static String decryptAes256ToString(byte[] aesKey, byte[] associatedData, byte[] nonce, String ciphertext)
            throws GeneralSecurityException, IOException {
        Security.addProvider(new BouncyCastleProvider());
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce);

            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData);

            return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
