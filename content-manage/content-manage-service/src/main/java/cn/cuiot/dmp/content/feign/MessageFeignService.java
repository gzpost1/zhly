package cn.cuiot.dmp.content.feign;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.req.MsgExistDataIdReqDto;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/14 15:32
 */
@Component
@FeignClient(value = "community-message")
public interface MessageFeignService {

    @PostMapping(value = "/api/userMessage/getAcceptDataIdList", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<Long>> getAcceptDataIdList(@RequestBody MsgExistDataIdReqDto reqDto);
}
