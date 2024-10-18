package cn.cuiot.dmp.system.application.param.vo.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 用户房屋信息
 *
 * @author hantingyao
 * @date 2024/6/13
 */
@Data
public class UserHouseAuditExportVo {

    /**
     * 姓名
     */
    @Excel(name = "昵称", width = 20, orderNum = "1")
    private String name;

    /**
     * 手机号
     */
    @Excel(name = "手机号码", width = 15, orderNum = "2")
    private String phoneNumber;

    /**
     * 楼盘名称
     */
    @Excel(name = "所属楼盘", width = 30, orderNum = "3")
    private String buildingName;

    /**
     * 房屋ID
     */
    @Excel(name = "认证房号", width = 20, orderNum = "4")
    private Long houseId;

    /**
     * 身份类型(1:户主,2:租户,3:家属)
     */
    @Excel(name = "认证身份", width = 10, orderNum = "5", replace = {"户主_1", "租户_2", "家属_3"})
    private String identityTypeName;

    /**
     * 创建时间
     */
    @Excel(name = "提交时间", width = 20, orderNum = "6", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * 身份类型(1:户主,2:租户,3:家属)
     */
    private Byte identityType;

    public String getIdentityTypeName() {
        return identityType.toString();
    }
}
