package cn.cuiot.dmp.video.service.vendor.vsuap;

import cn.cuiot.dmp.video.service.vendor.bean.req.vsuap.VsuapLayerNodeDetailReq;
import cn.cuiot.dmp.video.service.vendor.bean.req.vsuap.VsuapLayerNodeListReq;
import cn.cuiot.dmp.video.service.vendor.bean.resp.vsuap.VsuapBaseResp;
import cn.cuiot.dmp.video.service.vendor.bean.resp.vsuap.VsuapLayerNodeDetailResp;
import cn.cuiot.dmp.video.service.vendor.bean.resp.vsuap.VsuapLayerNodeListResp;
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
    public List<VsuapLayerNodeListResp> requestLayerNodeList(VsuapLayerNodeListReq req) {
        String gateway = "vsuap-platform-api/v1/layerNode/query";
        VsuapBaseResp<List<VsuapLayerNodeListResp>> resp = vsuapApiService.request(gateway, HttpMethod.POST, req,
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
    public VsuapLayerNodeDetailResp requestLayerNodeDetail(VsuapLayerNodeDetailReq req) {
        String gateway = "vsuap-platform-api/v1/layerNode/detail";
        VsuapBaseResp<VsuapLayerNodeDetailResp> resp = vsuapApiService.request(gateway, HttpMethod.POST, req,
                new TypeReference<VsuapBaseResp<VsuapLayerNodeDetailResp>>() {
                });
        return resp.getData();
    }
}
