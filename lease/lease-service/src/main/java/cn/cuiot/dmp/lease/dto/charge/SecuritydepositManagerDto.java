package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.lease.entity.charge.TbChargeAbrogate;
import cn.cuiot.dmp.lease.entity.charge.TbSecuritydepositRefund;
import lombok.Data;

import java.util.List;

/**
 * @Description 押金管理详情
 * @Date 2024/6/17 11:43
 * @Created by libo
 */
@Data
public class SecuritydepositManagerDto extends SecuritydepositManagerPageDto {

    /**
     * 作废明细
     */
    private List<TbChargeAbrogate> abrogateList;

    /**
     * 退款明细
     */
    private List<TbSecuritydepositRefund> securitydepositRefundList;
}
