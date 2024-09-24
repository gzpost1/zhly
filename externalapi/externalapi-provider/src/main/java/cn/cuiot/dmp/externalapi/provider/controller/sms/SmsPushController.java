package cn.cuiot.dmp.externalapi.provider.controller.sms;

import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.provider.rocketmq.SmsSendMqService;
import cn.cuiot.dmp.sms.query.SmsPushDataQuery;
import cn.cuiot.dmp.sms.vo.SmsPushDataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第三方推送数据
 *
 * @Author: zc
 * @Date: 2024-09-24
 */
@Slf4j
@RestController
@RequestMapping("/sms/push")
public class SmsPushController {

    @Autowired
    private SmsSendMqService smsSendMqService;

    /**
     * 第三方推送数据
     *
     * @return SmsPushDataVO
     * @Param query 参数
     */
    @PostMapping("/pushData")
    public SmsPushDataVO pushData(@RequestBody SmsPushDataQuery query) {
        log.info("接收第三方推送的发送短信记录.............." + JsonUtil.writeValueAsString(query));
        try {
            smsSendMqService.sendSmsPushData(query);
            return new SmsPushDataVO(0, "成功");
        } catch (Exception e) {
            return new SmsPushDataVO(-1, "失败");
        }
    }
}
