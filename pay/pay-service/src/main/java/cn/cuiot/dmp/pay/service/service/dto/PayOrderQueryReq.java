package cn.cuiot.dmp.pay.service.service.dto;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * 支付订单查询参数
 *
 * @author huq
 * @ClassName CombineQueryReq
 * @Date 2021/11/3 14:06
 **/
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderQueryReq implements Serializable {

    /**
     * 平台订单号
     */
    private Long orderId;
}
