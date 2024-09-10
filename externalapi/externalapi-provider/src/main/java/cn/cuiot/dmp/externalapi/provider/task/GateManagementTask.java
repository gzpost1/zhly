package cn.cuiot.dmp.externalapi.provider.task;

import cn.cuiot.dmp.common.utils.KtSignUtils;
import cn.cuiot.dmp.externalapi.service.constant.KeTuoPlatformConstant;
import cn.cuiot.dmp.externalapi.service.entity.park.GateManagementEntity;
import cn.cuiot.dmp.externalapi.service.service.park.GateManagementService;
import cn.cuiot.dmp.externalapi.service.service.park.ParkInfoService;
import cn.cuiot.dmp.externalapi.service.vendor.park.config.ParkProperties;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.ParkNodeStatusVO;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.ParkResultVO;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.ParkingNodeVO;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.ParkingPage;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 同步道闸数据
 * @author pengjian
 * @create 2024/9/9 14:09
 */
@Slf4j
@Component
public class GateManagementTask {
    @Autowired
    private GateManagementService gateManagementService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ParkProperties parkProperties;

    @Autowired
    private ParkInfoService parkInfoService;
    @XxlJob("syncGateManagement")
    public ReturnT<String> syncGateManagement(String param){
        log.info("同步道闸开始");
            List<Integer> parkIds = Optional.ofNullable(parkInfoService.queryParkIds()).orElse(new ArrayList<>());
            for(Integer parkId : parkIds){
                List<GateManagementEntity> entities = new ArrayList<>();
                JSONObject jsonObject =getJsonObject(KeTuoPlatformConstant.PARK_NODE_SERVICE_CODE);
                jsonObject.put("pageIndex", KeTuoPlatformConstant.PARK_NODE_PAGE_INDEX);
                jsonObject.put("pageSize", KeTuoPlatformConstant.PARK_NODE_PAGE_SIZE);
                jsonObject.put("parkId",parkId);
                String key = KtSignUtils.paramsSign(jsonObject, parkProperties.getAppSercert());
                jsonObject.put("key", key);

                //接口获取列的停车场信息
                HttpHeaders headers = getHttpHeaders();
                ResponseEntity<ParkResultVO<ParkingNodeVO>> responseEntity =
                        restTemplate.exchange(parkProperties.getUrl()+ KeTuoPlatformConstant.GET_PARK_NODE, HttpMethod.POST,
                                new HttpEntity<>(jsonObject,headers),
                                new ParameterizedTypeReference<ParkResultVO<ParkingNodeVO>>() {
                                });
                ParkResultVO<ParkingNodeVO> body = responseEntity.getBody();
                if(!StringUtils.equals(body.getResCode(),KeTuoPlatformConstant.SUCCESS_CODE)){
                    throw new RuntimeException("拉取闸道数据失败："+body.getResMsg());
                }
                List<ParkingNodeVO.NodeList> nodeList = Optional.ofNullable(body.getData().getNodeList()).orElse(new ArrayList<>());
                for(ParkingNodeVO.NodeList nodes: nodeList){
                    //查询道闸状态
                    JSONObject statusObject = getJsonObject(KeTuoPlatformConstant.PARK_NODE_STATUS);
                    statusObject.put("parkId",parkId);
                    String statusKey = KtSignUtils.paramsSign(statusObject, parkProperties.getAppSercert());
                    jsonObject.put("key", statusKey);
                    jsonObject.put("nodeId", nodes.getId());
                    //接口获取列的停车场信息
                    ResponseEntity<ParkResultVO<ParkNodeStatusVO>> statusResponseEntity =
                            restTemplate.exchange(parkProperties.getUrl()+ KeTuoPlatformConstant.GET_PARK_NODE, HttpMethod.POST,
                                    new HttpEntity<>(jsonObject,headers),
                                    new ParameterizedTypeReference<ParkResultVO<ParkNodeStatusVO>>() {
                                    });
                    ParkResultVO<ParkNodeStatusVO> statusVo = statusResponseEntity.getBody();
                    if(StringUtils.equals(statusVo.getResCode(), KeTuoPlatformConstant.SUCCESS_CODE)){
                        throw new RuntimeException("拉取闸道状态失败："+body.getResMsg()+":"+nodes.getId());
                    }
                    List<ParkNodeStatusVO.NodeLists> nodeLists = Optional.ofNullable(statusVo.getData().getNodeList()).orElse(new ArrayList<>());

                    for(ParkNodeStatusVO.NodeLists statusNodes : nodeLists){
                        GateManagementEntity entity = new GateManagementEntity();
                        entity.setNodeId(nodes.getId());
                        entity.setNodeName(nodes.getNodeName());
                        entity.setParkId(parkId);
                        entity.setUseType(nodes.getUseType());
                        entity.setStatus(statusNodes.getStatus());
                        entities.add(entity);
                    }

                }
                if(CollectionUtil.isNotEmpty(entities)){
                    gateManagementService.insertOrUpdateBatch(entities);
                }
            }
        return ReturnT.SUCCESS;
    }

    public  JSONObject getJsonObject(String serviceCode){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appId",parkProperties.getAppId());
        jsonObject.put("serviceCode",serviceCode);
        jsonObject.put("ts",new Date().getTime());
        jsonObject.put("reqId", UuidUtil.getTimeBasedUuid());
        return jsonObject;
    }
    /**
     * 设置请求头
     * @return
     */
    public HttpHeaders getHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set(KeTuoPlatformConstant.PARK_HEADER_VERSION, KeTuoPlatformConstant.PARK_VERSION);
        headers.set(KeTuoPlatformConstant.PARK_HEADER_ACCEPT, KeTuoPlatformConstant.PARK_ACCEPT);
        return headers;
    }
}
