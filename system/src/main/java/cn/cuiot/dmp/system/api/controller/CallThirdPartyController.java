package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.system.application.service.PortraitInputService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FootPlateCompanyDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.FootPlateDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PlatFromDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PortraitInputDTO;
import cn.cuiot.dmp.system.infrastructure.entity.vo.PortraitInputVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @return
     */
    @PostMapping(value = "/queryPortraitInputInfoPage")
    @RequiresPermissions
    public IdmResDTO<IPage<PortraitInputVo>> queryPortraitInputInfoPage(@RequestBody PortraitInputDTO para){
        return IdmResDTO.success(portraitInputService.queryPortraitInputPage(para));
    }

 }
