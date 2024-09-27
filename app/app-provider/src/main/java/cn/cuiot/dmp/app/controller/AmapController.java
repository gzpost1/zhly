package cn.cuiot.dmp.app.controller;

import cn.cuiot.dmp.base.application.config.AppProperties;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 高德配置
 *
 * @author: wuyongchong
 * @date: 2024/9/27 9:53
 */
@Slf4j
@RestController
@RequestMapping("/amap")
public class AmapController {

    @Autowired
    private AppProperties appProperties;

    /**
     * 返回高德Key
     */
    @PostMapping("/queryKey")
    public IdmResDTO<String> queryKey() {
        String key = appProperties.getAmapKey();
        return IdmResDTO.success(key);
    }

}
