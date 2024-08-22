package cn.cuiot.dmp.externalapi.service.vendor.video.vsuap;

import cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap.VsuapLayerNodeDetailReq;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap.VsuapLayerNodeListReq;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap.VsuapSecretKeyReq;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapBaseResp;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapLayerNodeDetailResp;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapLayerNodeListResp;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 云智眼-组织API
 *
 * @Author: zc
 * @Date: 2024-03-07
 */
@Service
public class VsuapLayerNodeApiService {

    @Autowired
    private VsuapApiService vsuapApiService;

    /**
     * 查询组织列表
     *
     * @Param: req 参数
     * @return: List<VsuapLayerNodeListResp> 组织列表
     */
    public List<VsuapLayerNodeListResp> requestLayerNodeList(VsuapLayerNodeListReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/layerNode/query";
        VsuapBaseResp<List<VsuapLayerNodeListResp>> resp = vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<List<VsuapLayerNodeListResp>>>() {
                });
        return resp.getData();
    }

    /**
     * 查询组织详情
     *
     * @Param: req 参数
     * @return: List<VsuapLayerNodeDetailResp> 组织详情
     */
    public VsuapLayerNodeDetailResp requestLayerNodeDetail(VsuapLayerNodeDetailReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/layerNode/detail";
        VsuapBaseResp<VsuapLayerNodeDetailResp> resp = vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapLayerNodeDetailResp>>() {
                });
        return resp.getData();
    }
}
