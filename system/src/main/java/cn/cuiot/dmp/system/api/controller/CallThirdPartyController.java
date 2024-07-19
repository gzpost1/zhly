package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.constant.PermissionContants;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.system.application.service.PortraitInputService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PlatFromDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PortraitInputCreateDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.PortraitInputVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 人像录入
 * @author pengjian
 * @create 2024/7/18 16:43
 */
@RestController
@RequestMapping("/call/party")
public class CallThirdPartyController {

    @Autowired
    private PortraitInputService portraitInputService;

    @PostMapping(value = "/createPortrait", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions
    public IdmResDTO createPortrait(@RequestBody PortraitInputCreateDto createDto) throws NoSuchAlgorithmException {
        return portraitInputService.createPortrait(createDto);
    }

    /**
     * 分页查询
     * @param para
     * @return
     */

    @PostMapping(value = "/queryPortraitInputInfo")
    @RequiresPermissions
    public IdmResDTO<IPage<PortraitInputVo>> queryPortraitInputInfo(@RequestBody PortraitInputVo para){
        return portraitInputService.queryPortraitInputInfo(para);
    }

    /**
     * 查询平台信息
     * @param para
     * @return
     */
    @PostMapping(value = "/queryPortraitInputInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions
    public IdmResDTO<List<PlatFromDto>> queryPlatformInfo(@RequestBody PortraitInputVo para){
        return portraitInputService.queryPlatformInfo(para);
    }

    /**
     * 保存与更新
     * @param dto
     * @return
     */
    @PostMapping(value = "/updatePortraitInputInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions
    public IdmResDTO updatePortraitInputInfo(@RequestBody PlatFromDto dto){
        return portraitInputService.updatePortraitInputInfo(dto);
    }


}
