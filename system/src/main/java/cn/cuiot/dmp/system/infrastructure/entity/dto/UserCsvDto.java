package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description 修改密码成功后，下载csv数据
 * @Author cds
 * @Date 2020/9/10
 */
@Data
public class UserCsvDto {

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 密码
     */
    private String password;



    public UserCsvDto() {
    }

    public UserCsvDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserCsvDto(String username, String phoneNumber, String password) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
