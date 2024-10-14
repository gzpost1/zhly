package cn.cuiot.dmp.app.vo.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户反馈
 * </p>
 *
 * @author hantingyao
 * @since 2024-06-14
 */
@Data
public class UserFeedbackExportVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID", orderNum = "1", width = 20)
    private Long userId;

    /**
     * 昵称
     */
    @Excel(name = "昵称", orderNum = "2")
    private String name;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码", orderNum = "3")
    private String phone;

    /**
     * 组织名称
     */
    @Excel(name = "组织名称", orderNum = "4")
    private String deptName;

    /**
     * 楼盘名称
     */
    @Excel(name = "楼盘名称", orderNum = "5")
    private String buildingName;

    /**
     * 创建时间
     */
    @Excel(name = "提交时间", orderNum = "6", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 状态(0:待回复,1:已回复)
     */
    @Excel(name = "回复状态", orderNum = "7", replace = {"待回复_0", "已回复_1"})
    private Byte status;

    /**
     * 回复时间
     */
    @Excel(name = "回复时间", orderNum = "8", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date replyTime;

    /**
     * 回复人
     */
    @Excel(name = "回复人", orderNum = "9")
    private String replyUserName;
}
