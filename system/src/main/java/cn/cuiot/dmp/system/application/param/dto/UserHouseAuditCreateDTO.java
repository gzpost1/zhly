package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/6/13
 */
@Data
public class UserHouseAuditCreateDTO implements Serializable {

    private static final long serialVersionUID = -4238008832410313162L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 企业ID
     */
    @NotNull(message = "企业ID不能为空")
    private Long companyId;

    /**
     * 楼盘ID
     */
    @NotNull(message = "楼盘ID不能为空")
    private Long buildingId;

    /**
     * 房屋ID
     */
    @NotNull(message = "房屋ID不能为空")
    private Long houseId;

    /**
     * 身份类型(1:户主,2:租户,3:家属)
     */
    @NotNull(message = "身份类型不能为空")
    private Byte identityType;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;

    /**
     * 证件类型（系统配置自定义）
     */
    @NotNull(message = "证件类型不能为空")
    private Long cardTypeId;

    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    private String identityNum;

    /**
     * 补充说明-纯文本
     */
    private String remark;

    /**
     * 补充说明-图片（仅限jpg, jpeg, png格式，6张）
     */
    private List<String> remarkImages;

}
