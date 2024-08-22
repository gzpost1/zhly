package cn.cuiot.dmp.externalapi.service.vendor.video.vsuap;

import cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap.*;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 云智眼-设备相关api
 *
 * @Author: zc
 * @Date: 2024-03-07
 */
@Service
public class VsuapDeviceApiService {

    @Autowired
    private VsuapApiService vsuapApiService;

    /**
     * 查询设备列表
     *
     * @Param: req 查询设备列表入参
     * @return: List<VsuapDeviceListResp> 设备列表
     */
    public VsuapBaseResp<List<VsuapDeviceListResp>> requestDeviceList(VsuapDeviceListReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/device/queryList";
        return vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<List<VsuapDeviceListResp>>>() {
                });
    }

    /**
     * 查询设备详情
     *
     * @Param: req 查询设备详情入参
     * @return: VsuapDeviceDetailResp 设备列表
     */
    public VsuapDeviceDetailResp requestDeviceDetail(VsuapDeviceDetailReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/device/detail";
        VsuapBaseResp<VsuapDeviceDetailResp> request = vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapDeviceDetailResp>>() {
                });
        return request.getData();
    }

    /**
     * 查询设备通道列表
     *
     * @Param: req 设备通道列表入参
     * @return: List<VsuapChannelResp> 通道列表
     */
    public VsuapBaseResp<List<VsuapChannelResp>> requestChannel(VsuapChannelReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/device/queryChannel";
        return vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<List<VsuapChannelResp>>>() {
                });
    }

    /**
     * 查询设备实时视频信息
     *
     * @Param: req 实时视频入参
     * @return: VsuapPlayOnResp 视频流数据
     */
    public VsuapPlayOnResp requestPlayOn(VsuapPlayOnReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/device/playOn";
        VsuapBaseResp<VsuapPlayOnResp> request = vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapPlayOnResp>>() {
                });
        return request.getData();
    }

    /**
     * 查询设备云端录像信息
     *
     * @Param: req 云端录像入参
     * @return: VsuapBaseResp<List> 录像分页数据
     */
    public VsuapBaseResp<List<VsuapRecordListResp>> requestRecordList(VsuapRecordListReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/device/recordList";
        return vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<List<VsuapRecordListResp>>>() {
                });
    }

    /**
     * 查询设备实时视频 HLS 信息
     *
     * @Param: req 实时视频HLS入参
     * @return: VsuapBaseResp<List> 录像分页数据
     */
    public VsuapPlayOnFlvHlsResp requestPlayOnFlvHls(VsuapPlayOnReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/device/playOnFlvHls";
        VsuapBaseResp<VsuapPlayOnFlvHlsResp> request = vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapPlayOnFlvHlsResp>>() {
                });
        return request.getData();
    }
}
