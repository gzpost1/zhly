package cn.cuiot.dmp.lease.dto.price;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author caorui
 * @date 2024/7/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PriceManageRecordPageQueryDTO extends PageQuery {

    private static final long serialVersionUID = -326149323764317287L;

    /**
     * 定价单编码
     */
    private Long priceId;

}
