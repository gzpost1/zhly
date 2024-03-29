package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description 修改密码成功后，下载csv数据
 * @Author liuxue
 * @Date 2022/9/15
 */
@Data
@AllArgsConstructor
public class OrgCsvDto {

    /**
     * 账户名
     */
    private String userName;

    /**
     * 账户密码
     */
    private String password;

}
