package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description 修改密码成功后，下载csv数据
 * @Author cds
 * @Date 2020/9/10
 */
@Data
@AllArgsConstructor
public class UserCsvDto {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
