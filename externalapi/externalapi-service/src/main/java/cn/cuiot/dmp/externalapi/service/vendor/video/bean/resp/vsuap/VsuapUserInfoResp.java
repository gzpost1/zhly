package cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap;

import lombok.Data;

/**
 * 云智眼-查询用户信息结果 resp
 *
 * @Author: zc
 * @Date: 2024-03-13
 */
@Data
public class VsuapUserInfoResp {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 是否是子账号（1：是；0：否）
     */
    private Integer isSubUser;

    /**
     * 所属主账号
     */
    private String belongUserId;
}
