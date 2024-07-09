package cn.cuiot.dmp.system.application.param.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/5
 */
@Data
public class VisitorRecordDTO implements Serializable {

    private static final long serialVersionUID = -8635607782088276026L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 楼盘ID
     */
    private Long buildingId;

    /**
     * 楼盘名称
     */
    private String buildingName;

    /**
     * 访客姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 性别（1男，2女，3保密）
     */
    private Byte sex;

    /**
     * 来访时间
     */
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
    private String visitReason;

}
