package cn.cuiot.dmp.gateway.fegin;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @classname MenuFeignService
 * @description 用户openFeign服务
 * @author wangyh
 * @date 2020-08-01
 */
@FeignClient(value = "community-system")
public interface SystemFeignService {

    /**
     * 查询组织对应的Api鉴权配置
     * @param appId
     * @return
     */
    @GetMapping(value = "/openFeign/organization/getApiAuthConfigByAppId")
    JSONObject getApiAuthConfigByAppId(@RequestParam(value = "appId") String appId);
}
