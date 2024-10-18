
package cn.cuiot.dmp.lease.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ContractLeaseStatisticVO implements Serializable {


    private static final long serialVersionUID = -1605552941457516157L;

    /**
     * 已租
     */
    private Long leased;

    /**
     * 待租
     */
    private Long leasing;


}
