package cn.cuiot.dmp.domain;

import cn.cuiot.dmp.domain.types.Email;
import cn.cuiot.dmp.domain.types.EncryptedValue;
import org.junit.Test;

/**
 * @Author 犬豪
 * @Date 2023/9/1 00:36
 * @Version V1.0
 */
public class EmailTest {
    @Test
    public void testEmail() {
        Email email = new Email("th_yangchn@163.com");
        System.out.println(email);

        Email email12 = new Email(
                new EncryptedValue("DB0A602053A685E2BE23356D6862514FDC5D321FB534526997879214B5F2FC487B9974578757E46F1D3B3C5286EE5A0D"));
        System.out.println(email12.decrypt());
    }
}
