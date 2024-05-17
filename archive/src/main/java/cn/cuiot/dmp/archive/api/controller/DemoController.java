package cn.cuiot.dmp.archive.api.controller;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author caorui
 * @date 2024/5/14
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    /**
     * 根据id获取详情
     */
    @PostMapping("/hello")
    public IdmResDTO<String> hello() {
        return IdmResDTO.success("hello");
    }

}
