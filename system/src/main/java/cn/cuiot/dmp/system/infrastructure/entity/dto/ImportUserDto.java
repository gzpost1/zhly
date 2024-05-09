package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import java.io.Serializable;
import lombok.Data;

/**
 * 导入用户
 *
 * @author zhh
 * @version 1.0
 * @date 2020/9/7 20:09
 */
@Data
public class ImportUserDto implements Serializable {

    @Excel(name = "用户名", orderNum = "0")
    private String username;

    @Excel(name = "姓名", orderNum = "1")
    private String name;

    @Excel(name = "手机号", orderNum = "2")
    private String phoneNumber;

    @Excel(name = "角色", orderNum = "3")
    private String roleName;

    @Excel(name = "岗位", orderNum = "4")
    private String postName;

    @Excel(name = "备注", orderNum = "5")
    private String remark;
}
