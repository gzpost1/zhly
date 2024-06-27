package cn.cuiot.dmp.lease.dto.price;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/27
 */
@Data
public class PriceManageRecordDTO implements Serializable {

    private static final long serialVersionUID = -6460507771155307308L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 操作名称
     */
    private String operateName;

    /**
     * 操作人id
     */
    private Long operatorId;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

    /**
     * 审核备注
     */
    private String auditRemark;

}
