package cn.cuiot.dmp.baseconfig.controller.app;

import cn.cuiot.dmp.baseconfig.flow.dto.app.BaseDto;
import cn.cuiot.dmp.baseconfig.flow.dto.app.query.PendingProcessQuery;
import cn.cuiot.dmp.baseconfig.flow.service.AppWorkInfoService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 2.3.【管理端-待办】
 * @author pengjian
 * @create 2024/5/27 11:33
 */

@RestController
@RequestMapping("/app/pend")
public class PendingProcessController {

    @Autowired
    private AppWorkInfoService appWorkInfoService;

    /**
     * 代办工单-待审批
     * @return
     */
    @RequestMapping("queryPendProcess")
    public IdmResDTO<BaseDto> queryPendProcess(@RequestBody PendingProcessQuery query){
        return appWorkInfoService.queryMyNotApprocalCount(query);

    }

    /**
     * 待处理
     * @param query
     * @return
     */
    public IdmResDTO<List<BaseDto>> queryPendProcessList(@RequestBody PendingProcessQuery query){
        return appWorkInfoService.queryPendProcessList(query);
    }
}
