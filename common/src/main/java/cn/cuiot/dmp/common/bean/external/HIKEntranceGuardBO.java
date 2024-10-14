package cn.cuiot.dmp.common.bean.external;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 海康门禁
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HIKEntranceGuardBO extends ExternalapiBaseStatusBO {

    /**
     * ak
     */
    private String ak;

    /**
     * sk
     */
    private String sk;

    /**
     * 企业id（仅做业务参数）
     */
    private Long companyId;
}
