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
@FeignClient(value = "system")
public interface MenuFeignService {

    /**
     * 根据用户和账户是否存在
     * @param userId
     * @param orgId
     * @return
     */
    @GetMapping(value = "/openFeign/checkUserAndOrg")
    JSONObject checkUserAndOrg(@RequestParam(value = "userId") String userId,
                               @RequestParam(value = "orgId") String orgId);
}
