package cn.cuiot.dmp.common.bean.external;

import lombok.Data;

/**
 * 状态BO
 *
 * @Author: zc
 * @Date: 2024-10-14
 */
@Data
public class ExternalapiBaseStatusBO {

    /**
     * 启停用状态（0：停用；1：启用）
     */
    private Byte status;
}