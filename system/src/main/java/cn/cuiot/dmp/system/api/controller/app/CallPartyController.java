package cn.cuiot.dmp.system.api.controller.app;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.system.application.service.PortraitInputService;
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

/**
 * 人像录入
 * @author pengjian
 * @create 2024/7/19 14:11
 */
@RestController
@RequestMapping("/app/party")
public class CallPartyController {
    @Autowired
    private PortraitInputService portraitInputService;


    /**
     * 创建人像信息
     * @param createDto
     * @return
     * @throws NoSuchAlgorithmException
     */
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


}
