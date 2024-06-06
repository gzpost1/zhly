package cn.cuiot.dmp.system.application.param.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/5
 */
@Data
public class VisitorRecordCreateDTO implements Serializable {

    private static final long serialVersionUID = 3579271509736972244L;

    /**
     * 楼盘ID
     */
    @NotNull(message = "楼盘ID不能为空")
    private Long buildingId;

    /**
     * 访客姓名
     */
    @NotBlank(message = "访客姓名不能为空")
    private String name;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;

    /**
     * 性别（1男，2女，3保密）
     */
    private Byte sex;

    /**
     * 来访时间
     */
    @NotNull(message = "来访时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date visitTime;

    /**
     * 是否驾车（0否，1是）
     */
    private Byte driveFlag;

    /**
     * 来访人数
     */
    private Integer visitorNum;

    /**
     * 来访事由
     */
    @NotBlank(message = "来访事由不能为空")
    private String visitReason;

}
