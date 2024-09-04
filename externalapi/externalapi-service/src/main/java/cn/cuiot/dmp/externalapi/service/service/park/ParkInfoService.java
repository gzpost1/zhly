package cn.cuiot.dmp.externalapi.service.service.park;

import cn.cuiot.dmp.base.application.dto.AuthDaHuaResp;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.KtSignUtils;
import cn.cuiot.dmp.externalapi.service.constant.KeTuoPlatformConstant;
import cn.cuiot.dmp.externalapi.service.entity.park.ParkInfoEntity;
import cn.cuiot.dmp.externalapi.service.mapper.park.ParkInfoMapper;
import cn.cuiot.dmp.externalapi.service.vendor.park.config.ParkProperties;
import cn.cuiot.dmp.externalapi.service.vendor.park.query.ParkingLotQuery;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.ParkResultVO;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.ParkingPage;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * @author pengjian
 * @since 2024-09-03
 */
@Service
public class ParkInfoService extends ServiceImpl<ParkInfoMapper, ParkInfoEntity>{

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ParkProperties parkProperties;
    /**
     * 同步停车场数据
     * @return
     */
    @Async
    public IdmResDTO syncParkInfo() {
        GetParkingLotList();
        return IdmResDTO.success();
    }

    /**
     * 获取停车场列表数据
     */
    public void GetParkingLotList(){

        int pageIndex =1;
        int pageSize =20;
        while(true){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appId",parkProperties.getAppId());
            jsonObject.put("serviceCode", KeTuoPlatformConstant.PARK_LOT_SERVICE_CODE);
            jsonObject.put("ts",new Date().getTime());
            jsonObject.put("reqId", UuidUtil.getTimeBasedUuid());
            jsonObject.put("pageIndex", pageIndex);
            jsonObject.put("pageSize", pageSize);
            String key = KtSignUtils.paramsSign(jsonObject, parkProperties.getAppSercert());
            jsonObject.put("key", key);

            System.out.println(JSONObject.toJSONString(jsonObject));
            HttpHeaders headers = getHttpHeaders();
            ResponseEntity<ParkResultVO<ParkingPage>> responseEntity =
                    restTemplate.exchange(parkProperties.getUrl()+ KeTuoPlatformConstant.GET_PARKING_LOT_LIST, HttpMethod.POST,
                            new HttpEntity<>(jsonObject,headers),
                            new ParameterizedTypeReference<ParkResultVO<ParkingPage>>() {
                            });
            ParkResultVO vo = responseEntity.getBody();
            ParkingPage data = (ParkingPage)vo.getData();
        }
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

    public static void main(String[] args) {
        System.out.println(UuidUtil.getTimeBasedUuid());
    }
}
