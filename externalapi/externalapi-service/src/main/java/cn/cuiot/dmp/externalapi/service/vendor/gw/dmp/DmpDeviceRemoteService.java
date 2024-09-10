package cn.cuiot.dmp.externalapi.service.vendor.gw.dmp;

import cn.cuiot.dmp.common.bean.external.GWEntranceGuardBO;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.*;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 格物平台-设备相关接口
 *
 * @Author: zc
 * @Date: 2024-09-06
 */
@Service
public class DmpDeviceRemoteService {

    @Autowired
    private DmpApiService dmpApiService;

    /**
     * 查询指定设备基本信息
     */
    public BaseDmpResp<DmpDeviceResp> getDevice(DmpDeviceReq req, GWEntranceGuardBO bo) {
        String gateway = "api/getDevice/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<DmpDeviceResp>>() {
        });
    }

    /**
     * 查询设备列表
     */
    public BaseDmpResp<DmpDeviceListResp> listDevices(DmpDevicePageReq req, GWEntranceGuardBO bo) {
        String gateway = "api/listDevices/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<DmpDeviceListResp>>() {
        });
    }

    /**
     * 删除单个设备
     */
    public BaseDmpResp<Object> deleteDevice(DmpDeviceReq req, GWEntranceGuardBO bo) {
        String gateway = "api/deleteDevice/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<Object>>() {
        });
    }

    /**
     * 批量删除设备（单次批量删除设备上限为200）
     */
    public BaseDmpResp<Object> batchDeleteDevice(DmpDeviceBatchCreateReq req, GWEntranceGuardBO bo) {
        String gateway = "api/batchDeleteDevice/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<Object>>() {
        });
    }

    /**
     * 禁用单个设备
     */
    public BaseDmpResp<Object> disableDevice(DmpDeviceReq req, GWEntranceGuardBO bo) {
        String gateway = "api/disableDevice/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<Object>>() {
        });
    }

    /**
     * 批量禁用设备（单次批量禁用设备上限为200）
     */
    public BaseDmpResp<Object> batchDisableDevice(DmpDeviceBatchCreateReq req, GWEntranceGuardBO bo) {
        String gateway = "/api/batchDisableDevice/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<Object>>() {
        });
    }

    /**
     * 启用单个设备
     */
    public BaseDmpResp<Object> enableDevice(DmpDeviceReq req, GWEntranceGuardBO bo) {
        String gateway = "api/enableDevice/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<Object>>() {
        });
    }

    /**
     * 批量启用设备（单次批量启用设备上限为200）
     */
    public BaseDmpResp<Object> batchEnableDevice(DmpDeviceBatchCreateReq req, GWEntranceGuardBO bo) {
        String gateway = "api/batchEnableDevice/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<Object>>() {
        });
    }

    /**
     * 查询单个设备状态
     */
    public BaseDmpResp<DmpDeviceResp> getStatus(DmpDeviceReq req, GWEntranceGuardBO bo) {
        String gateway = "api/getStatus/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<DmpDeviceResp>>() {
        });
    }

    /**
     * 批量查询设备状态
     */
    public BaseDmpResp<DmpDeviceResp> batchGetStatus(DmpDeviceBatchCreateReq req, GWEntranceGuardBO bo) {
        String gateway = "api/batchGetStatus/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<DmpDeviceResp>>() {
        });
    }

    /**
     * 创建单个设备
     */
    public DmpDeviceResp createDevice(DmpDeviceCreateReq req, GWEntranceGuardBO bo) {
        String gateway = "api/createDevice/V1/1Main/vV1.1";
        BaseDmpResp<DmpDeviceResp> resp = dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<DmpDeviceResp>>() {
        });
        return resp.getData();
    }

    /**
     * 批量创建设备（异步创建）
     */
    public BaseDmpResp<Object> batchCreateDevice(DmpDeviceBatchCreateReq req, GWEntranceGuardBO bo) {
        String gateway = "api/batchCreateDevice/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<Object>>() {
        });
    }

    /**
     * 查询设备属性最新值
     */
    public BaseDmpResp<DmpDevicePropertyListResp> getPropertyLatestValues(DmpDeviceReq req, GWEntranceGuardBO bo) {
        String gateway = "api/getPropertyLatestValues/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<DmpDevicePropertyListResp>>() {
        });
    }

    /**
     * 设置设备属性
     */
    public BaseDmpResp<Object> setDeviceProperty(DmpDevicePropertyReq req, GWEntranceGuardBO bo) {
        String gateway = "api/setDeviceProperty/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<Object>>() {
        });
    }

    /**
     * 调用设备服务
     */
    public BaseDmpResp<InvokeDeviceServiceResp> invokeDeviceService(InvokeDeviceServiceReq req, GWEntranceGuardBO bo) {
        String gateway = "api/invokeDeviceService/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<InvokeDeviceServiceResp>>() {
        });
    }
}
