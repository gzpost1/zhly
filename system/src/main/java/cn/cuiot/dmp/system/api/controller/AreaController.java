package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.system.application.service.AreaService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.AreaDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小区Controller
 *
 * @author wen
 * @date 2021-12-27 10:07:42
 */
@RestController
@RequestMapping("/area")
public class AreaController extends BaseController {

    @Autowired
    private AreaService areaService;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AreaDto> list(@RequestParam(value = "code", required = false) String code) {
        return areaService.listSon(code);
    }

}
