package cn.cuiot.dmp.externalapi.provider.controller.app.park;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.entity.park.PortraitInputEntity;
import cn.cuiot.dmp.externalapi.service.query.PortraitInputCreateDto;
import cn.cuiot.dmp.externalapi.service.service.park.PortraitInputService;
import cn.cuiot.dmp.externalapi.service.vo.park.PortraitInputVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
     * 更新人像信息
     * @param createDto
     * @return
     * @throws NoSuchAlgorithmException
     */
    @PostMapping(value = "/updatePortrait", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions
    public IdmResDTO updatePortrait(@RequestBody PortraitInputCreateDto createDto) throws NoSuchAlgorithmException{
        return portraitInputService.updatePortrait(createDto);
    }

    /**
     * 删除
     * @param idParam
     * @return
     * @throws NoSuchAlgorithmException
     */
    @PostMapping(value = "/deletePortrait", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions
    public IdmResDTO deletePortrait(@RequestBody @Valid IdParam idParam) throws NoSuchAlgorithmException {
        return portraitInputService.deletePortrait(idParam);
    }
    /**
     * 分页查询
     *
     * @param para
     * @return
     */

    @PostMapping(value = "/queryPortraitInputInfo")
    @RequiresPermissions
    public IdmResDTO<IPage<PortraitInputVo>> queryPortraitInputInfo(@RequestBody PortraitInputVo para){
        return portraitInputService.queryPortraitInputInfo(para);
    }

    /**
     * 查询人像详情
     * @param
     * @return
     */
    @PostMapping(value = "/queryPortraitInputDetail")
    @RequiresPermissions
    public IdmResDTO<PortraitInputEntity>  queryPortraitInputDetail(@RequestBody @Valid IdParam idParam){
        return portraitInputService.queryPortraitInputDetail(idParam);
    }
}
