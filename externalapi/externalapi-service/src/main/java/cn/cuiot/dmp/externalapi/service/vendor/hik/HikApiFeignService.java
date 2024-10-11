package cn.cuiot.dmp.externalapi.service.vendor.hik;

import cn.cuiot.dmp.common.bean.external.HIKEntranceGuardBO;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req.*;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 海康-相关API
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Service
public class HikApiFeignService {

    @Autowired
    private HikApiService hikApiService;

    /**
     * 查询门禁设备列表v2
     *
     * @return HikResourcesResp
     * @Param req 参数
     */
    public HikAcsListResp queryAcsDeviceSearch(HikAcsListReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/resource/v2/acsDevice/search";
        HikBaseResp<HikAcsListResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikAcsListResp>>() {
                });
        return resp.getData();
    }

    /**
     * 增量获取门禁设备数据
     *
     * @return HikResourcesResp
     * @Param req 参数
     */
    public HikAcsListResp queryAcsDeviceByTimeRange(HikAcsListReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/resource/v1/acsDevice/timeRange";
        HikBaseResp<HikAcsListResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikAcsListResp>>() {
                });
        return resp.getData();
    }

    /**
     * 获取门禁设备在线状态
     *
     * @return HikResourcesResp
     * @Param req 参数
     */
    public HikAcsStatesResp queryAcsStatus(HikAcsStatesReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/nms/v1/online/acs_device/get";
        HikBaseResp<HikAcsStatesResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikAcsStatesResp>>() {
                });
        return resp.getData();
    }

    /**
     * 根据区域编号获取下一级区域列表v2
     *
     * @return HikRegionResp
     * @Param req 参数
     */
    public HikRegionResp queryRegions(HikRegionReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/resource/v2/regions/nodesByParams";
        HikBaseResp<HikRegionResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikRegionResp>>() {
                });
        return resp.getData();
    }

    /**
     * 查询门禁点列表v2
     *
     * @return HikDoorResp
     * @Param req 参数
     */
    public HikDoorResp queryDoorSearch(HikDoorReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/resource/v2/door/search";
        HikBaseResp<HikDoorResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikDoorResp>>() {
                });
        return resp.getData();
    }

    /**
     * 增量获取门禁点数据
     *
     * @return HikDoorResp
     * @Param req 参数
     */
    public HikDoorResp queryDoorByTimeRange(HikDoorReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/resource/v1/door/timeRange";
        HikBaseResp<HikDoorResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikDoorResp>>() {
                });
        return resp.getData();
    }

    /**
     * 查询门禁点状态
     *
     * @return HikDoorStatesResp
     * @Param req 参数
     */
    public HikDoorStatesResp queryDoorStates(HikDoorStatesReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/acs/v1/door/states";
        HikBaseResp<HikDoorStatesResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikDoorStatesResp>>() {
                });
        return resp.getData();
    }

    /**
     * 门禁点反控
     *
     * @return HikDoorControlResp
     * @Param req 参数
     */
    public HikDoorControlResp doorDoControl(HikDoorControlReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/acs/v1/door/doControl";
        HikBaseResp<HikDoorControlResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikDoorControlResp>>() {
                });
        return resp.getData();
    }

    /**
     * 查询门禁读卡器列表
     *
     * @return HikReaderResp
     * @Param req 参数
     */
    public HikReaderResp queryReaderSearch(HikReaderReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/resource/v1/reader/search";
        HikBaseResp<HikReaderResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikReaderResp>>() {
                });
        return resp.getData();
    }

    /**
     * 增量获取门禁读卡器数据
     *
     * @return HikReaderResp
     * @Param req 参数
     */
    public HikReaderResp queryReaderByTimeRange(HikReaderReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/resource/v1/reader/timeRange";
        HikBaseResp<HikReaderResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikReaderResp>>() {
                });
        return resp.getData();
    }

    /**
     * 获取门禁读卡器在线状态
     *
     * @return HikReaderStatesResp
     * @Param req 参数
     */
    public HikReaderStatesResp queryReaderStates(HikReaderStatesReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/nms/v1/online/reader/get";
        HikBaseResp<HikReaderStatesResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikReaderStatesResp>>() {
                });
        return resp.getData();
    }

    /**
     * 查询组织列表v2
     *
     * @return HikOrgListResp
     * @Param req 参数
     */
    public HikOrgListResp queryOrgList(HikOrgListReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/resource/v2/org/advance/orgList";
        HikBaseResp<HikOrgListResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikOrgListResp>>() {
                });
        return resp.getData();
    }

    /**
     * 添加人员v2
     *
     * @return HikPersonAddResp
     * @Param req 参数
     */
    public HikPersonAddResp personAdd(HikPersonAddReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/resource/v2/person/single/add";
        HikBaseResp<HikPersonAddResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikPersonAddResp>>() {
                });
        return resp.getData();
    }

    /**
     * 修改人员
     *
     * @return HikBaseResp
     * @Param req 参数
     */
    public HikBaseResp<Object> personUpdate(HikPersonUpdateReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/resource/v1/person/single/update";
        return hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<Object>>() {
                });
    }

    /**
     * 批量删除人员
     *
     * @return List<HikPersonBatchDeleteResp>
     * @Param req 参数
     */
    public List<HikPersonBatchDeleteResp> personBatchDelete(HikPersonBatchDeleteReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/resource/v1/person/batch/delete";
        HikBaseResp<List<HikPersonBatchDeleteResp>> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<List<HikPersonBatchDeleteResp>>>() {
                });
        return resp.getData();
    }

    /**
     * 添加人脸
     *
     * @return HikFaceSingleAddResp
     * @Param req 参数
     */
    public HikFaceSingleAddResp faceSingleAdd(HikFaceSingleAddReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/resource/v1/face/single/add";
        HikBaseResp<HikFaceSingleAddResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikFaceSingleAddResp>>() {
                });
        return resp.getData();
    }

    /**
     * 修改人脸
     *
     * @return HikFaceSingleUpdateResp
     * @Param req 参数
     */
    public HikFaceSingleUpdateResp faceSingleUpdate(HikFaceSingleUpdateReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/resource/v1/face/single/update";
        HikBaseResp<HikFaceSingleUpdateResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikFaceSingleUpdateResp>>() {
                });
        return resp.getData();
    }

    /**
     * 删除人脸
     *
     * @return HikBaseResp<Object>
     * @Param req 参数
     */
    public HikBaseResp<Object> faceSingleDelete(HikFaceSingleDeleteReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/resource/v1/face/single/delete";
        return hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<Object>>() {
                });
    }

    /**
     * 查询权限配置
     *
     * @return HikAcpsAuthConfigSearchResp
     * @Param req 参数
     */
    public HikAcpsAuthConfigSearchResp acpsAuthConfigSearch(HikAcpsAuthConfigSearchReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/acps/v1/auth_config/search";
        HikBaseResp<HikAcpsAuthConfigSearchResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikAcpsAuthConfigSearchResp>>() {
                });
        return resp.getData();
    }

    /**
     * 添加权限配置
     *
     * @return HikAcpsAuthConfigAddResp
     * @Param req 参数
     */
    public HikAcpsAuthConfigAddResp acpsAuthConfigAdd(HikAcpsAuthConfigAddReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/acps/v1/auth_config/add";
        HikBaseResp<HikAcpsAuthConfigAddResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikAcpsAuthConfigAddResp>>() {
                });
        return resp.getData();
    }

    /**
     * 删除权限配置
     *
     * @return HikAcpsAuthConfigDeleteResp
     * @Param req 参数
     */
    public HikAcpsAuthConfigDeleteResp acpsAuthConfigDelete(HikAcpsAuthConfigDeleteReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/acps/v1/auth_config/delete";
        HikBaseResp<HikAcpsAuthConfigDeleteResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikAcpsAuthConfigDeleteResp>>() {
                });
        return resp.getData();
    }

    /**
     * 查询门禁点事件v2
     *
     * @return HikDoorEventsResp
     * @Param req 参数
     */
    public HikDoorEventsResp queryDoorEvents(HikDoorEventsReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/acs/v2/door/events";
        HikBaseResp<HikDoorEventsResp> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikDoorEventsResp>>() {
                });
        return resp.getData();
    }

    /**
     * 获取门禁事件的图片
     *
     * @return HikDoorEventsResp
     * @Param req 参数
     */
    public HikBaseResp<String> queryEventPictures(HikAcsEventPicturesReq req, HIKEntranceGuardBO bo) {
        String gateway = "/api/acs/v1/event/pictures";
        HikBaseResp<HikBaseResp<String>> resp = hikApiService.postForHttps(bo, gateway, JsonUtil.writeValueAsString(req),
                new TypeReference<HikBaseResp<HikBaseResp<String>>>() {
                });
        return resp.getData();
    }
}
