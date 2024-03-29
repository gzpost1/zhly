package cn.cuiot.dmp.domain;

import cn.cuiot.dmp.domain.types.EncryptedValue;
import cn.cuiot.dmp.domain.types.PhoneNumber;
import org.junit.Test;

/**
 * @Author 犬豪
 * @Date 2023/8/29 17:43
 * @Version V1.0
 */
public class PhoneNumberTest {

    @Test
    public void testPhoneNumber() {
        PhoneNumber phoneNumber = new PhoneNumber(new EncryptedValue("55ECFCBA5D8D7891C1EAF9D5955E2639EA54838942E50A60421BABFE545334E3"));
        System.out.println(phoneNumber.decrypt());
        System.out.println(phoneNumber);

    }

    @Test
    public void testEncrypetedValue() {
        System.out.println(new EncryptedValue("1EBDDB43283DAE95070DBA53F655348A25131126C885F663AB943A3EFD2603EF").decrypt());
    }
}
