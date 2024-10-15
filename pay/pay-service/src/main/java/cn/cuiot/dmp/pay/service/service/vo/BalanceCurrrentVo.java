package cn.cuiot.dmp.pay.service.service.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceCurrrentVo {

    /**
     * 当前金额
     */
   private Integer currentBalance;

    /**
     * 充值总金额
     */
   private Integer totalBalance;





}
