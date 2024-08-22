package cn.cuiot.dmp.common.bean.external;

import lombok.Data;

/**
 * 门禁（宇泛）-对接系统参数
 *
 * @date 2024/8/22 9:27
 * @author gxp
 */
@Data
public class YFEntranceGuardBO {

    /**
     * AppKey
     */
    private String appKey;

    /**
     * AppSecret
     */
    private String appSecret;

    /**
     * projectGuid
     */
    private String projectGuid;
}
