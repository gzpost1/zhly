package cn.cuiot.dmp.externalapi.provider.task;

import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.common.utils.KtSignUtils;
import cn.cuiot.dmp.externalapi.service.constant.KeTuoPlatformConstant;
import cn.cuiot.dmp.externalapi.service.entity.park.VehicleExitRecordsEntity;
import cn.cuiot.dmp.externalapi.service.service.park.ParkInfoService;
import cn.cuiot.dmp.externalapi.service.service.park.VehicleExitRecordsService;
import cn.cuiot.dmp.externalapi.service.vendor.park.config.ParkProperties;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.CarOutInfoVO;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.ParkNodeStatusVO;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.ParkResultVO;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.ParkingNodeVO;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xxl.job.core.biz.model.ReturnT;
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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author pengjian
 * @create 2024/9/9 17:37
 */
@Slf4j
@Component
public class CarInOutInfoTask {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ParkProperties parkProperties;

    @Autowired
    private ParkInfoService parkInfoService;

    @Autowired
    private VehicleExitRecordsService vehicleExitRecordsService;

    public ReturnT<String>  GetCarInoutInfo(String param){
        log.info("同步进出站数据开始");
        List<Integer> parkIds = Optional.ofNullable(parkInfoService.queryParkIds()).orElse(new ArrayList<>());
        for(Integer parkId : parkIds){
            // 获取当前日期和时间
            LocalDateTime now = LocalDateTime.now();
            // 创建一个DateTimeFormatter来定义日期时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // 使用formatter来格式化日期时间
            String startTime = now.format(formatter);
            String endTime = now.minusMinutes(6).format(formatter);
            int pageIndex =1;
            int pageSize =50;
            while (true){
                JSONObject jsonObject =getJsonObject(KeTuoPlatformConstant.CAR_IN_OUT_SERVICE_CODE);
                jsonObject.put("parkId",parkId);
                jsonObject.put("pageIndex",pageIndex);
                jsonObject.put("pageSize",pageSize);
                jsonObject.put("startTime",startTime);
                jsonObject.put("endTime",endTime);
                String key = KtSignUtils.paramsSign(jsonObject, parkProperties.getAppSercert());
                jsonObject.put("key", key);
                HttpHeaders headers = getHttpHeaders();
                //ParkResultVO<CarOutInfoVO>
                ResponseEntity<JSONObject> responseEntity =
                        restTemplate.exchange(parkProperties.getUrl()+ KeTuoPlatformConstant.CAR_IN_OUT_SERVICE_CODE_URL, HttpMethod.POST,
                                new HttpEntity<>(jsonObject,headers),
                                new ParameterizedTypeReference<JSONObject>() {
                                });

                JSONObject statusBody = responseEntity.getBody();

                if(!StringUtils.equals(statusBody.getString("resCode"), KeTuoPlatformConstant.SUCCESS_CODE)){
                    throw new RuntimeException("拉取闸道状态失败："+statusBody.getString("resMsg")+":"+parkId);
                }

                List<VehicleExitRecordsEntity> nodeLists = JSONObject.parseObject(JSONObject.toJSONString(statusBody.getJSONObject("data").getJSONArray("detailList")), new TypeReference<List<VehicleExitRecordsEntity>>() {
                });
                    if(CollectionUtil.isEmpty(nodeLists)){
                        break;
                    }
                nodeLists.stream().forEach(item->{
                    item.setParkId(parkId);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String formattedDate = sdf.format(item.getCapTime());
                    item.setId(parkId+item.getNodeId()+ formattedDate+item.getCapFlag());
                });

                    vehicleExitRecordsService.insertOrUpdateBatch(nodeLists);


                pageIndex++;
            }



        }
        log.info("同步进出站数据完成");

        return ReturnT.SUCCESS;
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

    /**
     * 组装请求参数
     * @param serviceCode
     * @return
     */
    public JSONObject getJsonObject(String serviceCode){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appId",parkProperties.getAppId());
        jsonObject.put("serviceCode",serviceCode);
        jsonObject.put("ts",new Date().getTime());
        jsonObject.put("reqId", UuidUtil.getTimeBasedUuid());
        return jsonObject;
    }
}
