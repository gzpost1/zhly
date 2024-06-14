package cn.cuiot.dmp.message.controller;//	模板

import cn.cuiot.dmp.base.application.annotation.InternalApi;
import cn.cuiot.dmp.base.infrastructure.dto.req.MsgExistDataIdReqDto;
import cn.cuiot.dmp.message.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/14 14:50
 */
@InternalApi
@RestController
@RequestMapping("/api")
public class ApiUserMessageController {

    @Autowired
    private UserMessageService userMessageService;

    /**
     * 获取用户已经接收的消息数据id列表
     * @param reqDto
     * @return
     */
    @PostMapping("/userMessage/getAcceptDataIdList")
    public List<Long> getAcceptDataIdList(@RequestBody MsgExistDataIdReqDto reqDto) {
        return userMessageService.getAcceptDataIdList(reqDto);
    }
}
