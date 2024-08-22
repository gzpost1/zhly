package cn.cuiot.dmp.externalapi.service.vendor.video.vsuap;

import cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap.*;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 云智眼-设备控制相关api
 *
 * @Author: zc
 * @Date: 2024-03-07
 */
@Service
public class VsuapDeviceControlApiService {

    @Autowired
    private VsuapApiService vsuapApiService;

    /**
     * 镜头光圈控制
     *
     * @Param: req 镜头光圈控制入参
     * @return: void
     */
    public void requestDeviceControlByIris(VsuapDeviceControlByIrisReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/device/deviceControlByIRIS";
        vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapDeviceControlByIrisResp>>() {
                });
    }

    /**
     * 镜头变焦
     *
     * @Param: req 镜头变焦入参
     * @return: void
     */
    public void requestDeviceControlByFocus(VsuapDeviceControlByFocusReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/device/deviceControlByFOCUS";
        vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapDeviceControlByFocusResp>>() {
                });
    }

    /**
     * 镜头变倍
     *
     * @Param: req 镜头变倍入参
     * @return: void
     */
    public void requestDeviceControlByZoom(VsuapDeviceControlByZoomReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/device/deviceControlByZOOM";
        vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapDeviceControlByZoomResp>>() {
                });
    }

    /**
     * 云台水平垂直方向控制
     *
     * @Param: req 控制入参
     * @return: void
     */
    public void requestDeviceControlByDirection(VsuapDeviceControlByDirectionReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/device/deviceControlByVERTICALAndHORIZONTAL";
        vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapDeviceControlByDirectionResp>>() {
                });
    }

    /**
     * 设置预置位
     *
     * @Param: req 设置预置位入参
     * @return: void
     */
    public void requestPresetConfig(VsuapPresetConfigReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/deviceControl/presetConfig";
        vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapPresetConfigResp>>() {
                });
    }

    /**
     * 删除预置位
     *
     * @Param: req 删除预置位入参
     * @return: void
     */
    public void requestPresetDelete(VsuapPresetDeleteReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/deviceControl/presetDelete";
        vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapPresetDeleteResp>>() {
                });
    }

    /**
     * 调用预置位
     *
     * @Param: req 调用预置位入参
     * @return: void
     */
    public void requestPresetExec(VsuapPresetExecReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/deviceControl/presetExec";
        vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapPresetExecResp>>() {
                });
    }

    /**
     * 预置位列表查询
     *
     * @Param: req 预置位列表查询入参
     * @return: void
     */
    public void requestPresetList(VsuapPresetListReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/deviceControl/presetList";
        vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<List<VsuapPresetListResp>>>() {
                });
    }

    /**
     * 添加巡航组
     *
     * @Param: req 添加巡航组入参
     * @return: void
     */
    public void requestCruiseAddReq(VsuapCruiseAddReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/deviceControl/cruiseAdd";
        vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapCruiseAddResp>>() {
                });
    }

    /**
     * 修改巡航组
     *
     * @Param: req 修改巡航组入参
     * @return: void
     */
    public void requestCruiseModify(VsuapCruiseModifyReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/deviceControl/cruiseModify";
        vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapCruiseModifyResp>>() {
                });
    }

    /**
     * 删除巡航组
     *
     * @Param: req 删除巡航组入参
     * @return:
     */
    public void requestCruiseDelete(VsuapCruiseDeleteReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/deviceControl/cruiseDelete";
        vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapCruiseDeleteResp>>() {
                });
    }

    /**
     * 巡航组列表查询
     *
     * @Param: req 巡航组列表查询入参
     * @return: VsuapBaseResp<List> 巡航组分页列表
     */
    public VsuapBaseResp<List<VsuapCruiseListResp>> requestCruiseList(VsuapCruiseListReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/deviceControl/cruiseList";
        return vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<List<VsuapCruiseListResp>>>() {
                });
    }

    /**
     * 巡航点列表查询
     *
     * @Param: req 巡航点列表查询入参
     * @return: VsuapBaseResp<List> 巡航点列表查询列表
     */
    public VsuapBaseResp<List<VsuapCruiseConfigListResp>> requestCruiseConfigList(VsuapCruiseConfigListReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/deviceControl/cruiseConfigList";
        return vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<List<VsuapCruiseConfigListResp>>>() {
                });
    }

    /**
     * 添加巡航点
     *
     * @Param: req 添加巡航点入参
     * @return: VsuapCruiseConfigAddResp 巡航点
     */
    public VsuapCruiseConfigAddResp requestCruiseConfigAdd(VsuapCruiseConfigAddReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/deviceControl/cruiseConfigAdd";
        VsuapBaseResp<VsuapCruiseConfigAddResp> request = vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapCruiseConfigAddResp>>() {
                });
        return request.getData();
    }

    /**
     * 修改巡航点
     *
     * @Param: req 修改巡航点入参
     * @return: void
     */
    public void requestCruiseConfigModify(VsuapCruiseConfigModifyReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/deviceControl/cruiseConfigModify";
        vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapCruiseConfigModifyResp>>() {
                });
    }

    /**
     * 删除巡航点
     *
     * @Param: req 删除巡航点入参
     * @return: void
     */
    public void requestCruiseConfigDelete(VsuapCruiseConfigDeleteReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/deviceControl/cruiseConfigDelete";
        vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapCruiseConfigDeleteResp>>() {
                });
    }

    /**
     * 开始巡航
     *
     * @Param: req 开始巡航入参
     * @return: void
     */
    public void requestStartCruise(VsuapStartCruiseReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/deviceControl/startCruise";
        vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapStartCruiseResp>>() {
                });
    }

    /**
     * 停止巡航
     *
     * @Param: req 停止巡航入参
     * @return: void
     */
    public void requestStopCruise(VsuapStopCruiseReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/deviceControl/stopCruise";
        vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<VsuapStopCruiseResp>>() {
                });
    }
}
