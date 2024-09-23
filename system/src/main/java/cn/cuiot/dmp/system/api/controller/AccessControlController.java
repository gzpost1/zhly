package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.system.application.service.AccessControlService;
import cn.cuiot.dmp.system.application.service.UniUbiService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.AccessCommunityDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.QueryAccessCommunity;
import cn.cuiot.dmp.system.infrastructure.entity.vo.UpdateAccessCommunityVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 门禁管理
 * @author pengjian
 * @create 2024/9/4 11:19
 */
@RestController
@RequestMapping("/access/control")
public class AccessControlController {

    @Autowired
    private AccessControlService accessControlService;
    /**
     * 同步门禁
     */
    @Autowired
    private UniUbiService uniUbiService;
    @PostMapping("/syncAccessControlData")
    public IdmResDTO syncAccessControlData(){
        uniUbiService.syncDeviceData();
        return IdmResDTO.success();
    }

    /**
     * 编辑楼盘信息
     * @return
     */
    @PostMapping("/updateAccessCommunity")
    public IdmResDTO updateAccessCommunity(@RequestBody @Valid UpdateAccessCommunityVO communityVO){
        accessControlService.updateAccessCommunity(communityVO);
        return IdmResDTO.success();
    }

    /**
     * 删除设备信息
     * @param communityVO
     * @return
     */
    @PostMapping("/deleteAccessCommunity")
    public IdmResDTO deleteAccessCommunity(@RequestBody @Valid UpdateAccessCommunityVO communityVO){
        return accessControlService.deleteAccessCommunity(communityVO);
    }

    /**
     * 分页查询数据信息
     * @param queryAccessCommunity
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<AccessCommunityDto>> queryForPage(@RequestBody QueryAccessCommunity queryAccessCommunity){
        return accessControlService.queryForPage(queryAccessCommunity);
    }

}
