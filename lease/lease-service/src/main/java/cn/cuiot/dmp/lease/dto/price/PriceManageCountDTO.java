package cn.cuiot.dmp.lease.dto.price;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/7/3
 */
@Data
public class PriceManageCountDTO implements Serializable {

    private static final long serialVersionUID = 6342266665270543772L;

    /**
     * 状态(0:全部,1:草稿,2:审核中,3:审核通过,4:审核不通过,5:已执行,6:已作废)
     */
    private Byte status;

    /**
     * 计数
     */
    private Integer count;

}
