package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.domain.types.Password;
/**
 * @author: wuyongchong
 * @date: 2024/4/19 16:18
 */
public class PasswordTest {

    public static void main(String[] args) {
        Password password = new Password();
        System.out.println(password.hashPassword("Yjwl@20201688"));

        Password dbPassword = new Password();
        dbPassword.setHashEncryptValue("miEKBmp/ZeDPqcZn8mWLz8d7JlqEvH3yHGO5MyCXc7O+Fis/D7ZCvTZGpFy3ZW7J");
        System.out.println(dbPassword.verifyPassword("Yjwl@20201688"));
    }
}
