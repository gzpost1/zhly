package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "visitor_record", autoResultMap = true)
public class VisitorRecordEntity extends BaseEntity {

    private static final long serialVersionUID = -4470444630205829006L;

    /**
     * 楼盘ID
     */
    private Long buildingId;

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
