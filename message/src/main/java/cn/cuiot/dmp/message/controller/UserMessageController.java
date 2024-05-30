package cn.cuiot.dmp.message.controller;//	模板

import cn.cuiot.dmp.message.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/28 9:47
 */
@RestController
@RequestMapping("/userMessage")
public class UserMessageController {

    @Autowired
    private UserMessageService userMessageService;

}
