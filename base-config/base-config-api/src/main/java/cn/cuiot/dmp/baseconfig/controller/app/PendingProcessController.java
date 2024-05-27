package cn.cuiot.dmp.baseconfig.controller.app;

import cn.cuiot.dmp.baseconfig.flow.dto.app.BaseDto;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2.3.【管理端-待办】
 * @author pengjian
 * @create 2024/5/27 11:33
 */

@RestController
@RequestMapping("/app/pend")
public class PendingProcessController {

    /**
     * 查询代办工单数量
     * @return
     */
    @RequestMapping("queryPendProcess")
    public IdmResDTO<BaseDto> queryPendProcess(@RequestBody BaseDto baseDto){

        return null;
    }
}
