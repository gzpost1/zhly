package cn.cuiot.dmp.system.application.param.vo.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * C端用户
 *
 * @author: wuyongchong
 * @date: 2024/6/14 9:25
 */
@Data
public class ClientUserExportVo implements Serializable {

    /**
     * pk
     */
    @Excel(name = "用户ID", orderNum = "0",width = 20)
    private Long id;

    /**
     * 姓名
     */
    @Excel(name = "昵称", orderNum = "1", width = 15)
    private String name;

    /**
     * 手机号
     */
    @Excel(name = "手机号", orderNum = "2", width = 11)
    private String phoneNumber;


//    /**
//     * 状态
//     */
//    private Integer status;

    /**
     * 创建时间
     */
    @Excel(name = "注册时间", orderNum = "3", width = 20, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
}
