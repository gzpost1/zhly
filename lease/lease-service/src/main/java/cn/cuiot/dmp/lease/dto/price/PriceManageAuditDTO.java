package cn.cuiot.dmp.lease.dto.price;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/6/27
 */
@Data
public class PriceManageAuditDTO implements Serializable {

    private static final long serialVersionUID = -2483467587551332135L;

    /**
     * 主键ID
     */
    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * 状态(1:草稿,2:审核中,3:审核通过,4:审核不通过,5:已执行,6:已作废)
     */
    private Byte status;

    /**
     * 备注
     */
    private String remark;

}
