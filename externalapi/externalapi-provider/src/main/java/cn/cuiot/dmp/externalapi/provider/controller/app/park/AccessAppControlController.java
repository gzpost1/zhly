package cn.cuiot.dmp.externalapi.provider.controller.app.park;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.query.AccessCommunityDto;
import cn.cuiot.dmp.externalapi.service.query.DeviceListDto;
import cn.cuiot.dmp.externalapi.service.query.QueryAccessCommunity;
import cn.cuiot.dmp.externalapi.service.service.park.AccessControlService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * app-智能门禁
 * @author pengjian
 * @create 2024/9/5 19:51
 */
@RestController
@RequestMapping("/app/access")
public class AccessAppControlController {


    @Autowired
    private AccessControlService accessControlService;
    /**
     * 分页查询数据信息
     * @param queryAccessCommunity
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<AccessCommunityDto>> queryForPage(@RequestBody QueryAccessCommunity queryAccessCommunity){
        return accessControlService.queryForPage(queryAccessCommunity);
    }

    /**
     * 查询授权门禁信息
     * @return
     */
    @PostMapping("/queryDevices")
    public IdmResDTO<List<DeviceListDto>>  queryDevices(){
        return accessControlService.queryDevices();
    }
}
