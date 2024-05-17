package cn.cuiot.dmp.system.infrastructure.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户导出
 * @author: wuyongchong
 * @date: 2020/6/15 18:45
 */
@Data
public class UserExportVo implements Serializable {

    @Excel(name = "用户名", orderNum = "0", width = 20)
    private String username;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID", orderNum = "1", width = 20)
    private String id;

    /**
     * 姓名
     */
    @Excel(name = "姓名", orderNum = "2", width = 20)
    private String name;


    /**
     * 手机号
     */
    @Excel(name = "手机号", orderNum = "3", width = 20)
    private String phoneNumber;

    /**
     * 角色
     */
    @Excel(name = "角色", orderNum = "4", width = 20)
    private String roleName;

    /**
     * 所属组织
     */
    @Excel(name = "所属组织", orderNum = "5", width = 20)
    private String deptPathName;


    /**
     * 岗位
     */
    @Excel(name = "岗位", orderNum = "6", width = 20)
    private String postName;

    /**
     * 备注
     */
    @Excel(name = "备注", orderNum = "7", width = 20)
    private String remark;

}
