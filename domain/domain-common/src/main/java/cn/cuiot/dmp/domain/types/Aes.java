package cn.cuiot.dmp.domain.types;

import cn.cuiot.dmp.exception.InvalidAesContent;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author jiangze
 * @classname Aes
 * @description Aes值对象
 * @date 2023-11-08
 */
@Data
@NoArgsConstructor
public class Aes {

    public Aes(@NonNull String secretKey, @NonNull String iv) {
        this.secretKey = secretKey;
        this.iv = iv;
    }

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 向量
     */
    private String iv;

    /**
     * 加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * 密钥生成器算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * AES解密
     *
     * @param content 加密内容
     * @return
     */
    public String getDecodeValue(String content) {
        try {
            //使用加密秘钥
            SecretKeySpec key = new SecretKeySpec(getSecretKey().getBytes(StandardCharsets.UTF_8), KEY_ALGORITHM);
            // 6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY，第三个参数为偏移量
            AlgorithmParameterSpec iv = new IvParameterSpec(getIv().getBytes());
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            // 8.将加密并编码后的内容解码成字节数组
            byte[] byteContent = Base64.getDecoder().decode(content);
            /*
             * 解密
             */
            byte[] byteDecode = cipher.doFinal(byteContent);
            return new String(byteDecode, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
            InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new InvalidAesContent(content);
        }
    }

}
