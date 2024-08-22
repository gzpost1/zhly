package cn.cuiot.dmp.externalapi.service.vendor.video.vsuap;

import cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap.VsuapAIAlarmListReq;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap.VsuapAIMethodListReq;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap.VsuapAIStatisticsReq;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap.VsuapSecretKeyReq;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapAIAlarmListResp;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapAIMethodListResp;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapAIStatisticsResp;
import cn.cuiot.dmp.externalapi.service.vendor.video.bean.resp.vsuap.VsuapBaseResp;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 云智眼-AI相关api
 *
 * @Author: zc
 * @Date: 2024-03-07
 */
@Service
public class VsuapAIApiService {

    @Autowired
    private VsuapApiService vsuapApiService;

    /**
     * 查询AI算法列表
     *
     * @Param: req AI算法入参
     * @return: List<VsuapAIMethodListResp> 算法列表
     */
    public VsuapBaseResp<List<VsuapAIMethodListResp>> requestAIMethodList(VsuapAIMethodListReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/ai/queryAIMethodList";
        return vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<List<VsuapAIMethodListResp>>>() {
                });
    }

    /**
     * 查询告警类AI分析结果
     *
     * @Param: req 告警类AI分析入参
     * @return: VsuapBaseResp<List> 告警类AI分析分页
     */
    public VsuapBaseResp<List<VsuapAIAlarmListResp>> requestAIAlarmLis(VsuapAIAlarmListReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/ai/queryAIAlarmList";
        return vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<List<VsuapAIAlarmListResp>>>() {
                });
    }

    /**
     * 查询统计类AI分析结果
     *
     * @Param: req 统计类AI分析入参
     * @return: List<VsuapAIStatisticsResp> 统计类AI分析列表
     */
    public List<VsuapAIStatisticsResp> requestAIStatistics(VsuapAIStatisticsReq req, VsuapSecretKeyReq secretKeyReq) {
        String gateway = "vsuap-platform-api/v1/ai/queryStatistics";
        VsuapBaseResp<List<VsuapAIStatisticsResp>> request = vsuapApiService.request(gateway, HttpMethod.POST, req, secretKeyReq,
                new TypeReference<VsuapBaseResp<List<VsuapAIStatisticsResp>>>() {
                });
        return request.getData();
    }
}
