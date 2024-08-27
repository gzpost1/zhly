package cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 云智眼-查询用户信息req
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VsuapUserInfoReq {
    /**
     * 请求ID
     */
    private String requestId;
}
