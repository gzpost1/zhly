package cn.cuiot.dmp.system.application.param.dto;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserHouseAuditPageQueryDTO extends PageQuery {

    private static final long serialVersionUID = -6746694234972740816L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 楼盘ID
     */
    private Long buildingId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 审核状态(0:待审核,1:审核通过,2:审核驳回)
     */
    private Byte auditStatus;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
