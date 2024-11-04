package cn.cuiot.dmp.externalapi.provider.controller.admin.gw.smog;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.query.gw.GwSmogEventQuery;
import cn.cuiot.dmp.externalapi.service.service.gw.GwSmogEventService;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwSmogEventPageVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台-格物烟雾报警器-设备告警
 *
 * @Author: xulei
 * @Date: 2024-10-23
 */
@RestController
@RequestMapping("/gw/smog/event")
public class GwSmogEventController {


    @Autowired
    private GwSmogEventService gwSmogEventService;

    /**
     * 分页查询
     */
    //@RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<GwSmogEventPageVo>> queryForPage(@RequestBody GwSmogEventQuery query) {
        return IdmResDTO.success(gwSmogEventService.queryForPage(query));
    }

    /**
     * 格物-烟雾报警器设备告警导出
     * @param query
     * @return
     */
    @RequiresPermissions
    @PostMapping("/export")
    public IdmResDTO export(@RequestBody GwSmogEventQuery query){
        gwSmogEventService.export(query);
        return IdmResDTO.success();
    }
}
