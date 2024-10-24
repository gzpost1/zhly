package cn.cuiot.dmp.externalapi.provider.controller.app.gw;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.query.gw.entranceguard.GwEntranceGuardOperationDto;
import cn.cuiot.dmp.externalapi.service.query.gw.entranceguard.GwEntranceGuardPageQuery;
import cn.cuiot.dmp.externalapi.service.service.gw.entranceguard.GwEntranceGuardService;
import cn.cuiot.dmp.externalapi.service.vo.gw.entranceguard.GwEntranceGuardAppPageVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * app-格物门禁
 *
 * @Author: zc
 * @Date: 2024-09-06
 */
@RestController
@RequestMapping("/app/gw/entranceGuard")
public class GwEntranceGuardAppController {

    @Autowired
    private GwEntranceGuardService gwEntranceGuardService;

    /**
     * 分页查询
     */
    @PostMapping("/queryAppForPage")
    public IdmResDTO<IPage<GwEntranceGuardAppPageVo>> queryAppForPage(@RequestBody GwEntranceGuardPageQuery query) {
        return IdmResDTO.success(gwEntranceGuardService.queryAppForPage(query));
    }

    /**
     * 开门
     */
    @PostMapping("/openTheDoor")
    public IdmResDTO<?> openTheDoor(@RequestBody @Valid IdParam param) {
        GwEntranceGuardOperationDto dto = new GwEntranceGuardOperationDto();
        dto.setId(param.getId());
        gwEntranceGuardService.openTheDoor(dto);
        return IdmResDTO.success();
    }

    /**
     * 重启
     */
    @PostMapping("/restart")
    public IdmResDTO<?> restart(@RequestBody @Valid IdParam param) {
        GwEntranceGuardOperationDto dto = new GwEntranceGuardOperationDto();
        dto.setId(param.getId());
        return IdmResDTO.success();
    }
}
