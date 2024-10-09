package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;

import lombok.Data;

/**
 * 批量删除人员 resp
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikPersonBatchDeleteResp {
    /**
     * 人员标志
     */
    private String personId;
    /**
     * 错误码
     */
    private String code;
    /**
     * 错误信息
     */
    private String msg;
}
