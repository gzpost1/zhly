package cn.cuiot.dmp.externalapi.provider.controller.sms;

import cn.cuiot.dmp.sms.query.SmsPushDataQuery;
import cn.cuiot.dmp.sms.service.SmsSendRecordService;
import cn.cuiot.dmp.sms.vo.SmsPushDataVO;
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
@RestController
@RequestMapping("/sms/push")
public class SmsPushController {

    @Autowired
    private SmsSendRecordService sendRecordService;

    /**
     * 第三方推送数据
     *
     * @return SmsPushDataVO
     * @Param query 参数
     */
    @PostMapping("/pushData")
    public SmsPushDataVO pushData(@RequestBody SmsPushDataQuery query) {
        try {
            sendRecordService.pushData(query);
            return new SmsPushDataVO(0, "成功");
        } catch (Exception e) {
            return new SmsPushDataVO(-1, "失败");
        }
    }
}
