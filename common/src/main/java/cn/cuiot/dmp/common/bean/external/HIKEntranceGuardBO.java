package cn.cuiot.dmp.common.bean.external;

import lombok.Data;

/**
 * 海康门禁
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HIKEntranceGuardBO {

    /**
     * 启停用状态（0：停用；1：启用）
     */
    private Byte status;

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
