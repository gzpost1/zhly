package cn.cuiot.dmp.message.controller;//	模板

import cn.cuiot.dmp.base.infrastructure.constants.MsgTagConstants;
import cn.cuiot.dmp.base.infrastructure.stream.StreamMessageSender;
import cn.cuiot.dmp.base.infrastructure.stream.messaging.SimpleMsg;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.message.service.UserMessageService;
import cn.hutool.core.lang.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    private StreamMessageSender streamMessageSender;

    @GetMapping("/im")
    public IdmResDTO<String> userMessageOutput() {
        streamMessageSender.sendGenericMessage("userMessageProduct-out-0",
                SimpleMsg.builder()
                        .delayTimeLevel(2)
                        .operateTag(MsgTagConstants.DEPARTMENT_ADD)
                        .data("newDept")
                        .info("创建组织部门")
                        .build());
        return IdmResDTO.success(UUID.fastUUID().toString());
    }

}
