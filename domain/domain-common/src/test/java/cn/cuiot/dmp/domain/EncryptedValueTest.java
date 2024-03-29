package cn.cuiot.dmp.domain;

import cn.cuiot.dmp.domain.types.EncryptedValue;
import org.junit.Test;

/**
 * @Author 犬豪
 * @Date 2023/9/6 13:57
 * @Version V1.0
 */
public class EncryptedValueTest {
    @Test
    public void testEncryptedValue() {
        String value = "1EBDDB43283DAE95070DBA53F655348A25131126C885F663AB943A3EFD2603EF";
        EncryptedValue encryptedValue = new EncryptedValue(value);
        System.out.println(encryptedValue);
        System.out.println(encryptedValue.getValue());
        System.out.println(encryptedValue.decrypt());
    }
}
