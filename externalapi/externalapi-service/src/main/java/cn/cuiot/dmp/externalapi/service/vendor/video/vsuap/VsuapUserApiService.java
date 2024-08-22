package cn.cuiot.dmp.externalapi.service.vendor.video.vsuap;

import cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap.VsuapSecretKeyReq;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap.VsuapUserInfoReq;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapBaseResp;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapUserInfoResp;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * 云智眼-用户信息
 *
 * @Author: zc
 * @Date: 2024-03-07
 */
@Service
public class VsuapUserApiService {

    @Autowired
    private VsuapApiService vsuapApiService;

    /**
     * 查询用户信息
     *
     * @Param: req AI算法入参
     * @return: List<VsuapUserInfoResp> 用户信息
     */
    public VsuapUserInfoResp requestUserInfo(VsuapUserInfoReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/user/query";
        VsuapBaseResp<VsuapUserInfoResp> resp = vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapUserInfoResp>>() {
                });
        return resp.getData();
    }
}
