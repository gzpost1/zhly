package cn.cuiot.dmp.externalapi.provider.controller.park;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.query.AccessCommunityDto;
import cn.cuiot.dmp.externalapi.service.query.DeviceListDto;
import cn.cuiot.dmp.externalapi.service.query.QueryAccessCommunity;
import cn.cuiot.dmp.externalapi.service.query.UpdateAccessCommunityVO;
import cn.cuiot.dmp.externalapi.service.service.park.AccessControlService;
import cn.cuiot.dmp.externalapi.service.service.park.UniUbiService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

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
    @RequiresPermissions
    public IdmResDTO syncAccessControlData(){
        uniUbiService.syncDeviceData();
        return IdmResDTO.success();
    }

    /**
     * 编辑楼盘信息
     * @return
     */
    @PostMapping("/updateAccessCommunity")
    @RequiresPermissions
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
    @RequiresPermissions
    public IdmResDTO deleteAccessCommunity(@RequestBody @Valid UpdateAccessCommunityVO communityVO){
        return accessControlService.deleteAccessCommunity(communityVO);
    }

    /**
     * 分页查询数据信息
     * @param queryAccessCommunity
     * @return
     */
    @PostMapping("/queryForPage")
    @RequiresPermissions
    public IdmResDTO<IPage<AccessCommunityDto>> queryForPage(@RequestBody QueryAccessCommunity queryAccessCommunity){
        return accessControlService.queryForPage(queryAccessCommunity);
    }

    /**
     * 查询授权门禁信息
     * @return
     */
    @PostMapping("/queryDevices")
    @RequiresPermissions
    public IdmResDTO<List<DeviceListDto>>  queryDevices(){
        return accessControlService.queryDevices();
    }
}
