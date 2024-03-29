package cn.cuiot.dmp.domain.types;

import lombok.Data;
import lombok.NonNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @Description 密码
 * @Author 犬豪
 * @Date 2023/8/28 11:16
 * @Version V1.0
 */
@Data
public class Password {

    // 盐的长度，这里设置为16字节
    private static final int SALT_LENGTH = 16;

    // SecureRandom用于生成安全的随机数
    private SecureRandom secureRandom;

    public Password(@NonNull String value) {
        secureRandom = new SecureRandom();
        this.hashEncryptValue = hashPassword(value);
    }
    public Password(){
        secureRandom = new SecureRandom();
    }
    private String hashEncryptValue;

    public String getHashEncryptValue(){
        return hashEncryptValue;
    }

    // 生成随机的盐
    private byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return salt;
    }

    // 哈希密码并附带盐一起存储
    public String hashPassword(String password) {
        try {
            // 生成随机的盐
            byte[] salt = generateSalt();
            // 使用盐对密码进行哈希处理
            byte[] hash = hash(password.getBytes(StandardCharsets.UTF_8), salt);

            // 将盐和哈希值合并存储
            byte[] saltedHash = new byte[salt.length + hash.length];
            System.arraycopy(salt, 0, saltedHash, 0, salt.length);
            System.arraycopy(hash, 0, saltedHash, salt.length, hash.length);

            // 使用Base64对合并后的数据进行编码，便于存储和传输
            return Base64.getEncoder().encodeToString(saltedHash);
        }catch (Exception e){
            return "";
        }
    }

    // 使用SHA-256算法和盐对输入数据进行哈希处理
    private byte[] hash(byte[] input, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        return md.digest(input);
    }

    // 验证密码是否正确
    public boolean verifyPassword(String passwordToVerify) {
        try {
            // 对存储的Base64编码的盐和哈希值进行解码
            byte[] saltedHash = Base64.getDecoder().decode(hashEncryptValue);

            // 从解码后的数据中提取盐
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(saltedHash, 0, salt, 0, salt.length);

            // 从解码后的数据中提取哈希值
            byte[] hash = new byte[saltedHash.length - salt.length];
            System.arraycopy(saltedHash, salt.length, hash, 0, hash.length);

            // 使用提取的盐对用户输入的密码进行哈希处理
            byte[] computedHash = hash(passwordToVerify.getBytes(StandardCharsets.UTF_8), salt);

            // 比较计算出的哈希值与存储的哈希值是否一致
            return MessageDigest.isEqual(computedHash, hash);
        }catch (Exception e) {
            return false;
        }

    }

    public boolean verifyPassword(String hashValue,String passwordToVerify) {
        try {
            // 对存储的Base64编码的盐和哈希值进行解码
            byte[] saltedHash = Base64.getDecoder().decode(hashValue);

            // 从解码后的数据中提取盐
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(saltedHash, 0, salt, 0, salt.length);

            // 从解码后的数据中提取哈希值
            byte[] hash = new byte[saltedHash.length - salt.length];
            System.arraycopy(saltedHash, salt.length, hash, 0, hash.length);

            // 使用提取的盐对用户输入的密码进行哈希处理
            byte[] computedHash = hash(passwordToVerify.getBytes(StandardCharsets.UTF_8), salt);

            // 比较计算出的哈希值与存储的哈希值是否一致
            return MessageDigest.isEqual(computedHash, hash);
        }catch (Exception e) {
            return false;
        }

    }
}
