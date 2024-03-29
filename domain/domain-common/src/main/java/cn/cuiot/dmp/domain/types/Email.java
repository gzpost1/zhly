package cn.cuiot.dmp.domain.types;

import cn.cuiot.dmp.exception.InvalidEmailFormat;
import cn.cuiot.dmp.util.Sm4;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

/**
 * encrypted
 *
 * @Description 邮箱
 * @Author 犬豪
 * @Date 2023/8/28 11:18
 * @Version V1.0
 */
@Data
@ToString(callSuper = true)
public class Email {
    /**
     * 邮件，符合RFC 5322规范，正则来自：http://emailregex.com/
     * What is the maximum length of a valid email address? https://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address/44317754
     * 注意email 要宽松一点。比如 jetz.chong@hutool.cn、jetz-chong@ hutool.cn、jetz_chong@hutool.cn、dazhi.duan@hutool.cn 宽松一点把，都算是正常的邮箱
     */
    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    public Email(@NonNull String value) {
        if (EMAIL_PATTERN.matcher(value).matches()) {
            this.encryptedValue = new EncryptedValue(Sm4.encryption(value));
        } else {
            throw new InvalidEmailFormat(value);
        }
    }

    public Email(@NonNull EncryptedValue encryptedValue) {
        String decryptValue = encryptedValue.decrypt();
        if (EMAIL_PATTERN.matcher(decryptValue).matches()) {
            this.encryptedValue = encryptedValue;
        } else {
            throw new InvalidEmailFormat(decryptValue);
        }
    }

    private EncryptedValue encryptedValue;

    public String getEncryptedValue() {
        return encryptedValue.getValue();
    }

    public String decrypt() {
        return encryptedValue.decrypt();
    }

}
