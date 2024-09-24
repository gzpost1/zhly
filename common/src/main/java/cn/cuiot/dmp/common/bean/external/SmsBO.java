package cn.cuiot.dmp.common.bean.external;

import lombok.Data;

/**
 * 短信配置
 *
 * @Author: zc
 * @Date: 2024-09-19
 */
@Data
public class SmsBO {

    /**
     * 启停用状态（0：停用；1：启用）
     */
    private Byte status;
}
