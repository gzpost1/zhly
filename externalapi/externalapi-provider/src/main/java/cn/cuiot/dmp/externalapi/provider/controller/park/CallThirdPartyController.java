package cn.cuiot.dmp.externalapi.provider.controller.park;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.entity.park.PortraitInputEntity;
import cn.cuiot.dmp.externalapi.service.query.*;
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
import java.util.List;

/**
 * 管理后台-人像录入
 * @author pengjian
 * @create 2024/7/18 16:43
 */
@RestController
@RequestMapping("/call/party")
public class CallThirdPartyController {

    @Autowired
    private PortraitInputService portraitInputService;


    /**
     * 创建人像信息
     * @param createDto
     * @return
     * @throws NoSuchAlgorithmException
     */
    @PostMapping(value = "/createPortrait", produces = MediaType.APPLICATION_JSON_VALUE)
//    @RequiresPermissions
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
     * 删除人像
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
     * 批量删除人像
     * @param
     * @return
     * @throws NoSuchAlgorithmException
     */
    @PostMapping(value = "/deleteBatchPortrait", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions
    public IdmResDTO deleteBatchPortrait(@RequestBody @Valid BatchPortraitDto batchPortraitDto){
        return portraitInputService.deleteBatchPortrait(batchPortraitDto);
    }
    /**
     * 查询平台信息
     * @param para
     * @return
     */
    @PostMapping(value = "/queryPortraitInputInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions
    public IdmResDTO<List<FootPlateDto>> queryPlatformInfo(@RequestBody PortraitInputVo para){
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

    /**
     * 查询企业填写的信息
     * @param queryDto
     * @return
     */
    @PostMapping(value = "/queryFootPlateCompanyInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions
    public IdmResDTO<FootPlateCompanyDto>  queryFootPlateCompanyInfo(@RequestBody FootPlateCompanyDto queryDto){
        return portraitInputService.queryFootPlateCompanyInfo(queryDto);
    }

    /**
     * 分页查询
     * @param para
     * glb-tg
     * @return
     */
    @PostMapping(value = "/queryPortraitInputInfoPage")
    @RequiresPermissions
    public IdmResDTO<IPage<PortraitInputVo>> queryPortraitInputInfoPage(@RequestBody PortraitInputDTO para){
        return IdmResDTO.success(portraitInputService.queryPortraitInputPage(para));
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
