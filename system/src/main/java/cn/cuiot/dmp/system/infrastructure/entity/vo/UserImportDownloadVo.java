package cn.cuiot.dmp.system.infrastructure.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户导入下载Vo
 *
 * @author: wuyongchong
 * @date: 2020/6/15 18:45
 */
@Data
public class UserImportDownloadVo implements Serializable {

    @Excel(name = "用户名", orderNum = "0", width = 20)
    private String username;


    /**
     * 手机号
     */
    @Excel(name = "手机号", orderNum = "1", width = 20)
    private String phoneNumber;

    /**
     * 密码
     */
    @Excel(name = "密码", orderNum = "2", width = 20)
    private String password;


    private Long id;
}
