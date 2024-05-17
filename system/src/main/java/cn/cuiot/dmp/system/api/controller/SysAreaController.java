package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.system.application.param.vo.AreaTreeNodeVO;
import cn.cuiot.dmp.system.application.service.SysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统配置-区域管理
 *
 * @author caorui
 * @date 2024/5/14
 */
@RestController
@RequestMapping("/sysArea")
public class SysAreaController {

    @Autowired
    private SysAreaService sysAreaService;

    /**
     * 获取地区树
     */
    @PostMapping("/getAreaTree")
    public List<AreaTreeNodeVO> getAreaTree() {
        return sysAreaService.getAreaTree();
    }

    /**
     * 根据区域编码获取区域名称
     */
    @GetMapping("/getAreaName")
    public IdmResDTO<String> getAreaName(@RequestParam(value = "areaCode") String areaCode) {
        return IdmResDTO.success(sysAreaService.getAreaName(areaCode));
    }

}
